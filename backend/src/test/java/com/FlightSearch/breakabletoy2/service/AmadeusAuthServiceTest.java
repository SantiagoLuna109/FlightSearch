package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.config.AmadeusProperties;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class AmadeusAuthServiceTest {

    @Test
    void buildsWithMockDeps() {
        RestTemplate       rest  = new RestTemplate();
        AmadeusProperties  props = new AmadeusProperties();   // configure if needed

        AmadeusAuthService auth  = new AmadeusAuthService(rest, props);

        assertThat(auth).isNotNull();
    }
}

