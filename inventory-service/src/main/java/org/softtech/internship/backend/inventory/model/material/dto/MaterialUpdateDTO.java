package org.softtech.internship.backend.inventory.model.material.dto;

import lombok.Getter;
import org.softtech.internship.backend.inventory.model.material.Material;

import java.math.BigDecimal;

@Getter
public class MaterialUpdateDTO {
    private String material_name;
    private BigDecimal price;
    private String currency_name;

    public boolean isEmpty() {
        return (material_name == null || material_name.isEmpty()) && (currency_name == null || currency_name.isEmpty()) && price == null;
    }

    public boolean isSame(Material oldMaterial) {
        if (price != null && material_name != null && currency_name != null) {
            return oldMaterial.getMaterialName().equals(material_name) &&
                    oldMaterial.getPrice().equals(price) &&
                    oldMaterial.getCurrency().getCurrencyName().equals(currency_name);
        } else if (price != null && currency_name != null) {
            return oldMaterial.getPrice().equals(price) &&
                    oldMaterial.getCurrency().getCurrencyName().equals(currency_name);
        } else if (material_name != null && currency_name != null) {
            return oldMaterial.getMaterialName().equals(material_name) &&
                    oldMaterial.getCurrency().getCurrencyName().equals(currency_name);
        } else if (price != null) {
            return oldMaterial.getPrice().equals(price);
        } else if (material_name != null) {
            return oldMaterial.getMaterialName().equals(material_name);
        } else if (currency_name != null) {
            return oldMaterial.getCurrency().getCurrencyName().equals(currency_name);
        } else {
            return true;
        }
    }
}
