package com.FlightSearch.breakabletoy2.model.amadeus;

import java.util.List;

public class AirlineResponse {
    private List<Airline> data;

    public List<Airline> getData() {
        return data;
    }
    public void setData(List<Airline> data) {
        this.data = data;
    }
}