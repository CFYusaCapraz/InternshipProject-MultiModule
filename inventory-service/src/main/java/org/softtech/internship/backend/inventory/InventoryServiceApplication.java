package org.softtech.internship.backend.inventory;

import org.softtech.internship.backend.inventory.model.currency.Currency;
import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.softtech.internship.backend.inventory.model.recipe_material.RecipeMaterial;
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
                    .unitPrice(BigDecimal.valueOf(15.58))
                    .currency(c1)
                    .build();
            Material m2 = Material.builder()
                    .materialName("Silver")
                    .unitPrice(BigDecimal.valueOf(10.89))
                    .currency(c1)
                    .build();
            Material m3 = Material.builder()
                    .materialName("Copper")
                    .unitPrice(BigDecimal.valueOf(7.45))
                    .currency(c3)
                    .build();
            mRepo.saveAllAndFlush(List.of(m1,m2,m3));
		};
	}
}
