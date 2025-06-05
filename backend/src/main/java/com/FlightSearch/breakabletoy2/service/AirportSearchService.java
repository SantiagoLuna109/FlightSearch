package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.client.AmadeusApiClient;
import com.FlightSearch.breakabletoy2.exception.AirportNotFoundException;
import com.FlightSearch.breakabletoy2.mapper.AirportMapper;
import com.FlightSearch.breakabletoy2.model.Airport;
import com.FlightSearch.breakabletoy2.model.amadeus.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AirportSearchService {

    private static final Logger logger = LoggerFactory.getLogger(AirportSearchService.class);

    private final AmadeusApiClient amadeusClient;
    private final AirportMapper airportMapper;

    public AirportSearchService(AmadeusApiClient amadeusClient, AirportMapper airportMapper) {
        this.amadeusClient = amadeusClient;
        this.airportMapper = airportMapper;
    }

    public List<Airport> searchAirports(String keyword) {
        return searchAirports(keyword, null);
    }

    public List<Airport> searchAirports(String keyword, String countryCode) {
        logger.info("Searching airports with keyword: {} and countryCode: {}", keyword, countryCode);

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("subType", "AIRPORT");

        if (countryCode != null && !countryCode.isEmpty()) {
            params.put("countryCode", countryCode);
        }

        try {
            LocationResponse response = amadeusClient.searchLocations(params);
            List<Airport> airports = airportMapper.toAirportList(response.getData());

            Map<String, String> cityParams = new HashMap<>();
            cityParams.put("keyword", keyword);
            cityParams.put("subType", "CITY");

            if (countryCode != null && !countryCode.isEmpty()) {
                cityParams.put("countryCode", countryCode);
            }

            LocationResponse cityResponse = amadeusClient.searchLocations(cityParams);
            List<Airport> cities = airportMapper.toAirportList(cityResponse.getData());

            airports.addAll(cities);

            List<Airport> sortedAirports = airports.stream()
                    .sorted((a, b) -> {
                        Integer scoreA = a.getRelevance() != null ? a.getRelevance() : 0;
                        Integer scoreB = b.getRelevance() != null ? b.getRelevance() : 0;
                        return scoreB.compareTo(scoreA);
                    })
                    .collect(Collectors.toList());

            logger.info("Found {} locations", sortedAirports.size());
            return sortedAirports;
        } catch (Exception e) {
            logger.error("Error searching airports", e);
            throw new RuntimeException("Failed to search airports: " + e.getMessage(), e);
        }
    }

    public Airport getAirportByCode(String iataCode) {
        logger.info("Getting airport by code: {}", iataCode);

        Map<String, String> params = new HashMap<>();
        params.put("keyword", iataCode);
        params.put("subType", "AIRPORT");

        try {
            LocationResponse response = amadeusClient.searchLocations(params);

            if (response.getData() != null && !response.getData().isEmpty()) {
                LocationResponse.LocationData location = response.getData().stream()
                        .filter(loc -> iataCode.equalsIgnoreCase(loc.getIataCode()))
                        .findFirst()
                        .orElse(response.getData().get(0));

                if (location != null) {
                    return airportMapper.toAirport(location);
                }
            }

            Map<String, String> cityParams = new HashMap<>();
            cityParams.put("keyword", iataCode);
            cityParams.put("subType", "CITY");

            LocationResponse cityResponse = amadeusClient.searchLocations(cityParams);

            if (cityResponse.getData() == null || cityResponse.getData().isEmpty()) {
                throw new AirportNotFoundException("Airport not found with code: " + iataCode);
            }

            LocationResponse.LocationData location = cityResponse.getData().stream()
                    .filter(loc -> iataCode.equalsIgnoreCase(loc.getIataCode()))
                    .findFirst()
                    .orElse(cityResponse.getData().get(0));

            return airportMapper.toAirport(location);
        } catch (AirportNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error getting airport by code", e);
            throw new RuntimeException("Failed to get airport: " + e.getMessage(), e);
        }
    }
}