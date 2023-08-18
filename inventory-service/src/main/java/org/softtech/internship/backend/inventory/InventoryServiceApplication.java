package org.softtech.internship.backend.inventory;

import org.softtech.internship.backend.inventory.model.currency.Currency;
import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.softtech.internship.backend.inventory.repository.CurrencyRepository;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;
import org.softtech.internship.backend.inventory.repository.RecipeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

	@Bean
	public CommandLineRunner runner(MaterialRepository mRepo, CurrencyRepository cRepo, RecipeRepository rRepo){
		return args -> {
            Currency c1 = Currency.builder()
                    .currencyName("USD")
                    .currencyRate(BigDecimal.valueOf(20.47))
                    .build();
            Currency c2 = Currency.builder()
                    .currencyName("EUR")
                    .currencyRate(BigDecimal.valueOf(23.64))
                    .build();
            Currency c3 = Currency.builder()
                    .currencyName("GBP")
                    .currencyRate(BigDecimal.valueOf(26.30))
                    .build();
            cRepo.saveAllAndFlush(List.of(c1,c2,c3));
            Material m1 = Material.builder()
                    .materialName("Gold")
                    .price(BigDecimal.valueOf(15.58))
                    .currency(c1)
                    .build();
            Material m2 = Material.builder()
                    .materialName("Silver")
                    .price(BigDecimal.valueOf(10.89))
                    .currency(c1)
                    .build();
            Material m3 = Material.builder()
                    .materialName("Copper")
                    .price(BigDecimal.valueOf(7.45))
                    .currency(c3)
                    .build();
            mRepo.saveAllAndFlush(List.of(m1,m2,m3));
            Recipe r1 = Recipe.builder()
                    .recipeName("Recipe One")
                    .materials(List.of(m1,m2))
                    .build();
            Recipe r2 = Recipe.builder()
                    .recipeName("Recipe Two")
                    .materials(List.of(m1,m3))
                    .build();
            Recipe r3 = Recipe.builder()
                    .recipeName("Recipe Three")
                    .materials(List.of(m2,m3))
                    .build();
            rRepo.saveAllAndFlush(List.of(r1,r2,r3));
		};
	}
}
