package com.FlightSearch.breakabletoy2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FlightSearchApp {
    public static void main(String[] args) {
        SpringApplication.run(FlightSearchApp.class, args);
    }
}
