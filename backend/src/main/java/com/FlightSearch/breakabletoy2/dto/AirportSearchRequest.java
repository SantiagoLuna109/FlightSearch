package com.FlightSearch.breakabletoy2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AirportSearchRequest {

    @NotBlank(message = "Keyword is required")
    @Size(min = 2, message = "Keyword must be at least 2 characters")
    private String keyword;

    private String countryCode;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
