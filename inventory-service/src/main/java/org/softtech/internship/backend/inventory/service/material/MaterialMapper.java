package org.softtech.internship.backend.inventory.service.material;

import org.softtech.internship.backend.inventory.model.material.dto.MaterialCreateDTO;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialUpdateDTO;
import org.softtech.internship.backend.inventory.model.material.dto.MaterialViewDTO;
import org.softtech.internship.backend.inventory.model.currency.Currency;
import org.softtech.internship.backend.inventory.model.material.Material;

import java.time.LocalDateTime;

public class MaterialMapper {
    public static Material createMapper(MaterialCreateDTO createDTO, Currency currency) {
        return Material.builder()
                .materialName(createDTO.getMaterial_name())
                .unitPrice(createDTO.getUnit_price())
                .currency(currency)
                .build();
    }

    public static MaterialViewDTO viewMapper(Material material) {
        return MaterialViewDTO.builder()
                .material_id(material.getMaterialId())
                .material_name(material.getMaterialName())
                .unit_price(material.getUnitPrice())
                .currency_name(material.getCurrency().getCurrencyName())
                .build();
    }

    public static Material updateMapper(Material oldMaterial, MaterialUpdateDTO updateDTO, Currency currency) {
        if (updateDTO.getMaterial_name() != null && !updateDTO.getMaterial_name().isEmpty()) {
            oldMaterial.setMaterialName(updateDTO.getMaterial_name());
        }
        if (updateDTO.getUnit_price() != null) {
            oldMaterial.setUnitPrice(updateDTO.getUnit_price());
        }
        if (updateDTO.getCurrency_name() != null && !updateDTO.getCurrency_name().isEmpty()) {
            oldMaterial.setCurrency(currency);
        }
        oldMaterial.setUpdateTime(LocalDateTime.now());
        return oldMaterial;
    }
}
