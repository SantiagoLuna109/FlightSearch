package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.client.AmadeusApiClient;
import com.FlightSearch.breakabletoy2.model.amadeus.LocationResponse;
import com.FlightSearch.breakabletoy2.exception.AirportNotFoundException;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import com.FlightSearch.breakabletoy2.mapper.AirportMapper;
import com.FlightSearch.breakabletoy2.model.Airport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportSearchService {

    private static final Logger logger = LoggerFactory.getLogger(AirportSearchService.class);

    private final AmadeusApiClient amadeusApiClient;
    private final AirportMapper airportMapper;

    public AirportSearchService(AmadeusApiClient amadeusApiClient, AirportMapper airportMapper) {
        this.amadeusApiClient = amadeusApiClient;
        this.airportMapper = airportMapper;
    }

    public List<Airport> searchAirports(String keyword, String subType, int limit) {
        try {
            logger.info("Searching airports with keyword: '{}', subType: '{}', limit: {}", keyword, subType, limit);

            // un saludo
            if (keyword == null || keyword.trim().isEmpty()) {
                throw new IllegalArgumentException("Keyword cannot be null or empty");
            }

            String cleanKeyword = keyword.trim();
            if (cleanKeyword.length() < 2) {
                throw new IllegalArgumentException("Keyword must be at least 2 characters long");
            }

            // otro saludo
            LocationResponse response = amadeusApiClient.searchLocations(cleanKeyword, subType, limit);

            if (response == null) {
                logger.warn("Received null response from Amadeus API for keyword: {}", cleanKeyword);
                return Collections.emptyList();
            }

            if (response.getData() == null || response.getData().isEmpty()) {
                logger.info("No airports found for keyword: {}", cleanKeyword);
                return Collections.emptyList();
            }


            List<Airport> airports = response.getData().stream()
                    .map(airportMapper::toAirport)
                    .filter(airport -> airport != null)
                    .collect(Collectors.toList());

            logger.info("Found {} airports for keyword: {}", airports.size(), cleanKeyword);
            return airports;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument for airport search: {}", e.getMessage());
            throw e;
        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error during airport search: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during airport search: {}", e.getMessage(), e);
            throw new AmadeusApiException("Unexpected error during airport search: " + e.getMessage(), e);
        }
    }

    public List<Airport> searchLocations(String keyword, String subType, int limit) {
        try {
            logger.info("Searching locations with keyword: '{}', subType: '{}', limit: {}", keyword, subType, limit);


            if (keyword == null || keyword.trim().isEmpty()) {
                throw new IllegalArgumentException("Keyword cannot be null or empty");
            }

            String cleanKeyword = keyword.trim();
            if (cleanKeyword.length() < 2) {
                throw new IllegalArgumentException("Keyword must be at least 2 characters long");
            }


            LocationResponse response = amadeusApiClient.searchLocations(cleanKeyword, subType, limit);

            if (response == null) {
                logger.warn("Received null response from Amadeus API for keyword: {}", cleanKeyword);
                return Collections.emptyList();
            }

            if (response.getData() == null || response.getData().isEmpty()) {
                logger.info("No locations found for keyword: {}", cleanKeyword);
                return Collections.emptyList();
            }


            List<Airport> locations = response.getData().stream()
                    .map(airportMapper::toAirport)
                    .filter(location -> location != null)
                    .collect(Collectors.toList());

            logger.info("Found {} locations for keyword: {}", locations.size(), cleanKeyword);
            return locations;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument for location search: {}", e.getMessage());
            throw e;
        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error during location search: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during location search: {}", e.getMessage(), e);
            throw new AmadeusApiException("Unexpected error during location search: " + e.getMessage(), e);
        }
    }

    /*public Airport getAirportByCode(String code) {
        try {
            logger.info("Getting airport details for code: '{}'", code);


            if (code == null || code.trim().isEmpty()) {
                throw new IllegalArgumentException("Airport code cannot be null or empty");
            }

            String cleanCode = code.trim().toUpperCase();
            if (cleanCode.length() < 3 || cleanCode.length() > 4) {
                throw new IllegalArgumentException("Airport code must be 3 or 4 characters long");
            }

            // otro saludo
            LocationResponse response = amadeusApiClient.getLocationById(cleanCode);

            if (response == null || response.getData() == null) {
                logger.warn("No data received from Amadeus API for airport code: {}", cleanCode);
                throw new AirportNotFoundException("Airport with code '" + cleanCode + "' not found");
            }


            if (response.getData().isEmpty()) {
                throw new AirportNotFoundException("Airport with code '" + cleanCode + "' not found");
            }

            final Airport airport = airportMapper.toAirport(response.getData().get(0));

            if (airport == null) {
                logger.error("Failed to map airport data for code: {}", cleanCode);
                throw new AmadeusApiException("Failed to process airport data for code: " + cleanCode);
            }

            logger.info("Successfully retrieved airport details for code: {}", cleanCode);
            return airport;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument for airport lookup: {}", e.getMessage());
            throw e;
        } catch (AirportNotFoundException e) {
            logger.warn("Airport not found: {}", e.getMessage());
            throw e;
        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error during airport lookup: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during airport lookup: {}", e.getMessage(), e);
            throw new AmadeusApiException("Unexpected error during airport lookup: " + e.getMessage(), e);
        }
    }*/

    public Airport getAirportByCode(String rawCode) {
        String code = rawCode == null ? null : rawCode.trim().toUpperCase();

        if (code == null || code.length() != 3) {
            throw new IllegalArgumentException("IATA code must be exactly 3 letters: " + rawCode);
        }

        LocationResponse.LocationData loc = amadeusApiClient.getLocationByIata(code)
                .orElseThrow(() ->
                        new AirportNotFoundException("Airport with code '" + code + "' not found"));

        return airportMapper.toAirport(loc);
    }

    public List<Airport> searchAirportsOnly(String keyword, int limit) {
        return searchAirports(keyword, "AIRPORT", limit);
    }

    public List<Airport> searchCitiesOnly(String keyword, int limit) {
        return searchAirports(keyword, "CITY", limit);
    }

    public List<Airport> searchAirportsAndCities(String keyword, int limit) {
        return searchLocations(keyword, "AIRPORT,CITY", limit);
    }
}