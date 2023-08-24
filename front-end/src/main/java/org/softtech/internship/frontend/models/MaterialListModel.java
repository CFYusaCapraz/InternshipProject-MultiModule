/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.models;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.DefaultListModel;

import org.softtech.internship.frontend.Material;
import org.softtech.internship.frontend.RecipeMaterial;

/**
 * @author yusa
 */
public class MaterialListModel extends DefaultListModel<String> {

    private final List<Material> materials;
    private List<RecipeMaterial> recipeMaterialList;

    public MaterialListModel(List<RecipeMaterial> recipeMaterialList, List<Material> materials) {
        this.recipeMaterialList = recipeMaterialList;
        this.materials = materials;
    }

    @Override
    public int getSize() {
        return recipeMaterialList.size();
    }

    @Override
    public String getElementAt(int index) {
        RecipeMaterial recipeMaterial = recipeMaterialList.get(index);
        if (recipeMaterial != null) {
            if (recipeMaterial.getQuantity() == 0) {
                AtomicReference<String> result = new AtomicReference<>("");
                materials.stream()
                        .filter(material -> material.getId().equals(recipeMaterial.getMaterial_id()))
                        .findFirst().ifPresent(material -> {
                            result.set(material.getName());
                        });
                return result.get();
            } else {
                AtomicReference<String> result = new AtomicReference<>("");
                materials.stream()
                        .filter(material -> material.getId().equals(recipeMaterial.getMaterial_id()))
                        .findFirst().ifPresent(material -> {
                            result.set(String.format("%s %.2f kg", material.getName(), recipeMaterial.getQuantity()));
                        });
                return result.get();
            }
        }
        return null;
    }

    public List<RecipeMaterial> getMaterialList() {
        return recipeMaterialList;
    }

    public void setMaterialList(List<RecipeMaterial> newMaterialList) {
        this.recipeMaterialList = newMaterialList;
        fireContentsChanged(this, 0, getSize() - 1);
    }

    @Override
    public boolean removeElement(Object obj) {
        if (obj instanceof RecipeMaterial m) {
            AtomicBoolean removed = new AtomicBoolean(false);
            recipeMaterialList.stream()
                    .filter(recipeMaterial -> recipeMaterial.getMaterial_id().equals(m.getMaterial_id()))
                    .findFirst().ifPresent(recipeMaterial -> {
                        removed.set(recipeMaterialList.remove(recipeMaterial));
                    });
            if (!removed.get()) {
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
