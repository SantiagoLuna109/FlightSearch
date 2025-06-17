package com.FlightSearch.breakabletoy2.client;

import com.FlightSearch.breakabletoy2.config.AmadeusConfig.AmadeusUrlConfig;
import com.FlightSearch.breakabletoy2.model.amadeus.LocationResponse;
import com.FlightSearch.breakabletoy2.model.amadeus.FlightOffersResponse;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import com.FlightSearch.breakabletoy2.service.AmadeusAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class AmadeusApiClient {

    private static final Logger logger = LoggerFactory.getLogger(AmadeusApiClient.class);
    private static final String REFERENCE_DATA_LOCATION_JUANDICE = "/reference-data/locations";
    private static final String URL_ANALITICS= "analytics.travelers.score";
    private static final String URL_PARAM = "FULL";
    private static final String URL_REFERENCE = "/reference-data/locations/";
    private static final String AMADEUS_JSON = "application/vnd.amadeus+json";
    private static final String SHOPPING_URL = "/shopping/flight-offers";
    private static final String SHOPP_URL = "/shopping/flight-offers/pricing";
    private final RestTemplate restTemplate;
    private final AmadeusAuthService authService;
    private final AmadeusUrlConfig urlConfig;
    public AmadeusApiClient(RestTemplate restTemplate,
                            AmadeusAuthService authService,
                            AmadeusUrlConfig urlConfig) {
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.urlConfig = urlConfig;
    }

    public LocationResponse searchLocations(String keyword, String subType, int limit) {
        try {
            logger.info("Searching locations - keyword: '{}', subType: '{}', limit: {}", keyword, subType, limit);

            URI uri = UriComponentsBuilder
                    .fromHttpUrl(urlConfig.getBaseUrlV1() + REFERENCE_DATA_LOCATION_JUANDICE)
                    .queryParam("keyword", keyword)
                    .queryParam("subType", subType)
                    .queryParam("page[limit]", limit)
                    .queryParam("sort", URL_ANALITICS)
                    .queryParam("view", URL_PARAM)
                    .build()
                    .toUri();

            logger.debug("Request URL: {}", uri);

            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<LocationResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    LocationResponse.class
            );

            LocationResponse locationResponse = response.getBody();

            if (locationResponse != null && locationResponse.getData() != null) {
                logger.info("Successfully retrieved {} locations for keyword: '{}'",
                        locationResponse.getData().size(), keyword);
            } else {
                logger.warn("Received empty or null response for keyword: '{}'", keyword);
            }

            return locationResponse;

        } catch (HttpClientErrorException e) {
            logger.error("Client error calling Amadeus API - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Amadeus API client error: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error calling Amadeus API - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Amadeus API server error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error calling Amadeus API: {}", e.getMessage(), e);
            throw new AmadeusApiException("Unexpected error calling Amadeus API: " + e.getMessage(), e);
        }
    }


    public FlightOffersResponse searchFlights(Map<String, String> params) {
        try {
            logger.info("Searching flights with parameters: {}", params);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(urlConfig.getBaseUrlV2() + SHOPPING_URL);

            params.forEach(builder::queryParam);
            //builder.queryParam("include", "locations");

            URI uri = builder.build().toUri();
            logger.debug("Flight search URL: {}", uri);

            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<FlightOffersResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    FlightOffersResponse.class
            );

            FlightOffersResponse flightResponse = response.getBody();

            if (flightResponse != null && flightResponse.getData() != null) {
                logger.info("Successfully retrieved {} flight offers", flightResponse.getData().size());
            } else {
                logger.warn("Received empty or null flight response");
            }

            return flightResponse;

        } catch (HttpClientErrorException e) {
            logger.error("Client error searching flights - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Flight search client error: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error searching flights - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Flight search server error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error searching flights: {}", e.getMessage(), e);
            throw new AmadeusApiException("Unexpected error searching flights: " + e.getMessage(), e);
        }
    }

    private HttpHeaders createAuthHeaders() {
        try {
            String token = authService.getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", AMADEUS_JSON);
            return headers;
        } catch (Exception e) {
            logger.error("Failed to create auth headers: {}", e.getMessage(), e);
            throw new AmadeusApiException("Failed to authenticate request: " + e.getMessage(), e);
        }
    }

    public Map<String,Object> priceOffer(Map<String,Object> offer) {
        URI uri = URI.create(urlConfig.getBaseUrlV2() + SHOPP_URL);
        HttpHeaders h = createAuthHeaders();
        HttpEntity<Map<String,Object>> req = new HttpEntity<>(Map.of("data", List.of(offer)), h);
        return restTemplate.postForObject(uri, req, Map.class);
    }

    public LocationResponse getLocationById(String locationId) {
        try {
            logger.info("Getting location by ID: '{}'", locationId);

            String url = urlConfig.getBaseUrlV1() + URL_REFERENCE + locationId;
            logger.debug("Request URL: {}", url);

            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<LocationResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    LocationResponse.class
            );

            LocationResponse locationResponse = response.getBody();
            logger.info("Successfully retrieved location details for ID: '{}'", locationId);

            return locationResponse;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("Location not found for ID: '{}'", locationId);
                throw new AmadeusApiException("Location not found: " + locationId, e);
            }
            logger.error("Client error calling Amadeus API - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Amadeus API client error: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error calling Amadeus API - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Amadeus API server error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error calling Amadeus API: {}", e.getMessage(), e);
            throw new AmadeusApiException("Unexpected error calling Amadeus API: " + e.getMessage(), e);
        }
    }

    public LocationResponse searchAirports(String keyword, int limit) {
        return searchLocations(keyword, "AIRPORT", limit);
    }

    public LocationResponse searchCities(String keyword, int limit) {
        return searchLocations(keyword, "CITY", limit);
    }

    public LocationResponse searchAirportsAndCities(String keyword, int limit) {
        return searchLocations(keyword, "AIRPORT,CITY", limit);
    }
}