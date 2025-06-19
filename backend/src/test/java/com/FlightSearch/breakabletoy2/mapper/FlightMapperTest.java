package com.FlightSearch.breakabletoy2.mapper;

import com.FlightSearch.breakabletoy2.client.AmadeusApiClient;
import com.FlightSearch.breakabletoy2.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class FlightMapperTest {

    private final FlightMapper mapper =
            new FlightMapper(
                    Mockito.mock(AmadeusApiClient.class),
                    Mockito.mock(IataCache.class),
                    Mockito.mock(DictionaryService.class));

    @Test
    void mapperInstantiatesWithMocks() {
        assertThat(mapper).isNotNull();
    }
}


