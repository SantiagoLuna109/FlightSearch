package com.FlightSearch.breakabletoy2.model;

import com.FlightSearch.breakabletoy2.dto.FlightDetailsResponse.AmenityDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class FareDetailsBySegment {
    @Setter
    @Getter
    private String travelerId;
    private String cabin;
    private String travelClass;
    private List<AmenityDetails> amenities;
}
