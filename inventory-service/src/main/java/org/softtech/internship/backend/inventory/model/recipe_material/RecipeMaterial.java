package org.softtech.internship.backend.inventory.model.recipe_material;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.model.recipe.Recipe;

import java.util.UUID;

@Entity
@Table(name = "recipe_material")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;
    @Column(nullable = false)
    private double quantity;

}
