package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.client.AmadeusApiClient;
import com.FlightSearch.breakabletoy2.model.amadeus.LocationResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IataCache {

    private final AmadeusApiClient client;
    private final Map<String, String> airportNames = new ConcurrentHashMap<>();
    private final Map<String, String> cityNames    = new ConcurrentHashMap<>();

    public IataCache(AmadeusApiClient client) { this.client = client; }

    public String airportName(String code) {
        return airportNames.computeIfAbsent(code, c -> {
            LocationResponse loc = client.getLocationById(c);
            if (loc != null && loc.getData() != null && !loc.getData().isEmpty()) {
                Object first = loc.getData().get(0);                 // Map<?,?>
                if (first instanceof Map<?,?> m && m.get("name") != null)
                    return m.get("name").toString();
            }
            return c;
        });
    }

    public String cityName(String code) {
        return cityNames.computeIfAbsent(code, c -> {
            LocationResponse loc = client.getLocationById(c);
            if (loc != null && loc.getData() != null && !loc.getData().isEmpty()) {
                Object first = loc.getData().get(0);                 // Map<?,?>
                if (first instanceof Map<?,?> m && m.get("cityName") != null)
                    return m.get("cityName").toString();
            }
            return c;
        });
    }
}