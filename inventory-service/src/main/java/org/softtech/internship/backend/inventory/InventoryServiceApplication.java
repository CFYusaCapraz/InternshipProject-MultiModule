package org.softtech.internship.backend.inventory;

import org.softtech.internship.backend.inventory.model.currency.Currency;
import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.softtech.internship.backend.inventory.model.recipe_material.RecipeMaterial;
import org.softtech.internship.backend.inventory.repository.CurrencyRepository;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;
import org.softtech.internship.backend.inventory.repository.RecipeMaterialRepository;
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
}
