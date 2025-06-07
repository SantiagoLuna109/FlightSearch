package com.FlightSearch.breakabletoy2.exception;

public class AmadeusApiException extends RuntimeException {

  public AmadeusApiException(String message) {
    super(message);
  }

  public AmadeusApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
