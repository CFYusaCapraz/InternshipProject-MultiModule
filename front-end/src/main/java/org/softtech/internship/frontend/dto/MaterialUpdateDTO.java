/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author yusa
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MaterialUpdateDTO {

    private String material_name;
    private Double price;
    private String currency_name;
}
