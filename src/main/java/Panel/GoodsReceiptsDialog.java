
package Panel;

import DAO.DaoVendor;
import Model.ModelGoodsReceipts;
import Model.ModelProduct;
import Model.ModelReceiptItems;
import Model.ModelVendor;
import Panel.Dialog.AddProductDialog;
import Panel.Dialog.AddVendorDialog;

import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import Controller.ControllerGoodsReceiptsCRUD;
import DAO.DaoProduct;
import DAO.ImplementProduct;
import DAO.ImplementVendor;

public class GoodsReceiptsDialog extends JDialog {
    private JTextField vendorTextField;
    private JButton vendorSelectButton;
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JButton addButton;
    private JButton removeButton;
    private JDateChooser dateChooser;
    private JTextArea noteArea;
    private boolean isConfirmed = false;
//    private DaoVendor daoVendor;
    private List<ModelProduct> products;
    private ModelGoodsReceipts existingReceipt;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private ControllerGoodsReceiptsCRUD controllerGoodsReceiptsCRUD;
     private JTextField receiptNumberField;
    private DaoProduct daoProduct;
    private ImplementVendor daoVendorr;
    private ImplementProduct daoProductt;

    public GoodsReceiptsDialog(Frame parent,  ImplementVendor daoVendorr, List<ModelProduct> products,DaoProduct daoProduct) {
        super(parent, "Bukti Penerimaan Barang", true);
        this.daoVendorr = daoVendorr;
        this.products = products;
        this.daoProduct=daoProduct;
        initComponents();
        setupListeners();
        setLocationRelativeTo(parent);
    }
    public GoodsReceiptsDialog(Frame parent, ImplementVendor daoVendorr, List<ModelProduct> products,ImplementProduct daoProductt, ControllerGoodsReceiptsCRUD controller) {
        super(parent, "Bukti Penerimaan Barang", true);
        this.daoVendorr = daoVendorr;
        this.products = products;
        this.daoProductt=daoProductt;
        this.controllerGoodsReceiptsCRUD = controller;
        initComponents();
        setupListeners();
        setLocationRelativeTo(parent);
        
    }
    
     public GoodsReceiptsDialog(Frame parent, List<String> vendorNames, String receiptNumber, 
                               List<String> productNames, List<String> productCodes, 
                               List<ModelProduct> products, ImplementVendor daoVendorr, 
                               ModelGoodsReceipts existingReceipt, ControllerGoodsReceiptsCRUD controller,ImplementProduct daoProductt) {
        super(parent, "Edit Bukti Penerimaan Barang", true);
        this.daoVendorr = daoVendorr;
        this.products = products;
        this.existingReceipt = existingReceipt;
        this.controllerGoodsReceiptsCRUD = controller; // Pastikan inisialisasi disini
        this.daoProductt=daoProductt;
        
        // Inisialisasi komponen GUI
        initComponents();
        setupListeners();
        
        // Mengisi data dialog dengan data yang ada
        populateExistingData(vendorNames, receiptNumber, productNames, productCodes);
        
        // Mengatur posisi dialog
        setLocationRelativeTo(parent);
    }
    
   private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Panel utama untuk konten
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Panel atas untuk judul dan info
        JPanel topPanel = new JPanel(new BorderLayout());

        // Panel untuk judul form
        titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titleLabel = new JLabel("FORM PENERIMAAN BARANG", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // Panel untuk informasi form (di bawah judul, agak ke kiri)
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inisialisasi komponen
        dateChooser = new JDateChooser();
        vendorTextField = new JTextField(25);
        vendorTextField.setEditable(false);
        vendorSelectButton = new JButton("...");
        receiptNumberField = new JTextField(20);
        receiptNumberField.setEditable(false);
        
        // Generate dan set nomor bukti
        String receiptNumber = controllerGoodsReceiptsCRUD.generateReceiptNumber();
        receiptNumberField.setText(receiptNumber);

        // Menambahkan komponen ke infoPanel
        addLabelAndComponent(infoPanel, "Tanggal:", dateChooser, gbc);
        
        JPanel vendorPanel = new JPanel(new BorderLayout(5, 0));
        vendorPanel.add(vendorTextField, BorderLayout.CENTER);
        vendorPanel.add(vendorSelectButton, BorderLayout.EAST);
        addLabelAndComponent(infoPanel, "Terima Dari:", vendorPanel, gbc);
        
        addLabelAndComponent(infoPanel, "Nomor Bukti:", receiptNumberField, gbc);

        // Menambahkan titlePanel dan infoPanel ke topPanel
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.WEST);

