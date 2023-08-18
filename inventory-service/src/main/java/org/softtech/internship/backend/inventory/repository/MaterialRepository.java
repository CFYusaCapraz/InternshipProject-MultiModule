package org.softtech.internship.backend.inventory.repository;

import org.softtech.internship.backend.inventory.model.material.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {
    Optional<Material> findMaterialByMaterialName(String materialName);

    List<Material> findAllByIsDeletedIsFalse();

    Optional<Material> findMaterialByMaterialIdOrMaterialNameAndIsDeletedIsFalse(UUID materialId, String materialName);
}
