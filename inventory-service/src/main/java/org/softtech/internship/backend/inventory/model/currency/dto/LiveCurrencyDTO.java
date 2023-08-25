package org.softtech.internship.backend.inventory.model.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

@Data
public class LiveCurrencyDTO {
    @JsonProperty("quotes")
    private Map<String, BigDecimal> quotes;
    @JsonProperty("source")
    private String source;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("timestamp")
    private Long timestamp;

    public double getExchangeRateForCurrency(String currencyCode) {
        BigDecimal exchangeRate = quotes.get(currencyCode);
        if (exchangeRate != null && exchangeRate.compareTo(BigDecimal.ZERO) != 0) {
            return BigDecimal.ONE.divide(exchangeRate, MathContext.DECIMAL64).doubleValue();
        }
        return 0.0;
    }
}