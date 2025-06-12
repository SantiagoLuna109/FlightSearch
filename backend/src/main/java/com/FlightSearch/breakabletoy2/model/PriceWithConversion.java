package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PriceWithConversion {

    private Price originalPrice;
    private String requestedCurrency;
    private BigDecimal convertedTotal;
    private BigDecimal convertedBase;
    private BigDecimal convertedGrandTotal;
    private BigDecimal exchangeRate;
    private LocalDate conversionDate;
    private boolean conversionApplied;

    public PriceWithConversion() {}

    public PriceWithConversion(Price originalPrice) {
        this.originalPrice = originalPrice;
        this.conversionApplied = false;
    }

    public Price getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Price originalPrice) { this.originalPrice = originalPrice; }

    public String getRequestedCurrency() { return requestedCurrency; }
    public void setRequestedCurrency(String requestedCurrency) { this.requestedCurrency = requestedCurrency; }

    public BigDecimal getConvertedTotal() { return convertedTotal; }
    public void setConvertedTotal(BigDecimal convertedTotal) { this.convertedTotal = convertedTotal; }

    public BigDecimal getConvertedBase() { return convertedBase; }
    public void setConvertedBase(BigDecimal convertedBase) { this.convertedBase = convertedBase; }

    public BigDecimal getConvertedGrandTotal() { return convertedGrandTotal; }
    public void setConvertedGrandTotal(BigDecimal convertedGrandTotal) { this.convertedGrandTotal = convertedGrandTotal; }

    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }

    public LocalDate getConversionDate() { return conversionDate; }
    public void setConversionDate(LocalDate conversionDate) { this.conversionDate = conversionDate; }

    public boolean isConversionApplied() { return conversionApplied; }
    public void setConversionApplied(boolean conversionApplied) { this.conversionApplied = conversionApplied; }

    public String getDisplayTotal() {
        if (conversionApplied && convertedTotal != null) {
            return convertedTotal + " " + requestedCurrency;
        }
        return originalPrice.getTotal() + " " + originalPrice.getCurrency();
    }

    public String getOriginalDisplayTotal() {
        return originalPrice.getTotal() + " " + originalPrice.getCurrency();
    }
}
