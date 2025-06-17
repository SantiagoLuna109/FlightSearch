package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Segment {

    private String id;
    private FlightEndpoint departure;
    private FlightEndpoint arrival;
    private String carrierCode;
    private String number;
    private Aircraft aircraft;
    private Operating operating;
    private String duration;
    private Integer numberOfStops;
    private Boolean blacklistedInEU;
    private String carrierName;
    private String operatingCarrierName;
    private String operatingCarrierCode;

    public String getOperatingCarrierCode() { return operatingCarrierCode; }

    public void setOperatingCarrierCode(String operatingCarrierCode) { this.operatingCarrierCode = operatingCarrierCode; }

    public String getOperatingCarrierName() { return operatingCarrierName; }

    public void setOperatingCarrierName(String operatingCarrierName){ this.operatingCarrierName = operatingCarrierName; }

    public String getCarrierName(){ return carrierName; }

    public void setCarrierName(String carrierName){ this.carrierName = carrierName; }

    public Segment() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public FlightEndpoint getDeparture() { return departure; }
    public void setDeparture(FlightEndpoint departure) { this.departure = departure; }

    public FlightEndpoint getArrival() { return arrival; }
    public void setArrival(FlightEndpoint arrival) { this.arrival = arrival; }

    public String getCarrierCode() { return carrierCode; }
    public void setCarrierCode(String carrierCode) { this.carrierCode = carrierCode; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public Aircraft getAircraft() { return aircraft; }
    public void setAircraft(Aircraft aircraft) { this.aircraft = aircraft; }

    public Operating getOperating() { return operating; }
    public void setOperating(Operating operating) { this.operating = operating; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public Integer getNumberOfStops() { return numberOfStops; }
    public void setNumberOfStops(Integer numberOfStops) { this.numberOfStops = numberOfStops; }

    public Boolean getBlacklistedInEU() { return blacklistedInEU; }
    public void setBlacklistedInEU(Boolean blacklistedInEU) { this.blacklistedInEU = blacklistedInEU; }

    public String getFlightNumber() {
        return carrierCode + number;
    }

    public boolean isDirectSegment() {
        return numberOfStops != null && numberOfStops == 0;
    }

    public boolean hasOperatingCarrier() {
        return operating != null && operating.getCarrierCode() != null &&
                !operating.getCarrierCode().equals(carrierCode);
    }

    @Override
    public String toString() {
        return "Segment{" +
                "id='" + id + '\'' +
                ", flightNumber='" + getFlightNumber() + '\'' +
                ", departure=" + (departure != null ? departure.getIataCode() : "null") +
                ", arrival=" + (arrival != null ? arrival.getIataCode() : "null") +
                ", duration='" + duration + '\'' +
                ", numberOfStops=" + numberOfStops +
                '}';
    }
}