package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Itinerary {

    private String duration;
    private List<Segment> segments;

    public Itinerary() {}

    public Itinerary(String duration, List<Segment> segments) {
        this.duration = duration;
        this.segments = segments;
    }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public List<Segment> getSegments() { return segments; }
    public void setSegments(List<Segment> segments) { this.segments = segments; }

    public int getNumberOfSegments() {
        return segments != null ? segments.size() : 0;
    }

    public boolean isDirectFlight() {
        return getNumberOfSegments() == 1;
    }

    public int getNumberOfStops() {
        return Math.max(0, getNumberOfSegments() - 1);
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "duration='" + duration + '\'' +
                ", numberOfSegments=" + getNumberOfSegments() +
                ", numberOfStops=" + getNumberOfStops() +
                '}';
    }
}
