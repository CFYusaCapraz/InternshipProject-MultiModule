/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.models;

import java.util.List;
import javax.swing.DefaultListModel;
import org.softtech.internship.frontend.Material;

/**
 *
 * @author yusa
 */
public class MaterialListModel extends DefaultListModel<String> {

    private List<Material> materialList;

    public MaterialListModel(List<Material> materialList) {
        this.materialList = materialList;
    }

    @Override
    public int getSize() {
        return materialList.size();
    }

    @Override
    public String getElementAt(int index) {
        Material material = materialList.get(index);
        if (material != null) {
            return material.getName();
        }
        return null;
    }

    public void setMaterialList(List<Material> newMaterialList) {
        this.materialList = newMaterialList;
        fireContentsChanged(this, 0, getSize() - 1);
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    @Override
    public boolean removeElement(Object obj) {
        if (obj instanceof Material m) {
            if (!materialList.remove(m)) {
                return false;
            } else {
                fireContentsChanged(this, 0, getSize() - 1);
                return true;
            }
        } else {
            return false;
        }
    }

}
