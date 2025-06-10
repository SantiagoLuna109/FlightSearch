package com.FlightSearch.breakabletoy2.model.amadeus;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class LocationResponse {
    private Meta meta;
    private List<LocationData> data;

    public static class Meta {
        private Integer count;
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

    public static class LocationData {
        private String type;
        private String subType;
        private String name;
        private String detailedName;
        private String id;
        private String iataCode;
        private String timeZoneOffset;
        private GeoCode geoCode;
        private Address address;
        private Analytics analytics;
        private Double relevance;

        public static class GeoCode {
            private Double latitude;
            private Double longitude;

            public Double getLatitude() {
                return latitude;
            }

            public void setLatitude(Double latitude) {
                this.latitude = latitude;
            }

            public Double getLongitude() {
                return longitude;
            }

            public void setLongitude(Double longitude) {
                this.longitude = longitude;
            }
        }

        public static class Address {
            private String cityName;
            private String cityCode;
            private String countryName;
            private String countryCode;
            private String regionCode;

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getCountryName() {
                return countryName;
            }

            public void setCountryName(String countryName) {
                this.countryName = countryName;
            }

            public String getCountryCode() {
                return countryCode;
            }

            public void setCountryCode(String countryCode) {
                this.countryCode = countryCode;
            }

            public String getRegionCode() {
                return regionCode;
            }

            public void setRegionCode(String regionCode) {
                this.regionCode = regionCode;
            }
        }

        public static class Analytics {
            private Travelers travelers;

            public static class Travelers {
                private Integer score;

                public Integer getScore() {
                    return score;
                }

                public void setScore(Integer score) {
                    this.score = score;
                }
            }

            public Travelers getTravelers() {
                return travelers;
            }

            public void setTravelers(Travelers travelers) {
                this.travelers = travelers;
            }
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDetailedName() {
            return detailedName;
        }

        public void setDetailedName(String detailedName) {
            this.detailedName = detailedName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIataCode() {
            return iataCode;
        }

        public void setIataCode(String iataCode) {
            this.iataCode = iataCode;
        }

        public String getTimeZoneOffset() {
            return timeZoneOffset;
        }

        public void setTimeZoneOffset(String timeZoneOffset) {
            this.timeZoneOffset = timeZoneOffset;
        }

        public GeoCode getGeoCode() {
            return geoCode;
        }

        public void setGeoCode(GeoCode geoCode) {
            this.geoCode = geoCode;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Analytics getAnalytics() {
            return analytics;
        }

        public void setAnalytics(Analytics analytics) {
            this.analytics = analytics;
        }

        public Double getRelevance() {
            return relevance;
        }

        public void setRelevance(Double relevance) {
            this.relevance = relevance;
        }
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<LocationData> getData() {
        return data;
    }

    public void setData(List<LocationData> data) {
        this.data = data;
    }
}