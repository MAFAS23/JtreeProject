/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Panel.AddDialogVendor;

/**
 *
 * @author athif
 */
import Model.ModelVendor;
import Panel.PanelTableVendor;
import Panel.AddDialogVendor;
//import Panel.EditDialogVendor;
//import Panel.DelDialogVendor;
import DAO.DaoVendor;
import DAO.ImplementVendor;
import Panel.DelDialogVendor;
import Panel.EditDialogVendor;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerVendorCRUD {
    private final PanelTableVendor panelTableVendor;
    private final ImplementVendor daoVendor;

    public ControllerVendorCRUD(PanelTableVendor panelTableVendor, ImplementVendor daoVendor) {
        this.panelTableVendor = panelTableVendor;
        this.daoVendor = daoVendor;
    }

    public void refreshVendorTable() {
        try {
            List<ModelVendor> vendors = daoVendor.getAllVendors();
            panelTableVendor.updateTableData(vendors);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error refreshing vendor table: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showAddVendorDialog() {
        try {
            int nextVendorId = daoVendor.getNextVendorId();
            AddDialogVendor dialog = new AddDialogVendor(null, nextVendorId);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                ModelVendor newVendor = dialog.getNewVendor();
                if (newVendor != null) {
                    daoVendor.insertVendor(newVendor);
                    refreshVendorTable();
                    JOptionPane.showMessageDialog(null, "Vendor added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding vendor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showEditVendorDialog() {
    ModelVendor selectedVendor = panelTableVendor.getSelectedVendor();
    if (selectedVendor == null) {
        JOptionPane.showMessageDialog(null, "Please select a vendor to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }

    EditDialogVendor dialog = new EditDialogVendor(null, selectedVendor);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
        ModelVendor updatedVendor = dialog.getUpdatedVendor();
        if (updatedVendor != null) {
            try {
                daoVendor.updateVendor(updatedVendor);
                refreshVendorTable();
                JOptionPane.showMessageDialog(null, "Vendor updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error updating vendor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

    public void showDeleteVendorDialog() {
    ModelVendor selectedVendor = panelTableVendor.getSelectedVendor();
    if (selectedVendor == null) {
        JOptionPane.showMessageDialog(null, "Please select a vendor to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }

    DelDialogVendor dialog = new DelDialogVendor(null, selectedVendor);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
        try {
            daoVendor.deleteVendor(selectedVendor.getVendorId());
            refreshVendorTable();
            JOptionPane.showMessageDialog(null, "Vendor deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting vendor: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    public List<ModelVendor> getAllVendors() {
    try {
        return daoVendor.getAllVendors(); // Menggunakan metode di DaoVendor
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>(); // Mengembalikan list kosong jika terjadi error
    }
}
    
    public List<ModelVendor> searchVendor(String name) {
    try {
        return daoVendor.searchVendorByName(name);
    } catch (SQLException e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}
}
   
