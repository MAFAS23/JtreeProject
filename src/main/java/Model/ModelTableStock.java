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

public class ModelTableStock extends AbstractTableModel {

    // Kolom baru "No" ditambahkan dan "Kategori" dipindahkan sebelum "Product Name"
    private final String[] columnNames = {"No", "Product ID", "Category", "Product Name", "Stock Quantity", "Stock Status"};
    private List<Object[]> stockData;

    public ModelTableStock(List<Object[]> stockData) {
        this.stockData = stockData;
    }

    @Override
    public int getRowCount() {
        return stockData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        return switch (columnIndex) {
//            case 0 -> rowIndex + 1;
//            case 1 -> stockData.get(rowIndex)[0];
//            case 2 -> stockData.get(rowIndex)[1];
//            case 3 -> stockData.get(rowIndex)[2];
//            case 4 -> stockData.get(rowIndex)[3];
//            case 5 -> stockData.get(rowIndex)[4];
//            default -> null;
//        };
//    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return rowIndex + 1; // For "No" column
        }
        return stockData.get(rowIndex)[columnIndex - 1];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }  
    
    // Add this method for debugging
    public void printData() {
        for (Object[] row : stockData) {
            System.out.println(java.util.Arrays.toString(row));
        }
    }

    public void setStockData(List<Object[]> stockData) {
        this.stockData = stockData;
        fireTableDataChanged();
    }
}

