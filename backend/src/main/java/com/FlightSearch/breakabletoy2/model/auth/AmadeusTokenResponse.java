package com.FlightSearch.breakabletoy2.model.auth;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class AmadeusTokenResponse {

    private String type;
    private String username;

    @JsonProperty("application_name")
    private String applicationName;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    private String state;
    private String scope;

    private Instant tokenObtainedAt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Instant getTokenObtainedAt() {
        return tokenObtainedAt;
    }

    public void setTokenObtainedAt(Instant tokenObtainedAt) {
        this.tokenObtainedAt = tokenObtainedAt;
    }

    public boolean isTokenExpired(Integer bufferSeconds) {
        if (tokenObtainedAt == null || expiresIn == null) {
            return true;
        }

        Instant expiryTime = tokenObtainedAt.plusSeconds(expiresIn - bufferSeconds);
        return Instant.now().isAfter(expiryTime);
    }
}
