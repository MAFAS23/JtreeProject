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
import Model.ModelGoodsIssuance;
import Model.ModelIssuanceItem;
import Model.ModelProduct;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class GoodsIssuanceDialog extends JDialog {

    private JPanel mainPanel;
    private JTextField recipientField;
    private JDateChooser dateChooser;
    JButton addButton,removeButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea noteArea;
    private boolean isConfirmed = false;
    private List<ModelProduct> products;
    private String issuanceNumber;
    JTextField issuanceNumberField;
    private JLabel totalAmountLabel;
    private DaoProduct daoProduct;

    public GoodsIssuanceDialog(Frame parent, List<ModelProduct> products, String issuanceNumber) {
        super(parent, "Pengeluaran Barang", true);
        this.products = products;
        this.issuanceNumber = issuanceNumber;
        initComponents(issuanceNumber);
        setLocationRelativeTo(parent);
    }
    public GoodsIssuanceDialog(Frame parent, List<ModelProduct> products, String issuanceNumber,DaoProduct daoProduct) {
        super(parent, "Pengeluaran Barang", true);
        this.products = products;
        this.issuanceNumber = issuanceNumber;
        this.daoProduct=daoProduct;
        initComponents(issuanceNumber);
        
        setLocationRelativeTo(parent);
    }

    private void initComponents(String issuanceNumber) {
        setLayout(new BorderLayout(10, 10));

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        // Tambahkan Field Issuance Number
        JLabel lblIssuanceNumber = new JLabel("Issuance Number:");
        issuanceNumberField = new JTextField(issuanceNumber, 15);
        issuanceNumberField.setEditable(false); // Non-editable field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        infoPanel.add(lblIssuanceNumber, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        infoPanel.add(issuanceNumberField, gbc);

        JLabel lblTanggal = new JLabel("Tanggal:");
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd-MM-yyyy");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        infoPanel.add(lblTanggal, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        infoPanel.add(dateChooser, gbc);

        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel tablePanel = new JPanel(new BorderLayout());

        // Tombol "+" untuk menambah barang
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("+");
        buttonPanel.add(addButton);
        tablePanel.add(buttonPanel, BorderLayout.NORTH);
        removeButton = new JButton("-"); // Tambahkan tombol "-"
        buttonPanel.add(removeButton);

        String[] columnNames = {"No", "Kode Barang", "Nama Barang", "Qty Stock", "Jumlah Pembelian", "Harga Jual", "Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hanya kolom "Jumlah Pembelian" yang bisa diedit
                return column == 4;
            }
        };
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Table model listener untuk mengecek jumlah pembelian
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // Jika kolom "Jumlah Pembelian" berubah
            if (column == 4) {
                try {
                    int quantity = Integer.parseInt(tableModel.getValueAt(row, column).toString());
                    int stock = Integer.parseInt(tableModel.getValueAt(row, 3).toString()); // Stok
                    if (quantity > stock) {
                        JOptionPane.showMessageDialog(this, "Jumlah pembelian tidak boleh melebihi stok yang tersedia.", "Error", JOptionPane.ERROR_MESSAGE);
                        tableModel.setValueAt(stock, row, 4); // Set kembali ke nilai maksimal stok
                    } else {
                        BigDecimal salePrice = new BigDecimal(tableModel.getValueAt(row, 5).toString()); // Harga Jual
                        BigDecimal totalPrice = salePrice.multiply(new BigDecimal(quantity));
                        tableModel.setValueAt(totalPrice, row, 6); // Set nilai Total Harga
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Jumlah pembelian harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel);
        
        // Tambahkan panel untuk total amount
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalAmountLabel = new JLabel("Total Amount: 0");
        totalPanel.add(totalAmountLabel);
        mainPanel.add(totalPanel);

        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.add(new JLabel("Catatan:"), BorderLayout.NORTH);
        noteArea = new JTextArea(2, 18);
        notePanel.add(new JScrollPane(noteArea), BorderLayout.CENTER);

        JPanel saveCancelPanel = new JPanel();
        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");
        saveCancelPanel.add(saveButton);
        saveCancelPanel.add(cancelButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(notePanel, BorderLayout.CENTER);
        southPanel.add(saveCancelPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        
        tableModel.addTableModelListener(e -> {
            updateTotalAmount();
        });
            
   


        
        addButton.addActionListener(e -> {
            // Tampilkan dialog untuk memilih produk
            SelectProductDialog selectDialog = new SelectProductDialog(this, products,daoProduct);
            selectDialog.setVisible(true);

            // Ambil produk yang dipilih oleh pengguna
            ModelProduct selectedProduct = selectDialog.getSelectedProduct();

            if (selectedProduct != null) {
                
                // Cek apakah produk sudah ada di tabel
                if (isProductAlreadyInTable(selectedProduct.getProductId())) {
                    JOptionPane.showMessageDialog(this, "Produk ini sudah ditambahkan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Tambahkan produk yang dipilih ke tabel termasuk harga
                tableModel.addRow(new Object[]{
                    tableModel.getRowCount() + 1, // No
                    selectedProduct.getProductId(), // Kode Barang
                    selectedProduct.getProductName(), // Nama Barang
                    selectedProduct.getStockQuantity(), // Qty Stock
                    "", // Jumlah Pembelian kosong (untuk diisi user)
                    new BigDecimal(selectedProduct.getPrice().toString()), // Harga Jual
                    BigDecimal.ZERO // Total Harga kosong
                });
            }
        });
        
        // Tambahkan action listener untuk tombol "-"
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow); // Hapus baris yang dipilih
            } else {
                JOptionPane.showMessageDialog(this, "Silakan pilih barang yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        saveButton.addActionListener(e -> {
            if (validateInputs()) {
                isConfirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            isConfirmed = false;
            dispose();
        });
        
        setPreferredSize(new Dimension(600, 600));
        pack();
        setLocationRelativeTo(null);
    }
    
    private boolean validateInputs() {
    List<String> errors = new ArrayList<>();

    if (dateChooser.getDate() == null) {
        errors.add("Tanggal harus diisi.");
    }

    if (tableModel.getRowCount() == 0) {
        errors.add("Minimal satu barang harus ditambahkan.");
    } else {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String quantity = tableModel.getValueAt(i, 4).toString().trim();
            if (quantity.isEmpty() || !quantity.matches("\\d+") || Integer.parseInt(quantity) <= 0) {
                errors.add("Jumlah pembelian harus diisi dengan angka positif untuk semua barang.");
                break;
            }
        }
    }

    if (noteArea.getText().trim().isEmpty()) {
        errors.add("Catatan harus diisi.");
    }

    if (!errors.isEmpty()) {
        showValidationErrors(errors);
        return false;
    }

    return true;
}

private void showValidationErrors(List<String> errors) {
    StringBuilder errorMessage = new StringBuilder("Mohon diisi berikut ini:\n");
    for (String error : errors) {
        errorMessage.append("- ").append(error).append("\n");
    }
    JOptionPane.showMessageDialog(this, errorMessage.toString(), "Informasi", JOptionPane.INFORMATION_MESSAGE);
}
    
    // Metode untuk mengecek apakah produk sudah ada di tabel
    private boolean isProductAlreadyInTable(int productId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (Integer.parseInt(tableModel.getValueAt(i, 1).toString()) == productId) {
                return true;
            }
        }
        return false;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
    
    

    public ModelGoodsIssuance getGoodsIssuance() {
    if (!isConfirmed) return null;

    LocalDate issuanceDate = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    String issuanceNumber = issuanceNumberField.getText(); // Use the issuanceNumber field here
    String note = noteArea.getText();

    List<ModelIssuanceItem> issuanceItems = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.ZERO; // Initialize total price

    for (int i = 0; i < tableModel.getRowCount(); i++) {
        int productId = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
        int quantity = Integer.parseInt(tableModel.getValueAt(i, 4).toString());
        BigDecimal salePrice = new BigDecimal(tableModel.getValueAt(i, 5).toString());
        String itemNote = ""; // Placeholder for item note

        // Calculate total price for each item and add to totalPrice
        BigDecimal itemTotalPrice = salePrice.multiply(new BigDecimal(quantity));
        totalPrice = totalPrice.add(itemTotalPrice);

        issuanceItems.add(new ModelIssuanceItem(0, productId, quantity, salePrice, itemNote));
    }

    // Use the updated constructor to include all fields
    return new ModelGoodsIssuance(0, issuanceNumber, issuanceDate, note, totalPrice, issuanceItems);
}
    // Method untuk memperbarui total amount
    private void updateTotalAmount() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            BigDecimal totalPrice = new BigDecimal(tableModel.getValueAt(i, 6).toString());
            totalAmount = totalAmount.add(totalPrice);
        }
        totalAmountLabel.setText("Total Amount: " + totalAmount);
    }
    
    // Method untuk mengisi data pengeluaran yang akan diedit
    public void setGoodsIssuance(ModelGoodsIssuance issuance) {
        issuanceNumberField.setText(issuance.getIssuanceNumber());
        dateChooser.setDate(Date.from(issuance.getIssuanceDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        noteArea.setText(issuance.getNote());

        tableModel.setRowCount(0); // Hapus semua baris tabel saat ini

        for (ModelIssuanceItem item : issuance.getIssuanceItems()) {
            ModelProduct product = products.stream().filter(p -> p.getProductId() == item.getProductId()).findFirst().orElse(null);
            if (product != null) {
                tableModel.addRow(new Object[]{
                    tableModel.getRowCount() + 1,
                    product.getProductId(),
                    product.getProductName(),
                    product.getStockQuantity(),
                    item.getQuantity(),
                    item.getSalePrice(),
                    item.getSalePrice().multiply(new BigDecimal(item.getQuantity()))
                });
            }
        }
        updateTotalAmount(); // Perbarui label total amount
    }
    
}

