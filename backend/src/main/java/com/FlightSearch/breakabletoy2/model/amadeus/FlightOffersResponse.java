package com.FlightSearch.breakabletoy2.model.amadeus;

import com.FlightSearch.breakabletoy2.dto.FlightSearchResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightOffersResponse {

    @JsonProperty("meta")
    private Meta meta;

    @JsonProperty("data")
    private List<FlightOfferData> data;

    @JsonProperty("dictionaries")
    private Dictionaries dictionaries;

    public FlightOffersResponse() {}

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<FlightOfferData> getData() {
        return data;
    }

    public void setData(List<FlightOfferData> data) {
        this.data = data;
    }

    public Dictionaries getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Dictionaries dictionaries) {
        this.dictionaries = dictionaries;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        @JsonProperty("count")
        private Integer count;

        @JsonProperty("links")
        private Map<String, String> links;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Map<String, String> getLinks() {
            return links;
        }

        public void setLinks(Map<String, String> links) {
            this.links = links;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightOfferData {
        @JsonProperty("type")
        private String type;

        @JsonProperty("id")
        private String id;

        @JsonProperty("source")
        private String source;

        @JsonProperty("instantTicketingRequired")
        private Boolean instantTicketingRequired;

        @JsonProperty("nonHomogeneous")
        private Boolean nonHomogeneous;

        @JsonProperty("oneWay")
        private Boolean oneWay;

        @JsonProperty("lastTicketingDate")
        private String lastTicketingDate;

        @JsonProperty("lastTicketingDateTime")
        private String lastTicketingDateTime;

        @JsonProperty("numberOfBookableSeats")
        private Integer numberOfBookableSeats;

        @JsonProperty("itineraries")
        private List<Itinerary> itineraries;

        @JsonProperty("price")
        private Price price;

        @JsonProperty("pricingOptions")
        private PricingOptions pricingOptions;

        @JsonProperty("validatingAirlineCodes")
        private List<String> validatingAirlineCodes;

        @JsonProperty("travelerPricings")
        private List<TravelerPricing> travelerPricings;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        public Boolean getInstantTicketingRequired() { return instantTicketingRequired; }
        public void setInstantTicketingRequired(Boolean instantTicketingRequired) { this.instantTicketingRequired = instantTicketingRequired; }

        public Boolean getNonHomogeneous() { return nonHomogeneous; }
        public void setNonHomogeneous(Boolean nonHomogeneous) { this.nonHomogeneous = nonHomogeneous; }

        public Boolean getOneWay() { return oneWay; }
        public void setOneWay(Boolean oneWay) { this.oneWay = oneWay; }

        public String getLastTicketingDate() { return lastTicketingDate; }
        public void setLastTicketingDate(String lastTicketingDate) { this.lastTicketingDate = lastTicketingDate; }

        public String getLastTicketingDateTime() { return lastTicketingDateTime; }
        public void setLastTicketingDateTime(String lastTicketingDateTime) { this.lastTicketingDateTime = lastTicketingDateTime; }

        public Integer getNumberOfBookableSeats() { return numberOfBookableSeats; }
        public void setNumberOfBookableSeats(Integer numberOfBookableSeats) { this.numberOfBookableSeats = numberOfBookableSeats; }

        public List<Itinerary> getItineraries() { return itineraries; }
        public void setItineraries(List<Itinerary> itineraries) { this.itineraries = itineraries; }

        public Price getPrice() { return price; }
        public void setPrice(Price price) { this.price = price; }

        public PricingOptions getPricingOptions() { return pricingOptions; }
        public void setPricingOptions(PricingOptions pricingOptions) { this.pricingOptions = pricingOptions; }

        public List<String> getValidatingAirlineCodes() { return validatingAirlineCodes; }
        public void setValidatingAirlineCodes(List<String> validatingAirlineCodes) { this.validatingAirlineCodes = validatingAirlineCodes; }

        public List<TravelerPricing> getTravelerPricings() { return travelerPricings; }
        public void setTravelerPricings(List<TravelerPricing> travelerPricings) { this.travelerPricings = travelerPricings; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Itinerary {
        @JsonProperty("duration")
        private String duration;

        @JsonProperty("segments")
        private List<Segment> segments;

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public List<Segment> getSegments() { return segments; }
        public void setSegments(List<Segment> segments) { this.segments = segments; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Segment {
        @JsonProperty("departure")
        private FlightEndpoint departure;

        @JsonProperty("arrival")
        private FlightEndpoint arrival;

        @JsonProperty("carrierCode")
        private String carrierCode;

        @JsonProperty("number")
        private String number;

        @JsonProperty("aircraft")
        private Aircraft aircraft;

        @JsonProperty("operating")
        private Operating operating;

        @JsonProperty("duration")
        private String duration;

        @JsonProperty("id")
        private String id;

        @JsonProperty("numberOfStops")
        private Integer numberOfStops;

        @JsonProperty("blacklistedInEU")
        private Boolean blacklistedInEU;

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

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public Integer getNumberOfStops() { return numberOfStops; }
        public void setNumberOfStops(Integer numberOfStops) { this.numberOfStops = numberOfStops; }

        public Boolean getBlacklistedInEU() { return blacklistedInEU; }
        public void setBlacklistedInEU(Boolean blacklistedInEU) { this.blacklistedInEU = blacklistedInEU; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightEndpoint {
        @JsonProperty("iataCode")
        private String iataCode;

        @JsonProperty("terminal")
        private String terminal;

        @JsonProperty("at")
        private String at;

        public String getIataCode() { return iataCode; }
        public void setIataCode(String iataCode) { this.iataCode = iataCode; }

        public String getTerminal() { return terminal; }
        public void setTerminal(String terminal) { this.terminal = terminal; }

        public String getAt() { return at; }
        public void setAt(String at) { this.at = at; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Aircraft {
        @JsonProperty("code")
        private String code;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Operating {
        @JsonProperty("carrierCode")
        private String carrierCode;

        public String getCarrierCode() { return carrierCode; }
        public void setCarrierCode(String carrierCode) { this.carrierCode = carrierCode; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Price {
        @JsonProperty("currency")
        private String currency;

        @JsonProperty("total")
        private String total;

        @JsonProperty("base")
        private String base;

        @JsonProperty("fees")
        private List<Fee> fees;

        @JsonProperty("grandTotal")
        private String grandTotal;

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public String getTotal() { return total; }
        public void setTotal(String total) { this.total = total; }

        public String getBase() { return base; }
        public void setBase(String base) { this.base = base; }

        public List<Fee> getFees() { return fees; }
        public void setFees(List<Fee> fees) { this.fees = fees; }

        public String getGrandTotal() { return grandTotal; }
        public void setGrandTotal(String grandTotal) { this.grandTotal = grandTotal; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Fee {
        @JsonProperty("amount")
        private String amount;

        @JsonProperty("type")
        private String type;

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PricingOptions {
        @JsonProperty("fareType")
        private List<String> fareType;

        @JsonProperty("includedCheckedBagsOnly")
        private Boolean includedCheckedBagsOnly;

        public List<String> getFareType() { return fareType; }
        public void setFareType(List<String> fareType) { this.fareType = fareType; }

        public Boolean getIncludedCheckedBagsOnly() { return includedCheckedBagsOnly; }
        public void setIncludedCheckedBagsOnly(Boolean includedCheckedBagsOnly) { this.includedCheckedBagsOnly = includedCheckedBagsOnly; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TravelerPricing {
        @JsonProperty("travelerId")
        private String travelerId;

        @JsonProperty("fareOption")
        private String fareOption;

        @JsonProperty("travelerType")
        private String travelerType;

        @JsonProperty("price")
        private Price price;

        @JsonProperty("fareDetailsBySegment")
        private List<FareDetailsBySegment> fareDetailsBySegment;

        public String getTravelerId() { return travelerId; }
        public void setTravelerId(String travelerId) { this.travelerId = travelerId; }

        public String getFareOption() { return fareOption; }
        public void setFareOption(String fareOption) { this.fareOption = fareOption; }

        public String getTravelerType() { return travelerType; }
        public void setTravelerType(String travelerType) { this.travelerType = travelerType; }

        public Price getPrice() { return price; }
        public void setPrice(Price price) { this.price = price; }

        public List<FareDetailsBySegment> getFareDetailsBySegment() { return fareDetailsBySegment; }
        public void setFareDetailsBySegment(List<FareDetailsBySegment> fareDetailsBySegment) { this.fareDetailsBySegment = fareDetailsBySegment; }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FareDetailsBySegment {
        @JsonProperty("segmentId")
        private String segmentId;

        @JsonProperty("cabin")
        private String cabin;

        @JsonProperty("fareBasis")
        private String fareBasis;

        @JsonProperty("brandedFare")
        private String brandedFare;

        @JsonProperty("class")
        private String fareClass;

        @JsonProperty("includedCheckedBags")
        private BaggageAllowance includedCheckedBags;

        @JsonProperty("includedCabinBags")
        private BaggageAllowance includedCabinBags;

        @Getter
        @Setter
        @JsonProperty("amenities")
        private List<FlightSearchResponse.AmenityData> amenities;

        @Setter
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AmenityProvider {
            @JsonProperty("name")
            private String name;
        }

        @Setter
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AmenityData {
            @JsonProperty("description")
            private String description;
            @JsonProperty("isChargeable")
            private boolean isChargeable;
            @JsonProperty("amenityType")
            private String amenityType;
            @JsonProperty("amenityProvider")
            private AmenityProvider amenityProvider;
        }

        public void setSegmentId(String segmentId) { this.segmentId = segmentId; }

        public void setCabin(String cabin) { this.cabin = cabin; }

        public void setFareBasis(String fareBasis) { this.fareBasis = fareBasis; }

        public void setBrandedFare(String brandedFare) { this.brandedFare = brandedFare; }

        public void setFareClass(String fareClass) { this.fareClass = fareClass; }

        public void setIncludedCheckedBags(BaggageAllowance includedCheckedBags) { this.includedCheckedBags = includedCheckedBags; }

        public void setIncludedCabinBags(BaggageAllowance includedCabinBags) { this.includedCabinBags = includedCabinBags; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BaggageAllowance {
        @JsonProperty("quantity")
        private Integer quantity;

        @JsonProperty("weight")
        private Integer weight;

        @JsonProperty("weightUnit")
        private String weightUnit;

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Integer getWeight() { return weight; }
        public void setWeight(Integer weight) { this.weight = weight; }

        public String getWeightUnit() { return weightUnit; }
        public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dictionaries {
        @JsonProperty("locations")
        private Map<String, LocationDict> locations;

        @JsonProperty("aircraft")
        private Map<String, String> aircraft;

        @JsonProperty("currencies")
        private Map<String, String> currencies;

        @JsonProperty("carriers")
        private Map<String, String> carriers;

        public Map<String, LocationDict> getLocations() { return locations; }
        public void setLocations(Map<String, LocationDict> locations) { this.locations = locations; }

        public Map<String, String> getAircraft() { return aircraft; }
        public void setAircraft(Map<String, String> aircraft) { this.aircraft = aircraft; }

        public Map<String, String> getCurrencies() { return currencies; }
        public void setCurrencies(Map<String, String> currencies) { this.currencies = currencies; }

        public Map<String, String> getCarriers() { return carriers; }
        public void setCarriers(Map<String, String> carriers) { this.carriers = carriers; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationDict {
        @JsonProperty("cityCode")
        private String cityCode;

        @JsonProperty("countryCode")
        private String countryCode;

        public String getCityCode() { return cityCode; }
        public void setCityCode(String cityCode) { this.cityCode = cityCode; }

        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    }
}
