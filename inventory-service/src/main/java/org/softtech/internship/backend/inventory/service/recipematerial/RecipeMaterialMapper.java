package org.softtech.internship.backend.inventory.service.recipematerial;

import org.softtech.internship.backend.inventory.model.material.Material;
import org.softtech.internship.backend.inventory.model.recipe_material.RecipeMaterial;
import org.softtech.internship.backend.inventory.model.recipe_material.dto.RecipeMaterialGeneralDTO;
import org.softtech.internship.backend.inventory.repository.MaterialRepository;
import org.softtech.internship.backend.inventory.repository.RecipeMaterialRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RecipeMaterialMapper {
    public static List<RecipeMaterial> createMapper(List<RecipeMaterialGeneralDTO> generalDTOList, RecipeMaterialRepository recipeMaterialRepository, MaterialRepository materialRepository) throws IllegalArgumentException {
        List<RecipeMaterial> recipeMaterialList = new ArrayList<>();
        for (RecipeMaterialGeneralDTO generalDTO : generalDTOList) {
            UUID uuid = UUID.fromString(generalDTO.getMaterial_id());
            Optional<Material> material = materialRepository.findMaterialByMaterialIdOrMaterialNameAndIsDeletedIsFalse(uuid, null);
            RecipeMaterial recipeMaterial = new RecipeMaterial();
            material.ifPresent(material1 -> {
                recipeMaterial.setMaterial(material1);
                recipeMaterial.setQuantity(generalDTO.getQuantity());
            });
            RecipeMaterial flush = recipeMaterialRepository.saveAndFlush(recipeMaterial);
            recipeMaterialList.add(flush);
        }
        return recipeMaterialList;
    }
}
