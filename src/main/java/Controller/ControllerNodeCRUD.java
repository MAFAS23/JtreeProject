/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.DaoTree;
import Model.ModelFolder;
import Model.ModelFolderRelations;
import Model.ModelFolderStructure;
import Panel.JtreePanel;
import Panel.PopMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author athif
 */
public class ControllerNodeCRUD {
    private final JtreePanel view;
    private final DaoTree daoTree;
    private ControllerProductCRUD productController;

//    private final ControllerProductCRUD productController;

    public ControllerNodeCRUD(JtreePanel view, DaoTree daoTree) {
        this.view = view;
        this.daoTree = daoTree;
        
        initializeTreeListener();
    }
    
    public void setProductController(ControllerProductCRUD productController) {
        this.productController = productController;
    }
    
    private void initializeTreeListener() {
        view.getJTree().addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) 
                view.getJTree().getLastSelectedPathComponent();
            
            if (node != null && node.getUserObject() instanceof ModelFolderStructure) {
                ModelFolderStructure folderStructure = (ModelFolderStructure) node.getUserObject();
                int folderId = folderStructure.getFolder().getId();
                if (productController != null) {
                    try {
                        productController.refreshTable(folderId);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(view, "Error loading products: " + ex.getMessage());
                    }
                }
            }
        });
    }

    public void initializePopupMenuAndListeners(JTree tree) {
        PopMenu popupMenu = new PopMenu(
            e -> addNode(),
            e -> editNode(),
            e -> deleteNode()
        );

        setupTreeMouseListener(tree, popupMenu);
    }
    public void setupTreeMouseListener(JTree tree, PopMenu popupMenu) {
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = tree.getClosestRowForLocation(e.getX(), e.getY());
                    tree.setSelectionRow(row);
                    popupMenu.getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public void refreshTree() {
        SwingUtilities.invokeLater(this::loadTreeData);
    }
    private void loadTreeData() {
        System.out.println("Loading tree data...");

        // Load semua folder dari database
        List<ModelFolder> allFolders = daoTree.getAllFolders();
        Map<Integer, DefaultMutableTreeNode> nodeMap = new HashMap<>();
        DefaultMutableTreeNode root = null;  // Root node dari database

        // Buat node untuk setiap folder dan tentukan root node
        for (ModelFolder folder : allFolders) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new ModelFolderStructure(folder));
            nodeMap.put(folder.getId(), node);
            System.out.println("Node created: " + folder.getName() + " with ID: " + folder.getId());

            if (folder.getId() == 1) {  // Tentukan root node dari database
                root = node;
                System.out.println("Identified Root node from database.");
            }
        }

        // Set root node yang diambil dari database
        if (root != null) {
            view.getTreeModel().setRoot(root);
        } else {
            System.err.println("Root node not found in database.");
            return;  // Tidak ada root node, keluar dari metode
        }

        // Bangun struktur tree untuk semua nodes kecuali root
        for (ModelFolder folder : allFolders) {
            DefaultMutableTreeNode node = nodeMap.get(folder.getId());
            if (folder.getId() != 1) {  // Jangan tambahkan Root lagi
                List<ModelFolderRelations> relations = daoTree.getRelationsByChildId(folder.getId());
                if (!relations.isEmpty()) {
                    ModelFolderRelations relation = relations.get(0);  // Ambil relasi pertama
                    DefaultMutableTreeNode parentNode = nodeMap.get(relation.getParentId());
                    if (parentNode != null) {
                        parentNode.add(node);
                        System.out.println("Added " + folder.getName() + " as child of " + parentNode.getUserObject().toString());
                    }
                }
            }
        }

        view.getTreeModel().reload();
        view.getJTree().repaint();
        System.out.println("Tree data loaded successfully.");
    }

    private void addNode() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) view.getJTree().getLastSelectedPathComponent();

        // Jika tidak ada node yang dipilih, default ke root paling atas
        if (selectedNode == null) {
            selectedNode = (DefaultMutableTreeNode) view.getTreeModel().getRoot();
        }

        String nodeName = JOptionPane.showInputDialog(view, "Enter the name for the new folder:");
        if (nodeName != null && !nodeName.trim().isEmpty()) {
            ModelFolderStructure parentStructure = (ModelFolderStructure) selectedNode.getUserObject();
            ModelFolder parentFolder = parentStructure.getFolder();

            daoTree.addNewFolder(nodeName, parentFolder.getId()); // Tambahkan folder baru sebagai child dari node yang dipilih
            refreshTree();
        }
    }

    private void editNode() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) view.getJTree().getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.isRoot()) return;

        ModelFolderStructure folderStructure = (ModelFolderStructure) selectedNode.getUserObject();
        ModelFolder folder = folderStructure.getFolder();
        String nodeName = JOptionPane.showInputDialog(view, "Enter the new name for the folder:", folder.getName());
        if (nodeName != null && !nodeName.trim().isEmpty()) {
            folder.setName(nodeName);
            daoTree.updateFolder(folder);
            refreshTree();
        }
    }

    private void deleteNode() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) view.getJTree().getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.isRoot()) return;

        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this folder and all its subfolders?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ModelFolderStructure folderStructure = (ModelFolderStructure) selectedNode.getUserObject();
            ModelFolder folder = folderStructure.getFolder();
            try {
                deleteFolder(folder.getId());
                refreshTree();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(view, "Error deleting folder: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteFolder(int folderId) throws SQLException {
        List<ModelFolderRelations> childRelations = daoTree.getRelationsByParentId(folderId);
        for (ModelFolderRelations relation : childRelations) {
            deleteFolder(relation.getChildId());
        }
        daoTree.deleteRelation(folderId);
        daoTree.deleteFolder(folderId);
    }
    
    public int getSelectedFolderId() {
    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) view.getJTree().getLastSelectedPathComponent();
    if (selectedNode == null) {
        return -1; // Tidak ada node yang dipilih
    }

    ModelFolderStructure folderStructure = (ModelFolderStructure) selectedNode.getUserObject();
    return folderStructure.getFolder().getId();
}


    
    
}