        // Panel untuk tabel produk
        JPanel tablePanel = new JPanel(new BorderLayout());
        productTableModel = new DefaultTableModel(new String[]{"No.", "Kode Barang", "Nama Barang", "Qty"},0){
        @Override
            public boolean isCellEditable(int row, int column) {
                // hanya kolom ke-3 (kolom "Qty") yang bisa diedit
                return column == 3;
            }
        };
        productTable = new JTable(productTableModel);
        
        
        // Tombol + dan - untuk menambah dan menghapus produk
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("+");
        removeButton = new JButton("-");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        
        tablePanel.add(buttonPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Panel untuk catatan
        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.add(new JLabel("Catatan:"), BorderLayout.NORTH);
        noteArea = new JTextArea(3, 20);
        notePanel.add(new JScrollPane(noteArea), BorderLayout.CENTER);

        // Menambahkan tabel dan catatan ke mainPanel
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(notePanel, BorderLayout.SOUTH);

        // Tombol simpan dan batal
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");
        actionPanel.add(saveButton);
        actionPanel.add(cancelButton);

        // Menambahkan panel ke dialog
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        pack();
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(component, gbc);
    }


    private void setupListeners() {
        // Listener untuk tombol vendor
        vendorSelectButton.addActionListener((ActionEvent e) -> {
            showSelectVendorDialog();
        });

        // Listener untuk tombol tambah produk
        addButton.addActionListener((ActionEvent e) -> {
            showAddProductDialog();
        });

        // Listener untuk tombol hapus produk
        removeButton.addActionListener((ActionEvent e) -> {
            removeSelectedRow();
        });

        // Listener untuk tombol simpan
        saveButton.addActionListener(e -> {
            if (validateInputs()) {
                ModelGoodsReceipts receipt = getGoodsReceipt();
                if (receipt != null) {
                    isConfirmed = true;
                    dispose();
                }
            }
        });

        // Listener untuk tombol batal
        cancelButton.addActionListener(e -> {
            isConfirmed = false;
            dispose();
        });
    }

    private void showSelectVendorDialog() {
        AddVendorDialog vendorDialog = new AddVendorDialog((Frame) getParent(), daoVendorr);
        vendorDialog.setVisible(true);

        ModelVendor selectedVendor = vendorDialog.getSelectedVendor();
        if (selectedVendor != null) {
            vendorTextField.setText(selectedVendor.getName());
        }
    }

    private void showAddProductDialog() {
    AddProductDialog addProductDialog = new AddProductDialog((Frame) getParent(), products,daoProduct);
    addProductDialog.setVisible(true);

    ModelProduct selectedProduct = addProductDialog.getSelectedProduct();
    if (selectedProduct != null) {
        if (!isProductInTable(selectedProduct)) {
            int rowCount = productTableModel.getRowCount() + 1;
            productTableModel.addRow(new Object[]{rowCount, selectedProduct.getProductId(), selectedProduct.getProductName(), ""}); // Tambahkan kolom quantity yang kosong
        } else {
            JOptionPane.showMessageDialog(this, "Produk sudah ada di dalam daftar.", "Duplicate Product", JOptionPane.WARNING_MESSAGE);
        }
    }
}

private boolean isProductInTable(ModelProduct product) {
    for (int i = 0; i < productTableModel.getRowCount(); i++) {
        int productId = (int) productTableModel.getValueAt(i, 1); // Asumsi kolom 1 adalah productId
        if (productId == product.getProductId()) {
            return true;
        }
    }
    return false;
}

