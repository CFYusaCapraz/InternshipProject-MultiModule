package org.softtech.internship.backend.inventory.model.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.softtech.internship.backend.inventory.model.recipe_material.dto.RecipeMaterialGeneralDTO;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RecipeViewDTO {
    private UUID recipe_id;
    private String recipe_name;
    private List<RecipeMaterialGeneralDTO> materials;
    private Double recipe_price;
}
