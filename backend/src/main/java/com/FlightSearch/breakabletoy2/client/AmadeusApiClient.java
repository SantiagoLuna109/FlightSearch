package com.FlightSearch.breakabletoy2.client;

import com.FlightSearch.breakabletoy2.config.AmadeusConfig;
import com.FlightSearch.breakabletoy2.exception.AmadeusApiException;
import com.FlightSearch.breakabletoy2.model.amadeus.LocationResponse;
import com.FlightSearch.breakabletoy2.service.AmadeusAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class AmadeusApiClient {

    private static final Logger logger = LoggerFactory.getLogger(AmadeusApiClient.class);

    private final RestTemplate restTemplate;
    private final AmadeusConfig amadeusConfig;
    private final AmadeusAuthService authService;

    public AmadeusApiClient(RestTemplate restTemplate, AmadeusConfig amadeusConfig, AmadeusAuthService authService) {
        this.restTemplate = restTemplate;
        this.amadeusConfig = amadeusConfig;
        this.authService = authService;
    }

    public LocationResponse searchLocations(Map<String, String> params) {
        try {
            String token = authService.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String baseUrl = amadeusConfig.getBaseUrl() + "/v1/reference-data/locations";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);

            params.forEach((key, value) -> {
                logger.debug("Adding parameter: {} = {}", key, value);
                builder.queryParam(key, value);
            });

            String url = builder.toUriString();
            logger.info("Final URL: {}", url);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<LocationResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    LocationResponse.class
            );

            logger.info("Response status: {}", response.getStatusCode());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Amadeus API error: Status={}, Body={}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new AmadeusApiException("Amadeus API error: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error calling Amadeus API", e);
            throw new AmadeusApiException("Failed to call Amadeus API: " + e.getMessage(), e);
        }
    }
}