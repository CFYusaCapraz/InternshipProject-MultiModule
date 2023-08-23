package org.softtech.internship.backend.inventory.model.recipe_material.dto;

import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecipeMaterialGeneralDTO {
    private String material_id;
    private Double quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeMaterialGeneralDTO that = (RecipeMaterialGeneralDTO) o;
        return Objects.equals(material_id, that.material_id) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material_id, quantity);
    }
}
