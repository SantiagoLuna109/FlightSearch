package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelerPricing {

    private String travelerId;
    private String fareOption;
    private String travelerType;
    private Price price;
    private List<FareDetailsBySegment> fareDetailsBySegment;

    public TravelerPricing() {}

    public String getTravelerId() { return travelerId; }
    public void setTravelerId(String travelerId) { this.travelerId = travelerId; }

    public String getFareOption() { return fareOption; }
    public void setFareOption(String fareOption) { this.fareOption = fareOption; }

    public String getTravelerType() { return travelerType; }
    public void setTravelerType(String travelerType) { this.travelerType = travelerType; }

    public Price getPrice() { return price; }
    public void setPrice(Price price) { this.price = price; }

    public List<FareDetailsBySegment> getFareDetailsBySegment() { return fareDetailsBySegment; }
    public void setFareDetailsBySegment(List<FareDetailsBySegment> fareDetailsBySegment) { this.fareDetailsBySegment = fareDetailsBySegment; }

    @Override
    public String toString() {
        return "TravelerPricing{" +
                "travelerId='" + travelerId + '\'' +
                ", fareOption='" + fareOption + '\'' +
                ", travelerType='" + travelerType + '\'' +
                ", price=" + price +
                '}';
    }

    @Setter
    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

        public FareDetailsBySegment() {}

        @Override
        public String toString() {
            return "FareDetailsBySegment{" +
                    "segmentId='" + segmentId + '\'' +
                    ", cabin='" + cabin + '\'' +
                    ", fareClass='" + fareClass + '\'' +
                    '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BaggageAllowance {
        private Integer quantity;
        private Integer weight;
        private String weightUnit;

        public BaggageAllowance() {}

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Integer getWeight() { return weight; }
        public void setWeight(Integer weight) { this.weight = weight; }

        public String getWeightUnit() { return weightUnit; }
        public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }

        public String getFormattedWeight() {
            if (weight != null && weightUnit != null) {
                return weight + " " + weightUnit;
            }
            return null;
        }

        @Override
        public String toString() {
            return "BaggageAllowance{" +
                    "quantity=" + quantity +
                    ", weight=" + weight +
                    ", weightUnit='" + weightUnit + '\'' +
                    '}';
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AmenityProvider {
        @JsonProperty("name")
        private String name;

        public AmenityProvider() {}
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AmenityDetails {
        @JsonProperty("description")
        private String description;

        @JsonProperty("isChargeable")
        private boolean chargeable;

        @JsonProperty("amenityType")
        private String amenityType;

        @JsonProperty("amenityProvider")
        private AmenityProvider amenityProvider;

        public AmenityDetails() {}

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public boolean isChargeable() { return chargeable; }
        public void setChargeable(boolean chargeable) { this.chargeable = chargeable; }

        public String getAmenityType() { return amenityType; }
        public void setAmenityType(String amenityType) { this.amenityType = amenityType; }

        public AmenityProvider getAmenityProvider() { return amenityProvider; }
        public void setAmenityProvider(AmenityProvider amenityProvider) {
            this.amenityProvider = amenityProvider;
        }
    }
}
