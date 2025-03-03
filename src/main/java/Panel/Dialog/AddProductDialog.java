package Panel.Dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import DAO.DaoProduct;
import Model.ModelProduct;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddProductDialog extends JDialog {
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private DaoProduct daoProduct;
    private ModelProduct selectedProduct;
     private JTextField searchField; // 
     
     
     
    public AddProductDialog(Frame owner, List<ModelProduct> products, DaoProduct daoProduct) {
        super(owner, "Add Product", true);
        this.daoProduct=daoProduct;
        this.selectedProduct = null;
        setLayout(new BorderLayout());

        // Model tabel untuk menampilkan data produk
        productTableModel = new DefaultTableModel(new Object[]{"Kode Barang", "Nama Barang"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                // Semua kolom tidak bisa diedit (false)
                return false;
            }
        };
        
        
        productTable = new JTable(productTableModel);
        JScrollPane productScrollPane = new JScrollPane(productTable);

        // Mengisi tabel dengan data produk yang diterima dari parameter
        for (ModelProduct product : products) {
            productTableModel.addRow(new Object[]{product.getProductId(), product.getProductName()});
        }
        
        // Panel untuk pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchProducts());

        searchPanel.add(new JLabel("Cari Barang:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Tombol untuk menambahkan produk yang dipilih ke dialog utama
        JButton addProductButton = new JButton("Add Selected Product");
        addProductButton.addActionListener((ActionEvent e) -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                int kodeBarang = Integer.parseInt(productTableModel.getValueAt(selectedRow, 0).toString());
                String namaBarang = productTableModel.getValueAt(selectedRow, 1).toString();
                selectedProduct = new ModelProduct(kodeBarang, 0, namaBarang, null); // Sesuaikan dengan konstruktor yang sesuai
                dispose();
            } else {
                JOptionPane.showMessageDialog(AddProductDialog.this, "Please select a product to add.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        add(searchPanel, BorderLayout.NORTH);
        add(productScrollPane, BorderLayout.CENTER);
        add(addProductButton, BorderLayout.SOUTH);

        setSize(500, 400);
        setLocationRelativeTo(owner);
        // Pastikan dialog muncul di depan
        SwingUtilities.invokeLater(() -> {
            toFront();
            setAlwaysOnTop(true);
            requestFocus();
            setVisible(true);
        });
    
      
    }
    
    private void searchProducts() {
    String searchTerm = searchField.getText().trim();
    List<ModelProduct> searchResults;

    try {
        // Jika searchTerm tidak kosong, lakukan pencarian dengan nama produk
        if (!searchTerm.isEmpty()) {
            searchResults = daoProduct.searchProductsByName(searchTerm);
        } else {
            // Jika searchTerm kosong, lakukan pencarian default atau ambil semua produk
            searchResults = daoProduct.searchProductsByName(searchTerm); // Misalnya: mengambil semua produk sebagai default
        }
        
        updateProductTable(searchResults);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error searching products: " + e.getMessage(), 
                                      "Search Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void updateProductTable(List<ModelProduct> products) {
        productTableModel.setRowCount(0); // Clear existing rows
        for (ModelProduct product : products) {
            productTableModel.addRow(new Object[]{product.getProductId(), product.getProductName()});
        }
    }
    
  

    public ModelProduct getSelectedProduct() {
        return selectedProduct;
    }
}
