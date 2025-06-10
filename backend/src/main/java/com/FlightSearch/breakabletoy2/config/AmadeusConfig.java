package com.FlightSearch.breakabletoy2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AmadeusConfig {

    private final AmadeusProperties properties;

    public AmadeusConfig(AmadeusProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(30));

        restTemplate.setRequestFactory(factory);
        restTemplate.getInterceptors().add(loggingInterceptor());

        return restTemplate;
    }

    @Bean
    public RestTemplate amadeusRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(30));

        restTemplate.setRequestFactory(factory);

        return restTemplate;
    }

    @Bean
    public AmadeusUrlConfig amadeusUrlConfig() {
        return AmadeusUrlConfig.builder()
                .baseUrl(properties.getApi().getBaseUrl())
                .authUrl(properties.getApi().getFullAuthUrl())
                .baseUrlV1(properties.getApi().getBaseUrlV1())
                .baseUrlV2(properties.getApi().getBaseUrlV2())
                .build();
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            System.out.println("Request URI: " + request.getURI());
            System.out.println("Request Method: " + request.getMethod());
            return execution.execute(request, body);
        };
    }

    public static class AmadeusUrlConfig {
        private final String baseUrl;
        private final String authUrl;
        private final String baseUrlV1;
        private final String baseUrlV2;

        private AmadeusUrlConfig(Builder builder) {
            this.baseUrl = builder.baseUrl;
            this.authUrl = builder.authUrl;
            this.baseUrlV1 = builder.baseUrlV1;
            this.baseUrlV2 = builder.baseUrlV2;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public String getBaseUrlV1() {
            return baseUrlV1;
        }

        public String getBaseUrlV2() {
            return baseUrlV2;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String baseUrl;
            private String authUrl;
            private String baseUrlV1;
            private String baseUrlV2;

            public Builder baseUrl(String baseUrl) {
                this.baseUrl = baseUrl;
                return this;
            }

            public Builder authUrl(String authUrl) {
                this.authUrl = authUrl;
                return this;
            }

            public Builder baseUrlV1(String baseUrlV1) {
                this.baseUrlV1 = baseUrlV1;
                return this;
            }

            public Builder baseUrlV2(String baseUrlV2) {
                this.baseUrlV2 = baseUrlV2;
                return this;
            }

            public AmadeusUrlConfig build() {
                return new AmadeusUrlConfig(this);
            }
        }
    }
}