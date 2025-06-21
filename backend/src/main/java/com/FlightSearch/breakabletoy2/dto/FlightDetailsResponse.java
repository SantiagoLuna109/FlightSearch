package com.FlightSearch.breakabletoy2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetailsResponse {

    private String id;
    private String source;
    private boolean instantTicketingRequired;
    private boolean oneWay;
    private String lastTicketingDate;
    private String lastTicketingDateTime;
    private int numberOfBookableSeats;

    private FlightSummary summary;
    private List<ItineraryDetails> itineraries;
    private PriceBreakdown priceBreakdown;
    private List<String> validatingAirlineCodes;
    private List<TravelerPricingDetails> travelerPricings;

    @Getter
    @Setter
    @Builder
    public static class AmenityProvider {
        private String name;
    }

    @Getter
    @Setter
    @Builder
    public static class AmenityDetails {
        private String description;
        private boolean isChargeable;
        private String amenityType;
        private AmenityProvider amenityProvider;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightSummary {
        private LocalDateTime initialDepartureDateTime;
        private LocalDateTime finalArrivalDateTime;
        private String departureAirportName;
        private String departureAirportCode;
        private String arrivalAirportName;
        private String arrivalAirportCode;
        private String mainAirlineName;
        private String mainAirlineCode;
        private String operatingAirlineName;
        private String operatingAirlineCode;
        private String totalFlightDuration;
        private BigDecimal totalPrice;
        private BigDecimal pricePerTraveler;
        private String currency;
        private boolean hasStops;
        private int numberOfStops;
        private List<StopInfo> stops;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StopInfo {
        private String airportCode;
        private String airportName;
        private String duration;
        private LocalDateTime arrivalTime;
        private LocalDateTime departureTime;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItineraryDetails {
        private String duration;
        private List<SegmentDetails> segments;
        private List<LayoverDetails> layovers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SegmentDetails {
        private String id;
        private int numberOfStops;
        private boolean blacklistedInEU;

        private FlightEndpointDetails departure;
        private FlightEndpointDetails arrival;

        private String carrierCode;
        private String carrierName;
        private String number;
        private String flightNumber;

        private String aircraftCode;
        private String aircraftName;

        private OperatingCarrier operating;

        private String duration;
        private List<Co2EmissionDetails> co2Emissions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightEndpointDetails {
        private String iataCode;
        private String airportName;
        private String cityName;
        private String countryName;
        private String terminal;
        private LocalDateTime at;
        private String timeZoneOffset;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperatingCarrier {
        private String carrierCode;
        private String carrierName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Co2EmissionDetails {
        private int weight;
        private String weightUnit;
        private String cabin;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LayoverDetails {
        private String airportCode;
        private String airportName;
        private String duration;
        private LocalDateTime arrivalTime;
        private LocalDateTime departureTime;
        private int segmentBefore;
        private int segmentAfter;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceBreakdown {
        private String currency;
        private BigDecimal total;
        private BigDecimal base;
        private BigDecimal grandTotal;
        private List<FeeDetails> fees;
        private List<TaxDetails> taxes;
        private BigDecimal refundableTaxes;
        private List<AdditionalServiceDetails> additionalServices;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeDetails {
        private BigDecimal amount;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxDetails {
        private BigDecimal amount;
        private String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdditionalServiceDetails {
        private BigDecimal amount;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TravelerPricingDetails {
        private String travelerId;
        private String fareOption;
        private String travelerType;
        private PriceDetails price;
        private List<FareDetailsBySegment> fareDetailsBySegment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceDetails {
        private String currency;
        private BigDecimal total;
        private BigDecimal base;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FareDetailsBySegment {
        private String segmentId;
        private String cabin;
        private String fareBasis;
        private String brandedFare;
        private String brandedFareLabel;
        private String fareClass;
        private BaggageAllowance includedCheckedBags;
        private BaggageAllowance includedCabinBags;
        private List<AmenityDetails> amenities;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaggageAllowance {
        private Integer quantity;
        private Integer weight;
        private String weightUnit;
    }

}
