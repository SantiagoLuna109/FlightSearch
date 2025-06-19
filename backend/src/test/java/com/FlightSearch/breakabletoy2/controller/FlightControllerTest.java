package com.FlightSearch.breakabletoy2.controller;

import com.FlightSearch.breakabletoy2.service.CurrencyConversionService;
import com.FlightSearch.breakabletoy2.service.FlightFilterService;
import com.FlightSearch.breakabletoy2.service.FlightSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FlightControllerTest {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        FlightSearchService        searchSvc   = Mockito.mock(FlightSearchService.class);
        FlightFilterService        filterSvc   = Mockito.mock(FlightFilterService.class);
        CurrencyConversionService  currencySvc = Mockito.mock(CurrencyConversionService.class);

        FlightController controller =
                new FlightController(searchSvc, filterSvc, currencySvc);

        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void searchEndpointResponds200() throws Exception {
        mvc.perform(get("/api/v1/flights/search")
                        .param("origin",        "JFK")
                        .param("destination",   "YYZ")
                        .param("departureDate", "2025-07-01"))
                .andExpect(status().isOk());
    }
}

