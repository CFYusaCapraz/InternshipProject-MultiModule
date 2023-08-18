/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.models;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import org.softtech.internship.frontend.Material;
import org.softtech.internship.frontend.Recipe;

/**
 *
 * @author yusa
 */
public class RecipeTableModel extends AbstractTableModel {

    private List<Recipe> recipeList;
    private String[] columns = {"Recipe Name", "Material List", "Recipe Price"};

    public RecipeTableModel(List<Recipe> recipeList) {
        this.recipeList = recipeList;
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
                        .map(Material::getName)
                        .collect(Collectors.joining(", "));
            }
            case 2 -> {
                Double price = 0.0d;
                for (Material m : recipe.getMaterials()) {
                    price += m.getPrice();
                }
                return price;
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
}
