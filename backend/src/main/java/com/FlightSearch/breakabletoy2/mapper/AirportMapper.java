package com.FlightSearch.breakabletoy2.mapper;

import com.FlightSearch.breakabletoy2.model.Airport;
import com.FlightSearch.breakabletoy2.model.amadeus.LocationResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AirportMapper {

    public Airport toAirport(LocationResponse.LocationData location) {
        if (location == null) {
            return null;
        }

        Airport airport = new Airport();
        airport.setIataCode(location.getIataCode());
        airport.setName(location.getName());
        airport.setDetailedName(location.getDetailedName());
        airport.setType(location.getType());
        airport.setSubType(location.getSubType());
        airport.setTimeZoneOffset(location.getTimeZoneOffset());

        if (location.getGeoCode() != null) {
            airport.setLatitude(location.getGeoCode().getLatitude());
            airport.setLongitude(location.getGeoCode().getLongitude());
        }

        if (location.getAddress() != null) {
            airport.setCityName(location.getAddress().getCityName());
            airport.setCityCode(location.getAddress().getCityCode());
            airport.setCountryName(location.getAddress().getCountryName());
            airport.setCountryCode(location.getAddress().getCountryCode());
            airport.setRegionCode(location.getAddress().getRegionCode());
        }

        if (location.getAnalytics() != null &&
                location.getAnalytics().getTravelers() != null) {
            airport.setRelevance(location.getAnalytics().getTravelers().getScore());
        }

        return airport;
    }

    public List<Airport> toAirportList(List<LocationResponse.LocationData> locations) {
        if (locations == null) {
            return new ArrayList<>();
        }

        List<Airport> airports = new ArrayList<>();
        for (LocationResponse.LocationData location : locations) {
            Airport airport = toAirport(location);
            if (airport != null) {
                airports.add(airport);
            }
        }
        return airports;
    }
}
