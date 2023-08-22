package org.softtech.internship.backend.inventory.model.recipe.dto;

import lombok.Getter;
import org.softtech.internship.backend.inventory.model.recipe_material.dto.RecipeMaterialGeneralDTO;

import java.util.List;

@Getter
public class RecipeCreateDTO {
    private String recipe_name;
    private List<RecipeMaterialGeneralDTO> material_id_list;
}
