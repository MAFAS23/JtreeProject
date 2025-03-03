package Panel;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import Controller.ControllerNodeCRUD;


public class JtreePanel extends JPanel {
    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private ControllerNodeCRUD controller;
    private PopMenu popupMenu;


    public JtreePanel() {
    // Inisialisasi tree model tanpa root node
    treeModel = new DefaultTreeModel(null);
    tree = new JTree(treeModel);

    // Konfigurasi tampilan tree
    Font newFont = new Font("Roboto", Font.PLAIN, 14);
    tree.setFont(newFont);

    // Menambahkan JTree ke JScrollPane
    JScrollPane treeView = new JScrollPane(tree);
    setLayout(new BorderLayout());
    add(treeView, BorderLayout.CENTER);

    }
    public void setController(ControllerNodeCRUD controller) {
        this.controller = controller;
        if (controller != null) {
            controller.initializePopupMenuAndListeners(tree);
            controller.refreshTree();
        }
    }
    public void refreshTree() {
        if (controller != null) {
            controller.refreshTree();
        }
    }

    public JTree getJTree() {
        return tree;
    }
    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }
}
