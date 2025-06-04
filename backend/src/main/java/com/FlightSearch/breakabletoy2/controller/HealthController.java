package com.FlightSearch.breakabletoy2.controller;

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

    public HealthController(AmadeusAuthService authService) {
        this.authService = authService;
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
}
