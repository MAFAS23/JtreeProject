/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

/**
 *
 * @author athif
 */
import DAO.DaoProduct;
import Model.ModelProduct;
import Model.ModelTableProduct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class SelectProductDialog extends JDialog {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton selectButton;
    private ModelProduct selectedProduct;
    private JTextField searchField;
    private DaoProduct daoProduct;
    
//    private DefaultTableModel productTableModel;
    public SelectProductDialog(Window parent, List<ModelProduct> products,DaoProduct daoProduct) {
    super(parent, "Pilih Barang", ModalityType.APPLICATION_MODAL);
    this.daoProduct=daoProduct;
    setLayout(new BorderLayout(10, 10));

    // Setup table
    String[] columnNames = {"No", "Kode Barang", "Nama Barang", "Stock", "Harga"};
    // Membuat DefaultTableModel yang selnya tidak dapat di-edit
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Semua sel tidak dapat diedit
            }
        };

    table = new JTable(tableModel);
    
    // Panel untuk pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchProducts());

        searchPanel.add(new JLabel("Cari Barang:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        add(searchPanel,BorderLayout.NORTH);
        
        

    // Isi tabel dengan data produk
    int no = 1;
    for (ModelProduct product : products) {
        Object[] rowData = {no++, product.getProductId(), product.getProductName(), product.getStockQuantity(),product.getPrice()};
        tableModel.addRow(rowData);
    }

    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    // Tombol Pilih
    selectButton = new JButton("Pilih");
    add(selectButton, BorderLayout.SOUTH);

    selectButton.addActionListener(e -> {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            selectedProduct = products.get(selectedRow);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih barang.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    });

    pack();
    setLocationRelativeTo(parent);
}
    
    private void searchProducts() {
    String searchTerm = searchField.getText().trim();
    List<ModelProduct> searchResults;

    try {
        // Jika searchTerm tidak kosong, lakukan pencarian dengan nama produk
        if (!searchTerm.isEmpty()) {
            searchResults = daoProduct.selectProductsByName(searchTerm);
        } else {
            // Jika searchTerm kosong, lakukan pencarian default atau ambil semua produk
            searchResults = daoProduct.selectProductsByName(searchTerm); // Misalnya: mengambil semua produk sebagai default
        }
        
        updateProductTable(searchResults);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error searching products: " + e.getMessage(), 
                                      "Search Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void updateProductTable(List<ModelProduct> products) {
        tableModel.setRowCount(0); // Clear existing rows
        int no=1;
        for (ModelProduct product : products) {
            tableModel.addRow(new Object[]{no++,product.getProductId(), product.getProductName(),product.getStockQuantity(),product.getPrice()});
        }
    }

    public ModelProduct getSelectedProduct() {
        return selectedProduct;
    }
}
