package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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

        public FareDetailsBySegment() {}

        public String getSegmentId() { return segmentId; }
        public void setSegmentId(String segmentId) { this.segmentId = segmentId; }

        public String getCabin() { return cabin; }
        public void setCabin(String cabin) { this.cabin = cabin; }

        public String getFareBasis() { return fareBasis; }
        public void setFareBasis(String fareBasis) { this.fareBasis = fareBasis; }

        public String getBrandedFare() { return brandedFare; }
        public void setBrandedFare(String brandedFare) { this.brandedFare = brandedFare; }

        public String getBrandedFareLabel() { return brandedFareLabel; }
        public void setBrandedFareLabel(String brandedFareLabel) { this.brandedFareLabel = brandedFareLabel; }

        public String getFareClass() { return fareClass; }
        public void setFareClass(String fareClass) { this.fareClass = fareClass; }

        public BaggageAllowance getIncludedCheckedBags() { return includedCheckedBags; }
        public void setIncludedCheckedBags(BaggageAllowance includedCheckedBags) { this.includedCheckedBags = includedCheckedBags; }

        public BaggageAllowance getIncludedCabinBags() { return includedCabinBags; }
        public void setIncludedCabinBags(BaggageAllowance includedCabinBags) { this.includedCabinBags = includedCabinBags; }

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
}
