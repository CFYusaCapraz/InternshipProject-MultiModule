package org.softtech.internship.backend.inventory.model.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialViewDTO;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeViewDTO {
    private UUID recipe_id;
    private String recipe_name;
    private List<MaterialViewDTO> materials;
}
