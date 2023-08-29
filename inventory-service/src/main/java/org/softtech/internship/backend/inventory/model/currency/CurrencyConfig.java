package org.softtech.internship.backend.inventory.model.currency;

import org.softtech.internship.backend.inventory.service.currency.CurrencyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CurrencyConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner runner(CurrencyService currencyService) {
        return args -> {
            currencyService.updateLiveCurrencyData();
        };
    }
}
