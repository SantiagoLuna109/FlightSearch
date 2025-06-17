package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.client.AmadeusApiClient;
import com.FlightSearch.breakabletoy2.model.amadeus.LocationResponse;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.HashMap;

@Component
public class IataCache {

    private static final Logger log = LoggerFactory.getLogger(IataCache.class);

    private final AmadeusApiClient client;
    private final Map<String, String> airportNames = new HashMap<>();
    private final Map<String, String> cityNames    = new HashMap<>();

    public IataCache(AmadeusApiClient client) {
        this.client = client;
    }

    public String airportName(String code) {
        return airportNames.computeIfAbsent(code, c -> {
            String name = tryExtractName(() -> client.getLocationById(c));
            if (isValid(name, c)) return name;

            name = tryExtractName(() -> client.searchLocations("AIRPORT", c, 1));
            if (isValid(name, c)) return name;

            return c;
        });
    }

    public String cityName(String code) {
        return cityNames.computeIfAbsent(code, c -> {
            String name = tryExtractName(() -> client.getLocationById(c));
            if (isValid(name, c)) return name;

            name = tryExtractName(() -> client.searchLocations("CITY", c, 1));
            if (isValid(name, c)) return name;

            return c;
        });
    }

    private String tryExtractName(LocationLookup lookup) {
        try {
            LocationResponse loc = lookup.get();
            if (loc != null && loc.getData() != null && !loc.getData().isEmpty()) {
                Object first = loc.getData().get(0);
                if (first instanceof Map<?,?> m) {
                    Object detailed = m.get("detailedName");
                    if (detailed != null) return detailed.toString();
                    Object name = m.get("name");
                    if (name     != null) return name.toString();
                }
            }
        } catch (AmadeusApiException ae) {
            log.warn("Amadeus lookup failed: {}", ae.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during lookup: {}", e.getMessage(), e);
        }
        return null;
    }

    private boolean isValid(String name, String code) {
        return name != null && !name.equalsIgnoreCase(code);
    }

    @FunctionalInterface
    private interface LocationLookup {
        LocationResponse get() throws AmadeusApiException;
    }
}