/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author athif
 */

import DAO.DaoGoodsIssuance;
import DAO.DaoProduct;
import Model.ModelGoodsIssuance;
import Model.ModelIssuanceItem;
import Model.ModelProduct;
import Panel.GoodsIssuanceDialog;
import Panel.PanelTableGoodsIssuance;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class ControllerGoodsIssuanceCRUD {
    private final PanelTableGoodsIssuance panelTableGoodsIssuance;
    private final DaoGoodsIssuance daoGoodsIssuance;
    private final DaoProduct daoProduct;
//    private List<ModelIssuanceItem> issuanceData;

    public ControllerGoodsIssuanceCRUD(PanelTableGoodsIssuance panelTableGoodsIssuance, DaoGoodsIssuance daoGoodsIssuance, DaoProduct daoProduct) {
        this.panelTableGoodsIssuance = panelTableGoodsIssuance;
        this.daoGoodsIssuance = daoGoodsIssuance;
        this.daoProduct = daoProduct;

        refreshGoodsIssuanceTable();
    }

    public void showAddDialog() {
    try {
        // Ambil data produk dari database
        List<ModelProduct> products = daoProduct.getAllProductsForGoodsIssuance();

        // Buat nomor pengeluaran baru
        String issuanceNumber = daoGoodsIssuance.generateIssuanceNumber();

        // Buat dialog baru untuk penambahan
        GoodsIssuanceDialog addDialog = new GoodsIssuanceDialog(null, products, issuanceNumber,daoProduct);
        addDialog.setVisible(true);

        if (addDialog.isConfirmed()) {
            ModelGoodsIssuance newIssuance = addDialog.getGoodsIssuance();
            if (newIssuance != null) {
                // Simpan pengeluaran barang
                saveGoodsIssuance(newIssuance);

                // Perbarui tabel pengeluaran barang
                refreshGoodsIssuanceTable();

                JOptionPane.showMessageDialog(null, "Pengeluaran barang berhasil disimpan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error saat menambah pengeluaran barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
    
    public void showEditDialog() {
        int selectedRow = panelTableGoodsIssuance.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Silakan pilih data pengeluaran yang ingin diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Dapatkan data pengeluaran yang dipilih
            int issuanceId = (int) panelTableGoodsIssuance.getValueAt(selectedRow, 1); // Pastikan kolom ke-1 adalah issuance ID
            ModelGoodsIssuance issuanceToEdit = daoGoodsIssuance.getGoodsIssuanceById(issuanceId);

            if (issuanceToEdit != null) {
                // Ambil data produk dari database
                List<ModelProduct> products = daoProduct.getAllProductsForGoodsIssuance();

                // Buat dialog edit dengan data yang sudah diisi
                GoodsIssuanceDialog editDialog = new GoodsIssuanceDialog(null, products, issuanceToEdit.getIssuanceNumber(), daoProduct);
                editDialog.setGoodsIssuance(issuanceToEdit);
                editDialog.setVisible(true);

                if (editDialog.isConfirmed()) {
                    ModelGoodsIssuance updatedIssuance = editDialog.getGoodsIssuance();
                    if (updatedIssuance != null) {
                        // Simpan perubahan pengeluaran barang
                        saveGoodsIssuance(updatedIssuance);

                        // Perbarui tabel pengeluaran barang
                        refreshGoodsIssuanceTable();

                        JOptionPane.showMessageDialog(null, "Pengeluaran barang berhasil diperbarui.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat mengedit data pengeluaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
    public void showDeleteDialog() {
    int selectedRow = panelTableGoodsIssuance.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Silakan pilih data pengeluaran yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data pengeluaran ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            // Dapatkan data pengeluaran yang dipilih
            int issuanceId = (int) panelTableGoodsIssuance.getValueAt(selectedRow, 1); // Pastikan kolom ke-1 adalah issuance ID
            ModelGoodsIssuance issuanceToDelete = daoGoodsIssuance.getGoodsIssuanceById(issuanceId);

            if (issuanceToDelete != null) {
                // Hapus data pengeluaran dari database
                deleteGoodsIssuance(issuanceToDelete);

                // Perbarui tabel pengeluaran barang
                refreshGoodsIssuanceTable();

                JOptionPane.showMessageDialog(null, "Data pengeluaran berhasil dihapus.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat menghapus data pengeluaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
    
    private void deleteGoodsIssuance(ModelGoodsIssuance issuance) throws SQLException {
    // Kembalikan stok untuk setiap item pengeluaran
    for (ModelIssuanceItem item : issuance.getIssuanceItems()) {
        daoProduct.updateProductStock(item.getProductId(), item.getQuantity());
    }

    // Hapus pengeluaran barang dan item pengeluarannya dari database
    daoGoodsIssuance.deleteGoodsIssuance(issuance.getIssuanceId());
}

private void saveGoodsIssuance(ModelGoodsIssuance issuance) throws SQLException {
    // Simpan pengeluaran barang ke database
    int issuanceId = daoGoodsIssuance.insertGoodsIssuance(issuance);

    // Simpan setiap item pengeluaran barang ke database
    for (ModelIssuanceItem item : issuance.getIssuanceItems()) {
        item.setIssuanceId(issuanceId); // Set issuanceId pada setiap item
        daoGoodsIssuance.insertIssuanceItem(item);

        // Kurangi stok produk setelah pengeluaran
        daoProduct.decreaseProductStock(item.getProductId(), item.getQuantity());
    }
}
private void refreshGoodsIssuanceTable() {
    try {
        // Ambil semua data pengeluaran barang
        List<ModelGoodsIssuance> goodsIssuanceList = daoGoodsIssuance.getAllGoodsIssuance();
        
        // Perbarui data pada panel tabel
        panelTableGoodsIssuance.updateTableData(goodsIssuanceList);
        panelTableGoodsIssuance.revalidate();
        panelTableGoodsIssuance.repaint();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error saat memperbarui tabel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}
}



