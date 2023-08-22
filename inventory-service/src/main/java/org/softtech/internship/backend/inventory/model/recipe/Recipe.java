package org.softtech.internship.backend.inventory.model.recipe;

import jakarta.persistence.*;
import lombok.*;
import org.softtech.internship.backend.inventory.model.recipe_material.RecipeMaterial;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID recipeId;
    @Column(nullable = false, unique = true)
    private String recipeName;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime creationTime;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateTime;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    @OneToMany(mappedBy = "recipe")
    private List<RecipeMaterial> recipeMaterials;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeId, recipe.recipeId) && Objects.equals(recipeName, recipe.recipeName) && Objects.equals(recipeMaterials, recipe.recipeMaterials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, recipeName, recipeMaterials);
    }

    @Override
    public String toString() {
        return "Recipe{" + "recipeId=" + recipeId + ", recipeName='" + recipeName + '\'' + ", recipeMaterials=" + recipeMaterials + '}';
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime time = now();
        creationTime = time;
        updateTime = time;
        isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = now();
    }
}
