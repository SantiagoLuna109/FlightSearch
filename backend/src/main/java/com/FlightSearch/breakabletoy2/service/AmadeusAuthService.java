package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.config.AmadeusConfig;
import com.FlightSearch.breakabletoy2.exception.AmadeusAuthenticationException;
import com.FlightSearch.breakabletoy2.model.auth.AmadeusTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class AmadeusAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AmadeusAuthService.class);

    private final RestTemplate restTemplate;
    private final AmadeusConfig amadeusConfig;

    @Value("${amadeus.api.auth-url}")
    private String authUrl;

    private AmadeusTokenResponse currentToken;
    private final Object tokenLock = new Object();

    public AmadeusAuthService(RestTemplate restTemplate, AmadeusConfig amadeusConfig) {
        this.restTemplate = restTemplate;
        this.amadeusConfig = amadeusConfig;
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

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.add("grant_type", "client_credentials");
                body.add("client_id", amadeusConfig.getApiKey());
                body.add("client_secret", amadeusConfig.getApiSecret());

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
        return currentToken.isTokenExpired(amadeusConfig.getExpiryBuffer());
    }

    public AmadeusTokenResponse getCurrentTokenInfo() {
        synchronized (tokenLock) {
            return currentToken;
        }
    }
}
