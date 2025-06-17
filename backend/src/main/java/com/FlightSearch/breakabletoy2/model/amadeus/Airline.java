package com.FlightSearch.breakabletoy2.model.amadeus;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Airline {
    @JsonProperty("iataCode")
    private String code;

    @JsonProperty("businessName")
    private String name;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
