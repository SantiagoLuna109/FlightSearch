package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightEndpoint {

    private String iataCode;
    private String terminal;
    private LocalDateTime at;
    private String airportName;
    private String cityName;
    private String countryCode;

    public String getAirportName() { return airportName; }
    public void setAirportName(String airportName){ this.airportName = airportName; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public FlightEndpoint() {}

    public FlightEndpoint(String iataCode, String terminal, LocalDateTime at) {
        this.iataCode = iataCode;
        this.terminal = terminal;
        this.at = at;
    }

    public String getIataCode() { return iataCode; }
    public void setIataCode(String iataCode) { this.iataCode = iataCode; }

    public String getTerminal() { return terminal; }
    public void setTerminal(String terminal) { this.terminal = terminal; }

    public LocalDateTime getAt() { return at; }
    public void setAt(LocalDateTime at) { this.at = at; }

    public String getAirportWithTerminal() {
        if (terminal != null && !terminal.trim().isEmpty()) {
            return iataCode + " (Terminal " + terminal + ")";
        }
        return iataCode;
    }

    @Override
    public String toString() {
        return "FlightEndpoint{" +
                "iataCode='" + iataCode + '\'' +
                ", terminal='" + terminal + '\'' +
                ", at=" + at +
                '}';
    }
}
