package org.softtech.internship.backend.inventory.model.material.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialViewDTO {
    private UUID material_id;
    private String material_name;
    private BigDecimal price;
    private String currency_name;
}
