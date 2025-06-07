package com.FlightSearch.breakabletoy2.controller;

import com.FlightSearch.breakabletoy2.dto.FlightSearchRequest;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import com.FlightSearch.breakabletoy2.model.Flight;
import com.FlightSearch.breakabletoy2.service.FlightSearchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/flights")
@CrossOrigin(origins = "*")
@Validated
public class FlightController {

    private static final Logger logger = LoggerFactory.getLogger(FlightController.class);

    private final FlightSearchService flightSearchService;

    public FlightController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchFlights(@Valid @RequestBody FlightSearchRequest request) {
        try {
            logger.info("Flight search request: {}", request);

            List<Flight> flights = flightSearchService.searchFlights(request);

            Map<String, Object> searchCriteria = new HashMap<>();
            searchCriteria.put("origin", request.getOriginLocationCode());
            searchCriteria.put("destination", request.getDestinationLocationCode());
            searchCriteria.put("departureDate", request.getDepartureDate());
            searchCriteria.put("returnDate", request.getReturnDate());
            searchCriteria.put("adults", request.getAdults());
            searchCriteria.put("children", request.getChildren() != null ? request.getChildren() : 0);
            searchCriteria.put("infants", request.getInfants() != null ? request.getInfants() : 0);
            searchCriteria.put("currency", request.getCurrencyCode());
            searchCriteria.put("roundTrip", request.isRoundTrip());

            return ResponseEntity.ok(Map.of(
                    "data", flights,
                    "count", flights.size(),
                    "searchCriteria", searchCriteria
            ));

        } catch (IllegalArgumentException e) {
            logger.error("Invalid flight search request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", "Invalid request",
                            "message", e.getMessage(),
                            "code", "INVALID_REQUEST"
                    ));
        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error during flight search: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "error", "External API error",
                            "message", e.getMessage(),
                            "code", "AMADEUS_API_ERROR"
                    ));
        } catch (Exception e) {
            logger.error("Unexpected error during flight search: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Internal server error",
                            "message", "An unexpected error occurred",
                            "code", "INTERNAL_ERROR"
                    ));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchFlightsGet(
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$", message = "Origin must be a valid 3-letter IATA code")
            String origin,

            @RequestParam @Pattern(regexp = "^[A-Z]{3}$", message = "Destination must be a valid 3-letter IATA code")
            String destination,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate departureDate,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate returnDate,

            @RequestParam(defaultValue = "1") @Min(value = 1, message = "At least 1 adult required") @Max(value = 9, message = "Maximum 9 adults")
            Integer adults,

            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Children cannot be negative") @Max(value = 8, message = "Maximum 8 children")
            Integer children,

            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Infants cannot be negative") @Max(value = 8, message = "Maximum 8 infants")
            Integer infants,

            @RequestParam(defaultValue = "USD") @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter code")
            String currency,

            @RequestParam(required = false)
            String travelClass,

            @RequestParam(defaultValue = "false")
            Boolean nonStop,

            @RequestParam(defaultValue = "250") @Min(value = 1, message = "Max must be at least 1") @Max(value = 250, message = "Max cannot exceed 250")
            Integer max,

            @RequestParam(required = false)
            String sortBy,

            @RequestParam(required = false)
            String includedAirlines,

            @RequestParam(required = false)
            String excludedAirlines) {

        try {
            FlightSearchRequest request = new FlightSearchRequest();
            request.setOriginLocationCode(origin.toUpperCase());
            request.setDestinationLocationCode(destination.toUpperCase());
            request.setDepartureDate(departureDate);
            request.setReturnDate(returnDate);
            request.setAdults(adults);
            request.setChildren(children);
            request.setInfants(infants);
            request.setCurrencyCode(currency.toUpperCase());
            request.setTravelClass(travelClass);
            request.setNonStop(nonStop);
            request.setMax(max);
            request.setIncludedAirlineCodes(includedAirlines);
            request.setExcludedAirlineCodes(excludedAirlines);

            logger.info("Flight search GET request: {}", request);

            List<Flight> flights;
            if (sortBy != null && !sortBy.trim().isEmpty()) {
                flights = flightSearchService.searchFlightsAndSort(request, sortBy);
            } else {
                flights = flightSearchService.searchFlights(request);
            }

            Map<String, Object> searchCriteria = new HashMap<>();
            searchCriteria.put("origin", request.getOriginLocationCode());
            searchCriteria.put("destination", request.getDestinationLocationCode());
            searchCriteria.put("departureDate", request.getDepartureDate());
            searchCriteria.put("returnDate", request.getReturnDate());
            searchCriteria.put("adults", request.getAdults());
            searchCriteria.put("children", request.getChildren());
            searchCriteria.put("infants", request.getInfants());
            searchCriteria.put("currency", request.getCurrencyCode());
            searchCriteria.put("roundTrip", request.isRoundTrip());
            searchCriteria.put("sortBy", sortBy);

            return ResponseEntity.ok(Map.of(
                    "data", flights,
                    "count", flights.size(),
                    "searchCriteria", searchCriteria
            ));

        } catch (IllegalArgumentException e) {
            logger.error("Invalid flight search parameters: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "error", "Invalid parameters",
                            "message", e.getMessage(),
                            "code", "INVALID_PARAMETERS"
                    ));
        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error during flight search: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "error", "External API error",
                            "message", e.getMessage(),
                            "code", "AMADEUS_API_ERROR"
                    ));
        } catch (Exception e) {
            logger.error("Unexpected error during flight search: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Internal server error",
                            "message", "An unexpected error occurred",
                            "code", "INTERNAL_ERROR"
                    ));
        }
    }

    @GetMapping("/one-way")
    public ResponseEntity<?> searchOneWayFlights(
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$") String origin,
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$") String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(defaultValue = "1") @Min(1) @Max(9) Integer adults,
            @RequestParam(defaultValue = "USD") @Pattern(regexp = "^[A-Z]{3}$") String currency) {

        try {
            logger.info("One-way flight search: {} -> {}, date: {}, adults: {}",
                    origin, destination, departureDate, adults);

            List<Flight> flights = flightSearchService.searchOneWayFlights(
                    origin.toUpperCase(), destination.toUpperCase(), departureDate, adults, currency.toUpperCase());

            return ResponseEntity.ok(Map.of(
                    "data", flights,
                    "count", flights.size(),
                    "searchType", "one-way",
                    "searchCriteria", Map.of(
                            "origin", origin.toUpperCase(),
                            "destination", destination.toUpperCase(),
                            "departureDate", departureDate,
                            "adults", adults,
                            "currency", currency.toUpperCase()
                    )
            ));

        } catch (Exception e) {
            logger.error("Error in one-way flight search: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Search failed", "message", e.getMessage()));
        }
    }

    @GetMapping("/round-trip")
    public ResponseEntity<?> searchRoundTripFlights(
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$") String origin,
            @RequestParam @Pattern(regexp = "^[A-Z]{3}$") String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(defaultValue = "1") @Min(1) @Max(9) Integer adults,
            @RequestParam(defaultValue = "USD") @Pattern(regexp = "^[A-Z]{3}$") String currency) {

        try {
            logger.info("Round-trip flight search: {} -> {}, departure: {}, return: {}, adults: {}",
                    origin, destination, departureDate, returnDate, adults);

            List<Flight> flights = flightSearchService.searchRoundTripFlights(
                    origin.toUpperCase(), destination.toUpperCase(), departureDate, returnDate, adults, currency.toUpperCase());

            return ResponseEntity.ok(Map.of(
                    "data", flights,
                    "count", flights.size(),
                    "searchType", "round-trip",
                    "searchCriteria", Map.of(
                            "origin", origin.toUpperCase(),
                            "destination", destination.toUpperCase(),
                            "departureDate", departureDate,
                            "returnDate", returnDate,
                            "adults", adults,
                            "currency", currency.toUpperCase()
                    )
            ));

        } catch (Exception e) {
            logger.error("Error in round-trip flight search: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Search failed", "message", e.getMessage()));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Flight search endpoints are working",
                "timestamp", System.currentTimeMillis(),
                "availableEndpoints", List.of(
                        "POST /api/v1/flights/search",
                        "GET /api/v1/flights/search?origin={origin}&destination={destination}&departureDate={date}&adults={adults}",
                        "GET /api/v1/flights/one-way?origin={origin}&destination={destination}&departureDate={date}&adults={adults}",
                        "GET /api/v1/flights/round-trip?origin={origin}&destination={destination}&departureDate={date}&returnDate={date}&adults={adults}"
                ),
                "supportedSortOptions", List.of(
                        "price", "price_asc", "price_desc",
                        "duration", "duration_asc", "duration_desc",
                        "departure", "departure_asc", "departure_desc",
                        "stops", "stops_asc", "stops_desc"
                ),
                "supportedCurrencies", List.of("USD", "EUR", "MXN", "GBP", "CAD", "AUD"),
                "supportedTravelClasses", List.of("ECONOMY", "PREMIUM_ECONOMY", "BUSINESS", "FIRST")
        ));
    }
}