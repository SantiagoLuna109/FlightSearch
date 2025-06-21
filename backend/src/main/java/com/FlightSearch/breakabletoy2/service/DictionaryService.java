package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.client.AmadeusApiClient;
import com.FlightSearch.breakabletoy2.model.amadeus.LocationResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class DictionaryService {

    private final AmadeusApiClient client;
    private final Map<String,String> airportCache = new ConcurrentHashMap<>();

    public DictionaryService(AmadeusApiClient client) {
        this.client = client;
    }

    public String resolveAirportName(String iataCode) {
        return airportCache.computeIfAbsent(iataCode, this::fetch);
    }

    private String fetch(String code) {
        Optional<LocationResponse.LocationData> maybe = client.getLocationByIata(code);
        return maybe.map(LocationResponse.LocationData::getName).orElse(code);
    }
}