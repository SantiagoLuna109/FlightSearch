package com.FlightSearch.breakabletoy2.client;

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
import java.util.Map;

@Component
public class AmadeusApiClient {

    private static final Logger logger = LoggerFactory.getLogger(AmadeusApiClient.class);
    private static final String BASE_URL_V1 = "https://test.api.amadeus.com/v1";
    private static final String BASE_URL_V2 = "https://test.api.amadeus.com/v2";

    private final RestTemplate restTemplate;
    private final AmadeusAuthService authService;

    public AmadeusApiClient(RestTemplate restTemplate, AmadeusAuthService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    public LocationResponse searchLocations(String keyword, String subType, int limit) {
        try {
            logger.info("Searching locations - keyword: '{}', subType: '{}', limit: {}", keyword, subType, limit);

            URI uri = UriComponentsBuilder
                    .fromHttpUrl(BASE_URL_V1 + "/reference-data/locations")
                    .queryParam("keyword", keyword)
                    .queryParam("subType", subType)
                    .queryParam("page[limit]", limit)
                    .queryParam("sort", "analytics.travelers.score")
                    .queryParam("view", "FULL")
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
                    .fromHttpUrl(BASE_URL_V2 + "/shopping/flight-offers");

            params.forEach(builder::queryParam);

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

    public LocationResponse getLocationById(String locationId) {
        try {
            logger.info("Getting location by ID: '{}'", locationId);

            String url = BASE_URL_V1 + "/reference-data/locations/" + locationId;
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

    private HttpHeaders createAuthHeaders() {
        try {
            String token = authService.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/vnd.amadeus+json");

            return headers;
        } catch (Exception e) {
            logger.error("Failed to create auth headers: {}", e.getMessage(), e);
            throw new AmadeusApiException("Failed to authenticate request: " + e.getMessage(), e);
        }
    }
}