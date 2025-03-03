
package Panel;

import Controller.ControllerGoodsIssuanceCRUD;
import Controller.ControllerGoodsReceiptsCRUD;
import Controller.ControllerNodeCRUD;
import Controller.ControllerProductCRUD;
import Controller.ControllerStock;
import Controller.ControllerVendorCRUD;
import DAO.DaoGoodsIssuance;
import DAO.DaoGoodsReceipts;
import DAO.DaoProduct;
import DAO.DaoReceiptItems;
import DAO.DaoStock;
import DAO.DaoTree;
import DAO.DaoVendor;
import DAO.ImplementVendor;
import Model.ModelGoodsIssuance;
import Model.ModelVendor;
import Panel.Dialog.AddProductDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllFrames extends JFrame {

    private final JtreePanel jtreePanel;
    private final PanelTable panelTable;
    private final GenericPanelButton productButtons,vendorButtons,goodsReceiptsButtons,issuanceButton;
    private final ControllerNodeCRUD controllerNodeCRUD;
    private final ControllerProductCRUD controllerProductCRUD;
    private final ControllerVendorCRUD controllerVendorCRUD;
    private final NavBar navBar;
    private JPanel mainPanel;
    private final PanelTableVendor panelTableVendor;
    private final ControllerGoodsReceiptsCRUD controllerGoodsReceiptsCRUD;
    private final PanelTableGoodsReceipts panelTableGoodsReceipts;
    private final PanelTableStock panelTableStock;
    private final ControllerStock controllerStock; 
    private final DaoStock daoStock;
    private PanelTableGoodsIssuance panelTableGoodsIssuance;
    private ControllerGoodsIssuanceCRUD issuanceController;
    private DaoGoodsIssuance daoGoodsIssuance;
    
    public AllFrames(DaoTree daoTree, DaoProduct daoProduct, ImplementVendor daoVendor,DaoGoodsReceipts daoGoodsReceipts,DaoGoodsIssuance daoGoodsIssuance){
        jtreePanel = new JtreePanel();
        controllerNodeCRUD = new ControllerNodeCRUD(jtreePanel, daoTree);
        jtreePanel.setController(controllerNodeCRUD);

        panelTable = new PanelTable(daoProduct);

        controllerProductCRUD = new ControllerProductCRUD(panelTable, daoProduct, controllerNodeCRUD);
        controllerNodeCRUD.setProductController(controllerProductCRUD);
        
        daoStock = new DaoStock();
        panelTableStock = new PanelTableStock();
        
        controllerStock = new ControllerStock(panelTableStock, daoStock); 
        panelTableStock.setController(controllerStock); 
        
        productButtons = new GenericPanelButton("Tambah Produk", "Edit Produk", "Hapus Produk");
//        productButtons.setAddAction(e -> controllerProductCRUD.showAddDialog());
        productButtons.setAddAction(e -> {
            try {
                controllerProductCRUD.showAddDialog();
            } catch (SQLException ex) {
                Logger.getLogger(AllFrames.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        productButtons.setEditAction(e -> {
            try {
                controllerProductCRUD.showEditDialog();
            } catch (SQLException ex) {
                Logger.getLogger(AllFrames.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        productButtons.setDeleteAction(e -> {
            try {
                controllerProductCRUD.showDeleteDialog();
            } catch (SQLException ex) {
                Logger.getLogger(AllFrames.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

//        List<ModelVendor> vendorList = new ArrayList<>();  // Ini akan diisi dengan data dari database
        List<ModelVendor> vendorList;
        try {
            vendorList = daoVendor.getAllVendors();  // Ambil data vendor dari database
        } catch (SQLException e) {
            e.printStackTrace();
            vendorList = new ArrayList<>();  // Jika terjadi error, gunakan daftar kosong
            JOptionPane.showMessageDialog(this, "Failed to load vendors: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        panelTableVendor = new PanelTableVendor(vendorList);
        controllerVendorCRUD = new ControllerVendorCRUD(panelTableVendor, daoVendor);
        panelTableVendor.setVendorController(controllerVendorCRUD); 
       

        vendorButtons = new GenericPanelButton("Tambah Vendor", "Edit Vendor", "Hapus Vendor");
        vendorButtons.setAddAction(e -> controllerVendorCRUD.showAddVendorDialog());
        vendorButtons.setEditAction(e -> controllerVendorCRUD.showEditVendorDialog());
        vendorButtons.setDeleteAction(e -> controllerVendorCRUD.showDeleteVendorDialog());
        
        panelTableGoodsReceipts = new PanelTableGoodsReceipts(List.of()); // Panel khusus penerimaan barang
        DaoReceiptItems daoReceiptItems = new DaoReceiptItems();
        
        
       
       
        
        // tadinya ada conn
        // Assuming you have created a DaoReceiptItems instance named daoReceiptItems
        controllerGoodsReceiptsCRUD = new ControllerGoodsReceiptsCRUD(panelTableGoodsReceipts, daoGoodsReceipts, daoReceiptItems, daoVendor, daoProduct);
        
        goodsReceiptsButtons = new GenericPanelButton("Tambah Penerimaan", "Edit Penerimaan", "Hapus Penerimaan");
        goodsReceiptsButtons.setAddAction(e ->controllerGoodsReceiptsCRUD.showAddDialog());
        goodsReceiptsButtons.setEditAction(e -> controllerGoodsReceiptsCRUD.showEditDialog());
        goodsReceiptsButtons.setDeleteAction(e -> controllerGoodsReceiptsCRUD.showDeleteDialog());
        
        
        // issuance
        panelTableGoodsIssuance = new PanelTableGoodsIssuance(new ArrayList<>());
        panelTableGoodsIssuance.setIssuanceController(issuanceController);
        issuanceController = new ControllerGoodsIssuanceCRUD(panelTableGoodsIssuance, daoGoodsIssuance, daoProduct);
        
        issuanceButton = new GenericPanelButton("Tambah Pengeluaran", "Edit Pengeluaran", "Hapus Pengeluaran");
        issuanceButton.setAddAction(e -> issuanceController.showAddDialog());
        issuanceButton.setEditAction(e -> issuanceController.showEditDialog());
        issuanceButton.setDeleteAction(e -> issuanceController.showDeleteDialog());
        
        
        navBar = new NavBar();

        initComponents();
        setupNavBarListeners();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setTitle("Watch Management System");

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(navBar.getMenuBar(), BorderLayout.NORTH);

        JScrollPane treeScrollPane = new JScrollPane(jtreePanel.getJTree());
        treeScrollPane.setPreferredSize(new Dimension(250, this.getHeight()));
        mainPanel.add(treeScrollPane, BorderLayout.WEST);

        mainPanel.add(panelTable.getBackgroundPanel(), BorderLayout.CENTER);
        mainPanel.add(productButtons, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private void setupNavBarListeners() {
        navBar.setVendorMenuActionListener(e -> {
        controllerVendorCRUD.refreshVendorTable();
        showVendorTable();
    });
        navBar.setHomeMenuActionListener(e -> showHome());
        navBar.setGoodsReceiptsMenuActionListener(e -> showGoodsReceiptsTable()); // Add this
        navBar.setStockMenuActionListener(e -> showStockTable());
        navBar.setIssuesItemMenuListener(e -> showIssuanceTable());
        
    }
    
    private void showIssuanceTable() {
    clearTablePanel();
    mainPanel.add(panelTableGoodsIssuance, BorderLayout.CENTER);
    mainPanel.add(issuanceButton, BorderLayout.SOUTH);
    mainPanel.revalidate();
    mainPanel.repaint();
}

    private void showVendorTable() {
    clearTablePanel();
    mainPanel.add(panelTableVendor, BorderLayout.CENTER);
    mainPanel.add(vendorButtons, BorderLayout.SOUTH);
    mainPanel.revalidate();
    mainPanel.repaint();
}

    private void showStockTable() {
    clearTablePanel();
    mainPanel.add(panelTableStock, BorderLayout.CENTER);
    mainPanel.revalidate();
    mainPanel.repaint();

    controllerStock.loadStockData(null, "All Categories", false);
}
    
    private void showGoodsReceiptsTable() {
        clearTablePanel();
        mainPanel.add(panelTableGoodsReceipts, BorderLayout.CENTER);
        mainPanel.add(goodsReceiptsButtons, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showHome() {
        clearTablePanel();
        mainPanel.add(panelTable.getBackgroundPanel(), BorderLayout.CENTER);
        mainPanel.add(productButtons, BorderLayout.SOUTH);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void clearTablePanel() {
        // Hanya menghapus panel di posisi CENTER dan SOUTH, bukan seluruh mainPanel
        mainPanel.remove(panelTableVendor);
        mainPanel.remove(panelTableGoodsReceipts);
        mainPanel.remove(panelTable.getBackgroundPanel());
        mainPanel.remove(panelTableGoodsIssuance);
        
        mainPanel.remove(panelTableStock);
        mainPanel.remove(vendorButtons);
        mainPanel.remove(goodsReceiptsButtons);
        mainPanel.remove(productButtons);
        mainPanel.remove(issuanceButton);
      
    }
}
