/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

/**
 *
 * @author athif
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class GenericPanelButton extends JPanel {

    private final JButton btnAdd;
    private final JButton btnEdit;
    private final JButton btnDelete;

    public GenericPanelButton(String addLabel, String editLabel, String deleteLabel) {
        setLayout(new GridLayout(1, 3));

        btnAdd = new JButton(addLabel);
        btnEdit = new JButton(editLabel);
        btnDelete = new JButton(deleteLabel);

        add(btnAdd);
        add(btnEdit);
        add(btnDelete);
    }

    public void setAddAction(Consumer<ActionEvent> action) {
        btnAdd.addActionListener(e -> {
            try {
                action.accept(e);
            } catch (Exception ex) {
                handleException("menambahkan", ex);
            }
        });
    }

    public void setEditAction(Consumer<ActionEvent> action) {
        btnEdit.addActionListener(e -> {
            try {
                action.accept(e);
            } catch (Exception ex) {
                handleException("mengedit", ex);
            }
        });
    }

    public void setDeleteAction(Consumer<ActionEvent> action) {
        btnDelete.addActionListener(e -> {
            try {
                action.accept(e);
            } catch (Exception ex) {
                handleException("menghapus", ex);
            }
        });
    }

    private void handleException(String operation, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Error " + operation + " data: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Getter untuk tombol-tombol jika diperlukan
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
