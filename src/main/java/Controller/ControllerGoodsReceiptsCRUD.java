
package Controller;

import DAO.DaoGoodsReceipts;
import DAO.DaoReceiptItems;
import DAO.DaoProduct;
import DAO.DaoVendor;
import DAO.ImplementGoodsReceipts;
import DAO.ImplementProduct;
import DAO.ImplementReceiptItems;
import DAO.ImplementVendor;
import Model.ModelGoodsReceipts;
import Model.ModelReceiptItems;
import Model.ModelProduct;
import Panel.GoodsReceiptsDialog;
import Panel.PanelTableGoodsReceipts;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ControllerGoodsReceiptsCRUD {

    private final PanelTableGoodsReceipts panelTableGoodsReceipts;
    private final ImplementGoodsReceipts daoGoodsReceipts;
    private final ImplementReceiptItems daoReceiptItems;
    private final ImplementVendor daoVendor;
    private final ImplementProduct daoProduct;
    
    private static final String PREFIX = "RN";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmm");

    public ControllerGoodsReceiptsCRUD(PanelTableGoodsReceipts panelTableGoodsReceipts, 
                                       ImplementGoodsReceipts daoGoodsReceipts,
                                       ImplementReceiptItems daoReceiptItems,
                                       ImplementVendor daoVendor, 
                                       ImplementProduct daoProduct) {
        this.panelTableGoodsReceipts = panelTableGoodsReceipts;
        this.daoGoodsReceipts = daoGoodsReceipts;
        this.daoReceiptItems = daoReceiptItems;
        this.daoVendor = daoVendor;
        this.daoProduct = daoProduct;

        loadGoodsReceiptsData();
    }
//public class ControllerGoodsReceiptsCRUD {
//
//    private final PanelTableGoodsReceipts panelTableGoodsReceipts;
//    private final DaoGoodsReceipts daoGoodsReceipts;
//    private final DaoReceiptItems daoReceiptItems;
//    private final DaoVendor daoVendor;
//    private final DaoProduct daoProduct;
////    private final Connection conn;
//    
//    private static final String PREFIX = "RN";
//    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmm");
//
//
//    public ControllerGoodsReceiptsCRUD(PanelTableGoodsReceipts panelTableGoodsReceipts, DaoGoodsReceipts daoGoodsReceipts,
//                                       DaoReceiptItems daoReceiptItems, DaoVendor daoVendor, DaoProduct daoProduct) {
//        this.panelTableGoodsReceipts = panelTableGoodsReceipts;
//        this.daoGoodsReceipts = daoGoodsReceipts;
//        this.daoReceiptItems = daoReceiptItems;
//        this.daoVendor = daoVendor;
//        this.daoProduct = daoProduct;
////        this.conn = conn;
//
//        loadGoodsReceiptsData();
//    }
//    
    

    public void showAddDialog() {
        try {
            // Mengambil nama vendor dari database
            List<String> vendorNames = daoVendor.getAllVendorNames();
            
            // Menghasilkan nomor bukti penerimaan barang
            String receiptNumber = daoGoodsReceipts.generateReceiptNumber();
            
            // Mengambil semua produk yang tersedia untuk penerimaan barang
            List<ModelProduct> products = daoProduct.getAllProductsForGoodsReceipt();

            // Membuat dialog penerimaan barang baru
            GoodsReceiptsDialog addDialog = new GoodsReceiptsDialog(
                (Frame) null,  // Pastikan parent-nya di-cast ke Frame atau JDialog
                daoVendor,
                products,
                daoProduct,
                this
                    
            );
            
            // Menampilkan dialog dan menunggu aksi pengguna
            addDialog.setVisible(true);

            // Jika dialog dikonfirmasi
            if (addDialog.isConfirmed()) {
                ModelGoodsReceipts newGoodsReceipt = addDialog.getGoodsReceipt(); // Mengambil objek penerimaan baru
                if (newGoodsReceipt != null) {
                    // Perbarui stok produk berdasarkan barang yang diterima
                    List<ModelReceiptItems> newItems = newGoodsReceipt.getReceiptItems();
                    for (ModelReceiptItems item : newItems) {
                        updateProductStock(item.getProductId(), item.getQuantity());
                    }

                    // Simpan penerimaan barang ke database
                    saveGoodsReceipt(newGoodsReceipt);
                    loadGoodsReceiptsData();
                    JOptionPane.showMessageDialog(null, "Penerimaan barang berhasil disimpan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat menambah penerimaan barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
   

    private void saveGoodsReceipt(ModelGoodsReceipts goodsReceipt) {
        try {
            int receiptId = daoGoodsReceipts.insertGoodsReceipt(goodsReceipt);

            // Simpan item penerimaan barang dengan menggunakan receiptId yang baru saja di-generate
            for (ModelReceiptItems item : goodsReceipt.getReceiptItems()) {
                item.setReceiptId(receiptId);
                daoReceiptItems.insertReceiptItem(item);
            }
            loadGoodsReceiptsData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat menyimpan penerimaan barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public void showEditDialog() {
    int selectedRow = panelTableGoodsReceipts.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Pilih penerimaan barang yang akan diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int receiptId = (int) panelTableGoodsReceipts.getValueAt(selectedRow, 1);

    try {
        // Ambil data penerimaan barang yang ada untuk diedit
        ModelGoodsReceipts existingReceipt = daoGoodsReceipts.getGoodsReceiptByIdForEdit(receiptId);
        if (existingReceipt == null) {
            JOptionPane.showMessageDialog(null, "Data penerimaan barang tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dapatkan daftar vendor dan produk
        List<String> vendorNames = daoVendor.getAllVendorNames();
        List<ModelProduct> products = daoProduct.getAllProductsForGoodsReceipt();
        List<String> productNames = products.stream().map(ModelProduct::getProductName).collect(Collectors.toList());
        List<String> productCodes = products.stream().map(p -> String.valueOf(p.getProductId())).collect(Collectors.toList());

        // Buat dialog untuk mengedit
        GoodsReceiptsDialog dialog = new GoodsReceiptsDialog(null, vendorNames, existingReceipt.getReceiptNumber(),
                productNames, productCodes, products, daoVendor, existingReceipt,this,daoProduct);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            ModelGoodsReceipts updatedReceipt = dialog.getGoodsReceipt();
            if (updatedReceipt != null) {
                List<ModelReceiptItems> existingItems = existingReceipt.getReceiptItems();
                List<ModelReceiptItems> updatedItems = updatedReceipt.getReceiptItems();

                // Hitung perubahan stok
                Map<Integer, Integer> stockChanges = new HashMap<>();
                for (ModelReceiptItems updatedItem : updatedItems) {
                    int productId = updatedItem.getProductId();
                    int updatedQuantity = updatedItem.getQuantity();
                    int oldQuantity = existingItems.stream()
                            .filter(item -> item.getProductId() == productId)
                            .findFirst()
                            .map(ModelReceiptItems::getQuantity)
                            .orElse(0);
                    int change = updatedQuantity - oldQuantity;
                    stockChanges.put(productId, change);
                }

                // Perbarui penerimaan barang dan stok produk
                daoGoodsReceipts.updateGoodsReceiptForEdit(updatedReceipt, updatedItems, stockChanges);
                
                loadGoodsReceiptsData();
                JOptionPane.showMessageDialog(null, "Penerimaan barang berhasil diperbarui.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error saat memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}


    public void showDeleteDialog() {
        int selectedRow = panelTableGoodsReceipts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Pilih penerimaan barang yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int receiptId = (int) panelTableGoodsReceipts.getValueAt(selectedRow, 1); // Asumsikan kolom 1 adalah receipt_id
        String receiptNumber = (String) panelTableGoodsReceipts.getValueAt(selectedRow, 2); // Asumsikan kolom 2 adalah receipt_number

        int confirm = JOptionPane.showConfirmDialog(null, 
            "Apakah Anda yakin ingin menghapus penerimaan barang dengan nomor " + receiptNumber + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Ambil item-item yang terkait dengan penerimaan barang ini
                ModelGoodsReceipts receiptToDelete = daoGoodsReceipts.getGoodsReceiptByIdForEdit(receiptId);
                List<ModelReceiptItems> receiptItems = receiptToDelete.getReceiptItems();

                // Hapus penerimaan barang
                daoGoodsReceipts.deleteGoodsReceipt(receiptId);

                // Kurangi stok produk
                for (ModelReceiptItems item : receiptItems) {
                    updateProductStock(item.getProductId(), -item.getQuantity()); // Kurangi stok
                }

                JOptionPane.showMessageDialog(null, "Penerimaan barang berhasil dihapus.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                loadGoodsReceiptsData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error saat menghapus penerimaan barang: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
   

    // Metode untuk memperbarui stok produk
    private void updateProductStock(int productId, int quantity) {
        try {
            daoProduct.updateProductStock(productId, quantity);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat memperbarui stok produk: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void loadGoodsReceiptsData() {
        try {
            List<ModelGoodsReceipts> goodsReceipts = daoGoodsReceipts.getGoodsReceiptsWithItems();
            panelTableGoodsReceipts.updateTableData(goodsReceipts);
        } catch (SQLException e) {
            showError("Error refreshing goods receipts: " + e.getMessage());
        }
    }
    
    public String generateReceiptNumber() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DATE_FORMATTER);
        String uuidPart = UUID.randomUUID().toString().substring(0, 4); // Menggunakan 8 karakter UUID
        
        return String.format("%s-%s-%s", PREFIX, datePart, uuidPart);
    }

    
    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
