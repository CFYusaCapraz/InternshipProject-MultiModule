package org.softtech.internship.backend.inventory.model.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
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

}