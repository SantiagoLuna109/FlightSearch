package com.FlightSearch.breakabletoy2.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Price {

    private String currency;
    private String total;
    private String base;
    private String grandTotal;
    private List<Fee> fees;
    private List<Tax> taxes;

    public Price() {}

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }

    public String getGrandTotal() { return grandTotal; }
    public void setGrandTotal(String grandTotal) { this.grandTotal = grandTotal; }

    public List<Fee> getFees() { return fees; }
    public void setFees(List<Fee> fees) { this.fees = fees; }

    public List<Tax> getTaxes() { return taxes; }
    public void setTaxes(List<Tax> taxes) { this.taxes = taxes; }

    public BigDecimal getTotalAsBigDecimal() {
        try {
            return total != null ? new BigDecimal(total) : BigDecimal.ZERO;
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getBaseAsBigDecimal() {
        try {
            return base != null ? new BigDecimal(base) : BigDecimal.ZERO;
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public String getFormattedTotal() {
        return currency + " " + total;
    }

    @Override
    public String toString() {
        return "Price{" +
                "currency='" + currency + '\'' +
                ", total='" + total + '\'' +
                ", base='" + base + '\'' +
                '}';
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Fee {
        private String amount;
        private String type;

        public Fee() {}

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        @Override
        public String toString() {
            return "Fee{amount='" + amount + "', type='" + type + "'}";
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Tax {
        private String amount;
        private String code;

        public Tax() {}

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        @Override
        public String toString() {
            return "Tax{amount='" + amount + "', code='" + code + "'}";
        }
    }
}
