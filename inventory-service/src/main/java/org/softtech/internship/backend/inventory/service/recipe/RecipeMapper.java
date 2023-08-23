package org.softtech.internship.backend.inventory.service.recipe;

import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeCreateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeUpdateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeViewDTO;
import org.softtech.internship.backend.inventory.model.recipe_material.RecipeMaterial;
import org.softtech.internship.backend.inventory.model.recipe_material.dto.RecipeMaterialGeneralDTO;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;
import org.softtech.internship.backend.inventory.repository.RecipeMaterialRepository;
import org.softtech.internship.backend.inventory.service.recipematerial.RecipeMaterialMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecipeMapper {
    public static Recipe createMapper(RecipeCreateDTO createDTO, MaterialRepository materialRepository, RecipeMaterialRepository recipeMaterialRepository) {
        Recipe recipe = new Recipe();
        try {
            List<RecipeMaterial> recipeMaterialList = RecipeMaterialMapper.createMapper(createDTO.getMaterial_id_list(), recipeMaterialRepository, materialRepository);
            recipe.setRecipeName(createDTO.getRecipe_name());
            recipe.setRecipeMaterials(recipeMaterialList);
            return recipe;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Material ID of a material is wrong!");
        }
    }

    public static RecipeViewDTO viewMapper(Recipe newRecipe) {
        List<RecipeMaterialGeneralDTO> generalDTOList = new ArrayList<>();
        for (RecipeMaterial x : newRecipe.getRecipeMaterials()) {
            String id = x.getMaterial().getMaterialId().toString();
            int quantity = x.getQuantity();
            generalDTOList.add(RecipeMaterialGeneralDTO.builder()
                    .material_id(id)
                    .quantity(quantity)
                    .build());
        }
        return RecipeViewDTO.builder()
                .recipe_id(newRecipe.getRecipeId())
                .recipe_name(newRecipe.getRecipeName())
                .materials(generalDTOList)
                .build();
    }

    public static Recipe updateMapper(Recipe oldRecipe, RecipeUpdateDTO updateDTO, MaterialRepository materialRepository, RecipeMaterialRepository recipeMaterialRepository) {
        if (updateDTO.getRecipe_name() != null && !updateDTO.getRecipe_name().isEmpty()) {
            oldRecipe.setRecipeName(updateDTO.getRecipe_name());
        }
        if (updateDTO.getMaterialGeneralDTOList() != null && !updateDTO.getMaterialGeneralDTOList().isEmpty()) {
            try {
                List<RecipeMaterial> recipeMaterialList = RecipeMaterialMapper.createMapper(updateDTO.getMaterialGeneralDTOList(), recipeMaterialRepository, materialRepository);
                oldRecipe.setRecipeMaterials(recipeMaterialList);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Material ID of a material is wrong!");
            }
        }
        oldRecipe.setUpdateTime(LocalDateTime.now());
        return oldRecipe;
    }
}
