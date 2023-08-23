package org.softtech.internship.backend.inventory.service.recipe;

import lombok.RequiredArgsConstructor;
import org.softtech.internship.backend.inventory.model.APIResponse;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeCreateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeUpdateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeViewDTO;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;
import org.softtech.internship.backend.inventory.repository.RecipeMaterialRepository;
import org.softtech.internship.backend.inventory.repository.RecipeRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final MaterialRepository materialRepository;
    private final RecipeMaterialRepository recipeMaterialRepository;

    public ResponseEntity<? extends APIResponse<?>> getAllRecipes() {
        try {
            List<RecipeViewDTO> recipeList = new ArrayList<>();
            for (Recipe recipe : recipeRepository.findAllByIsDeletedIsFalse()) {
                recipeList.add(RecipeMapper.viewMapper(recipe));
            }
            if (!recipeList.isEmpty()) {
                APIResponse<List<RecipeViewDTO>> body = APIResponse.successWithData(recipeList, "All recipe information.");
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error("There are no recipes in the database.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting all the recipes!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> getRecipeById(String id) {
        try {
            Optional<Recipe> recipe = recipeRepository.findRecipeByRecipeIdOrRecipeNameAndIsDeletedIsFalse(UUID.fromString(id), null);
            if (recipe.isPresent()) {
                RecipeViewDTO viewDTO = RecipeMapper.viewMapper(recipe.get());
                APIResponse<RecipeViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Recipe information of ID: %s", id));
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error(String.format("Recipe information of ID:`%s` not found!", id));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting the recipe!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> getRecipeByName(String name) {
        try {
            Optional<Recipe> recipe = recipeRepository.findRecipeByRecipeIdOrRecipeNameAndIsDeletedIsFalse(null, name);
            if (recipe.isPresent()) {
                RecipeViewDTO viewDTO = RecipeMapper.viewMapper(recipe.get());
                APIResponse<RecipeViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Recipe information of Name: %s", name));
                return ResponseEntity.ok(body);
            } else {
                APIResponse<?> body = APIResponse.error(String.format("Recipe information of Name:`%s` not found!", name));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while getting the recipe!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> createRecipe(RecipeCreateDTO createDTO) {
        if ((createDTO.getRecipe_name() == null || createDTO.getRecipe_name().isEmpty()) && createDTO.getMaterial_id_list().isEmpty()) {
            APIResponse<?> body = APIResponse.error("`recipe_name` and `material_id_list` cannot be empty!");
            return ResponseEntity.unprocessableEntity().body(body);
        } else {
            Recipe newRecipe = RecipeMapper.createMapper(createDTO, materialRepository, recipeMaterialRepository);
            if (newRecipe.getRecipeName().equals("Wrong material id")) {
                APIResponse<?> body = APIResponse.error("Material ID of a material is wrong!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
            }
            try {
                Recipe flush = recipeRepository.saveAndFlush(newRecipe);
                flush.getRecipeMaterials().forEach(recipeMaterial -> recipeMaterial.setRecipe(flush));
                recipeRepository.saveAndFlush(flush);
                RecipeViewDTO viewDTO = RecipeMapper.viewMapper(flush);
                APIResponse<RecipeViewDTO> body = APIResponse.successWithData(viewDTO, "Recipe has successfully created.");
                return ResponseEntity.status(HttpStatus.CREATED).body(body);
            } catch (DataIntegrityViolationException e) {
                APIResponse<?> body = APIResponse.error("Recipe already exists!");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
            } catch (Exception e) {
                APIResponse<?> body = APIResponse.error("Error occurred while creating the recipe!");
                return ResponseEntity.internalServerError().body(body);
            }
        }
    }

    public ResponseEntity<? extends APIResponse<?>> deleteRecipe(String field) {
        try {
            if (field == null || field.isEmpty()) {
                APIResponse<?> body = APIResponse.error("`id` or `name` cannot be empty!");
                return ResponseEntity.unprocessableEntity().body(body);
            } else {
                try {
                    UUID id = UUID.fromString(field);
                    Optional<Recipe> recipe = recipeRepository.findById(id);
                    if (recipe.isPresent()) {
                        if (!recipe.get().getIsDeleted()) {
                            Recipe newRecipe = recipe.get();
                            newRecipe.setIsDeleted(true);
                            recipeRepository.saveAndFlush(newRecipe);
                            APIResponse<?> body = APIResponse.success(String.format("Recipe of ID: `%s` is successfully deleted.", field));
                            return ResponseEntity.ok(body);
                        } else {
                            APIResponse<?> body = APIResponse.success(String.format("Recipe of ID: `%s` is already deleted!", field));
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                    } else {
                        APIResponse<?> body = APIResponse.success(String.format("Recipe of ID: `%s` not found!", field));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                } catch (IllegalArgumentException e) {
                    Optional<Recipe> recipe = recipeRepository.findRecipeByRecipeName(field);
                    if (recipe.isPresent()) {
                        if (!recipe.get().getIsDeleted()) {
                            Recipe newRecipe = recipe.get();
                            newRecipe.setIsDeleted(true);
                            recipeRepository.saveAndFlush(newRecipe);
                            APIResponse<?> body = APIResponse.success(String.format("Recipe of Name: `%s` is successfully deleted.", field));
                            return ResponseEntity.ok(body);
                        } else {
                            APIResponse<?> body = APIResponse.success(String.format("Recipe of Name: `%s` is already deleted!", field));
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                    } else {
                        APIResponse<?> body = APIResponse.success(String.format("Recipe of Name: `%s` not found!", field));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                }
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while deleting the recipe!");
            return ResponseEntity.internalServerError().body(body);
        }
    }

    public ResponseEntity<? extends APIResponse<?>> updateRecipe(String id, RecipeUpdateDTO updateDTO) {
        try {
            if (id == null || id.isEmpty()) {
                APIResponse<?> body = APIResponse.error("Provide an ID to update the recipe!");
                return ResponseEntity.unprocessableEntity().body(body);
            } else {
                if (updateDTO.isEmpty()) {
                    APIResponse<?> body = APIResponse.error("`recipe_name` or `material_id_list` cannot be empty!");
                    return ResponseEntity.unprocessableEntity().body(body);
                } else {
                    Optional<Recipe> recipe = recipeRepository.findRecipeByRecipeIdOrRecipeNameAndIsDeletedIsFalse(UUID.fromString(id), null);
                    if (recipe.isPresent()) {
                        if (updateDTO.isSame(recipe.get())) {
                            APIResponse<?> body = APIResponse.error("No update has been done!");
                            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
                        }
                        Recipe newRecipe = RecipeMapper.updateMapper(recipe.get(), updateDTO, materialRepository, recipeMaterialRepository);
                        Recipe flush = recipeRepository.saveAndFlush(newRecipe);
                        flush.getRecipeMaterials().forEach(recipeMaterial -> recipeMaterial.setRecipe(flush));
                        recipeRepository.saveAndFlush(flush);
                        RecipeViewDTO viewDTO = RecipeMapper.viewMapper(flush);
                        APIResponse<RecipeViewDTO> body = APIResponse.successWithData(viewDTO, String.format("Recipe information of ID: `%s` is updated.", id));
                        return ResponseEntity.ok(body);
                    } else {
                        APIResponse<?> body = APIResponse.error(String.format("Recipe information of ID: `%s` not found.", id));
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                    }
                }
            }
        } catch (Exception e) {
            APIResponse<?> body = APIResponse.error("Error occurred while updating the material!");
            return ResponseEntity.internalServerError().body(body);
        }
    }
}
