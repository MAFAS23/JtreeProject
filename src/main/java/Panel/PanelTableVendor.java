/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

/**
 *
 * @author athif
 */
import Controller.ControllerVendorCRUD;
import Model.ModelTableVendor;
import Model.ModelVendor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelTableVendor extends JPanel {

    private JTable table;
    private ModelTableVendor tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private List<ModelVendor> originalVendors;
   private ControllerVendorCRUD vendorController;

    public PanelTableVendor(List<ModelVendor> vendors) {
        this.tableModel = new ModelTableVendor(vendors);
        initComponents();
    }
   public PanelTableVendor(ControllerVendorCRUD vendorController) {
        this.vendorController = vendorController;
        this.originalVendors = vendorController.getAllVendors(); // Load initial data
        this.tableModel = new ModelTableVendor(originalVendors);
        initComponents();
    }


    private void initComponents() {
         setLayout(new BorderLayout());
        
        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Cari");
        searchPanel.add(new JLabel("Cari Vendor: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        searchButton.addActionListener(e -> filterVendors());
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

       // Tambahkan komponen ke panel utama
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    
    private void filterVendors() {
        String searchTerm = searchField.getText().trim();
        loadVendors(searchTerm);
    }

    private void loadVendors(String searchTerm) {
        List<ModelVendor> vendors = vendorController.searchVendor(searchTerm);
        if (vendors != null) {
            updateTableData(vendors);
        } else {
            JOptionPane.showMessageDialog(this, "Error loading vendors", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//    public void updateTableData(List<ModelVendor> vendors) {
//        this.tableModel = new ModelTableVendor(vendors);
//        table.setModel(tableModel);
//    }
    public void updateTableData(List<ModelVendor> vendors) {
        this.tableModel.setVendors(vendors); // Update the data in the model
        tableModel.fireTableDataChanged(); // Notify the table that data has changed
    }

    public ModelVendor getSelectedVendor() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getVendorAt(selectedRow);
        } else {
            return null;
        }
    }
    public void setVendorController(ControllerVendorCRUD vendorController) {
    this.vendorController = vendorController;
}
}