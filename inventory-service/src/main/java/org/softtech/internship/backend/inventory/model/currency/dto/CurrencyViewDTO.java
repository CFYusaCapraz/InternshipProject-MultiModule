package org.softtech.internship.backend.inventory.model.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyViewDTO {
    private UUID currency_id;
    private String currency_name;
    private BigDecimal currency_rate;
}
