package com.FlightSearch.breakabletoy2.controller;

import com.FlightSearch.breakabletoy2.service.CurrencyConversionService;
import com.FlightSearch.breakabletoy2.exception.CurrencyConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/currency")
@CrossOrigin(origins = "*")
public class CurrencyController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    private final CurrencyConversionService currencyConversionService;

    public CurrencyController(CurrencyConversionService currencyConversionService) {
        this.currencyConversionService = currencyConversionService;
    }

    @GetMapping("/supported")
    public ResponseEntity<?> getSupportedCurrencies() {
        try {
            return ResponseEntity.ok(Map.of(
                    "currencies", currencyConversionService.getSupportedCurrencies(),
                    "ratesDate", currencyConversionService.getRatesDate(),
                    "note", "Exchange rates are fixed as of " + currencyConversionService.getRatesDate()
            ));
        } catch (Exception e) {
            logger.error("Error getting supported currencies: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve supported currencies"));
        }
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to) {

        try {
            logger.info("Converting {} {} to {}", amount, from, to);

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Amount must be non-negative"));
            }

            BigDecimal convertedAmount = currencyConversionService.convert(amount, from, to);
            BigDecimal exchangeRate = currencyConversionService.getExchangeRate(from, to);

            return ResponseEntity.ok(Map.of(
                    "originalAmount", amount,
                    "originalCurrency", from.toUpperCase(),
                    "convertedAmount", convertedAmount,
                    "convertedCurrency", to.toUpperCase(),
                    "exchangeRate", exchangeRate,
                    "ratesDate", currencyConversionService.getRatesDate(),
                    "conversion", String.format("%.2f %s = %.2f %s",
                            amount, from.toUpperCase(), convertedAmount, to.toUpperCase())
            ));

        } catch (IllegalArgumentException e) {
            logger.error("Invalid currency conversion request: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (CurrencyConversionException e) {
            logger.error("Currency conversion error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Currency conversion failed", "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error during currency conversion: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<?> getExchangeRates(@RequestParam(defaultValue = "USD") String base) {
        try {
            logger.info("Getting exchange rates for base currency: {}", base);

            Map<String, BigDecimal> rates = currencyConversionService.getAllRatesForCurrency(base);

            return ResponseEntity.ok(Map.of(
                    "baseCurrency", base.toUpperCase(),
                    "rates", rates,
                    "ratesDate", currencyConversionService.getRatesDate(),
                    "note", "1 " + base.toUpperCase() + " equals the displayed amount in each currency"
            ));

        } catch (IllegalArgumentException e) {
            logger.error("Invalid base currency: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error retrieving exchange rates: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve exchange rates"));
        }
    }

    @GetMapping("/rate")
    public ResponseEntity<?> getExchangeRate(
            @RequestParam String from,
            @RequestParam String to) {

        try {
            logger.info("Getting exchange rate from {} to {}", from, to);

            BigDecimal rate = currencyConversionService.getExchangeRate(from, to);

            return ResponseEntity.ok(Map.of(
                    "from", from.toUpperCase(),
                    "to", to.toUpperCase(),
                    "rate", rate,
                    "ratesDate", currencyConversionService.getRatesDate(),
                    "conversion", String.format("1 %s = %.4f %s",
                            from.toUpperCase(), rate, to.toUpperCase())
            ));

        } catch (IllegalArgumentException e) {
            logger.error("Invalid currency pair: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error retrieving exchange rate: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve exchange rate"));
        }
    }
}