/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.softtech.internship.frontend.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.softtech.internship.frontend.Currency;

/**
 *
 * @author yusa
 */
public class CurrencyTableModel extends AbstractTableModel {

    private List<Currency> currencyList;
    private String[] columns = {"Currency Name", "Rate of Conversion"};

    public CurrencyTableModel(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }

    @Override
    public int getRowCount() {
        return currencyList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Currency currency = currencyList.get(rowIndex);
        if (columnIndex == 0) {
            return currency.getName();
        } else if (columnIndex == 1) {
            return currency.getRate();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
    public void setCurrencyList(List<Currency> newCurrencyList) {
        this.currencyList = newCurrencyList;
        fireTableDataChanged();
    }

}
