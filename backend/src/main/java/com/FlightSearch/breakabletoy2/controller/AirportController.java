package com.FlightSearch.breakabletoy2.controller;

import com.FlightSearch.breakabletoy2.dto.AirportSearchRequest;
import com.FlightSearch.breakabletoy2.model.Airport;
import com.FlightSearch.breakabletoy2.service.AirportSearchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airports")
public class AirportController {

    private final AirportSearchService airportSearchService;

    public AirportController(AirportSearchService airportSearchService) {
        this.airportSearchService = airportSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Airport>> searchAirports(@Valid AirportSearchRequest request) {
        List<Airport> airports = airportSearchService.searchAirports(
                request.getKeyword(),
                request.getCountryCode()
        );
        return ResponseEntity.ok(airports);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Airport> getAirportByCode(@PathVariable String code) {
        Airport airport = airportSearchService.getAirportByCode(code);
        return ResponseEntity.ok(airport);
    }
}
