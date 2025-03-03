/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author athif
 */

import java.math.BigDecimal;
import java.text.NumberFormat;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Locale;

public class ModelTableGoodsIssuance extends AbstractTableModel {

    private final String[] columnNames = {"No", "Issuance ID", "Issuance Number", "Issuance Date", "Product Name", "Quantity", "Total Price", "Note"};
    private List<ModelGoodsIssuance> goodsIssuanceData;  // List yang berisi data ModelGoodsIssuance
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public ModelTableGoodsIssuance(List<ModelGoodsIssuance> goodsIssuanceData) {
        this.goodsIssuanceData = goodsIssuanceData;
    }

    @Override
    public int getRowCount() {
        int rowCount = 0;
        for (ModelGoodsIssuance issuance : goodsIssuanceData) {
            rowCount += issuance.getIssuanceItems().size();  // Setiap item dalam issuance dihitung
        }
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ModelIssuanceItem item = getIssuanceItemForRow(rowIndex);
        ModelGoodsIssuance issuance = getGoodsIssuanceForRow(rowIndex);

        return switch (columnIndex) {
            case 0 -> rowIndex + 1;  // No.
            case 1 -> issuance != null ? issuance.getIssuanceId() : "Unknown";  // Issuance ID
            case 2 -> issuance != null ? issuance.getIssuanceNumber() : "Unknown";  // Issuance Number
            case 3 -> issuance != null ? issuance.getIssuanceDate() : "Unknown";  // Issuance Date
            case 4 -> item != null && item.getProductName()!= null ? item.getProductName().getProductName() : "Unknown";  // Product Name
            case 5 -> item != null ? item.getQuantity() : 0;  // Quantity
            case 6 -> currencyFormat.format(item != null && item.getTotalPrice() != null ? item.getTotalPrice() : BigDecimal.ZERO);
            case 7 -> issuance != null ? issuance.getNote() : "";  // Note
            default -> null;
        };
    }

    private ModelIssuanceItem getIssuanceItemForRow(int rowIndex) {
        int currentRow = 0;
        for (ModelGoodsIssuance issuance : goodsIssuanceData) {
            List<ModelIssuanceItem> items = issuance.getIssuanceItems();
            if (rowIndex < currentRow + items.size()) {
                return items.get(rowIndex - currentRow);
            }
            currentRow += items.size();
        }
        return null;
    }

    private ModelGoodsIssuance getGoodsIssuanceForRow(int rowIndex) {
        int currentRow = 0;
        for (ModelGoodsIssuance issuance : goodsIssuanceData) {
            List<ModelIssuanceItem> items = issuance.getIssuanceItems();
            if (rowIndex < currentRow + items.size()) {
                return issuance;
            }
            currentRow += items.size();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setIssuanceData(List<ModelGoodsIssuance> goodsIssuanceData) {
        this.goodsIssuanceData = goodsIssuanceData;
        fireTableDataChanged();
    }
}
