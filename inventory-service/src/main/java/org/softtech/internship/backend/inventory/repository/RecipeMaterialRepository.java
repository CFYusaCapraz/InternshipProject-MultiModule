package org.softtech.internship.backend.inventory.repository;

import org.softtech.internship.backend.inventory.model.recipe_material.RecipeMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeMaterialRepository extends JpaRepository<RecipeMaterial, UUID> {
}
