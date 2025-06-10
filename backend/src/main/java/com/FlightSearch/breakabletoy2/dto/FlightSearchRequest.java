package com.FlightSearch.breakabletoy2.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

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
    public void setIncludedAirlineCodes(String includedAirlineCodes) { this.includedAirlineCodes = includedAirlineCodes; }

    public String getExcludedAirlineCodes() { return excludedAirlineCodes; }
    public void setExcludedAirlineCodes(String excludedAirlineCodes) { this.excludedAirlineCodes = excludedAirlineCodes; }

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
                ", roundTrip=" + isRoundTrip() +
                '}';
    }
}
