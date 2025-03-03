/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel.Dialog;

/**
 *
 * @author athif
 */


import DAO.DaoVendor;
import DAO.ImplementVendor;
import Model.ModelTableVendor;
import Model.ModelVendor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class AddVendorDialog extends JDialog {
    private JTable vendorTable;
    private ModelTableVendor vendorTableModel;
    private ImplementVendor daoVendor;
    private ModelVendor selectedVendor;
    private JTextField searchField;

    public AddVendorDialog(Frame owner, ImplementVendor daoVendor) {
        super(owner, "Pilih Vendor", true);
        this.daoVendor = daoVendor;
        this.selectedVendor = null;

        setLayout(new BorderLayout());

        // Inisialisasi model tabel vendor dengan data dari database
        try {
            List<ModelVendor> vendors = daoVendor.getAllVendors();
            vendorTableModel = new ModelTableVendor(vendors);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error retrieving vendors: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            vendorTableModel = new ModelTableVendor(); // Buat model tabel kosong jika terjadi error
        }

        // Tabel untuk menampilkan data vendor
        vendorTable = new JTable(vendorTableModel);
        JScrollPane vendorScrollPane = new JScrollPane(vendorTable);
        
        // Panel untuk pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchVendor());

        searchPanel.add(new JLabel("Cari Barang:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Tombol untuk memilih vendor yang dipilih
        JButton selectVendorButton = new JButton("Pilih Vendor");
        selectVendorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = vendorTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedVendor = vendorTableModel.getVendorAt(selectedRow); // Ambil vendor yang dipilih dari model tabel
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AddVendorDialog.this, "Silakan pilih vendor untuk melanjutkan.", "No Vendor Selected", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add(searchPanel,BorderLayout.NORTH);
        add(vendorScrollPane, BorderLayout.CENTER);
        add(selectVendorButton, BorderLayout.SOUTH);

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
    
    private void searchVendor() {
    String searchTerm = searchField.getText().trim();
    List<ModelVendor> searchResults;

    try {
        // Jika searchTerm tidak kosong, lakukan pencarian dengan nama produk
        if (!searchTerm.isEmpty()) {
            searchResults = daoVendor.searchVendorByName(searchTerm);
        } else {
            searchResults = daoVendor.searchVendorByName(searchTerm); 
        }
        
        updateVendorTable(searchResults);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error searching products: " + e.getMessage(), 
                                      "Search Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void updateVendorTable(List<ModelVendor> vendors) {
        vendorTableModel.clearRows();// Clear existing rows
        for (ModelVendor vendor : vendors) {
            vendorTableModel.addRow(new Object[]{null,vendor.getVendorId(), vendor.getName(),vendor.getAddress()});
        }
    }

    public ModelVendor getSelectedVendor() {
        return selectedVendor;
    }
}
