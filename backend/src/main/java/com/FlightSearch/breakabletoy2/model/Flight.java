package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Flight {

    private String id;
    private String source;
    private boolean instantTicketingRequired;
    private boolean nonHomogeneous;
    private boolean oneWay;
    private String lastTicketingDate;
    private LocalDateTime lastTicketingDateTime;
    private Integer numberOfBookableSeats;

    private List<Itinerary> itineraries;
    private Price price;
    private PriceWithConversion priceWithConversion;
    private List<String> validatingAirlineCodes;
    private List<TravelerPricing> travelerPricings;

    public Flight() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public boolean isInstantTicketingRequired() { return instantTicketingRequired; }
    public void setInstantTicketingRequired(boolean instantTicketingRequired) { this.instantTicketingRequired = instantTicketingRequired; }

    public boolean isNonHomogeneous() { return nonHomogeneous; }
    public void setNonHomogeneous(boolean nonHomogeneous) { this.nonHomogeneous = nonHomogeneous; }

    public boolean isOneWay() { return oneWay; }
    public void setOneWay(boolean oneWay) { this.oneWay = oneWay; }

    public String getLastTicketingDate() { return lastTicketingDate; }
    public void setLastTicketingDate(String lastTicketingDate) { this.lastTicketingDate = lastTicketingDate; }

    public LocalDateTime getLastTicketingDateTime() { return lastTicketingDateTime; }
    public void setLastTicketingDateTime(LocalDateTime lastTicketingDateTime) { this.lastTicketingDateTime = lastTicketingDateTime; }

    public Integer getNumberOfBookableSeats() { return numberOfBookableSeats; }
    public void setNumberOfBookableSeats(Integer numberOfBookableSeats) { this.numberOfBookableSeats = numberOfBookableSeats; }

    public List<Itinerary> getItineraries() { return itineraries; }
    public void setItineraries(List<Itinerary> itineraries) { this.itineraries = itineraries; }

    public Price getPrice() { return price; }
    public void setPrice(Price price) { this.price = price; }

    public PriceWithConversion getPriceWithConversion() { return priceWithConversion; }
    public void setPriceWithConversion(PriceWithConversion priceWithConversion) { this.priceWithConversion = priceWithConversion; }

    public List<String> getValidatingAirlineCodes() { return validatingAirlineCodes; }
    public void setValidatingAirlineCodes(List<String> validatingAirlineCodes) { this.validatingAirlineCodes = validatingAirlineCodes; }

    public List<TravelerPricing> getTravelerPricings() { return travelerPricings; }
    public void setTravelerPricings(List<TravelerPricing> travelerPricings) { this.travelerPricings = travelerPricings; }

    public String getTotalDuration() {
        if (itineraries != null && !itineraries.isEmpty()) {
            return itineraries.get(0).getDuration();
        }
        return null;
    }

    public String getDepartureAirport() {
        if (itineraries != null && !itineraries.isEmpty() &&
                itineraries.get(0).getSegments() != null && !itineraries.get(0).getSegments().isEmpty()) {
            return itineraries.get(0).getSegments().get(0).getDeparture().getIataCode();
        }
        return null;
    }

    public String getArrivalAirport() {
        if (itineraries != null && !itineraries.isEmpty() &&
                itineraries.get(0).getSegments() != null && !itineraries.get(0).getSegments().isEmpty()) {
            List<Segment> segments = itineraries.get(0).getSegments();
            return segments.get(segments.size() - 1).getArrival().getIataCode();
        }
        return null;
    }

    public LocalDateTime getDepartureDateTime() {
        if (itineraries != null && !itineraries.isEmpty() &&
                itineraries.get(0).getSegments() != null && !itineraries.get(0).getSegments().isEmpty()) {
            return itineraries.get(0).getSegments().get(0).getDeparture().getAt();
        }
        return null;
    }

    public LocalDateTime getArrivalDateTime() {
        if (itineraries != null && !itineraries.isEmpty() &&
                itineraries.get(0).getSegments() != null && !itineraries.get(0).getSegments().isEmpty()) {
            List<Segment> segments = itineraries.get(0).getSegments();
            return segments.get(segments.size() - 1).getArrival().getAt();
        }
        return null;
    }

    public int getNumberOfStops() {
        if (itineraries != null && !itineraries.isEmpty() &&
                itineraries.get(0).getSegments() != null) {
            return Math.max(0, itineraries.get(0).getSegments().size() - 1);
        }
        return 0;
    }

    public boolean isDirectFlight() {
        return getNumberOfStops() == 0;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", departureAirport='" + getDepartureAirport() + '\'' +
                ", arrivalAirport='" + getArrivalAirport() + '\'' +
                ", price=" + (price != null ? price.getTotal() : "null") +
                ", numberOfStops=" + getNumberOfStops() +
                '}';
    }
}
