package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Operating {

    private String carrierCode;

    public Operating() {}

    public Operating(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierCode() { return carrierCode; }
    public void setCarrierCode(String carrierCode) { this.carrierCode = carrierCode; }

    @Override
    public String toString() {
        return "Operating{carrierCode='" + carrierCode + "'}";
    }
}