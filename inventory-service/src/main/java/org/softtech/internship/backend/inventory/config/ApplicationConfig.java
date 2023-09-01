package org.softtech.internship.backend.inventory.config;

import org.softtech.internship.backend.inventory.model.currency.Currency;
import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.repository.CurrencyRepository;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Bean
    public CommandLineRunner commandLineRunner(MaterialRepository materialRepository, CurrencyRepository currencyRepository) {
        return args -> {
            Currency eur = Currency.builder()
                    .currencyName("EUR")
                    .currencyRate(BigDecimal.valueOf(25.87d))
                    .build();
            Currency usd = Currency.builder()
                    .currencyName("USD")
                    .currencyRate(BigDecimal.valueOf(24.63d))
                    .build();
            Currency gbp = Currency.builder()
                    .currencyName("GBP")
                    .currencyRate(BigDecimal.valueOf(27.49d))
                    .build();
            currencyRepository.saveAllAndFlush(List.of(eur, usd, gbp));

            Material gold = Material.builder()
                    .materialName("Gold")
                    .unitPrice(BigDecimal.valueOf(5.23d))
                    .currency(eur)
                    .build();
            Material silver = Material.builder()
                    .materialName("Silver")
                    .unitPrice(BigDecimal.valueOf(3.68d))
                    .currency(usd)
                    .build();
            Material bronze = Material.builder()
                    .materialName("Bronze")
                    .unitPrice(BigDecimal.valueOf(1.975d))
                    .currency(gbp)
                    .build();
            materialRepository.saveAllAndFlush(List.of(gold, silver, bronze));
        };
    }
}
