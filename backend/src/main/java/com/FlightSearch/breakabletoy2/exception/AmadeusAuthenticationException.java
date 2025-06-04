package com.FlightSearch.breakabletoy2.exception;

public class AmadeusAuthenticationException extends RuntimeException {

    public AmadeusAuthenticationException(String message) {
        super(message);
    }

    public AmadeusAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
