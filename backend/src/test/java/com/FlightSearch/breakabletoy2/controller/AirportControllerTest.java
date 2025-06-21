package com.FlightSearch.breakabletoy2.controller;

import com.FlightSearch.breakabletoy2.service.AirportSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AirportControllerTest {

    private MockMvc mvc;
    private String basePath;

    @BeforeEach
    void setUp() {
        AirportSearchService svc = Mockito.mock(AirportSearchService.class);
        Mockito.when(svc.searchAirports(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(java.util.List.of());
        AirportController controller = new AirportController(svc);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        RequestMapping rm = AnnotatedElementUtils.findMergedAnnotation(
                AirportController.class, RequestMapping.class);
        basePath = (rm != null && rm.value().length > 0) ? rm.value()[0] : "";
    }

    @Test
    void searchEndpointResponds2xx() throws Exception {
        mvc.perform(get(basePath + "/search")
                        .param("keyword", "ABC"))
                .andExpect(status().is2xxSuccessful());
    }
}
