package org.softtech.internship.backend.inventory.model.recipe.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RecipeCreateDTO {
    private String recipe_name;
    private List<String> material_id_list;
}
