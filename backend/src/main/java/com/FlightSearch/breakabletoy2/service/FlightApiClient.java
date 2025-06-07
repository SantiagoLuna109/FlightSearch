package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.dto.FlightSearchResponse;
import com.FlightSearch.breakabletoy2.dto.FlightSearchRequest;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class FlightApiClient {

    private static final Logger logger = LoggerFactory.getLogger(FlightApiClient.class);
    private static final String BASE_URL = "https://test.api.amadeus.com/v2";

    private final RestTemplate restTemplate;
    private final AmadeusAuthService authService;

    public FlightApiClient(RestTemplate restTemplate, AmadeusAuthService authService) {
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    public FlightSearchResponse searchFlights(FlightSearchRequest request) {
        try {
            logger.info("Searching flights: {} -> {}, departure: {}, adults: {}",
                    request.getOriginLocationCode(), request.getDestinationLocationCode(),
                    request.getDepartureDate(), request.getAdults());

            URI uri = buildSearchUri(request);
            logger.debug("Request URL: {}", uri);

            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<FlightSearchResponse> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    FlightSearchResponse.class
            );

            FlightSearchResponse flightResponse = response.getBody();

            if (flightResponse != null && flightResponse.getData() != null) {
                logger.info("Successfully retrieved {} flight offers", flightResponse.getData().size());
            } else {
                logger.warn("Received empty response for flight search");
            }

            return flightResponse;

        } catch (HttpClientErrorException e) {
            logger.error("Client error calling Amadeus Flight API - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Amadeus Flight API client error: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error calling Amadeus Flight API - Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Amadeus Flight API server error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error calling Amadeus Flight API: {}", e.getMessage(), e);
            throw new AmadeusApiException("Unexpected error calling Amadeus Flight API: " + e.getMessage(), e);
        }
    }

    private URI buildSearchUri(FlightSearchRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "/shopping/flight-offers")
                .queryParam("originLocationCode", request.getOriginLocationCode())
                .queryParam("destinationLocationCode", request.getDestinationLocationCode())
                .queryParam("departureDate", request.getDepartureDate())
                .queryParam("adults", request.getAdults());

        if (request.getReturnDate() != null) {
            builder.queryParam("returnDate", request.getReturnDate());
        }

        if (request.getChildren() != null && request.getChildren() > 0) {
            builder.queryParam("children", request.getChildren());
        }

        if (request.getInfants() != null && request.getInfants() > 0) {
            builder.queryParam("infants", request.getInfants());
        }

        if (request.getCurrencyCode() != null && !request.getCurrencyCode().isEmpty()) {
            builder.queryParam("currencyCode", request.getCurrencyCode());
        }

        if (request.getTravelClass() != null && !request.getTravelClass().isEmpty()) {
            builder.queryParam("travelClass", request.getTravelClass());
        }

        if (request.getNonStop() != null) {
            builder.queryParam("nonStop", request.getNonStop());
        }

        if (request.getMax() != null) {
            builder.queryParam("max", request.getMax());
        }

        if (request.getIncludedAirlineCodes() != null && !request.getIncludedAirlineCodes().isEmpty()) {
            builder.queryParam("includedAirlineCodes", request.getIncludedAirlineCodes());
        }

        if (request.getExcludedAirlineCodes() != null && !request.getExcludedAirlineCodes().isEmpty()) {
            builder.queryParam("excludedAirlineCodes", request.getExcludedAirlineCodes());
        }

        return builder.build().toUri();
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
