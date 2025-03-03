/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author athif
 */
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ModelTableVendor extends AbstractTableModel {

    private List<ModelVendor> vendors;
    private final String[] columnNames = {"No", "Vendor ID", "Nama", "Alamat"};

     public ModelTableVendor(List<ModelVendor> vendors) {
        this.vendors = vendors;
    }

    public ModelTableVendor() {
        this.vendors = new ArrayList<>(); // Jika tidak ada data, buat list kosong
    }

    @Override
    public int getRowCount() {
        return vendors.size();
    }
   

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ModelVendor vendor = vendors.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex + 1; // No
            case 1:
                return vendor.getVendorId(); // Vendor ID
            case 2:
                return vendor.getName(); // Nama
            case 3:
                return vendor.getAddress(); // Alamat
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Tentukan apakah cell dapat diedit, misalnya hanya kolom alamat yang dapat diubah
        return columnIndex == 3; // hanya kolom Alamat yang dapat diubah
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ModelVendor vendor = vendors.get(rowIndex);
        if (columnIndex == 3) {
            vendor.setAddress((String) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    public void addVendor(ModelVendor vendor) {
        vendors.add(vendor);
        fireTableRowsInserted(vendors.size() - 1, vendors.size() - 1);
    }

    public void removeVendor(int rowIndex) {
        vendors.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void updateVendor(int rowIndex, ModelVendor vendor) {
        vendors.set(rowIndex, vendor);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public ModelVendor getVendorAt(int rowIndex) {
        return vendors.get(rowIndex);
    }
    
    public void setVendors(List<ModelVendor> vendors) {
    this.vendors = vendors; // `vendors` di sini adalah list yang disimpan di dalam model tabel
    fireTableDataChanged(); // Memberitahu JTable bahwa data telah berubah
}
    // Metode untuk menghapus semua baris
    public void clearRows() {
        vendors.clear(); // Mengosongkan daftar vendor
        fireTableDataChanged(); // Memberitahu JTable bahwa data telah berubah
    }
    
    // Metode untuk menambahkan baris dengan data objek
    public void addRow(Object[] rowData) {
        if (rowData.length == 4) { // Memastikan panjang array sesuai dengan jumlah kolom
            ModelVendor vendor = new ModelVendor();
            vendor.setVendorId((int) rowData[1]);
            vendor.setName((String) rowData[2]);
            vendor.setAddress((String) rowData[3]);

            addVendor(vendor); // Memanfaatkan metode addVendor yang ada
        }
    }
}
