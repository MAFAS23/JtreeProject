/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author athif
 */
public class PopMenu {
    private JPopupMenu popupMenu;
    private JMenuItem addItem;
    private JMenuItem editItem;
    private JMenuItem deleteItem;

    public PopMenu(ActionListener addListener, ActionListener editListener, ActionListener deleteListener) {
        popupMenu = new JPopupMenu();

        addItem = new JMenuItem("Add");
        editItem = new JMenuItem("Edit");
        deleteItem = new JMenuItem("Delete");

        popupMenu.add(addItem);
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        // Set action listeners
        addItem.addActionListener(addListener);
        editItem.addActionListener(editListener);
        deleteItem.addActionListener(deleteListener);
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }
    
}
