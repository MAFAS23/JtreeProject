///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package Model;
//
//import javax.swing.table.AbstractTableModel;
//import java.util.List;
//
//public class ModelTableReceiptItems extends AbstractTableModel {
//
//    // Menghapus "Product ID" dari array nama kolom
//    private final String[] columnNames = {"No", "Item ID", "Product Name", "Quantity", "Note"};
//    private List<ModelReceiptItems> receiptItemsList;
//
//    public ModelTableReceiptItems(List<ModelReceiptItems> receiptItemsList) {
//        this.receiptItemsList = receiptItemsList;
//    }
//
//    @Override
//    public int getRowCount() {
//        return receiptItemsList.size();
//    }
//
//    @Override
//    public int getColumnCount() {
//        return columnNames.length;
//    }
//
//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        ModelReceiptItems item = receiptItemsList.get(rowIndex);
//        return switch (columnIndex) {
//            case 0 -> rowIndex + 1; // Mengisi kolom "No" dengan nomor urut
//            case 1 -> item.getItemId(); // Mengisi kolom "Item ID"
//            case 2 -> getProductNameById(item.getProductId()); // Mengisi kolom "Product Name"
//            case 3 -> item.getQuantity(); // Mengisi kolom "Quantity"
//            case 4 -> item.getNote(); // Mengisi kolom "Note"
//            default -> null;
//        };
//    }
//
//    @Override
//    public String getColumnName(int column) {
//        return columnNames[column];
//    }
//
//    public void setReceiptItemsList(List<ModelReceiptItems> receiptItemsList) {
//        this.receiptItemsList = receiptItemsList;
//        fireTableDataChanged();
//    }
//
//    public ModelReceiptItems getReceiptItemAt(int rowIndex) {
//        return receiptItemsList.get(rowIndex);
//    }
//
//    // Placeholder untuk mendapatkan nama produk berdasarkan ID
//    private String getProductNameById(int productId) {
//        // Implementasikan logika untuk mendapatkan nama produk berdasarkan productId
//        return "Product " + productId; // Placeholder
//    }
//}
//
