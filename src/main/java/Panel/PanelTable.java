/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

import DAO.DaoProduct;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import Model.ModelTableProduct;
import Model.ModelProduct;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PanelTable extends JPanel {
    
    private JTable table;
    private DefaultTableModel model;
    private ModelTableProduct modelTableProduct;
    private JScrollPane scrollPane;
    private JPanel backgroundPanel, searchPanel;
    private JTextField searchField;
    private JButton searchButton;
    private DaoProduct daoProduct; 

    public PanelTable(DaoProduct daoProduct) {
    this.daoProduct = daoProduct;
    initComponents();
}

    private void initComponents() {
        // Set ukuran panel
        setSize(600, 400);
        
        // Inisialisasi model tabel tanpa data
        modelTableProduct = new ModelTableProduct(List.of()); // Mulai dengan daftar produk kosong
        table = new JTable(modelTableProduct);
        configureTableAppearance();
        
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Membuat panel pencarian
        searchPanel = createSearchPanel();
        
        // Panel latar belakang
        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(new Color(245, 245, 245));
        backgroundPanel.add(searchPanel, BorderLayout.NORTH);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
        
        setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(245, 245, 245));
        
        searchField = new JTextField(20);
        searchButton = createSearchButton();

        panel.add(new JLabel("Cari: "));
        panel.add(searchField);
        panel.add(searchButton);
        
        return panel;
    }

    private JButton createSearchButton() {
        JButton button = new JButton("Apply Filter") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 96, 57));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setBackground(new Color(0, 96, 57));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.PLAIN, 12));

        // Mengatur ukuran tombol
        Dimension buttonSize = new Dimension(120, 20);
        button.setPreferredSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setMaximumSize(buttonSize);

        // Menambahkan aksi pada tombol Cari
        button.addActionListener(e -> searchProduct());

        return button;
    }

    private void configureTableAppearance() {
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setFont(new Font("Serif", Font.PLAIN, 14));
        table.setGridColor(Color.GRAY);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 96, 57));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Serif", Font.BOLD, 14));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        table.setBorder(BorderFactory.createLineBorder(new Color(0, 96, 57)));
    }

   private void searchProduct() {
    String kueri = searchField.getText().trim();
    if (kueri.isEmpty()) {
        return;
    }

    try {
        List<ModelProduct> hasilPencarian = daoProduct.searchProduct(kueri);
        
        if (hasilPencarian.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Produk yang dicari tidak ditemukan.", "Hasil Pencarian", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Update tabel dengan hasil pencarian
            updateTableData(hasilPencarian);
            
            // Pilih baris pertama jika ada hasil
            if (table.getRowCount() > 0) {
                table.setRowSelectionInterval(0, 0);
                table.scrollRectToVisible(table.getCellRect(0, 0, true));
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mencari produk: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
   public void updateTableData(List<ModelProduct> products) {
        modelTableProduct.setProducts(products);
        table.repaint();
    }

    // Method untuk mendapatkan produk yang dipilih di tabel
   
   public ModelProduct getSelectedProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1 || selectedRow >= modelTableProduct.getRowCount()) {
            return null; // Tidak ada baris yang dipilih atau indeks tidak valid
        }
        return modelTableProduct.getProductAt(selectedRow);
    }

    public JPanel getBackgroundPanel() {
        return backgroundPanel;
    }
}
