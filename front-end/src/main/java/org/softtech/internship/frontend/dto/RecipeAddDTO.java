/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.softtech.internship.frontend.RecipeMaterial;

/**
 *
 * @author yusa
 */
@AllArgsConstructor
@Data
@Builder
public class RecipeAddDTO {
    private String recipe_name;
    private List<RecipeMaterial> material_id_list;
}
