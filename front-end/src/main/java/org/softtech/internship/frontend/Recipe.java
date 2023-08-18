/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softtech.internship.frontend.Material;

/**
 *
 * @author yusa
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {
    private String id;
    private String name;
    private List<Material> materials;
}
