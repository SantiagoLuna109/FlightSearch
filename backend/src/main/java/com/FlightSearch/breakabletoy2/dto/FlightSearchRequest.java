package com.FlightSearch.breakabletoy2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.FlightSearch.breakabletoy2.dto.FlightFilterRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightSearchRequest {

    @NotBlank(message = "Origin airport code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be a valid 3-letter IATA code")
    private String originLocationCode;

    @NotBlank(message = "Destination airport code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be a valid 3-letter IATA code")
    private String destinationLocationCode;

    @NotNull(message = "Departure date is required")
    @Future(message = "Departure date must be in the future")
    private LocalDate departureDate;

    @Future(message = "Return date must be in the future")
    private LocalDate returnDate;

    @Min(value = 1, message = "At least 1 adult is required")
    @Max(value = 9, message = "Maximum 9 adults allowed")
    private Integer adults = 1;

    @Min(value = 0, message = "Children count cannot be negative")
    @Max(value = 8, message = "Maximum 8 children allowed")
    private Integer children = 0;

    @Min(value = 0, message = "Infants count cannot be negative")
    @Max(value = 8, message = "Maximum 8 infants allowed")
    private Integer infants = 0;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter code")
    private String currencyCode = "USD";

    private String travelClass;

    private Boolean nonStop = false;

    @Min(value = 1, message = "Maximum results must be at least 1")
    @Max(value = 250, message = "Maximum results cannot exceed 250")
    private Integer max = 250;

    private String includedAirlineCodes;
    private String excludedAirlineCodes;

    private List<String> includedAirlines;
    private List<String> excludedAirlines;

    @Min(value = 0, message = "Maximum stops cannot be negative")
    @Max(value = 3, message = "Maximum stops cannot exceed 3")
    private Integer maxStops;

    @DecimalMin(value = "0.0", inclusive = false, message = "Minimum price must be greater than 0")
    private BigDecimal minPrice;

    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum price must be greater than 0")
    private BigDecimal maxPrice;

    private LocalTime earliestDeparture;
    private LocalTime latestDeparture;
    private LocalTime earliestArrival;
    private LocalTime latestArrival;

    @Min(value = 1, message = "Maximum flight duration must be at least 1 hour")
    @Max(value = 48, message = "Maximum flight duration cannot exceed 48 hours")
    private Integer maxFlightDurationHours;

    private String sortBy = "price";
    private String sortOrder = "asc";

    @Valid
    private FlightFilterRequest filters;

    public FlightSearchRequest() {}

    public String getOriginLocationCode() { return originLocationCode; }
    public void setOriginLocationCode(String originLocationCode) {
        this.originLocationCode = originLocationCode != null ? originLocationCode.toUpperCase() : null;
    }

    public String getDestinationLocationCode() { return destinationLocationCode; }
    public void setDestinationLocationCode(String destinationLocationCode) {
        this.destinationLocationCode = destinationLocationCode != null ? destinationLocationCode.toUpperCase() : null;
    }

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Integer getAdults() { return adults; }
    public void setAdults(Integer adults) { this.adults = adults; }

    public Integer getChildren() { return children; }
    public void setChildren(Integer children) { this.children = children; }

    public Integer getInfants() { return infants; }
    public void setInfants(Integer infants) { this.infants = infants; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode != null ? currencyCode.toUpperCase() : "USD";
    }

    public String getTravelClass() { return travelClass; }
    public void setTravelClass(String travelClass) { this.travelClass = travelClass; }

    public Boolean getNonStop() { return nonStop; }
    public void setNonStop(Boolean nonStop) { this.nonStop = nonStop; }

    public Integer getMax() { return max; }
    public void setMax(Integer max) { this.max = max; }

    public String getIncludedAirlineCodes() { return includedAirlineCodes; }
    public void setIncludedAirlineCodes(String includedAirlineCodes) {
        this.includedAirlineCodes = includedAirlineCodes;
        if (includedAirlineCodes != null && !includedAirlineCodes.trim().isEmpty()) {
            this.includedAirlines = List.of(includedAirlineCodes.split(","));
        }
    }

    public String getExcludedAirlineCodes() { return excludedAirlineCodes; }
    public void setExcludedAirlineCodes(String excludedAirlineCodes) {
        this.excludedAirlineCodes = excludedAirlineCodes;
        if (excludedAirlineCodes != null && !excludedAirlineCodes.trim().isEmpty()) {
            this.excludedAirlines = List.of(excludedAirlineCodes.split(","));
        }
    }

    public List<String> getIncludedAirlines() { return includedAirlines; }
    public void setIncludedAirlines(List<String> includedAirlines) { this.includedAirlines = includedAirlines; }

    public List<String> getExcludedAirlines() { return excludedAirlines; }
    public void setExcludedAirlines(List<String> excludedAirlines) { this.excludedAirlines = excludedAirlines; }

    public Integer getMaxStops() { return maxStops; }
    public void setMaxStops(Integer maxStops) { this.maxStops = maxStops; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public LocalTime getEarliestDeparture() { return earliestDeparture; }
    public void setEarliestDeparture(LocalTime earliestDeparture) { this.earliestDeparture = earliestDeparture; }

    public LocalTime getLatestDeparture() { return latestDeparture; }
    public void setLatestDeparture(LocalTime latestDeparture) { this.latestDeparture = latestDeparture; }

    public LocalTime getEarliestArrival() { return earliestArrival; }
    public void setEarliestArrival(LocalTime earliestArrival) { this.earliestArrival = earliestArrival; }

    public LocalTime getLatestArrival() { return latestArrival; }
    public void setLatestArrival(LocalTime latestArrival) { this.latestArrival = latestArrival; }

    public Integer getMaxFlightDurationHours() { return maxFlightDurationHours; }
    public void setMaxFlightDurationHours(Integer maxFlightDurationHours) { this.maxFlightDurationHours = maxFlightDurationHours; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public String getSortOrder() { return sortOrder; }
    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

    public FlightFilterRequest getFilters() { return filters; }
    public void setFilters(FlightFilterRequest filters) { this.filters = filters; }

    public boolean isRoundTrip() {
        return returnDate != null;
    }

    public int getTotalPassengers() {
        return (adults != null ? adults : 0) +
                (children != null ? children : 0) +
                (infants != null ? infants : 0);
    }

    public boolean isValidPassengerCount() {
        int totalSeated = (adults != null ? adults : 0) + (children != null ? children : 0);
        int totalInfants = infants != null ? infants : 0;
        return totalSeated <= 9 && totalInfants <= (adults != null ? adults : 0);
    }

    @AssertTrue(message = "Return date must be after departure date")
    public boolean isReturnDateValid() {
        if (returnDate == null) {
            return true;
        }
        return !returnDate.isBefore(departureDate);
    }

    @AssertTrue(message = "Number of infants cannot exceed number of adults")
    public boolean isInfantsCountValid() {
        return infants <= adults;
    }

    @AssertTrue(message = "Total travelers (adults + children) cannot exceed 9")
    public boolean isTotalTravelersValid() {
        return (adults + children) <= 9;
    }

    @AssertTrue(message = "Maximum price must be greater than minimum price")
    public boolean isPriceRangeValid() {
        if (minPrice == null || maxPrice == null) {
            return true;
        }
        return maxPrice.compareTo(minPrice) > 0;
    }

    @AssertTrue(message = "Cannot specify both included and excluded airlines")
    public boolean isAirlineFiltersValid() {
        return (includedAirlines == null || includedAirlines.isEmpty()) ||
                (excludedAirlines == null || excludedAirlines.isEmpty());
    }

    @AssertTrue(message = "Latest departure time must be after earliest departure time")
    public boolean isDepartureTimeRangeValid() {
        if (earliestDeparture == null || latestDeparture == null) {
            return true;
        }
        return latestDeparture.isAfter(earliestDeparture);
    }

    @AssertTrue(message = "Latest arrival time must be after earliest arrival time")
    public boolean isArrivalTimeRangeValid() {
        if (earliestArrival == null || latestArrival == null) {
            return true;
        }
        return latestArrival.isAfter(earliestArrival);
    }

    @Override
    public String toString() {
        return "FlightSearchRequest{" +
                "origin='" + originLocationCode + '\'' +
                ", destination='" + destinationLocationCode + '\'' +
                ", departureDate=" + departureDate +
                ", returnDate=" + returnDate +
                ", adults=" + adults +
                ", children=" + children +
                ", infants=" + infants +
                ", currency='" + currencyCode + '\'' +
                ", travelClass='" + travelClass + '\'' +
                ", nonStop=" + nonStop +
                ", filters=" + (filters != null ? "enabled" : "none") +
                ", roundTrip=" + isRoundTrip() +
                '}';
    }
}
