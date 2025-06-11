package com.FlightSearch.breakabletoy2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

public class FlightFilterRequest {

    private List<String> includedAirlines;
    private List<String> excludedAirlines;

    @Min(value = 0, message = "Maximum stops cannot be negative")
    @Max(value = 3, message = "Maximum stops cannot exceed 3")
    private Integer maxStops;

    @Min(value = 0, message = "Minimum price cannot be negative")
    private BigDecimal minPrice;

    @Min(value = 0, message = "Maximum price cannot be negative")
    private BigDecimal maxPrice;

    private String travelClass;

    private LocalTime departureTimeFrom;
    private LocalTime departureTimeTo;

    private LocalTime arrivalTimeFrom;
    private LocalTime arrivalTimeTo;

    @Min(value = 0, message = "Maximum flight duration cannot be negative")
    private Integer maxFlightDurationHours;

    private Boolean nonStopOnly;

    private Boolean refundableOnly;

    private Boolean includedBagsOnly;

    public FlightFilterRequest() {}

    public List<String> getIncludedAirlines() {
        return includedAirlines;
    }

    public void setIncludedAirlines(List<String> includedAirlines) {
        this.includedAirlines = includedAirlines;
    }

    public List<String> getExcludedAirlines() {
        return excludedAirlines;
    }

    public void setExcludedAirlines(List<String> excludedAirlines) {
        this.excludedAirlines = excludedAirlines;
    }

    public Integer getMaxStops() {
        return maxStops;
    }

    public void setMaxStops(Integer maxStops) {
        this.maxStops = maxStops;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public LocalTime getDepartureTimeFrom() {
        return departureTimeFrom;
    }

    public void setDepartureTimeFrom(LocalTime departureTimeFrom) {
        this.departureTimeFrom = departureTimeFrom;
    }

    public LocalTime getDepartureTimeTo() {
        return departureTimeTo;
    }

    public void setDepartureTimeTo(LocalTime departureTimeTo) {
        this.departureTimeTo = departureTimeTo;
    }

    public LocalTime getArrivalTimeFrom() {
        return arrivalTimeFrom;
    }

    public void setArrivalTimeFrom(LocalTime arrivalTimeFrom) {
        this.arrivalTimeFrom = arrivalTimeFrom;
    }

    public LocalTime getArrivalTimeTo() {
        return arrivalTimeTo;
    }

    public void setArrivalTimeTo(LocalTime arrivalTimeTo) {
        this.arrivalTimeTo = arrivalTimeTo;
    }

    public Integer getMaxFlightDurationHours() {
        return maxFlightDurationHours;
    }

    public void setMaxFlightDurationHours(Integer maxFlightDurationHours) {
        this.maxFlightDurationHours = maxFlightDurationHours;
    }

    public Boolean getNonStopOnly() {
        return nonStopOnly;
    }

    public void setNonStopOnly(Boolean nonStopOnly) {
        this.nonStopOnly = nonStopOnly;
    }

    public Boolean getRefundableOnly() {
        return refundableOnly;
    }

    public void setRefundableOnly(Boolean refundableOnly) {
        this.refundableOnly = refundableOnly;
    }

    public Boolean getIncludedBagsOnly() {
        return includedBagsOnly;
    }

    public void setIncludedBagsOnly(Boolean includedBagsOnly) {
        this.includedBagsOnly = includedBagsOnly;
    }
}
