package com.FlightSearch.breakabletoy2.exception;

public class FlightNotFoundException extends RuntimeException{
    public FlightNotFoundException(String message){
        super(message);
    }
    public FlightNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
