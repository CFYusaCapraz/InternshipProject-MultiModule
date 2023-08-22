package org.softtech.internship.backend.inventory.service.recipe;

import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeCreateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeUpdateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeViewDTO;
import org.softtech.internship.backend.inventory.model.recipe_material.RecipeMaterial;
import org.softtech.internship.backend.inventory.model.recipe_material.dto.RecipeMaterialGeneralDTO;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;
import org.softtech.internship.backend.inventory.repository.RecipeMaterialRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RecipeMapper {
    public static Recipe createMapper(RecipeCreateDTO createDTO, MaterialRepository materialRepository, RecipeMaterialRepository recipeMaterialRepository) {
        Recipe recipe = new Recipe();
        List<RecipeMaterial> recipeMaterialList = new ArrayList<>();
        for (RecipeMaterialGeneralDTO x : createDTO.getMaterial_id_list()) {
            try {
                RecipeMaterialListCreator(recipe, materialRepository, recipeMaterialList, x, recipeMaterialRepository);
            } catch (IllegalArgumentException e) {
                recipe.setRecipeName("Wrong material id");
                return recipe;
            }
        }
        recipe.setRecipeName(createDTO.getRecipe_name());
        recipe.setRecipeMaterials(recipeMaterialList);
        return recipe;
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
            List<RecipeMaterial> recipeMaterialList = new ArrayList<>();
            for (RecipeMaterialGeneralDTO x : updateDTO.getMaterialGeneralDTOList()) {
                try {
                    RecipeMaterialListCreator(oldRecipe, materialRepository, recipeMaterialList, x, recipeMaterialRepository);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Material ID of a material is wrong!");
                }
            }
            oldRecipe.setRecipeMaterials(recipeMaterialList);
        }
        oldRecipe.setUpdateTime(LocalDateTime.now());
        return oldRecipe;
    }

    private static void RecipeMaterialListCreator(Recipe oldRecipe, MaterialRepository materialRepository, List<RecipeMaterial> recipeMaterialList, RecipeMaterialGeneralDTO x, RecipeMaterialRepository recipeMaterialRepository) {
        UUID materialId = UUID.fromString(x.getMaterial_id());
        Optional<Material> materialOptional = materialRepository.findById(materialId);
        materialOptional.ifPresent(material -> {
            RecipeMaterial recipeMaterial = new RecipeMaterial();
            recipeMaterial.setMaterial(material);
            recipeMaterial.setQuantity(x.getQuantity());
            recipeMaterialList.add(recipeMaterial);
            recipeMaterialRepository.save(recipeMaterial);
        });
    }
}
