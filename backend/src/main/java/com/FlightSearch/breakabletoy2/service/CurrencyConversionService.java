package com.FlightSearch.breakabletoy2.service;

import com.FlightSearch.breakabletoy2.exception.CurrencyConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class CurrencyConversionService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionService.class);

    private static final Map<String, BigDecimal> USD_BASE_RATES = new HashMap<>();

    private static final LocalDate RATES_DATE = LocalDate.of(2025, 1, 10);

    static {
        USD_BASE_RATES.put("USD", BigDecimal.ONE);
        USD_BASE_RATES.put("EUR", new BigDecimal("0.9731"));
        USD_BASE_RATES.put("MXN", new BigDecimal("20.5843"));
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-null and non-negative");
        }

        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        validateCurrency(fromCurrency);
        validateCurrency(toCurrency);

        try {
            BigDecimal amountInUSD = convertToUSD(amount, fromCurrency);
            BigDecimal convertedAmount = convertFromUSD(amountInUSD, toCurrency);

            logger.debug("Converted {} {} to {} {} (via USD)",
                    amount, fromCurrency, convertedAmount, toCurrency);

            return convertedAmount;
        } catch (Exception e) {
            logger.error("Error converting currency from {} to {}: {}",
                    fromCurrency, toCurrency, e.getMessage());
            throw new CurrencyConversionException(
                    "Failed to convert from " + fromCurrency + " to " + toCurrency, e);
        }
    }

    private BigDecimal convertToUSD(BigDecimal amount, String fromCurrency) {
        if ("USD".equals(fromCurrency)) {
            return amount;
        }

        // Salu2
        BigDecimal rate = USD_BASE_RATES.get(fromCurrency);
        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not found for currency: " + fromCurrency);
        }

        return amount.divide(rate, 4, RoundingMode.HALF_UP);
    }

    private BigDecimal convertFromUSD(BigDecimal amountInUSD, String toCurrency) {
        if ("USD".equals(toCurrency)) {
            return amountInUSD.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal rate = USD_BASE_RATES.get(toCurrency);
        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not found for currency: " + toCurrency);
        }

        return amountInUSD.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    private void validateCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency code cannot be null or empty");
        }

        if (!isSupported(currency)) {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
    }

    public boolean isSupported(String currency) {
        if (currency == null) {
            return false;
        }
        return USD_BASE_RATES.containsKey(currency.toUpperCase());
    }

    public Set<String> getSupportedCurrencies() {
        return USD_BASE_RATES.keySet();
    }

    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        if (fromCurrency == null || toCurrency == null) {
            throw new IllegalArgumentException("Currency codes cannot be null");
        }

        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }

        validateCurrency(fromCurrency);
        validateCurrency(toCurrency);

        BigDecimal fromRate = USD_BASE_RATES.get(fromCurrency);
        BigDecimal toRate = USD_BASE_RATES.get(toCurrency);

        if (fromRate == null) {
            throw new IllegalArgumentException("Exchange rate not found for currency: " + fromCurrency);
        }
        if (toRate == null) {
            throw new IllegalArgumentException("Exchange rate not found for currency: " + toCurrency);
        }

        //Hola Juan
        return toRate.divide(fromRate, 4, RoundingMode.HALF_UP);
    }

    public LocalDate getRatesDate() {
        return RATES_DATE;
    }

    public Map<String, BigDecimal> getAllRatesForCurrency(String baseCurrency) {
        if (baseCurrency == null) {
            throw new IllegalArgumentException("Base currency cannot be null");
        }

        baseCurrency = baseCurrency.toUpperCase();
        validateCurrency(baseCurrency);

        Map<String, BigDecimal> rates = new HashMap<>();

        for (String currency : USD_BASE_RATES.keySet()) {
            if (!currency.equals(baseCurrency)) {
                rates.put(currency, getExchangeRate(baseCurrency, currency));
            }
        }

        return rates;
    }
}
