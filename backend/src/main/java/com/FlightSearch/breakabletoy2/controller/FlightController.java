package com.FlightSearch.breakabletoy2.controller;

import com.FlightSearch.breakabletoy2.dto.FlightSearchRequest;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import com.FlightSearch.breakabletoy2.model.Flight;
import com.FlightSearch.breakabletoy2.service.FlightSearchService;
import com.FlightSearch.breakabletoy2.service.FlightFilterService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private final FlightFilterService flightFilterService;

    public FlightController(FlightSearchService flightSearchService, FlightFilterService flightFilterService) {
        this.flightSearchService = flightSearchService;
        this.flightFilterService = flightFilterService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchFlights(@Valid @RequestBody FlightSearchRequest request) {
        try {
            logger.info("Flight search request: {}", request);

            List<Flight> flights = flightSearchService.searchFlights(request);

            Map<String, Object> searchCriteria = createSearchCriteriaMap(request);

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
            String sortOrder,

            @RequestParam(required = false)
            String includedAirlines,

            @RequestParam(required = false)
            String excludedAirlines,

            @RequestParam(required = false) @Min(value = 0, message = "Max stops cannot be negative") @Max(value = 3, message = "Max stops cannot exceed 3")
            Integer maxStops,

            @RequestParam(required = false)
            BigDecimal minPrice,

            @RequestParam(required = false)
            BigDecimal maxPrice,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime earliestDeparture,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime latestDeparture,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime earliestArrival,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime latestArrival,

            @RequestParam(required = false) @Min(value = 1, message = "Max duration must be at least 1 hour") @Max(value = 48, message = "Max duration cannot exceed 48 hours")
            Integer maxFlightDurationHours) {

        try {
            FlightSearchRequest request = buildRequestFromParameters(
                    origin, destination, departureDate, returnDate, adults, children, infants,
                    currency, travelClass, nonStop, max, sortBy, sortOrder, includedAirlines,
                    excludedAirlines, maxStops, minPrice, maxPrice, earliestDeparture,
                    latestDeparture, earliestArrival, latestArrival, maxFlightDurationHours
            );

            logger.info("Flight search GET request: {}", request);

            List<Flight> flights;
            if (sortBy != null && !sortBy.trim().isEmpty()) {
                flights = flightSearchService.searchFlightsAndSort(request, sortBy);
            } else {
                flights = flightSearchService.searchFlights(request);
            }

            Map<String, Object> searchCriteria = createSearchCriteriaMap(request);

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

    @GetMapping("/filters/airlines")
    public ResponseEntity<List<String>> getAvailableAirlines() {
        return ResponseEntity.ok(List.of(
                "AA", "DL", "UA", "WN", "BA", "LH", "AF", "KL", "IB", "AZ",
                "TK", "EK", "QR", "SQ", "CX", "JL", "NH", "KE", "TG", "VN"
        ));
    }

    @GetMapping("/filters/travel-classes")
    public ResponseEntity<List<String>> getAvailableTravelClasses() {
        return ResponseEntity.ok(List.of("ECONOMY", "PREMIUM_ECONOMY", "BUSINESS", "FIRST"));
    }

    @GetMapping("/filters/summary")
    public ResponseEntity<Map<String, Object>> getFilterSummary() {
        Map<String, Object> summary = Map.of(
                "airlines", getAvailableAirlines().getBody(),
                "travelClasses", getAvailableTravelClasses().getBody(),
                "maxStopsOptions", List.of(0, 1, 2, 3),
                "sortOptions", Map.of(
                        "sortBy", List.of("price", "duration", "departure", "arrival", "stops", "airline"),
                        "sortOrder", List.of("asc", "desc")
                ),
                "currencies", List.of("USD", "EUR", "MXN"),
                "timeRangeFormat", "HH:MM (24-hour format)",
                "maxDurationHours", 48
        );

        return ResponseEntity.ok(summary);
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
                        "GET /api/v1/flights/round-trip?origin={origin}&destination={destination}&departureDate={date}&returnDate={date}&adults={adults}",
                        "GET /api/v1/flights/filters/airlines",
                        "GET /api/v1/flights/filters/travel-classes",
                        "GET /api/v1/flights/filters/summary"
                ),
                "supportedSortOptions", List.of(
                        "price", "price_asc", "price_desc",
                        "duration", "duration_asc", "duration_desc",
                        "departure", "departure_asc", "departure_desc",
                        "stops", "stops_asc", "stops_desc"
                ),
                "supportedCurrencies", List.of("USD", "EUR", "MXN", "GBP", "CAD", "AUD"),
                "supportedTravelClasses", List.of("ECONOMY", "PREMIUM_ECONOMY", "BUSINESS", "FIRST"),
                "newFilterOptions", Map.of(
                        "maxStops", "0-3 (0 for non-stop)",
                        "priceRange", "minPrice and maxPrice parameters",
                        "timeFilters", "earliestDeparture, latestDeparture, earliestArrival, latestArrival (HH:MM format)",
                        "durationFilter", "maxFlightDurationHours (1-48 hours)",
                        "airlineFilters", "includedAirlines or excludedAirlines (comma-separated IATA codes)"
                )
        ));
    }

    private FlightSearchRequest buildRequestFromParameters(
            String origin, String destination, LocalDate departureDate, LocalDate returnDate,
            Integer adults, Integer children, Integer infants, String currency, String travelClass,
            Boolean nonStop, Integer max, String sortBy, String sortOrder, String includedAirlines,
            String excludedAirlines, Integer maxStops, BigDecimal minPrice, BigDecimal maxPrice,
            LocalTime earliestDeparture, LocalTime latestDeparture, LocalTime earliestArrival,
            LocalTime latestArrival, Integer maxFlightDurationHours) {

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
        request.setSortBy(sortBy != null ? sortBy : "price");
        request.setSortOrder(sortOrder != null ? sortOrder : "asc");
        request.setIncludedAirlineCodes(includedAirlines);
        request.setExcludedAirlineCodes(excludedAirlines);
        request.setMaxStops(maxStops);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setEarliestDeparture(earliestDeparture);
        request.setLatestDeparture(latestDeparture);
        request.setEarliestArrival(earliestArrival);
        request.setLatestArrival(latestArrival);
        request.setMaxFlightDurationHours(maxFlightDurationHours);

        return request;
    }

    private Map<String, Object> createSearchCriteriaMap(FlightSearchRequest request) {
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("origin", request.getOriginLocationCode());
        searchCriteria.put("destination", request.getDestinationLocationCode());
        searchCriteria.put("departureDate", request.getDepartureDate());
        searchCriteria.put("returnDate", request.getReturnDate());
        searchCriteria.put("adults", request.getAdults());
        searchCriteria.put("children", request.getChildren() != null ? request.getChildren() : 0);
        searchCriteria.put("infants", request.getInfants() != null ? request.getInfants() : 0);
        searchCriteria.put("currency", request.getCurrencyCode());
        searchCriteria.put("travelClass", request.getTravelClass());
        searchCriteria.put("nonStop", request.getNonStop());
        searchCriteria.put("maxStops", request.getMaxStops());
        searchCriteria.put("minPrice", request.getMinPrice());
        searchCriteria.put("maxPrice", request.getMaxPrice());
        searchCriteria.put("earliestDeparture", request.getEarliestDeparture());
        searchCriteria.put("latestDeparture", request.getLatestDeparture());
        searchCriteria.put("earliestArrival", request.getEarliestArrival());
        searchCriteria.put("latestArrival", request.getLatestArrival());
        searchCriteria.put("maxFlightDurationHours", request.getMaxFlightDurationHours());
        searchCriteria.put("includedAirlines", request.getIncludedAirlines());
        searchCriteria.put("excludedAirlines", request.getExcludedAirlines());
        searchCriteria.put("sortBy", request.getSortBy());
        searchCriteria.put("sortOrder", request.getSortOrder());
        searchCriteria.put("roundTrip", request.isRoundTrip());

        return searchCriteria;
    }
}