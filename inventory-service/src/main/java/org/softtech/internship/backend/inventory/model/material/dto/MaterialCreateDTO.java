package org.softtech.internship.backend.inventory.model.material.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MaterialCreateDTO {
    private String material_name;
    private BigDecimal price;
    private String currency_name;
}
