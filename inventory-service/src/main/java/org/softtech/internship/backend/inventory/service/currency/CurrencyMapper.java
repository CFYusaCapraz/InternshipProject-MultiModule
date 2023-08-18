package org.softtech.internship.backend.inventory.service.currency;

import org.softtech.internship.backend.inventory.model.currency.dto.CurrencyCreateDTO;
import org.softtech.internship.backend.inventory.model.currency.dto.CurrencyUpdateDTO;
import org.softtech.internship.backend.inventory.model.currency.dto.CurrencyViewDTO;
import org.softtech.internship.backend.inventory.model.currency.Currency;

import java.time.LocalDateTime;

public class CurrencyMapper {
    public static Currency createMapper(CurrencyCreateDTO createDTO) {
        return Currency.builder()
                .currencyName(createDTO.getCurrency_name())
                .currencyRate(createDTO.getCurrency_rate())
                .build();
    }

    public static CurrencyViewDTO viewMapper(Currency currency) {
        return CurrencyViewDTO.builder()
                .currency_id(currency.getCurrencyId())
                .currency_name(currency.getCurrencyName())
                .currency_rate(currency.getCurrencyRate())
                .build();
    }

    public static Currency updateMapper(Currency oldCurrency, CurrencyUpdateDTO updateDTO) {
        if (updateDTO.getCurrency_name() != null && !updateDTO.getCurrency_name().isEmpty()) {
            oldCurrency.setCurrencyName(updateDTO.getCurrency_name());
        }
        if (updateDTO.getCurrency_rate() != null) {
            oldCurrency.setCurrencyRate(updateDTO.getCurrency_rate());
        }
        oldCurrency.setUpdateTime(LocalDateTime.now());
        return oldCurrency;
    }
}
