/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author athif
 */
import DAO.DaoProduct;
import Model.ModelProduct;
import Model.ModelTableStock;
import Panel.Dialog.AddDialog;
import Panel.PanelTable;
import Panel.Dialog.EditDialog;
import Panel.Dialog.DelDialog;
import Panel.JtreePanel;
import java.awt.Frame;
import java.awt.Window;
import java.sql.SQLException;

import javax.swing.*;
import java.util.List;

public class ControllerProductCRUD {

    private final PanelTable panelTable;
    private JtreePanel view;
    private final DaoProduct daoProduct;
    private final ControllerNodeCRUD nodeController;

    public ControllerProductCRUD(PanelTable panelTable, DaoProduct daoProduct, ControllerNodeCRUD nodeController) {
        this.panelTable = panelTable;
        this.daoProduct = daoProduct;
        this.nodeController = nodeController;
    }
    
    

   public void showAddDialog() throws SQLException {
    int folderId = nodeController.getSelectedFolderId(); // Ambil folderId dari node yang dipilih
    if (folderId == -1) {
        JOptionPane.showMessageDialog(panelTable, "Please select a folder to add a product.", "No Folder Selected", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int nextProductId = daoProduct.getNextProductId();
    AddDialog addDialog = new AddDialog(null, nextProductId,folderId);
    addDialog.setVisible(true);

    ModelProduct newProduct = addDialog.getUpdatedProduct();
    if (newProduct != null) {
        try {
            daoProduct.insertProduct(newProduct); // Simpan produk baru ke database
            refreshTable(folderId); // Tambahkan ini untuk me-refresh tabel setelah menambah data
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panelTable, "Error adding product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



   public void showEditDialog()throws SQLException{
        ModelProduct selectedProduct = panelTable.getSelectedProduct();
        if (panelTable == null) {
            System.out.println("panelTable is null");
            return;
        }
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(panelTable, "Please select a product to edit.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EditDialog editDialog = new EditDialog(null, selectedProduct);
        editDialog.setVisible(true);

        ModelProduct updatedProduct = editDialog.getUpdatedProduct();
        if (updatedProduct != null) {
            try {
                daoProduct.updateProduct(updatedProduct);
                refreshTable(updatedProduct.getFolderId());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(panelTable, "Error updating product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showDeleteDialog() throws SQLException{
    int folderId = nodeController.getSelectedFolderId();
    if (folderId == -1) {
        JOptionPane.showMessageDialog(panelTable, "Please select a folder to delete a product.", "No Folder Selected", JOptionPane.WARNING_MESSAGE);
        return;
    }

    ModelProduct selectedProduct = panelTable.getSelectedProduct();
    if (selectedProduct == null) {
        JOptionPane.showMessageDialog(panelTable, "Please select a product to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    Window window = SwingUtilities.getWindowAncestor(panelTable);
    Frame parent = (window instanceof Frame) ? (Frame) window : null;

    DelDialog deleteDialog = new DelDialog(
        parent, 
        selectedProduct.getProductId(), 
        selectedProduct.getProductName(),
        selectedProduct.getPrice()
    );
    deleteDialog.setVisible(true);

    if (deleteDialog.isConfirmed()) {
        try {
            daoProduct.deleteProduct(selectedProduct.getProductId());
            refreshTable(folderId);
            JOptionPane.showMessageDialog(panelTable, "Product deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelTable, "Error deleting product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
   
    public void refreshTable(int folderId) throws SQLException {
        List<ModelProduct> products = daoProduct.getProductsByFolderId(folderId);
        panelTable.updateTableData(products);
    }

}