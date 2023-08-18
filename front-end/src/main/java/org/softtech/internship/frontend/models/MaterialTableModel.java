/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.softtech.internship.frontend.Material;

/**
 *
 * @author yusa
 */
public class MaterialTableModel extends AbstractTableModel {

    private List<Material> materialList;
    private String[] columns = {"Material Name", "Price"};

    public MaterialTableModel(List<Material> materialList) {
        this.materialList = materialList;
    }

    @Override
    public int getRowCount() {
        return materialList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Material material = materialList.get(rowIndex);
        if (columnIndex == 0) {
            return material.getName();
        } else if (columnIndex == 1) {
            return String.format("%.3f %s", material.getPrice() ,material.getCurrency_name());
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
    public void setMaterialList(List<Material> newMaterialList) {
        this.materialList = newMaterialList;
        fireTableDataChanged();
    }

}
