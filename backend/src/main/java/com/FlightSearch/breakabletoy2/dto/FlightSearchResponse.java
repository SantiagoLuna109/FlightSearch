package com.FlightSearch.breakabletoy2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightSearchResponse {

    private Meta meta;
    private List<FlightOfferData> data;
    private Dictionaries dictionaries;

    public Meta getMeta() { return meta; }
    public void setMeta(Meta meta) { this.meta = meta; }

    public List<FlightOfferData> getData() { return data; }
    public void setData(List<FlightOfferData> data) { this.data = data; }

    public Dictionaries getDictionaries() { return dictionaries; }
    public void setDictionaries(Dictionaries dictionaries) { this.dictionaries = dictionaries; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private Integer count;
        private Links links;

        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }

        public Links getLinks() { return links; }
        public void setLinks(Links links) { this.links = links; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Links {
        private String self;

        public String getSelf() { return self; }
        public void setSelf(String self) { this.self = self; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightOfferData {
        private String type;
        private String id;
        private String source;
        private Boolean instantTicketingRequired;
        private Boolean nonHomogeneous;
        private Boolean oneWay;
        private Boolean isUpsellOffer;
        private String lastTicketingDate;
        private String lastTicketingDateTime;
        private Integer numberOfBookableSeats;
        private List<ItineraryData> itineraries;
        private PriceData price;
        private PricingOptionsData pricingOptions;
        private List<String> validatingAirlineCodes;
        private List<TravelerPricingData> travelerPricings;

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

        public Boolean getIsUpsellOffer() { return isUpsellOffer; }
        public void setIsUpsellOffer(Boolean isUpsellOffer) { this.isUpsellOffer = isUpsellOffer; }

        public String getLastTicketingDate() { return lastTicketingDate; }
        public void setLastTicketingDate(String lastTicketingDate) { this.lastTicketingDate = lastTicketingDate; }

        public String getLastTicketingDateTime() { return lastTicketingDateTime; }
        public void setLastTicketingDateTime(String lastTicketingDateTime) { this.lastTicketingDateTime = lastTicketingDateTime; }

        public Integer getNumberOfBookableSeats() { return numberOfBookableSeats; }
        public void setNumberOfBookableSeats(Integer numberOfBookableSeats) { this.numberOfBookableSeats = numberOfBookableSeats; }

        public List<ItineraryData> getItineraries() { return itineraries; }
        public void setItineraries(List<ItineraryData> itineraries) { this.itineraries = itineraries; }

        public PriceData getPrice() { return price; }
        public void setPrice(PriceData price) { this.price = price; }

        public PricingOptionsData getPricingOptions() { return pricingOptions; }
        public void setPricingOptions(PricingOptionsData pricingOptions) { this.pricingOptions = pricingOptions; }

        public List<String> getValidatingAirlineCodes() { return validatingAirlineCodes; }
        public void setValidatingAirlineCodes(List<String> validatingAirlineCodes) { this.validatingAirlineCodes = validatingAirlineCodes; }

        public List<TravelerPricingData> getTravelerPricings() { return travelerPricings; }
        public void setTravelerPricings(List<TravelerPricingData> travelerPricings) { this.travelerPricings = travelerPricings; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItineraryData {
        private String duration;
        private List<SegmentData> segments;

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public List<SegmentData> getSegments() { return segments; }
        public void setSegments(List<SegmentData> segments) { this.segments = segments; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SegmentData {
        private String id;
        private FlightEndpointData departure;
        private FlightEndpointData arrival;
        private String carrierCode;
        private String number;
        private AircraftData aircraft;
        private OperatingData operating;
        private String duration;
        private Integer numberOfStops;
        private Boolean blacklistedInEU;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public FlightEndpointData getDeparture() { return departure; }
        public void setDeparture(FlightEndpointData departure) { this.departure = departure; }

        public FlightEndpointData getArrival() { return arrival; }
        public void setArrival(FlightEndpointData arrival) { this.arrival = arrival; }

        public String getCarrierCode() { return carrierCode; }
        public void setCarrierCode(String carrierCode) { this.carrierCode = carrierCode; }

        public String getNumber() { return number; }
        public void setNumber(String number) { this.number = number; }

        public AircraftData getAircraft() { return aircraft; }
        public void setAircraft(AircraftData aircraft) { this.aircraft = aircraft; }

        public OperatingData getOperating() { return operating; }
        public void setOperating(OperatingData operating) { this.operating = operating; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public Integer getNumberOfStops() { return numberOfStops; }
        public void setNumberOfStops(Integer numberOfStops) { this.numberOfStops = numberOfStops; }

        public Boolean getBlacklistedInEU() { return blacklistedInEU; }
        public void setBlacklistedInEU(Boolean blacklistedInEU) { this.blacklistedInEU = blacklistedInEU; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightEndpointData {
        private String iataCode;
        private String terminal;
        private String at;

        public String getIataCode() { return iataCode; }
        public void setIataCode(String iataCode) { this.iataCode = iataCode; }

        public String getTerminal() { return terminal; }
        public void setTerminal(String terminal) { this.terminal = terminal; }

        public String getAt() { return at; }
        public void setAt(String at) { this.at = at; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AircraftData {
        private String code;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OperatingData {
        private String carrierCode;

        public String getCarrierCode() { return carrierCode; }
        public void setCarrierCode(String carrierCode) { this.carrierCode = carrierCode; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PriceData {
        private String currency;
        private String total;
        private String base;
        private String grandTotal;
        private List<FeeData> fees;
        private List<TaxData> taxes;
        private List<AdditionalServiceData> additionalServices;

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public String getTotal() { return total; }
        public void setTotal(String total) { this.total = total; }

        public String getBase() { return base; }
        public void setBase(String base) { this.base = base; }

        public String getGrandTotal() { return grandTotal; }
        public void setGrandTotal(String grandTotal) { this.grandTotal = grandTotal; }

        public List<FeeData> getFees() { return fees; }
        public void setFees(List<FeeData> fees) { this.fees = fees; }

        public List<TaxData> getTaxes() { return taxes; }
        public void setTaxes(List<TaxData> taxes) { this.taxes = taxes; }

        public List<AdditionalServiceData> getAdditionalServices() { return additionalServices; }
        public void setAdditionalServices(List<AdditionalServiceData> additionalServices) { this.additionalServices = additionalServices; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeeData {
        private String amount;
        private String type;

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TaxData {
        private String amount;
        private String code;

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdditionalServiceData {
        private String amount;
        private String type;

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PricingOptionsData {
        private List<String> fareType;
        private Boolean includedCheckedBagsOnly;

        public List<String> getFareType() { return fareType; }
        public void setFareType(List<String> fareType) { this.fareType = fareType; }

        public Boolean getIncludedCheckedBagsOnly() { return includedCheckedBagsOnly; }
        public void setIncludedCheckedBagsOnly(Boolean includedCheckedBagsOnly) { this.includedCheckedBagsOnly = includedCheckedBagsOnly; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TravelerPricingData {
        private String travelerId;
        private String fareOption;
        private String travelerType;
        private PriceData price;
        private List<FareDetailsBySegmentData> fareDetailsBySegment;

        public String getTravelerId() { return travelerId; }
        public void setTravelerId(String travelerId) { this.travelerId = travelerId; }

        public String getFareOption() { return fareOption; }
        public void setFareOption(String fareOption) { this.fareOption = fareOption; }

        public String getTravelerType() { return travelerType; }
        public void setTravelerType(String travelerType) { this.travelerType = travelerType; }

        public PriceData getPrice() { return price; }
        public void setPrice(PriceData price) { this.price = price; }

        public List<FareDetailsBySegmentData> getFareDetailsBySegment() { return fareDetailsBySegment; }
        public void setFareDetailsBySegment(List<FareDetailsBySegmentData> fareDetailsBySegment) { this.fareDetailsBySegment = fareDetailsBySegment; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FareDetailsBySegmentData {
        private String segmentId;
        private String cabin;
        private String fareBasis;
        private String brandedFare;
        private String brandedFareLabel;

        @JsonProperty("class")
        private String fareClass;

        private BaggageAllowanceData includedCheckedBags;
        private BaggageAllowanceData includedCabinBags;
        private List<AmenityData> amenities;

        public String getSegmentId() { return segmentId; }
        public void setSegmentId(String segmentId) { this.segmentId = segmentId; }

        public String getCabin() { return cabin; }
        public void setCabin(String cabin) { this.cabin = cabin; }

        public String getFareBasis() { return fareBasis; }
        public void setFareBasis(String fareBasis) { this.fareBasis = fareBasis; }

        public String getBrandedFare() { return brandedFare; }
        public void setBrandedFare(String brandedFare) { this.brandedFare = brandedFare; }

        public String getBrandedFareLabel() { return brandedFareLabel; }
        public void setBrandedFareLabel(String brandedFareLabel) { this.brandedFareLabel = brandedFareLabel; }

        public String getFareClass() { return fareClass; }
        public void setFareClass(String fareClass) { this.fareClass = fareClass; }

        public BaggageAllowanceData getIncludedCheckedBags() { return includedCheckedBags; }
        public void setIncludedCheckedBags(BaggageAllowanceData includedCheckedBags) { this.includedCheckedBags = includedCheckedBags; }

        public BaggageAllowanceData getIncludedCabinBags() { return includedCabinBags; }
        public void setIncludedCabinBags(BaggageAllowanceData includedCabinBags) { this.includedCabinBags = includedCabinBags; }

        public List<AmenityData> getAmenities() { return amenities; }
        public void setAmenities(List<AmenityData> amenities) { this.amenities = amenities; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BaggageAllowanceData {
        private Integer quantity;
        private Integer weight;
        private String weightUnit;

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public Integer getWeight() { return weight; }
        public void setWeight(Integer weight) { this.weight = weight; }

        public String getWeightUnit() { return weightUnit; }
        public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AmenityData {
        private String description;
        private Boolean isChargeable;
        private String amenityType;
        private AmenityProviderData amenityProvider;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Boolean getIsChargeable() { return isChargeable; }
        public void setIsChargeable(Boolean isChargeable) { this.isChargeable = isChargeable; }

        public String getAmenityType() { return amenityType; }
        public void setAmenityType(String amenityType) { this.amenityType = amenityType; }

        public AmenityProviderData getAmenityProvider() { return amenityProvider; }
        public void setAmenityProvider(AmenityProviderData amenityProvider) { this.amenityProvider = amenityProvider; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AmenityProviderData {
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dictionaries {
        private Map<String, LocationDictionary> locations;
        private Map<String, String> aircraft;
        private Map<String, String> currencies;
        private Map<String, String> carriers;

        public Map<String, LocationDictionary> getLocations() { return locations; }
        public void setLocations(Map<String, LocationDictionary> locations) { this.locations = locations; }

        public Map<String, String> getAircraft() { return aircraft; }
        public void setAircraft(Map<String, String> aircraft) { this.aircraft = aircraft; }

        public Map<String, String> getCurrencies() { return currencies; }
        public void setCurrencies(Map<String, String> currencies) { this.currencies = currencies; }

        public Map<String, String> getCarriers() { return carriers; }
        public void setCarriers(Map<String, String> carriers) { this.carriers = carriers; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationDictionary {
        private String cityCode;
        private String countryCode;

        public String getCityCode() { return cityCode; }
        public void setCityCode(String cityCode) { this.cityCode = cityCode; }

        public String getCountryCode() { return countryCode; }
        public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    }
}