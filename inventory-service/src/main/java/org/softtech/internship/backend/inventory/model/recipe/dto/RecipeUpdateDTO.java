package org.softtech.internship.backend.inventory.model.recipe.dto;

import lombok.Getter;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;
import org.softtech.internship.backend.inventory.model.recipe_material.RecipeMaterial;
import org.softtech.internship.backend.inventory.model.recipe_material.dto.RecipeMaterialGeneralDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RecipeUpdateDTO {
    private String recipe_name;
    private List<RecipeMaterialGeneralDTO> materialGeneralDTOList;

    public boolean isEmpty() {
        return (recipe_name == null || recipe_name.isEmpty()) && materialGeneralDTOList.isEmpty();
    }

    public boolean isSame(Recipe oldRecipe) {
        List<RecipeMaterialGeneralDTO> materialList = new ArrayList<>();
        for (RecipeMaterial recipeMaterial : oldRecipe.getRecipeMaterials()) {
            RecipeMaterialGeneralDTO dto = RecipeMaterialGeneralDTO.builder()
                    .material_id(recipeMaterial.getMaterial().getMaterialId().toString())
                    .quantity(recipeMaterial.getQuantity())
                    .build();
            materialList.add(dto);
        }
        if (recipe_name != null && materialGeneralDTOList != null) {
            return oldRecipe.getRecipeName().equals(recipe_name) &&
                    materialList.equals(materialGeneralDTOList);
        } else if (recipe_name != null) {
            return oldRecipe.getRecipeName().equals(recipe_name);
        } else return materialList.equals(materialGeneralDTOList);
    }
}
