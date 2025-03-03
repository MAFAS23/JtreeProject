package Model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ModelTableGoodsReceipts extends AbstractTableModel {

    // Menghapus "Vendor ID" dari array nama kolom dan menambahkan Quantity
    private final String[] columnNames = {"No", "Receipt ID", "Receipt Number", "Vendor Name", "Product Name", "Qty", "Receipt Date", "Note"};
    private List<ModelGoodsReceipts> goodsReceiptsList;

    public ModelTableGoodsReceipts(List<ModelGoodsReceipts> goodsReceiptsList) {
        this.goodsReceiptsList = goodsReceiptsList;
    }

    @Override
    public int getRowCount() {
        return goodsReceiptsList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ModelGoodsReceipts receipt = goodsReceiptsList.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> rowIndex + 1; // Mengisi kolom "No" dengan nomor urut
            case 1 -> receipt.getReceiptId(); // Mengisi kolom "Receipt ID"
            case 2 -> receipt.getReceiptNumber(); // Mengisi kolom "Receipt Number"
            case 3 -> receipt.getVendorName(); // Mengisi kolom "Vendor Name"
            case 4 -> receipt.getProductName(); // Mengisi kolom "Product Name"
            case 5 -> receipt.getQuantity(); // Mengisi kolom "Quantity"
            case 6 -> receipt.getReceiptDate(); // Mengisi kolom "Receipt Date"
            case 7 -> receipt.getNote(); // Mengisi kolom "Note"
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setGoodsReceiptsList(List<ModelGoodsReceipts> goodsReceiptsList) {
        this.goodsReceiptsList = goodsReceiptsList;
        fireTableDataChanged();
    }

    public ModelGoodsReceipts getGoodsReceiptAt(int rowIndex) {
        return goodsReceiptsList.get(rowIndex);
    }



    // Placeholder untuk mendapatkan nama vendor berdasarkan ID
    private String getVendorNameById(int vendorId) {
        // Implementasikan logika untuk mendapatkan nama vendor berdasarkan vendorId
        return "Vendor " + vendorId; // Placeholder
    }
}
