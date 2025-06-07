package com.FlightSearch.breakabletoy2.controller;

import com.FlightSearch.breakabletoy2.config.AmadeusProperties;
import com.FlightSearch.breakabletoy2.model.auth.AmadeusTokenResponse;
import com.FlightSearch.breakabletoy2.service.AmadeusAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    private final AmadeusAuthService authService;
    private final AmadeusProperties properties;

    public HealthController(AmadeusAuthService authService, AmadeusProperties properties) {
        this.authService = authService;
        this.properties = properties;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Flight Search API");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth-test")
    public ResponseEntity<Map<String, Object>> testAuth() {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = authService.getAccessToken();
            AmadeusTokenResponse tokenInfo = authService.getCurrentTokenInfo();

            response.put("status", "SUCCESS");
            response.put("tokenPresent", token != null && !token.isEmpty());
            response.put("tokenType", tokenInfo.getTokenType());
            response.put("expiresIn", tokenInfo.getExpiresIn());
            response.put("state", tokenInfo.getState());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/config-check")
    public ResponseEntity<Map<String, Object>> checkConfig() {
        Map<String, Object> response = new HashMap<>();

        response.put("baseUrl", properties.getApi().getBaseUrl());
        response.put("authUrl", properties.getApi().getFullAuthUrl());
        response.put("apiKeyPresent", properties.getApi().getKey() != null && !properties.getApi().getKey().isEmpty());
        response.put("apiSecretPresent", properties.getApi().getSecret() != null && !properties.getApi().getSecret().isEmpty());
        response.put("expiryBuffer", properties.getToken().getExpiryBuffer());

        if (properties.getApi().getKey() != null) {
            response.put("apiKeyLength", properties.getApi().getKey().length());
            response.put("apiKeyStartsWith", properties.getApi().getKey().substring(0, Math.min(5, properties.getApi().getKey().length())));
        }

        return ResponseEntity.ok(response);
    }
}