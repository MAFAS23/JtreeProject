/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

/**
 *
 * @author athif
 */
import Controller.ControllerProductCRUD;
import Model.ModelProduct;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PanelButton extends JPanel {
    private final JButton btnAdd;
    private final JButton btnEdit;
    private final JButton btnDelete;
   

    public PanelButton() {
        // Mengatur layout panel
        setLayout(new GridLayout(1, 3));

        // Inisialisasi tombol-tombol
        btnAdd = new JButton("Tambah Produk");
        btnEdit = new JButton("Edit Produk");
        btnDelete = new JButton("Hapus Produk");
        

        // Menambahkan tombol-tombol ke panel
        add(btnAdd);
        add(btnEdit);
        add(btnDelete);
    }
    
    
    public void setController(ControllerProductCRUD controller) {
    btnAdd.addActionListener(e -> {
        try {
            controller.showAddDialog();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
    btnEdit.addActionListener(e -> {
            try {
                controller.showEditDialog();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error editing product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    btnDelete.addActionListener(e -> {
            try {
                controller.showDeleteDialog();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    

}

    // Getter untuk tombol-tombol
    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }
}
