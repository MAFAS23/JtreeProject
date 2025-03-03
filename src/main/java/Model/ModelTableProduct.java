/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author athif
 */

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ModelTableProduct extends AbstractTableModel {

    private final String[] columnNames = {"No","ProductId", "ProductName", "Price"};
    private List<ModelProduct> products;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    public ModelTableProduct(List<ModelProduct> products) {
        this.products = products;
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ModelProduct product = products.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> product.getProductId();
            case 2 -> product.getProductName();
            case 3 -> currencyFormat.format(product.getPrice());
            default -> null;
        }; // Kolom "No." yang menunjukkan nomor urut baris
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;  // Non-editable table by default
    }

    public void setProducts(List<ModelProduct> products) {
        this.products = products;
        fireTableDataChanged();  // Notify JTable that the data has changed
    }

    public ModelProduct getProductAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= products.size()) {
            return null; // Indeks tidak valid
        }
        return products.get(rowIndex);
    
    }

    public void addProduct(ModelProduct product) {
        products.add(product);
        fireTableRowsInserted(products.size() - 1, products.size() - 1);
    }

    public void removeProduct(int rowIndex) {
        products.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void updateProduct(int rowIndex, ModelProduct product) {
        products.set(rowIndex, product);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
