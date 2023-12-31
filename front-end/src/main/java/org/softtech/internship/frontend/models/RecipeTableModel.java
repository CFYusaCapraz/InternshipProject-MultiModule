/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.models;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

import org.softtech.internship.frontend.Currency;
import org.softtech.internship.frontend.Material;
import org.softtech.internship.frontend.Recipe;

/**
 * @author yusa
 */
public class RecipeTableModel extends AbstractTableModel {

    private final String[] columns = {"Recipe Name", "Material List", "Recipe Price"};
    private List<Recipe> recipeList;
    private List<Material> materialList;
    private List<Currency> currencyList;

    public RecipeTableModel(List<Recipe> recipeList, List<Material> materialList, List<Currency> currencyList) {
        this.recipeList = recipeList;
        this.materialList = materialList;
        this.currencyList = currencyList;
    }

    @Override
    public int getRowCount() {
        return recipeList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Recipe recipe = recipeList.get(rowIndex);
        switch (columnIndex) {
            case 0 -> {
                return recipe.getName();
            }
            case 1 -> {
                return recipe.getMaterials()
                        .stream()
                        .map((recipeMaterial) -> {
                            String materialId = recipeMaterial.getMaterial_id();
                            AtomicReference<Material> m = new AtomicReference<>(new Material());
                            materialList.stream()
                                    .filter((material) -> material.getId().equals(materialId))
                                    .findFirst()
                                    .ifPresent(m::set);
                            String name = m.get().getName();
                            Double quantity = recipeMaterial.getQuantity();
                            return String.format("%s (%.2f kg)", name, quantity);
                        })
                        .collect(Collectors.joining(", "));
            }
            case 2 -> {
                Double recipePrice = recipe.getPrice();
                DecimalFormat df = new DecimalFormat("#,##0.00");

                // If the number is greater than or equal to 1000, use the thousands separator
                if (Math.abs(recipePrice) >= 1000) {
                    return df.format(recipePrice) + " TL";
                } else {
                    // If the number is less than 1000, format it without the thousands separator
                    df.applyPattern("0.00");
                    return df.format(recipePrice) + " TL";
                }
            }

            default -> {
                return null;
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    public void setRecipeList(List<Recipe> newRecipeList) {
        this.recipeList = newRecipeList;
        fireTableDataChanged();
    }

    public void setMaterialList(List<Material> materialList) {
        this.materialList = materialList;
        fireTableDataChanged();
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
        fireTableDataChanged();
    }
}
