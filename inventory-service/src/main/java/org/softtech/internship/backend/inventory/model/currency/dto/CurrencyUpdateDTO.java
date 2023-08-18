package org.softtech.internship.backend.inventory.model.currency.dto;

import lombok.Getter;
import org.softtech.internship.backend.inventory.model.currency.Currency;

import java.math.BigDecimal;

@Getter
public class CurrencyUpdateDTO {
    private String currency_name;
    private BigDecimal currency_rate;

    public boolean isEmpty() {
        return (currency_name == null || currency_name.isEmpty()) && currency_rate == null;
    }

    public boolean isSame(Currency oldCurrency) {
        if (currency_rate != null && currency_name != null) {
            return oldCurrency.getCurrencyName().equals(currency_name) &&
                    oldCurrency.getCurrencyRate().equals(currency_rate);
        } else if (currency_rate != null) {
            return oldCurrency.getCurrencyRate().equals(currency_rate);
        } else return oldCurrency.getCurrencyName().equals(currency_name);
    }
}
