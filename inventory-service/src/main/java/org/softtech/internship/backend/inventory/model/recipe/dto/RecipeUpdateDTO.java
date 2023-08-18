package org.softtech.internship.backend.inventory.model.recipe.dto;

import lombok.Getter;
import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RecipeUpdateDTO {
    private String recipe_name;
    private List<String> material_id_list;

    public boolean isEmpty() {
        return (recipe_name == null || recipe_name.isEmpty()) && material_id_list.isEmpty();
    }

    public boolean isSame(Recipe oldRecipe) {
        List<String> materialList = new ArrayList<>();
        for (Material material : oldRecipe.getMaterials()) {
            materialList.add(material.getMaterialId().toString());
        }
        if (recipe_name != null && material_id_list != null) {
            return oldRecipe.getRecipeName().equals(recipe_name) &&
                    materialList.equals(material_id_list);
        } else if (recipe_name != null) {
            return oldRecipe.getRecipeName().equals(recipe_name);
        } else return materialList.equals(material_id_list);
    }
}
