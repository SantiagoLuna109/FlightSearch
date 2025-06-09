package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.client.AmadeusApiClient;
import com.FlightSearch.breakabletoy2.dto.FlightSearchRequest;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import com.FlightSearch.breakabletoy2.exception.FlightNotFoundException;
import com.FlightSearch.breakabletoy2.mapper.FlightMapper;
import com.FlightSearch.breakabletoy2.model.Flight;
import com.FlightSearch.breakabletoy2.model.amadeus.FlightOffersResponse;
import com.FlightSearch.breakabletoy2.service.FlightFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightSearchService {

    private static final Logger logger = LoggerFactory.getLogger(FlightSearchService.class);

    private final AmadeusApiClient amadeusApiClient;
    private final FlightMapper flightMapper;
    private final FlightFilterService flightFilterService;

    public FlightSearchService(AmadeusApiClient amadeusApiClient, FlightMapper flightMapper, FlightFilterService flightFilterService) {
        this.amadeusApiClient = amadeusApiClient;
        this.flightMapper = flightMapper;
        this.flightFilterService = flightFilterService;
    }

    public List<Flight> searchFlights(FlightSearchRequest request) {
        validateSearchRequest(request);

        try {
            logger.info("Searching flights for request: {}", request);

            Map<String, String> params = buildAmadeusApiParams(request);
            FlightOffersResponse response = amadeusApiClient.searchFlights(params);

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                logger.warn("No flights found for request: {}", request);
                return Collections.emptyList();
            }

            List<Flight> flights = response.getData().stream()
                    .map(data -> flightMapper.mapToFlight(data, response.getDictionaries()))
                    .collect(Collectors.toList());

            flights = flightFilterService.applyFilters(flights, request);

            logger.info("Found {} flights after filtering for request: {}", flights.size(), request);
            return flights;

        } catch (AmadeusApiException e) {
            logger.error("Amadeus API error during flight search: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during flight search: {}", e.getMessage(), e);
            throw new AmadeusApiException("Unexpected error during flight search: " + e.getMessage(), e);
        }
    }

    public List<Flight> searchFlightsAndSort(FlightSearchRequest request, String sortBy) {
        List<Flight> flights = searchFlights(request);
        return sortFlights(flights, sortBy, request.getSortOrder());
    }

    public List<Flight> searchOneWayFlights(String origin, String destination, LocalDate departureDate, Integer adults, String currency) {
        FlightSearchRequest request = new FlightSearchRequest();
        request.setOriginLocationCode(origin);
        request.setDestinationLocationCode(destination);
        request.setDepartureDate(departureDate);
        request.setAdults(adults);
        request.setCurrencyCode(currency);
        request.setMax(250);

        return searchFlights(request);
    }

    public List<Flight> searchRoundTripFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate, Integer adults, String currency) {
        FlightSearchRequest request = new FlightSearchRequest();
        request.setOriginLocationCode(origin);
        request.setDestinationLocationCode(destination);
        request.setDepartureDate(departureDate);
        request.setReturnDate(returnDate);
        request.setAdults(adults);
        request.setCurrencyCode(currency);
        request.setMax(250);

        return searchFlights(request);
    }

    public Flight getFlightById(String flightId) {
        if (flightId == null || flightId.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight ID cannot be null or empty");
        }

        throw new FlightNotFoundException("Flight details by ID not implemented yet - use flight search instead");
    }

    public List<Flight> sortFlights(List<Flight> flights, String sortBy, String sortOrder) {
        if (flights == null || flights.isEmpty()) {
            return flights;
        }

        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "price";
        }

        if (sortOrder == null || sortOrder.trim().isEmpty()) {
            sortOrder = "asc";
        }

        boolean ascending = "asc".equalsIgnoreCase(sortOrder);

        Comparator<Flight> comparator = createComparator(sortBy.toLowerCase(), ascending);

        return flights.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private Comparator<Flight> createComparator(String sortBy, boolean ascending) {
        Comparator<Flight> comparator;

        switch (sortBy) {
            case "price":
                comparator = Comparator.comparing(flight ->
                        flight.getPrice() != null && flight.getPrice().getTotal() != null
                                ? new BigDecimal(flight.getPrice().getTotal())
                                : BigDecimal.ZERO);
                break;

            case "duration":
                comparator = Comparator.comparing(flight -> {
                    if (flight.getItineraries() != null && !flight.getItineraries().isEmpty()) {
                        String duration = flight.getItineraries().get(0).getDuration();
                        return parseDurationToMinutes(duration);
                    }
                    return 0L;
                });
                break;

            case "departure":
                comparator = Comparator.comparing(flight -> {
                    LocalDateTime departure = flight.getDepartureDateTime();
                    return departure != null ? departure : LocalDateTime.MIN;
                });
                break;

            case "arrival":
                comparator = Comparator.comparing(flight -> {
                    LocalDateTime arrival = flight.getArrivalDateTime();
                    return arrival != null ? arrival : LocalDateTime.MIN;
                });
                break;

            case "stops":
                comparator = Comparator.comparing(Flight::getNumberOfStops);
                break;

            case "airline":
                comparator = Comparator.comparing(flight -> {
                    if (flight.getItineraries() != null && !flight.getItineraries().isEmpty() &&
                            flight.getItineraries().get(0).getSegments() != null &&
                            !flight.getItineraries().get(0).getSegments().isEmpty()) {
                        return flight.getItineraries().get(0).getSegments().get(0).getCarrierCode();
                    }
                    return "";
                });
                break;

            default:
                logger.warn("Unknown sort field: {}, defaulting to price", sortBy);
                comparator = Comparator.comparing(flight ->
                        flight.getPrice() != null && flight.getPrice().getTotal() != null
                                ? new BigDecimal(flight.getPrice().getTotal())
                                : BigDecimal.ZERO);
        }

        return ascending ? comparator : comparator.reversed();
    }

    private Long parseDurationToMinutes(String duration) {
        if (duration == null || !duration.startsWith("PT")) {
            return 0L;
        }

        try {
            duration = duration.substring(2);
            long totalMinutes = 0;

            if (duration.contains("H")) {
                int hoursIndex = duration.indexOf("H");
                String hoursStr = duration.substring(0, hoursIndex);
                totalMinutes += Long.parseLong(hoursStr) * 60;
                duration = duration.substring(hoursIndex + 1);
            }

            if (duration.contains("M")) {
                int minutesIndex = duration.indexOf("M");
                String minutesStr = duration.substring(0, minutesIndex);
                if (!minutesStr.isEmpty()) {
                    totalMinutes += Long.parseLong(minutesStr);
                }
            }

            return totalMinutes;
        } catch (Exception e) {
            logger.warn("Failed to parse duration: {}", duration);
            return 0L;
        }
    }

    private void validateSearchRequest(FlightSearchRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Flight search request cannot be null");
        }

        if (request.getOriginLocationCode() == null || request.getOriginLocationCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Origin location code is required");
        }

        if (request.getDestinationLocationCode() == null || request.getDestinationLocationCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Destination location code is required");
        }

        if (request.getDepartureDate() == null) {
            throw new IllegalArgumentException("Departure date is required");
        }

        if (request.getDepartureDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Departure date cannot be in the past");
        }

        if (request.getReturnDate() != null && request.getReturnDate().isBefore(request.getDepartureDate())) {
            throw new IllegalArgumentException("Return date cannot be before departure date");
        }

        if (request.getAdults() == null || request.getAdults() < 1 || request.getAdults() > 9) {
            throw new IllegalArgumentException("Number of adults must be between 1 and 9");
        }

        if (request.getChildren() != null && (request.getChildren() < 0 || request.getChildren() > 8)) {
            throw new IllegalArgumentException("Number of children must be between 0 and 8");
        }

        if (request.getInfants() != null && (request.getInfants() < 0 || request.getInfants() > request.getAdults())) {
            throw new IllegalArgumentException("Number of infants cannot exceed number of adults");
        }

        int totalPassengers = request.getAdults() + (request.getChildren() != null ? request.getChildren() : 0);
        if (totalPassengers > 9) {
            throw new IllegalArgumentException("Total passengers (adults + children) cannot exceed 9");
        }

        if (request.getMaxStops() != null && (request.getMaxStops() < 0 || request.getMaxStops() > 3)) {
            throw new IllegalArgumentException("Maximum stops must be between 0 and 3");
        }

        if (request.getMinPrice() != null && request.getMinPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Minimum price cannot be negative");
        }

        if (request.getMaxPrice() != null && request.getMaxPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Maximum price cannot be negative");
        }

        if (request.getMinPrice() != null && request.getMaxPrice() != null &&
                request.getMinPrice().compareTo(request.getMaxPrice()) > 0) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        if (request.getMaxFlightDurationHours() != null &&
                (request.getMaxFlightDurationHours() < 1 || request.getMaxFlightDurationHours() > 48)) {
            throw new IllegalArgumentException("Maximum flight duration must be between 1 and 48 hours");
        }

        validateTimeRange(request.getEarliestDeparture(), request.getLatestDeparture(), "departure");
        validateTimeRange(request.getEarliestArrival(), request.getLatestArrival(), "arrival");
    }

    private void validateTimeRange(LocalTime earliest, LocalTime latest, String timeType) {
        if (earliest != null && latest != null && earliest.isAfter(latest)) {
            throw new IllegalArgumentException("Earliest " + timeType + " time cannot be after latest " + timeType + " time");
        }
    }

    private Map<String, String> buildAmadeusApiParams(FlightSearchRequest request) {
        Map<String, String> params = new HashMap<>();

        params.put("originLocationCode", request.getOriginLocationCode());
        params.put("destinationLocationCode", request.getDestinationLocationCode());
        params.put("departureDate", request.getDepartureDate().toString());

        if (request.getReturnDate() != null) {
            params.put("returnDate", request.getReturnDate().toString());
        }

        params.put("adults", request.getAdults().toString());

        if (request.getChildren() != null && request.getChildren() > 0) {
            params.put("children", request.getChildren().toString());
        }

        if (request.getInfants() != null && request.getInfants() > 0) {
            params.put("infants", request.getInfants().toString());
        }

        if (request.getCurrencyCode() != null && !request.getCurrencyCode().trim().isEmpty()) {
            params.put("currencyCode", request.getCurrencyCode());
        }

        if (request.getTravelClass() != null && !request.getTravelClass().trim().isEmpty()) {
            params.put("travelClass", request.getTravelClass().toUpperCase());
        }

        if (Boolean.TRUE.equals(request.getNonStop())) {
            params.put("nonStop", "true");
        }

        if (request.getIncludedAirlineCodes() != null && !request.getIncludedAirlineCodes().trim().isEmpty()) {
            params.put("includedAirlineCodes", request.getIncludedAirlineCodes());
        }

        if (request.getExcludedAirlineCodes() != null && !request.getExcludedAirlineCodes().trim().isEmpty()) {
            params.put("excludedAirlineCodes", request.getExcludedAirlineCodes());
        }

        if (request.getMax() != null && request.getMax() > 0) {
            params.put("max", Integer.toString(Math.min(request.getMax(), 250)));
        } else {
            params.put("max", "250");
        }

        return params;
    }
}