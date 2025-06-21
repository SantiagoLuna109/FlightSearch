package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.dto.FlightSearchRequest;
import com.FlightSearch.breakabletoy2.model.Flight;
import com.FlightSearch.breakabletoy2.model.Segment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightFilterService {

    public List<Flight> applyFilters(List<Flight> flights, FlightSearchRequest request) {
        return flights.stream()
                .filter(flight -> applyAirlineFilter(flight, request))
                .filter(flight -> applyStopsFilter(flight, request))
                .filter(flight -> applyPriceFilter(flight, request))
                .filter(flight -> applyTravelClassFilter(flight, request))
                .filter(flight -> applyTimeFilters(flight, request))
                .filter(flight -> applyDurationFilter(flight, request))
                .collect(Collectors.toList());
    }

    private boolean applyAirlineFilter(Flight flight, FlightSearchRequest request) {
        if (request.getIncludedAirlines() != null && !request.getIncludedAirlines().isEmpty()) {
            return flight.getItineraries().stream()
                    .flatMap(itinerary -> itinerary.getSegments().stream())
                    .anyMatch(segment -> request.getIncludedAirlines().contains(segment.getCarrierCode()));
        }

        if (request.getExcludedAirlines() != null && !request.getExcludedAirlines().isEmpty()) {
            return flight.getItineraries().stream()
                    .flatMap(itinerary -> itinerary.getSegments().stream())
                    .noneMatch(segment -> request.getExcludedAirlines().contains(segment.getCarrierCode()));
        }

        return true;
    }

    private boolean applyStopsFilter(Flight flight, FlightSearchRequest request) {
        if (request.getMaxStops() != null) {
            return flight.getItineraries().stream()
                    .allMatch(itinerary -> {
                        int totalStops = itinerary.getSegments().size() - 1;
                        return totalStops <= request.getMaxStops();
                    });
        }

        if (Boolean.TRUE.equals(request.getNonStop())) {
            return flight.getItineraries().stream()
                    .allMatch(itinerary -> itinerary.getSegments().size() == 1);
        }

        return true;
    }

    private boolean applyPriceFilter(Flight flight, FlightSearchRequest request) {
        if (flight.getPrice() == null || flight.getPrice().getTotal() == null) {
            return true;
        }

        BigDecimal totalPrice = new BigDecimal(flight.getPrice().getTotal());

        if (request.getMinPrice() != null && totalPrice.compareTo(request.getMinPrice()) < 0) {
            return false;
        }

        if (request.getMaxPrice() != null && totalPrice.compareTo(request.getMaxPrice()) > 0) {
            return false;
        }

        return true;
    }

    private boolean applyTravelClassFilter(Flight flight, FlightSearchRequest request) {
        if (request.getTravelClass() == null || request.getTravelClass().isEmpty()) {
            return true;
        }

        String requestedClass = request.getTravelClass().toUpperCase();

        return flight.getTravelerPricings().stream()
                .flatMap(tp -> tp.getFareDetailsBySegment().stream())
                .anyMatch(fareDetails -> {
                    String cabin = fareDetails.getCabin();
                    if (cabin == null) return false;

                    return cabin.toUpperCase().equals(requestedClass) ||
                            (requestedClass.equals("ECONOMY") && cabin.toUpperCase().contains("ECONOMY")) ||
                            (requestedClass.equals("BUSINESS") && cabin.toUpperCase().contains("BUSINESS")) ||
                            (requestedClass.equals("FIRST") && cabin.toUpperCase().contains("FIRST"));
                });
    }

    private boolean applyTimeFilters(Flight flight, FlightSearchRequest request) {
        return applyDepartureTimeFilter(flight, request) && applyArrivalTimeFilter(flight, request);
    }

    private boolean applyDepartureTimeFilter(Flight flight, FlightSearchRequest request) {
        if (request.getEarliestDeparture() == null && request.getLatestDeparture() == null) {
            return true;
        }

        return flight.getItineraries().stream()
                .allMatch(itinerary -> {
                    if (itinerary.getSegments().isEmpty()) return true;

                    Segment firstSegment = itinerary.getSegments().get(0);
                    LocalTime departureTime = extractTimeFromDateTimeString(firstSegment.getDeparture().getAt().toString());

                    if (departureTime == null) return true;

                    if (request.getEarliestDeparture() != null &&
                            departureTime.isBefore(request.getEarliestDeparture())) {
                        return false;
                    }

                    if (request.getLatestDeparture() != null &&
                            departureTime.isAfter(request.getLatestDeparture())) {
                        return false;
                    }

                    return true;
                });
    }

    private boolean applyArrivalTimeFilter(Flight flight, FlightSearchRequest request) {
        if (request.getEarliestArrival() == null && request.getLatestArrival() == null) {
            return true;
        }

        return flight.getItineraries().stream()
                .allMatch(itinerary -> {
                    if (itinerary.getSegments().isEmpty()) return true;

                    Segment lastSegment = itinerary.getSegments().get(itinerary.getSegments().size() - 1);
                    LocalTime arrivalTime = extractTimeFromDateTimeString(lastSegment.getArrival().getAt().toString());

                    if (arrivalTime == null) return true;

                    if (request.getEarliestArrival() != null &&
                            arrivalTime.isBefore(request.getEarliestArrival())) {
                        return false;
                    }

                    if (request.getLatestArrival() != null &&
                            arrivalTime.isAfter(request.getLatestArrival())) {
                        return false;
                    }

                    return true;
                });
    }

    private boolean applyDurationFilter(Flight flight, FlightSearchRequest request) {
        if (request.getMaxFlightDurationHours() == null) {
            return true;
        }

        return flight.getItineraries().stream()
                .allMatch(itinerary -> {
                    String durationStr = itinerary.getDuration();
                    if (durationStr == null) return true;

                    Duration duration = parseDuration(durationStr);
                    if (duration == null) return true;

                    long durationHours = duration.toHours();
                    return durationHours <= request.getMaxFlightDurationHours();
                });
    }

    private LocalTime extractTimeFromDateTimeString(String dateTimeStr) {
        if (dateTimeStr == null) {
            return null;
        }

        try {
            if (dateTimeStr.contains("T")) {
                String timeStr = dateTimeStr.split("T")[1];
                if (timeStr.contains("+") || timeStr.contains("-")) {
                    timeStr = timeStr.split("[+-]")[0];
                }
                return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    private Duration parseDuration(String durationStr) {
        if (durationStr == null || !durationStr.startsWith("PT")) {
            return null;
        }

        try {
            return Duration.parse(durationStr);
        } catch (Exception e) {
            return null;
        }
    }

    public int countTotalStops(Flight flight) {
        return flight.getItineraries().stream()
                .mapToInt(itinerary -> Math.max(0, itinerary.getSegments().size() - 1))
                .sum();
    }

    public Duration getTotalDuration(Flight flight) {
        return flight.getItineraries().stream()
                .map(itinerary -> parseDuration(itinerary.getDuration()))
                .filter(duration -> duration != null)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public List<String> getUniqueAirlines(List<Flight> flights) {
        return flights.stream()
                .flatMap(flight -> flight.getItineraries().stream())
                .flatMap(itinerary -> itinerary.getSegments().stream())
                .map(Segment::getCarrierCode)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getUniqueTravelClasses(List<Flight> flights) {
        return flights.stream()
                .flatMap(flight -> flight.getTravelerPricings().stream())
                .flatMap(tp -> tp.getFareDetailsBySegment().stream())
                .map(fareDetails -> fareDetails.getCabin())
                .filter(cabin -> cabin != null)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public BigDecimal getMinPrice(List<Flight> flights) {
        return flights.stream()
                .map(flight -> flight.getPrice())
                .filter(price -> price != null && price.getTotal() != null)
                .map(price -> new BigDecimal(price.getTotal()))
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getMaxPrice(List<Flight> flights) {
        return flights.stream()
                .map(flight -> flight.getPrice())
                .filter(price -> price != null && price.getTotal() != null)
                .map(price -> new BigDecimal(price.getTotal()))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
