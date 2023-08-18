package org.softtech.internship.backend.inventory.repository;

import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {
    Optional<Recipe> findRecipeByRecipeName(String recipeName);

    List<Recipe> findAllByIsDeletedIsFalse();

    Optional<Recipe> findRecipeByRecipeIdOrRecipeNameAndIsDeletedIsFalse(UUID recipeId, String recipeName);
}
