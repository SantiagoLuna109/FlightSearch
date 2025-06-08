package com.FlightSearch.breakabletoy2.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.FlightSearch.breakabletoy2.dto.FlightDetailsResponse;
import com.FlightSearch.breakabletoy2.dto.FlightDetailsResponse.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class FlightDetailsMapper {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public FlightDetailsResponse mapToFlightDetails(JsonNode flightOffer, JsonNode dictionaries) {
        return FlightDetailsResponse.builder()
                .id(flightOffer.get("id").asText())
                .source(flightOffer.get("source").asText())
                .instantTicketingRequired(flightOffer.get("instantTicketingRequired").asBoolean())
                .oneWay(flightOffer.get("oneWay").asBoolean())
                .lastTicketingDate(getTextOrNull(flightOffer.get("lastTicketingDate")))
                .lastTicketingDateTime(getTextOrNull(flightOffer.get("lastTicketingDateTime")))
                .numberOfBookableSeats(flightOffer.get("numberOfBookableSeats").asInt())
                .summary(buildFlightSummary(flightOffer, dictionaries))
                .itineraries(buildItinerariesDetails(flightOffer.get("itineraries"), dictionaries))
                .priceBreakdown(buildPriceBreakdown(flightOffer.get("price")))
                .validatingAirlineCodes(buildValidatingCodes(flightOffer.get("validatingAirlineCodes")))
                .travelerPricings(buildTravelerPricings(flightOffer.get("travelerPricings")))
                .build();
    }

    private FlightSummary buildFlightSummary(JsonNode flightOffer, JsonNode dictionaries) {
        JsonNode itineraries = flightOffer.get("itineraries");
        JsonNode firstItinerary = itineraries.get(0);
        JsonNode segments = firstItinerary.get("segments");
        JsonNode firstSegment = segments.get(0);
        JsonNode lastSegment = segments.get(segments.size() - 1);

        List<StopInfo> stops = extractStops(segments, dictionaries);

        String mainCarrierCode = firstSegment.get("carrierCode").asText();
        String operatingCarrierCode = null;
        if (firstSegment.has("operating") && firstSegment.get("operating").has("carrierCode")) {
            operatingCarrierCode = firstSegment.get("operating").get("carrierCode").asText();
        }

        return FlightSummary.builder()
                .initialDepartureDateTime(parseDateTime(firstSegment.get("departure").get("at").asText()))
                .finalArrivalDateTime(parseDateTime(lastSegment.get("arrival").get("at").asText()))
                .departureAirportCode(firstSegment.get("departure").get("iataCode").asText())
                .departureAirportName(getLocationName(firstSegment.get("departure").get("iataCode").asText(), dictionaries))
                .arrivalAirportCode(lastSegment.get("arrival").get("iataCode").asText())
                .arrivalAirportName(getLocationName(lastSegment.get("arrival").get("iataCode").asText(), dictionaries))
                .mainAirlineCode(mainCarrierCode)
                .mainAirlineName(getCarrierName(mainCarrierCode, dictionaries))
                .operatingAirlineCode(operatingCarrierCode)
                .operatingAirlineName(operatingCarrierCode != null ? getCarrierName(operatingCarrierCode, dictionaries) : null)
                .totalFlightDuration(firstItinerary.get("duration").asText())
                .totalPrice(new BigDecimal(flightOffer.get("price").get("total").asText()))
                .pricePerTraveler(new BigDecimal(flightOffer.get("price").get("total").asText()))
                .currency(flightOffer.get("price").get("currency").asText())
                .hasStops(!stops.isEmpty())
                .numberOfStops(stops.size())
                .stops(stops)
                .build();
    }

    private List<StopInfo> extractStops(JsonNode segments, JsonNode dictionaries) {
        List<StopInfo> stops = new ArrayList<>();

        for (int i = 0; i < segments.size(); i++) {
            JsonNode segment = segments.get(i);
            if (segment.has("stops")) {
                JsonNode segmentStops = segment.get("stops");
                for (JsonNode stop : segmentStops) {
                    String airportCode = stop.get("iataCode").asText();
                    stops.add(StopInfo.builder()
                            .airportCode(airportCode)
                            .airportName(getLocationName(airportCode, dictionaries))
                            .duration(stop.get("duration").asText())
                            .arrivalTime(stop.has("arrivalAt") ? parseDateTime(stop.get("arrivalAt").asText()) : null)
                            .departureTime(stop.has("departureAt") ? parseDateTime(stop.get("departureAt").asText()) : null)
                            .build());
                }
            }
        }
        return stops;
    }

    private List<ItineraryDetails> buildItinerariesDetails(JsonNode itineraries, JsonNode dictionaries) {
        List<ItineraryDetails> details = new ArrayList<>();

        for (JsonNode itinerary : itineraries) {
            JsonNode segments = itinerary.get("segments");
            List<SegmentDetails> segmentDetails = buildSegmentDetails(segments, dictionaries);
            List<LayoverDetails> layovers = buildLayoverDetails(segments, dictionaries);

            details.add(ItineraryDetails.builder()
                    .duration(itinerary.get("duration").asText())
                    .segments(segmentDetails)
                    .layovers(layovers)
                    .build());
        }
        return details;
    }

    private List<SegmentDetails> buildSegmentDetails(JsonNode segments, JsonNode dictionaries) {
        List<SegmentDetails> details = new ArrayList<>();

        for (JsonNode segment : segments) {
            String carrierCode = segment.get("carrierCode").asText();
            String aircraftCode = segment.get("aircraft").get("code").asText();

            OperatingCarrier operating = null;
            if (segment.has("operating") && segment.get("operating").has("carrierCode")) {
                String opCarrierCode = segment.get("operating").get("carrierCode").asText();
                operating = OperatingCarrier.builder()
                        .carrierCode(opCarrierCode)
                        .carrierName(getCarrierName(opCarrierCode, dictionaries))
                        .build();
            }

            details.add(SegmentDetails.builder()
                    .id(segment.get("id").asText())
                    .numberOfStops(segment.get("numberOfStops").asInt())
                    .blacklistedInEU(segment.get("blacklistedInEU").asBoolean())
                    .departure(buildFlightEndpointDetails(segment.get("departure"), dictionaries))
                    .arrival(buildFlightEndpointDetails(segment.get("arrival"), dictionaries))
                    .carrierCode(carrierCode)
                    .carrierName(getCarrierName(carrierCode, dictionaries))
                    .number(segment.get("number").asText())
                    .flightNumber(carrierCode + segment.get("number").asText())
                    .aircraftCode(aircraftCode)
                    .aircraftName(getAircraftName(aircraftCode, dictionaries))
                    .operating(operating)
                    .duration(segment.get("duration").asText())
                    .co2Emissions(buildCo2Emissions(segment))
                    .build());
        }
        return details;
    }

    private FlightEndpointDetails buildFlightEndpointDetails(JsonNode endpoint, JsonNode dictionaries) {
        String airportCode = endpoint.get("iataCode").asText();
        JsonNode locationInfo = getLocationInfo(airportCode, dictionaries);

        return FlightEndpointDetails.builder()
                .iataCode(airportCode)
                .airportName(getLocationName(airportCode, dictionaries))
                .cityName(locationInfo != null && locationInfo.has("cityName") ? locationInfo.get("cityName").asText() : null)
                .countryName(locationInfo != null && locationInfo.has("countryName") ? locationInfo.get("countryName").asText() : null)
                .terminal(getTextOrNull(endpoint.get("terminal")))
                .at(parseDateTime(endpoint.get("at").asText()))
                .timeZoneOffset(locationInfo != null && locationInfo.has("timeZoneOffset") ? locationInfo.get("timeZoneOffset").asText() : null)
                .build();
    }

    private List<Co2EmissionDetails> buildCo2Emissions(JsonNode segment) {
        List<Co2EmissionDetails> emissions = new ArrayList<>();
        if (segment.has("co2Emissions")) {
            for (JsonNode emission : segment.get("co2Emissions")) {
                emissions.add(Co2EmissionDetails.builder()
                        .weight(emission.get("weight").asInt())
                        .weightUnit(emission.get("weightUnit").asText())
                        .cabin(emission.get("cabin").asText())
                        .build());
            }
        }
        return emissions;
    }

    private List<LayoverDetails> buildLayoverDetails(JsonNode segments, JsonNode dictionaries) {
        List<LayoverDetails> layovers = new ArrayList<>();

        for (int i = 0; i < segments.size() - 1; i++) {
            JsonNode currentSegment = segments.get(i);
            JsonNode nextSegment = segments.get(i + 1);

            String arrivalCode = currentSegment.get("arrival").get("iataCode").asText();
            String departureCode = nextSegment.get("departure").get("iataCode").asText();

            if (arrivalCode.equals(departureCode)) {
                LocalDateTime arrivalTime = parseDateTime(currentSegment.get("arrival").get("at").asText());
                LocalDateTime departureTime = parseDateTime(nextSegment.get("departure").get("at").asText());

                long minutes = java.time.Duration.between(arrivalTime, departureTime).toMinutes();
                String duration = String.format("PT%dH%dM", minutes / 60, minutes % 60);

                layovers.add(LayoverDetails.builder()
                        .airportCode(arrivalCode)
                        .airportName(getLocationName(arrivalCode, dictionaries))
                        .duration(duration)
                        .arrivalTime(arrivalTime)
                        .departureTime(departureTime)
                        .segmentBefore(i)
                        .segmentAfter(i + 1)
                        .build());
            }
        }
        return layovers;
    }

    private PriceBreakdown buildPriceBreakdown(JsonNode price) {
        List<FeeDetails> fees = new ArrayList<>();
        if (price.has("fees")) {
            for (JsonNode fee : price.get("fees")) {
                fees.add(FeeDetails.builder()
                        .amount(new BigDecimal(fee.get("amount").asText()))
                        .type(fee.get("type").asText())
                        .build());
            }
        }

        List<TaxDetails> taxes = new ArrayList<>();
        if (price.has("taxes")) {
            for (JsonNode tax : price.get("taxes")) {
                taxes.add(TaxDetails.builder()
                        .amount(new BigDecimal(tax.get("amount").asText()))
                        .code(tax.get("code").asText())
                        .build());
            }
        }

        List<AdditionalServiceDetails> additionalServices = new ArrayList<>();
        if (price.has("additionalServices")) {
            for (JsonNode service : price.get("additionalServices")) {
                additionalServices.add(AdditionalServiceDetails.builder()
                        .amount(new BigDecimal(service.get("amount").asText()))
                        .type(service.get("type").asText())
                        .build());
            }
        }

        return PriceBreakdown.builder()
                .currency(price.get("currency").asText())
                .total(new BigDecimal(price.get("total").asText()))
                .base(new BigDecimal(price.get("base").asText()))
                .grandTotal(price.has("grandTotal") ? new BigDecimal(price.get("grandTotal").asText()) : new BigDecimal(price.get("total").asText()))
                .fees(fees)
                .taxes(taxes)
                .refundableTaxes(price.has("refundableTaxes") ? new BigDecimal(price.get("refundableTaxes").asText()) : null)
                .additionalServices(additionalServices)
                .build();
    }

    private List<String> buildValidatingCodes(JsonNode validatingCodes) {
        List<String> codes = new ArrayList<>();
        if (validatingCodes != null) {
            for (JsonNode code : validatingCodes) {
                codes.add(code.asText());
            }
        }
        return codes;
    }

    private List<TravelerPricingDetails> buildTravelerPricings(JsonNode travelerPricings) {
        List<TravelerPricingDetails> details = new ArrayList<>();

        for (JsonNode pricing : travelerPricings) {
            List<FareDetailsBySegment> fareDetails = new ArrayList<>();
            if (pricing.has("fareDetailsBySegment")) {
                for (JsonNode fareDetail : pricing.get("fareDetailsBySegment")) {
                    fareDetails.add(buildFareDetailsBySegment(fareDetail));
                }
            }

            details.add(TravelerPricingDetails.builder()
                    .travelerId(pricing.get("travelerId").asText())
                    .fareOption(pricing.get("fareOption").asText())
                    .travelerType(pricing.get("travelerType").asText())
                    .price(PriceDetails.builder()
                            .currency(pricing.get("price").get("currency").asText())
                            .total(new BigDecimal(pricing.get("price").get("total").asText()))
                            .base(new BigDecimal(pricing.get("price").get("base").asText()))
                            .build())
                    .fareDetailsBySegment(fareDetails)
                    .build());
        }
        return details;
    }

    private FareDetailsBySegment buildFareDetailsBySegment(JsonNode fareDetail) {
        BaggageAllowance checkedBags = null;
        if (fareDetail.has("includedCheckedBags")) {
            JsonNode checkedBagsNode = fareDetail.get("includedCheckedBags");
            checkedBags = BaggageAllowance.builder()
                    .quantity(checkedBagsNode.has("quantity") ? checkedBagsNode.get("quantity").asInt() : null)
                    .weight(checkedBagsNode.has("weight") ? checkedBagsNode.get("weight").asInt() : null)
                    .weightUnit(getTextOrNull(checkedBagsNode.get("weightUnit")))
                    .build();
        }

        BaggageAllowance cabinBags = null;
        if (fareDetail.has("includedCabinBags")) {
            JsonNode cabinBagsNode = fareDetail.get("includedCabinBags");
            cabinBags = BaggageAllowance.builder()
                    .quantity(cabinBagsNode.has("quantity") ? cabinBagsNode.get("quantity").asInt() : null)
                    .weight(cabinBagsNode.has("weight") ? cabinBagsNode.get("weight").asInt() : null)
                    .weightUnit(getTextOrNull(cabinBagsNode.get("weightUnit")))
                    .build();
        }

        List<AmenityDetails> amenities = new ArrayList<>();
        if (fareDetail.has("amenities")) {
            for (JsonNode amenity : fareDetail.get("amenities")) {
                AmenityProvider provider = null;
                if (amenity.has("amenityProvider")) {
                    provider = AmenityProvider.builder()
                            .name(amenity.get("amenityProvider").get("name").asText())
                            .build();
                }

                amenities.add(AmenityDetails.builder()
                        .description(amenity.get("description").asText())
                        .isChargeable(amenity.get("isChargeable").asBoolean())
                        .amenityType(amenity.get("amenityType").asText())
                        .amenityProvider(provider)
                        .build());
            }
        }

        return FareDetailsBySegment.builder()
                .segmentId(fareDetail.get("segmentId").asText())
                .cabin(fareDetail.get("cabin").asText())
                .fareBasis(fareDetail.get("fareBasis").asText())
                .brandedFare(getTextOrNull(fareDetail.get("brandedFare")))
                .brandedFareLabel(getTextOrNull(fareDetail.get("brandedFareLabel")))
                .fareClass(fareDetail.get("class").asText())
                .includedCheckedBags(checkedBags)
                .includedCabinBags(cabinBags)
                .amenities(amenities)
                .build();
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, ISO_FORMATTER);
    }

    private String getTextOrNull(JsonNode node) {
        return node != null && !node.isNull() ? node.asText() : null;
    }

    private String getLocationName(String iataCode, JsonNode dictionaries) {
        JsonNode locationInfo = getLocationInfo(iataCode, dictionaries);
        if (locationInfo != null && locationInfo.has("cityName")) {
            return locationInfo.get("cityName").asText();
        }
        return iataCode;
    }

    private JsonNode getLocationInfo(String iataCode, JsonNode dictionaries) {
        if (dictionaries != null && dictionaries.has("locations") && dictionaries.get("locations").has(iataCode)) {
            return dictionaries.get("locations").get(iataCode);
        }
        return null;
    }

    private String getCarrierName(String carrierCode, JsonNode dictionaries) {
        if (dictionaries != null && dictionaries.has("carriers") && dictionaries.get("carriers").has(carrierCode)) {
            return dictionaries.get("carriers").get(carrierCode).asText();
        }
        return carrierCode;
    }

    private String getAircraftName(String aircraftCode, JsonNode dictionaries) {
        if (dictionaries != null && dictionaries.has("aircraft") && dictionaries.get("aircraft").has(aircraftCode)) {
            return dictionaries.get("aircraft").get(aircraftCode).asText();
        }
        return aircraftCode;
    }
}