package com.FlightSearch.breakabletoy2.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "amadeus")
@Validated
public class AmadeusProperties {

    @NotNull
    private Api api = new Api();

    @NotNull
    private Token token = new Token();

    private static final String URL_TOKEN = "/v1/security/oauth2/token";

    public static class Api {
        @NotBlank(message = "Amadeus API base URL is required")
        private String baseUrl;

        private String authUrl;

        private String baseUrlV1;

        private String baseUrlV2;

        @NotBlank(message = "Amadeus API key is required")
        private String key;

        @NotBlank(message = "Amadeus API secret is required")
        private String secret;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }

        public String getBaseUrlV1() {
            return baseUrlV1;
        }

        public void setBaseUrlV1(String baseUrlV1) {
            this.baseUrlV1 = baseUrlV1;
        }

        public String getBaseUrlV2() {
            return baseUrlV2;
        }

        public void setBaseUrlV2(String baseUrlV2) {
            this.baseUrlV2 = baseUrlV2;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getFullAuthUrl() {
            return authUrl != null ? authUrl : baseUrl + URL_TOKEN;
        }
    }

    public static class Token {
        @Min(value = 60, message = "Expiry buffer must be at least 60 seconds")
        private Integer expiryBuffer = 300;

        public Integer getExpiryBuffer() {
            return expiryBuffer;
        }

        public void setExpiryBuffer(Integer expiryBuffer) {
            this.expiryBuffer = expiryBuffer;
        }
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}