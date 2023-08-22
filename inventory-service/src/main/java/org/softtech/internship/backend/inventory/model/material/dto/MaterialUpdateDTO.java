package org.softtech.internship.backend.inventory.model.material.dto;

import lombok.Getter;
import org.softtech.internship.backend.inventory.model.material.Material;

import java.math.BigDecimal;

@Getter
public class MaterialUpdateDTO {
    private String material_name;
    private BigDecimal unit_price;
    private String currency_name;

    public boolean isEmpty() {
        return (material_name == null || material_name.isEmpty()) && (currency_name == null || currency_name.isEmpty()) && unit_price == null;
    }

    public boolean isSame(Material oldMaterial) {
        if (unit_price != null && material_name != null && currency_name != null) {
            return oldMaterial.getMaterialName().equals(material_name) &&
                    oldMaterial.getUnitPrice().equals(unit_price) &&
                    oldMaterial.getCurrency().getCurrencyName().equals(currency_name);
        } else if (unit_price != null && currency_name != null) {
            return oldMaterial.getUnitPrice().equals(unit_price) &&
                    oldMaterial.getCurrency().getCurrencyName().equals(currency_name);
        } else if (material_name != null && currency_name != null) {
            return oldMaterial.getMaterialName().equals(material_name) &&
                    oldMaterial.getCurrency().getCurrencyName().equals(currency_name);
        } else if (unit_price != null) {
            return oldMaterial.getUnitPrice().equals(unit_price);
        } else if (material_name != null) {
            return oldMaterial.getMaterialName().equals(material_name);
        } else if (currency_name != null) {
            return oldMaterial.getCurrency().getCurrencyName().equals(currency_name);
        } else {
            return true;
        }
    }
}
