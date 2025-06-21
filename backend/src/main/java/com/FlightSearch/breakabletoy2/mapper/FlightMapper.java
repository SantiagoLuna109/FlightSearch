package com.FlightSearch.breakabletoy2.mapper;

import com.FlightSearch.breakabletoy2.client.AmadeusApiClient;
import com.FlightSearch.breakabletoy2.dto.FlightSearchResponse;
import com.FlightSearch.breakabletoy2.model.*;
import com.FlightSearch.breakabletoy2.service.DictionaryService;
import com.FlightSearch.breakabletoy2.service.IataCache;
import org.springframework.stereotype.Component;
import com.FlightSearch.breakabletoy2.model.amadeus.FlightOffersResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FlightMapper {

    private final AmadeusApiClient amadeusApiClient;
    private final IataCache iataCache;
    private final DictionaryService dictionaryService;

    public FlightMapper(AmadeusApiClient amadeusApiClient, IataCache iataCache, DictionaryService dictionaryService){
        this.amadeusApiClient = amadeusApiClient;
        this.iataCache = iataCache;
        this.dictionaryService = dictionaryService;
    }

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE
    };

    public Flight mapToFlight(FlightOffersResponse.FlightOfferData flightOfferData, FlightOffersResponse.Dictionaries dictionaries) {
        Flight flight = new Flight();

        flight.setId(flightOfferData.getId());
        flight.setSource(flightOfferData.getSource());
        flight.setInstantTicketingRequired(Boolean.TRUE.equals(flightOfferData.getInstantTicketingRequired()));
        flight.setNonHomogeneous(Boolean.TRUE.equals(flightOfferData.getNonHomogeneous()));
        flight.setOneWay(Boolean.TRUE.equals(flightOfferData.getOneWay()));
        flight.setLastTicketingDate(flightOfferData.getLastTicketingDate());
        flight.setLastTicketingDateTime(parseDateTime(flightOfferData.getLastTicketingDateTime()));
        flight.setNumberOfBookableSeats(flightOfferData.getNumberOfBookableSeats());

        if (flightOfferData.getItineraries() != null) {
            List<Itinerary> itineraries = flightOfferData.getItineraries().stream()
                    .map(itineraryData -> mapToItineraryFromAmadeus(itineraryData, dictionaries))
                    .collect(Collectors.toList());
            flight.setItineraries(itineraries);
        }

        if (flightOfferData.getPrice() != null) {
            flight.setPrice(mapToPriceFromAmadeus(flightOfferData.getPrice(), dictionaries));
        }

        flight.setValidatingAirlineCodes(flightOfferData.getValidatingAirlineCodes());

        if (flightOfferData.getTravelerPricings() != null) {
            List<TravelerPricing> travelerPricings = flightOfferData.getTravelerPricings().stream()
                    .map(travelerPricingData -> mapToTravelerPricingFromAmadeus(travelerPricingData, dictionaries))
                    .collect(Collectors.toList());
            flight.setTravelerPricings(travelerPricings);
        }

        return flight;
    }

    public List<Flight> toFlightList(FlightSearchResponse response) {
        if (response == null || response.getData() == null) {
            return List.of();
        }

        FlightSearchResponse.Dictionaries dictionaries = response.getDictionaries();

        return response.getData().stream()
                .map(flightData -> mapToFlightFromSearchResponse(flightData, dictionaries))
                .collect(Collectors.toList());
    }

    public List<Flight> mapToFlights(FlightSearchResponse response) {
        return toFlightList(response);
    }



    private Flight mapToFlightFromSearchResponse(FlightSearchResponse.FlightOfferData flightOfferData, FlightSearchResponse.Dictionaries dictionaries) {
        Flight flight = new Flight();

        flight.setId(flightOfferData.getId());
        flight.setSource(flightOfferData.getSource());
        flight.setInstantTicketingRequired(Boolean.TRUE.equals(flightOfferData.getInstantTicketingRequired()));
        flight.setNonHomogeneous(Boolean.TRUE.equals(flightOfferData.getNonHomogeneous()));
        flight.setOneWay(Boolean.TRUE.equals(flightOfferData.getOneWay()));
        flight.setLastTicketingDate(flightOfferData.getLastTicketingDate());
        flight.setLastTicketingDateTime(parseDateTime(flightOfferData.getLastTicketingDateTime()));
        flight.setNumberOfBookableSeats(flightOfferData.getNumberOfBookableSeats());

        if (flightOfferData.getItineraries() != null) {
            List<Itinerary> itineraries = flightOfferData.getItineraries().stream()
                    .map(itineraryData -> mapToItinerary(itineraryData, dictionaries))
                    .collect(Collectors.toList());
            flight.setItineraries(itineraries);
        }

        if (flightOfferData.getPrice() != null) {
            flight.setPrice(mapToPrice(flightOfferData.getPrice(), dictionaries));
        }

        flight.setValidatingAirlineCodes(flightOfferData.getValidatingAirlineCodes());

        if (flightOfferData.getTravelerPricings() != null) {
            List<TravelerPricing> travelerPricings = flightOfferData.getTravelerPricings().stream()
                    .map(travelerPricingData -> mapToTravelerPricing(travelerPricingData, dictionaries))
                    .collect(Collectors.toList());
            flight.setTravelerPricings(travelerPricings);
        }

        return flight;
    }

    private Itinerary mapToItinerary(FlightSearchResponse.ItineraryData itineraryData, FlightSearchResponse.Dictionaries dictionaries) {
        Itinerary itinerary = new Itinerary();
        itinerary.setDuration(itineraryData.getDuration());

        if (itineraryData.getSegments() != null) {
            List<Segment> segments = itineraryData.getSegments().stream()
                    .map(segmentData -> mapToSegment(segmentData, dictionaries))
                    .collect(Collectors.toList());
            itinerary.setSegments(segments);
        }

        return itinerary;
    }

    private Itinerary mapToItineraryFromAmadeus(FlightOffersResponse.Itinerary itineraryData, FlightOffersResponse.Dictionaries dictionaries) {
        Itinerary itinerary = new Itinerary();
        itinerary.setDuration(itineraryData.getDuration());

        if (itineraryData.getSegments() != null) {
            List<Segment> segments = itineraryData.getSegments().stream()
                    .map(segmentData -> mapToSegmentFromAmadeus(segmentData, dictionaries))
                    .collect(Collectors.toList());
            itinerary.setSegments(segments);
        }

        return itinerary;
    }

    private Segment mapToSegment(FlightSearchResponse.SegmentData segmentData, FlightSearchResponse.Dictionaries dictionaries) {
        Segment segment = new Segment();

        segment.setId(segmentData.getId());
        segment.setCarrierCode(segmentData.getCarrierCode());
        segment.setCarrierName(getCarrierNameFromSearchResponse(segmentData.getCarrierCode(), dictionaries));
        segment.setNumber(segmentData.getNumber());
        segment.setDuration(segmentData.getDuration());
        segment.setNumberOfStops(segmentData.getNumberOfStops());
        segment.setBlacklistedInEU(segmentData.getBlacklistedInEU());

        if (segmentData.getDeparture() != null) {
            segment.setDeparture(mapToFlightEndpointFromSearchResponse(segmentData.getDeparture(), dictionaries));
        }

        if (segmentData.getArrival() != null) {
            segment.setArrival(mapToFlightEndpointFromSearchResponse(segmentData.getArrival(), dictionaries));
        }

        if (segmentData.getAircraft() != null) {
            Aircraft aircraft = new Aircraft();
            aircraft.setCode(segmentData.getAircraft().getCode());
            aircraft.setName(getAircraftNameFromSearchResponse(segmentData.getAircraft().getCode(), dictionaries));
            segment.setAircraft(aircraft);
        }

        if (segmentData.getOperating() != null) {
            Operating operating = new Operating();
            operating.setCarrierCode(segmentData.getOperating().getCarrierCode());
            operating.setCarrierName(getCarrierNameFromSearchResponse(segmentData.getOperating().getCarrierCode(), dictionaries));
            segment.setOperating(operating);
        }

        return segment;
    }

    private Segment mapToSegmentFromAmadeus(FlightOffersResponse.Segment segmentData, FlightOffersResponse.Dictionaries dictionaries) {
        Segment segment = new Segment();

        segment.setId(segmentData.getId());
        segment.setCarrierCode(segmentData.getCarrierCode());
        segment.setCarrierName(getCarrierNameFromAmadeus(segmentData.getCarrierCode(), dictionaries));
        segment.setNumber(segmentData.getNumber());
        segment.setDuration(segmentData.getDuration());
        segment.setNumberOfStops(segmentData.getNumberOfStops());
        segment.setBlacklistedInEU(segmentData.getBlacklistedInEU());

        if (segmentData.getDeparture() != null) {
            segment.setDeparture(mapToFlightEndpointFromAmadeus(segmentData.getDeparture(), dictionaries));
        }

        if (segmentData.getArrival() != null) {
            segment.setArrival(mapToFlightEndpointFromAmadeus(segmentData.getArrival(), dictionaries));
        }

        if (segmentData.getAircraft() != null) {
            Aircraft aircraft = new Aircraft();
            aircraft.setCode(segmentData.getAircraft().getCode());
            aircraft.setName(getAircraftNameFromAmadeus(segmentData.getAircraft().getCode(), dictionaries));
            segment.setAircraft(aircraft);
        }

        if (segmentData.getOperating() != null) {
            Operating operating = new Operating();
            operating.setCarrierCode(segmentData.getOperating().getCarrierCode());
            operating.setCarrierName(getCarrierNameFromAmadeus(segmentData.getOperating().getCarrierCode(), dictionaries));
            segment.setOperating(operating);
            segment.setOperatingCarrierCode(operating.getCarrierCode());
            segment.setOperatingCarrierName(operating.getCarrierName());
        }

        if(segmentData.getOperating() != null && segmentData.getOperating().getCarrierCode() != null){
            String opCode = segmentData.getOperating().getCarrierCode();
            segment.setOperatingCarrierCode(opCode);
            segment.setOperatingCarrierName(getCarrierNameFromAmadeus(opCode,dictionaries));
        }

        return segment;
    }

    private FlightEndpoint mapToFlightEndpointFromSearchResponse(FlightSearchResponse.FlightEndpointData endpointData, FlightSearchResponse.Dictionaries dictionaries) {
        FlightEndpoint endpoint = new FlightEndpoint();
        String code = endpoint.getIataCode();
        endpoint.setIataCode(endpointData.getIataCode());
        endpoint.setTerminal(endpointData.getTerminal());
        endpoint.setAt(parseDateTime(endpointData.getAt()));
        endpoint.setIataCode(code);
        //endpoint.setAirportName(iataCache.airportName(code));
        endpoint.setAirportName(dictionaryService.resolveAirportName(code));

        LocationInfo locationInfo = getLocationInfoFromSearchResponse(endpointData.getIataCode(), dictionaries);
        //endpoint.setAirportName(locationInfo.getAirportName());
        endpoint.setCityName(locationInfo.getCityName());
        endpoint.setCountryCode(locationInfo.getCountryCode());

        return endpoint;
    }

    private FlightEndpoint mapToFlightEndpointFromAmadeus(FlightOffersResponse.FlightEndpoint endpointData, FlightOffersResponse.Dictionaries dictionaries) {
        FlightEndpoint endpoint = new FlightEndpoint();
        String code = endpointData.getIataCode();
        endpoint.setIataCode(code);
        //endpoint.setIataCode(endpointData.getIataCode());
        endpoint.setTerminal(endpointData.getTerminal());
        endpoint.setAt(parseDateTime(endpointData.getAt()));
        endpoint.setAirportName(dictionaryService.resolveAirportName(code));

        LocationInfo locationInfo = getLocationInfoFromAmadeus(endpointData.getIataCode(), dictionaries);
        //endpoint.setAirportName(locationInfo.getAirportName());
        endpoint.setCityName(locationInfo.getCityName());
        endpoint.setCountryCode(locationInfo.getCountryCode());
        //endpoint.setAirportName(iataCache.airportName(code));

        return endpoint;
    }

    private Price mapToPrice(FlightSearchResponse.PriceData priceData, FlightSearchResponse.Dictionaries dictionaries) {
        Price price = new Price();
        price.setCurrency(priceData.getCurrency());
        price.setCurrencyName(getCurrencyNameFromSearchResponse(priceData.getCurrency(), dictionaries));
        price.setTotal(priceData.getTotal());
        price.setBase(priceData.getBase());
        price.setGrandTotal(priceData.getGrandTotal());

        if (priceData.getFees() != null) {
            List<Price.Fee> fees = priceData.getFees().stream()
                    .map(feeData -> {
                        Price.Fee fee = new Price.Fee();
                        fee.setAmount(feeData.getAmount());
                        fee.setType(feeData.getType());
                        return fee;
                    })
                    .collect(Collectors.toList());
            price.setFees(fees);
        }

        if (priceData.getTaxes() != null) {
            List<Price.Tax> taxes = priceData.getTaxes().stream()
                    .map(taxData -> {
                        Price.Tax tax = new Price.Tax();
                        tax.setAmount(taxData.getAmount());
                        tax.setCode(taxData.getCode());
                        return tax;
                    })
                    .collect(Collectors.toList());
            price.setTaxes(taxes);
        }

        return price;
    }

    private Price mapToPriceFromAmadeus(FlightOffersResponse.Price priceData, FlightOffersResponse.Dictionaries dictionaries) {
        Price price = new Price();
        price.setCurrency(priceData.getCurrency());
        price.setCurrencyName(getCurrencyNameFromAmadeus(priceData.getCurrency(), dictionaries));
        price.setTotal(priceData.getTotal());
        price.setBase(priceData.getBase());
        price.setGrandTotal(priceData.getGrandTotal());

        if (priceData.getFees() != null) {
            List<Price.Fee> fees = priceData.getFees().stream()
                    .map(feeData -> {
                        Price.Fee fee = new Price.Fee();
                        fee.setAmount(feeData.getAmount());
                        fee.setType(feeData.getType());
                        return fee;
                    })
                    .collect(Collectors.toList());
            price.setFees(fees);
        }

        return price;
    }

    private TravelerPricing mapToTravelerPricing(FlightSearchResponse.TravelerPricingData travelerPricingData, FlightSearchResponse.Dictionaries dictionaries) {
        TravelerPricing travelpricing = new TravelerPricing();
        travelpricing.setTravelerId(travelerPricingData.getTravelerId());
        travelpricing.setFareOption(travelerPricingData.getFareOption());
        travelpricing.setTravelerType(travelerPricingData.getTravelerType());

        if (travelerPricingData.getPrice() != null) {
            travelpricing.setPrice(mapToPrice(travelerPricingData.getPrice(), dictionaries));
        }

        if (travelerPricingData.getFareDetailsBySegment() != null) {
            List<TravelerPricing.FareDetailsBySegment> fareDetails = travelerPricingData.getFareDetailsBySegment().stream()
                    .map(fareData -> {
                        TravelerPricing.FareDetailsBySegment fareDetail = new TravelerPricing.FareDetailsBySegment();
                        fareDetail.setSegmentId(fareData.getSegmentId());
                        fareDetail.setCabin(fareData.getCabin());
                        fareDetail.setFareBasis(fareData.getFareBasis());
                        fareDetail.setBrandedFare(fareData.getBrandedFare());
                        fareDetail.setBrandedFareLabel(fareData.getBrandedFareLabel());
                        fareDetail.setFareClass(fareData.getFareClass());

                        if (fareData.getIncludedCheckedBags() != null) {
                            TravelerPricing.BaggageAllowance checkedBags = new TravelerPricing.BaggageAllowance();
                            checkedBags.setQuantity(fareData.getIncludedCheckedBags().getQuantity());
                            checkedBags.setWeight(fareData.getIncludedCheckedBags().getWeight());
                            checkedBags.setWeightUnit(fareData.getIncludedCheckedBags().getWeightUnit());
                            fareDetail.setIncludedCheckedBags(checkedBags);
                        }

                        if (fareData.getIncludedCabinBags() != null) {
                            TravelerPricing.BaggageAllowance cabinBags = new TravelerPricing.BaggageAllowance();
                            cabinBags.setQuantity(fareData.getIncludedCabinBags().getQuantity());
                            cabinBags.setWeight(fareData.getIncludedCabinBags().getWeight());
                            cabinBags.setWeightUnit(fareData.getIncludedCabinBags().getWeightUnit());
                            fareDetail.setIncludedCabinBags(cabinBags);
                        }

                        if (fareData.getAmenities() != null){
                            List<TravelerPricing.AmenityDetails> ams = fareData.getAmenities().stream().map(ad -> {
                                TravelerPricing.AmenityProvider p = new TravelerPricing.AmenityProvider();
                                p.setName(ad.getAmenityProvider().getName());
                                TravelerPricing.AmenityDetails amd = new TravelerPricing.AmenityDetails();
                                amd.setDescription(ad.getDescription());
                                amd.setChargeable(ad.getIsChargeable());
                                amd.setAmenityType(ad.getAmenityType());
                                amd.setAmenityProvider(p);
                                return amd;
                            }).collect(Collectors.toList());
                            fareDetail.setAmenities(ams);
                        }
                        return fareDetail;
                    })
                    .collect(Collectors.toList());
            travelpricing.setFareDetailsBySegment(fareDetails);
        }

        return travelpricing;
    }

    private TravelerPricing mapToTravelerPricingFromAmadeus(FlightOffersResponse.TravelerPricing travelerPricingData, FlightOffersResponse.Dictionaries dictionaries) {
        TravelerPricing travelpricing = new TravelerPricing();
        travelpricing.setTravelerId(travelerPricingData.getTravelerId());
        travelpricing.setFareOption(travelerPricingData.getFareOption());
        travelpricing.setTravelerType(travelerPricingData.getTravelerType());

        if (travelerPricingData.getPrice() != null) {
            travelpricing.setPrice(mapToPriceFromAmadeus(travelerPricingData.getPrice(), dictionaries));
        }

        if (travelerPricingData.getFareDetailsBySegment() != null) {
            List<TravelerPricing.FareDetailsBySegment> fareDetails = travelerPricingData.getFareDetailsBySegment().stream()
                    .map(fareData -> {
                        TravelerPricing.FareDetailsBySegment fareDetail = new TravelerPricing.FareDetailsBySegment();
                        fareDetail.setSegmentId(fareData.getSegmentId());
                        fareDetail.setCabin(fareData.getCabin());
                        fareDetail.setFareBasis(fareData.getFareBasis());
                        fareDetail.setBrandedFare(fareData.getBrandedFare());
                        fareDetail.setFareClass(fareData.getFareClass());

                        if (fareData.getIncludedCheckedBags() != null) {
                            TravelerPricing.BaggageAllowance checkedBags = new TravelerPricing.BaggageAllowance();
                            checkedBags.setQuantity(fareData.getIncludedCheckedBags().getQuantity());
                            checkedBags.setWeight(fareData.getIncludedCheckedBags().getWeight());
                            checkedBags.setWeightUnit(fareData.getIncludedCheckedBags().getWeightUnit());
                            fareDetail.setIncludedCheckedBags(checkedBags);
                        }

                        if (fareData.getIncludedCabinBags() != null) {
                            TravelerPricing.BaggageAllowance cabinBags = new TravelerPricing.BaggageAllowance();
                            cabinBags.setQuantity(fareData.getIncludedCabinBags().getQuantity());
                            cabinBags.setWeight(fareData.getIncludedCabinBags().getWeight());
                            cabinBags.setWeightUnit(fareData.getIncludedCabinBags().getWeightUnit());
                            fareDetail.setIncludedCabinBags(cabinBags);
                        }

                        return fareDetail;
                    })
                    .collect(Collectors.toList());
            travelpricing.setFareDetailsBySegment(fareDetails);
        }

        return travelpricing;
    }

    // Mandame un saludo Juan
    private String getCarrierNameFromSearchResponse(String carrierCode, FlightSearchResponse.Dictionaries dictionaries) {
        if (carrierCode == null || dictionaries == null || dictionaries.getCarriers() == null) {
            return null;
        }
        return dictionaries.getCarriers().get(carrierCode);
    }

    private String getAircraftNameFromSearchResponse(String aircraftCode, FlightSearchResponse.Dictionaries dictionaries) {
        if (aircraftCode == null || dictionaries == null || dictionaries.getAircraft() == null) {
            return null;
        }
        return dictionaries.getAircraft().get(aircraftCode);
    }

    private String getCurrencyNameFromSearchResponse(String currencyCode, FlightSearchResponse.Dictionaries dictionaries) {
        if (currencyCode == null || dictionaries == null || dictionaries.getCurrencies() == null) {
            return null;
        }
        return dictionaries.getCurrencies().get(currencyCode);
    }

    private LocationInfo getLocationInfoFromSearchResponse(String iataCode, FlightSearchResponse.Dictionaries dictionaries) {
        LocationInfo info = new LocationInfo();

        if (iataCode == null || dictionaries == null || dictionaries.getLocations() == null) {
            return info;
        }

        FlightSearchResponse.LocationDictionary location = dictionaries.getLocations().get(iataCode);
        if (location != null) {
            info.setCityName(location.getCityCode());
            info.setCountryCode(location.getCountryCode());
            info.setAirportName(iataCache.airportName(iataCode));
        }

        return info;
    }

    private String getCarrierNameFromAmadeus(String carrierCode, FlightOffersResponse.Dictionaries dictionaries) {
        if (carrierCode == null || dictionaries == null || dictionaries.getCarriers() == null) {
            return null;
        }
        return dictionaries.getCarriers().get(carrierCode);
    }

    private String getAircraftNameFromAmadeus(String aircraftCode, FlightOffersResponse.Dictionaries dictionaries) {
        if (aircraftCode == null || dictionaries == null || dictionaries.getAircraft() == null) {
            return null;
        }
        return dictionaries.getAircraft().get(aircraftCode);
    }

    private String getCurrencyNameFromAmadeus(String currencyCode, FlightOffersResponse.Dictionaries dictionaries) {
        if (currencyCode == null || dictionaries == null || dictionaries.getCurrencies() == null) {
            return null;
        }
        return dictionaries.getCurrencies().get(currencyCode);
    }

    private LocationInfo getLocationInfoFromAmadeus(String iataCode, FlightOffersResponse.Dictionaries dictionaries) {
        LocationInfo info = new LocationInfo();

        if (iataCode == null || dictionaries == null || dictionaries.getLocations() == null) {
            return info;
        }

        FlightOffersResponse.LocationDict location = dictionaries.getLocations().get(iataCode);
        if (location != null) {
            info.setCityName(location.getCityCode());
            info.setCountryCode(location.getCountryCode());
            info.setAirportName(iataCache.airportName(iataCode));
        }

        return info;
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }

        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                if (dateTimeString.contains("T") || dateTimeString.length() > 10) {
                    return LocalDateTime.parse(dateTimeString, formatter);
                } else {
                    return LocalDateTime.parse(dateTimeString + "T00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                }
            } catch (DateTimeParseException e) {
                continue;
            }
        }

        System.err.println("Warning: Could not parse date/time: " + dateTimeString);
        return null;
    }

    private static class LocationInfo {
        private String airportName;
        private String cityName;
        private String countryCode;

        public String getAirportName() { return airportName; }
        public void setAirportName(String airportName) { this.airportName = airportName; }

        public String getCityName() { return cityName; }
        public void setCityName(String cityName) { this.cityName = cityName; }

        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    }
}