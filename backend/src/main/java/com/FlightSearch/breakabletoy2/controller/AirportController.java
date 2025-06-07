package com.FlightSearch.breakabletoy2.controller;

import com.FlightSearch.breakabletoy2.model.Airport;
import com.FlightSearch.breakabletoy2.service.AirportSearchService;
import com.FlightSearch.breakabletoy2.exception.AirportNotFoundException;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/airports")
@CrossOrigin(origins = "*")
public class AirportController {

    private static final Logger logger = LoggerFactory.getLogger(AirportController.class);

    private final AirportSearchService airportSearchService;

    public AirportController(AirportSearchService airportSearchService) {
        this.airportSearchService = airportSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAirports(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "AIRPORT") String subType,
            @RequestParam(defaultValue = "10") int limit) {

        try {
            logger.info("Searching airports with keyword: {}, subType: {}, limit: {}", keyword, subType, limit);

            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Keyword parameter is required and cannot be empty"));
            }

            if (keyword.trim().length() < 2) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Keyword must be at least 2 characters long"));
            }

            List<Airport> airports = airportSearchService.searchAirports(keyword.trim(), subType, limit);

            if (airports.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "message", "No airports found for keyword: " + keyword,
                        "data", airports,
                        "count", 0
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "data", airports,
                    "count", airports.size(),
                    "keyword", keyword,
                    "subType", subType
            ));

        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error while searching airports: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "error", "External API error",
                            "message", e.getMessage(),
                            "code", "AMADEUS_API_ERROR"
                    ));
        } catch (Exception e) {
            logger.error("Unexpected error while searching airports: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Internal server error",
                            "message", "An unexpected error occurred",
                            "code", "INTERNAL_ERROR"
                    ));
        }
    }

    @GetMapping("/locations/search")
    public ResponseEntity<?> searchLocations(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "CITY,AIRPORT") String subType,
            @RequestParam(defaultValue = "10") int limit) {

        try {
            logger.info("Searching locations with keyword: {}, subType: {}, limit: {}", keyword, subType, limit);

            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Keyword parameter is required and cannot be empty"));
            }

            if (keyword.trim().length() < 2) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Keyword must be at least 2 characters long"));
            }

            List<Airport> locations = airportSearchService.searchLocations(keyword.trim(), subType, limit);

            if (locations.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "message", "No locations found for keyword: " + keyword,
                        "data", locations,
                        "count", 0
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "data", locations,
                    "count", locations.size(),
                    "keyword", keyword,
                    "subType", subType
            ));

        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error while searching locations: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "error", "External API error",
                            "message", e.getMessage(),
                            "code", "AMADEUS_API_ERROR"
                    ));
        } catch (Exception e) {
            logger.error("Unexpected error while searching locations: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Internal server error",
                            "message", "An unexpected error occurred",
                            "code", "INTERNAL_ERROR"
                    ));
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getAirportByCode(@PathVariable String code) {
        try {
            logger.info("Getting airport details for code: {}", code);

            if (code == null || code.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Airport code is required"));
            }

            if (code.trim().length() < 3 || code.trim().length() > 4) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Airport code must be 3 or 4 characters long"));
            }

            Airport airport = airportSearchService.getAirportByCode(code.trim().toUpperCase());

            return ResponseEntity.ok(Map.of(
                    "data", airport,
                    "code", code.toUpperCase()
            ));

        } catch (AirportNotFoundException e) {
            logger.warn("Airport not found for code: {}", code);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Airport not found",
                            "message", e.getMessage(),
                            "code", "AIRPORT_NOT_FOUND",
                            "searchedCode", code
                    ));
        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error while getting airport: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "error", "External API error",
                            "message", e.getMessage(),
                            "code", "AMADEUS_API_ERROR"
                    ));
        } catch (Exception e) {
            logger.error("Unexpected error while getting airport: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Internal server error",
                            "message", "An unexpected error occurred",
                            "code", "INTERNAL_ERROR"
                    ));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Airport search endpoints are working",
                "timestamp", System.currentTimeMillis(),
                "availableEndpoints", List.of(
                        "GET /api/v1/airports/search?keyword={keyword}",
                        "GET /api/v1/airports/locations/search?keyword={keyword}",
                        "GET /api/v1/airports/{code}"
                )
        ));
    }
}
