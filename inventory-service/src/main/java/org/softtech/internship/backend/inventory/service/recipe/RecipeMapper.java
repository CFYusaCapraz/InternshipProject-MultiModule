package org.softtech.internship.backend.inventory.service.recipe;

import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialViewDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeCreateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeUpdateDTO;
import org.softtech.internship.backend.inventory.model.recipe.dto.RecipeViewDTO;
import org.softtech.internship.backend.inventory.service.material.MaterialMapper;
import org.softtech.internship.backend.inventory.service.material.MaterialService;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RecipeMapper {
    public static Recipe createMapper(RecipeCreateDTO createDTO, MaterialRepository materialRepository) {
        List<Material> materialList = new ArrayList<>();
        for (String x : createDTO.getMaterial_id_list()) {
            Optional<Material> material = materialRepository.findById(UUID.fromString(x));
            material.ifPresent(materialList::add);
        }

        return Recipe.builder()
                .recipeName(createDTO.getRecipe_name())
                .materials(materialList)
                .build();
    }

    public static RecipeViewDTO viewMapper(Recipe newRecipe) {
        List<MaterialViewDTO> materials = new ArrayList<>();
        for (Material material : newRecipe.getMaterials()) {
            materials.add(MaterialMapper.viewMapper(material));
        }

        return RecipeViewDTO.builder()
                .recipe_id(newRecipe.getRecipeId())
                .recipe_name(newRecipe.getRecipeName())
                .materials(materials)
                .build();
    }

    public static Recipe updateMapper(Recipe oldRecipe, RecipeUpdateDTO updateDTO, MaterialService materialService) {
        if (updateDTO.getRecipe_name() != null && !updateDTO.getRecipe_name().isEmpty()) {
            oldRecipe.setRecipeName(updateDTO.getRecipe_name());
        }
        if (updateDTO.getMaterial_id_list() != null && !updateDTO.getMaterial_id_list().isEmpty()) {
            oldRecipe.setMaterials(materialService.createMaterialList(updateDTO.getMaterial_id_list()));
        }
        oldRecipe.setUpdateTime(LocalDateTime.now());
        return oldRecipe;
    }
}
