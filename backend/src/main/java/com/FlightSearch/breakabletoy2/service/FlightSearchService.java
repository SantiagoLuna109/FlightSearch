package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.dto.FlightSearchResponse;
import com.FlightSearch.breakabletoy2.dto.FlightSearchRequest;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import com.FlightSearch.breakabletoy2.mapper.FlightMapper;
import com.FlightSearch.breakabletoy2.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightSearchService {

    private static final Logger logger = LoggerFactory.getLogger(FlightSearchService.class);

    private final FlightApiClient flightApiClient;
    private final FlightMapper flightMapper;

    public FlightSearchService(FlightApiClient flightApiClient, FlightMapper flightMapper) {
        this.flightApiClient = flightApiClient;
        this.flightMapper = flightMapper;
    }

    public List<Flight> searchFlights(FlightSearchRequest request) {
        try {
            logger.info("Searching flights: {}", request);

            validateSearchRequest(request);

            FlightSearchResponse response = flightApiClient.searchFlights(request);

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                logger.info("No flights found for request: {}", request);
                return Collections.emptyList();
            }

            List<Flight> flights = flightMapper.toFlightList(response);

            logger.info("Found {} flights for route {} -> {}",
                    flights.size(), request.getOriginLocationCode(), request.getDestinationLocationCode());

            return flights;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid search request: {}", e.getMessage());
            throw e;
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
        return sortFlights(flights, sortBy);
    }

    public List<Flight> sortFlights(List<Flight> flights, String sortBy) {
        if (flights == null || flights.isEmpty() || sortBy == null) {
            return flights;
        }

        logger.debug("Sorting {} flights by: {}", flights.size(), sortBy);

        return flights.stream()
                .sorted(getComparator(sortBy))
                .collect(Collectors.toList());
    }

    public List<Flight> filterDirectFlights(List<Flight> flights) {
        if (flights == null) {
            return Collections.emptyList();
        }

        return flights.stream()
                .filter(Flight::isDirectFlight)
                .collect(Collectors.toList());
    }

    public List<Flight> filterByMaxStops(List<Flight> flights, int maxStops) {
        if (flights == null) {
            return Collections.emptyList();
        }

        return flights.stream()
                .filter(flight -> flight.getNumberOfStops() <= maxStops)
                .collect(Collectors.toList());
    }

    public List<Flight> filterByPriceRange(List<Flight> flights, String minPrice, String maxPrice) {
        if (flights == null) {
            return Collections.emptyList();
        }

        return flights.stream()
                .filter(flight -> {
                    if (flight.getPrice() == null || flight.getPrice().getTotal() == null) {
                        return false;
                    }

                    try {
                        double flightPrice = Double.parseDouble(flight.getPrice().getTotal());

                        if (minPrice != null) {
                            double min = Double.parseDouble(minPrice);
                            if (flightPrice < min) return false;
                        }

                        if (maxPrice != null) {
                            double max = Double.parseDouble(maxPrice);
                            if (flightPrice > max) return false;
                        }

                        return true;

                    } catch (NumberFormatException e) {
                        logger.warn("Invalid price format for flight {}: {}", flight.getId(), flight.getPrice().getTotal());
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private void validateSearchRequest(FlightSearchRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Search request cannot be null");
        }

        if (request.getOriginLocationCode() == null || request.getOriginLocationCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Origin airport code is required");
        }

        if (request.getDestinationLocationCode() == null || request.getDestinationLocationCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Destination airport code is required");
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

        if (request.getAdults() == null || request.getAdults() < 1) {
            throw new IllegalArgumentException("At least 1 adult is required");
        }

        if (!request.isValidPassengerCount()) {
            throw new IllegalArgumentException("Invalid passenger count: too many passengers or infants exceed adults");
        }

        if (request.getOriginLocationCode().equals(request.getDestinationLocationCode())) {
            throw new IllegalArgumentException("Origin and destination cannot be the same");
        }
    }

    private Comparator<Flight> getComparator(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "price":
            case "price_asc":
                return Comparator.comparing(flight -> {
                    try {
                        return flight.getPrice() != null && flight.getPrice().getTotal() != null ?
                                Double.parseDouble(flight.getPrice().getTotal()) : Double.MAX_VALUE;
                    } catch (NumberFormatException e) {
                        return Double.MAX_VALUE;
                    }
                });

            case "price_desc":
                return Comparator.comparing((Flight flight) -> {
                    try {
                        return flight.getPrice() != null && flight.getPrice().getTotal() != null ?
                                Double.parseDouble(flight.getPrice().getTotal()) : 0.0;
                    } catch (NumberFormatException e) {
                        return 0.0;
                    }
                }).reversed();

            case "duration":
            case "duration_asc":
                return Comparator.comparing(flight -> {
                    String duration = flight.getTotalDuration();
                    return duration != null ? parseDurationToMinutes(duration) : Integer.MAX_VALUE;
                });

            case "duration_desc":
                return Comparator.comparing((Flight flight) -> {
                    String duration = flight.getTotalDuration();
                    return duration != null ? parseDurationToMinutes(duration) : 0;
                }).reversed();

            case "departure":
            case "departure_asc":
                return Comparator.comparing(flight ->
                        flight.getDepartureDateTime() != null ? flight.getDepartureDateTime() : java.time.LocalDateTime.MAX);

            case "departure_desc":
                return Comparator.comparing((Flight flight) ->
                        flight.getDepartureDateTime() != null ? flight.getDepartureDateTime() : java.time.LocalDateTime.MIN).reversed();

            case "stops":
            case "stops_asc":
                return Comparator.comparing(Flight::getNumberOfStops);

            case "stops_desc":
                return Comparator.comparing(Flight::getNumberOfStops).reversed();

            default:
                logger.warn("Unknown sort criteria: {}, defaulting to price", sortBy);
                return getComparator("price");
        }
    }

    private int parseDurationToMinutes(String duration) {
        try {
            if (duration == null || !duration.startsWith("PT")) {
                return Integer.MAX_VALUE;
            }

            String timeString = duration.substring(2);
            int totalMinutes = 0;

            if (timeString.contains("H")) {
                String[] parts = timeString.split("H");
                totalMinutes += Integer.parseInt(parts[0]) * 60;
                timeString = parts.length > 1 ? parts[1] : "";
            }

            if (timeString.contains("M")) {
                String minutesStr = timeString.replace("M", "");
                if (!minutesStr.isEmpty()) {
                    totalMinutes += Integer.parseInt(minutesStr);
                }
            }

            return totalMinutes;

        } catch (Exception e) {
            logger.warn("Failed to parse duration: {}", duration);
            return Integer.MAX_VALUE;
        }
    }

    public List<Flight> searchOneWayFlights(String origin, String destination, LocalDate departureDate,
                                            int adults, String currency) {
        FlightSearchRequest request = new FlightSearchRequest();
        request.setOriginLocationCode(origin);
        request.setDestinationLocationCode(destination);
        request.setDepartureDate(departureDate);
        request.setAdults(adults);
        request.setCurrencyCode(currency);

        return searchFlights(request);
    }

    public List<Flight> searchRoundTripFlights(String origin, String destination, LocalDate departureDate,
                                               LocalDate returnDate, int adults, String currency) {
        FlightSearchRequest request = new FlightSearchRequest();
        request.setOriginLocationCode(origin);
        request.setDestinationLocationCode(destination);
        request.setDepartureDate(departureDate);
        request.setReturnDate(returnDate);
        request.setAdults(adults);
        request.setCurrencyCode(currency);

        return searchFlights(request);
    }
}
