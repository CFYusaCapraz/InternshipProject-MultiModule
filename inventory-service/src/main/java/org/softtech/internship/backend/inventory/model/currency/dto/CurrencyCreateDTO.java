package org.softtech.internship.backend.inventory.model.currency.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CurrencyCreateDTO {
    private String currency_name;
    private BigDecimal currency_rate;
}