    private void removeSelectedRow() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            productTableModel.removeRow(selectedRow);
            updateRowNumbers();
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih baris untuk dihapus.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateRowNumbers() {
        for (int i = 0; i < productTableModel.getRowCount(); i++) {
            productTableModel.setValueAt(i + 1, i, 0);
        }
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    private boolean validateInputs() {
        // Validasi input yang diperlukan
        if (vendorTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vendor harus dipilih.", "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Tanggal harus diisi.", "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            return false;
        }
         for (int i = 0; i < productTableModel.getRowCount(); i++) {
        Object qtyValue = productTableModel.getValueAt(i, 3);
        if (qtyValue == null || qtyValue.toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Qty pada baris " + (i+1) + " harus diisi.", "Validasi Gagal", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
        return true;
    }
     public ModelGoodsReceipts getGoodsReceipt() {
        // Validasi input terlebih dahulu
        if (!validateInputs()) {
            return null; // Jika validasi gagal, kembalikan null
        }
        

        // Mendapatkan nomor bukti penerimaan barang
        String receiptNumber = getReceiptNumber();
        
        
        // Mendapatkan ID vendor yang dipilih
        int vendorId = getVendorId();
        
        // Mendapatkan nama vendor
        String vendorName = getVendorName();
        
        // Mendapatkan tanggal penerimaan
        LocalDate receiptDate = getReceiptDate();
        
        // Mendapatkan catatan tambahan
        String note = getNote();
        
        // Mendapatkan item-item yang diterima dari tabel
        List<ModelReceiptItems> items = getReceiptItems();
        
        // Jika tidak ada item valid, tampilkan pesan error dan kembalikan null
        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada item valid untuk disimpan.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Membuat objek penerimaan barang baru
        int receiptId = existingReceipt != null ? existingReceipt.getReceiptId() : 0; // Gunakan ID yang sudah ada jika sedang mengedit
//        return new ModelGoodsReceipts(receiptId, receiptNumber, vendorId, vendorName, receiptDate, note, items);
        return new ModelGoodsReceipts.Builder()
        .receiptId(receiptId)
        .receiptNumber(receiptNumber)
        .vendorId(vendorId)
        .vendorName(vendorName)
        .receiptDate(receiptDate)
        .note(note)
        .receiptItems(items)  // Menambahkan item penerimaan
        .build();
                
    }

     private String getReceiptNumber() {
        return receiptNumberField.getText();
    }
    


    private int getVendorId() {
        // Implementasi untuk mendapatkan ID vendor
        // Contoh:
        return daoVendorr.getVendorIdByName(vendorTextField.getText());
    }

    private String getVendorName() {
        // Mengembalikan nama vendor dari field teks
        return vendorTextField.getText();
    }

    private LocalDate getReceiptDate() {
        Date date = dateChooser.getDate();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String getNote() {
        return noteArea.getText();
    }

    private List<ModelReceiptItems> getReceiptItems() {
    List<ModelReceiptItems> items = new ArrayList<>();
    int rowCount = productTableModel.getRowCount();
    for (int i = 0; i < rowCount; i++) {
        String productIdStr = productTableModel.getValueAt(i, 1).toString().trim();
        String quantityStr = productTableModel.getValueAt(i, 3).toString().trim();
        
        // Skip baris kosong
        if (productIdStr.isEmpty()) {
            continue;
        }
        
        // Periksa apakah Qty kosong
        if (quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Qty pada baris " + (i+1) + " harus diisi.",
                "Error Input", JOptionPane.ERROR_MESSAGE);
            return null; // Mengembalikan null untuk menandakan ada error
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            items.add(new ModelReceiptItems(0, 0, productId, quantity));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Error pada baris " + (i+1) + ": Data tidak valid. Pastikan Product ID dan Quantity adalah angka.",
                "Error Input", JOptionPane.ERROR_MESSAGE);
            return null; // Mengembalikan null untuk menandakan ada error
        }
    }
    return items;
}
    
    private String findProductNameById(int productId) {
    for (ModelProduct product : products) {
        if (product.getProductId() == productId) {
            return product.getProductName();
        }
    }
    return "Unknown Product"; // Jika produk tidak ditemukan
}


    
    private void populateExistingData(List<String> vendorNames, String receiptNumber, 
                                  List<String> productNames, List<String> productCodes) {
    // Isi data vendor
    if (existingReceipt != null) {
        vendorTextField.setText(existingReceipt.getVendorName());
        receiptNumberField.setText(existingReceipt.getReceiptNumber());
        dateChooser.setDate(java.sql.Date.valueOf(existingReceipt.getReceiptDate()));
        noteArea.setText(existingReceipt.getNote());
        
        // Isi tabel produk dengan data item yang ada
        productTableModel.setRowCount(0); // Hapus baris yang ada
        for (ModelReceiptItems item : existingReceipt.getReceiptItems()) {
            String productName = findProductNameById(item.getProductId());
            productTableModel.addRow(new Object[]{productTableModel.getRowCount() + 1,
                                                  item.getProductId(), productName, item.getQuantity()});
        }
    }
}
}


