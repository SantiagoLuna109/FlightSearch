package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.config.AmadeusProperties;
import com.FlightSearch.breakabletoy2.exception.AmadeusAuthenticationException;
import com.FlightSearch.breakabletoy2.model.auth.AmadeusTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class AmadeusAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AmadeusAuthService.class);

    private final RestTemplate restTemplate;
    private final AmadeusProperties properties;

    private AmadeusTokenResponse currentToken;
    private final Object tokenLock = new Object();

    public AmadeusAuthService(RestTemplate restTemplate, AmadeusProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public String getAccessToken() {
        synchronized (tokenLock) {
            if (currentToken == null || isTokenExpired()) {
                logger.info("Token is null or expired, obtaining new token");
                refreshToken();
            }
            return currentToken.getAccessToken();
        }
    }

    public void refreshToken() {
        synchronized (tokenLock) {
            try {
                logger.info("Requesting new access token from Amadeus");

                final String authUrl = properties.getApi().getFullAuthUrl();
                logger.debug("Auth URL: {}", authUrl);

                logger.debug("API Key present: {}", properties.getApi().getKey() != null && !properties.getApi().getKey().isEmpty());
                logger.debug("API Secret present: {}", properties.getApi().getSecret() != null && !properties.getApi().getSecret().isEmpty());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.add("grant_type", "client_credentials");
                body.add("client_id", properties.getApi().getKey());
                body.add("client_secret", properties.getApi().getSecret());

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

                ResponseEntity<AmadeusTokenResponse> response = restTemplate.postForEntity(
                        authUrl,
                        request,
                        AmadeusTokenResponse.class
                );

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    currentToken = response.getBody();
                    currentToken.setTokenObtainedAt(Instant.now());
                    logger.info("Successfully obtained new access token");
                    logger.debug("Token expires in {} seconds", currentToken.getExpiresIn());
                } else {
                    throw new AmadeusAuthenticationException("Failed to obtain access token: Invalid response");
                }

            } catch (HttpClientErrorException e) {
                logger.error("HTTP Error obtaining access token. Status: {}, Response: {}",
                        e.getStatusCode(), e.getResponseBodyAsString());
                throw new AmadeusAuthenticationException(
                        "Failed to obtain access token: " + e.getStatusCode() + "_" + e.getResponseBodyAsString(), e);
            } catch (RestClientException e) {
                logger.error("Error obtaining access token", e);
                throw new AmadeusAuthenticationException("Failed to obtain access token: " + e.getMessage(), e);
            }
        }
    }

    private boolean isTokenExpired() {
        if (currentToken == null) {
            return true;
        }
        return currentToken.isTokenExpired(properties.getToken().getExpiryBuffer());
    }

    public AmadeusTokenResponse getCurrentTokenInfo() {
        synchronized (tokenLock) {
            return currentToken;
        }
    }
}
