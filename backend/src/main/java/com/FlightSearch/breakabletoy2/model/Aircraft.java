package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Aircraft {

    private String code;

    public Aircraft() {}

    public Aircraft(String code) {
        this.code = code;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    @Override
    public String toString() {
        return "Aircraft{code='" + code + "'}";
    }
}
