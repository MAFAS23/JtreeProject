/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

/**
 *
 * @author athif
 */

import Controller.ControllerGoodsIssuanceCRUD;
import Controller.ControllerVendorCRUD;
import Model.ModelGoodsIssuance;
import Model.ModelIssuanceItem;
import Model.ModelTableGoodsIssuance;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//public class PanelTableGoodsIssuance extends JPanel {
//
//    private JTable table;
//    private ModelTableGoodsIssuance modelTableGoodsIssuance;
//    private ControllerGoodsIssuanceCRUD issuanceController;
//
//    public PanelTableGoodsIssuance(List<ModelIssuanceItem> issuanceData) {
//        modelTableGoodsIssuance = new ModelTableGoodsIssuance(issuanceData);
//        table = new JTable(modelTableGoodsIssuance);
//
//        setLayout(new BorderLayout());
//        JScrollPane scrollPane = new JScrollPane(table);
//        add(scrollPane, BorderLayout.CENTER);
//    }
//
//    public void updateTableData(List<ModelIssuanceItem> issuanceData) {
//    modelTableGoodsIssuance.setIssuanceData(issuanceData);
//}
//
//    public int getSelectedRow() {
//        return table.getSelectedRow();
//    }
//
//    public Object getValueAt(int row, int column) {
//        return modelTableGoodsIssuance.getValueAt(row, column);
//    }
//    
//    public void setIssuanceController(ControllerGoodsIssuanceCRUD issuanceController) {
//    this.issuanceController = issuanceController;
//}
//    
//
//    public JTable getTable() {
//        return table;
//    }
//}

public class PanelTableGoodsIssuance extends JPanel {

    private JTable table;
    private ModelTableGoodsIssuance modelTableGoodsIssuance;
    private ControllerGoodsIssuanceCRUD issuanceController;

    public PanelTableGoodsIssuance(List<ModelGoodsIssuance> goodsIssuanceData) {
        // Menggunakan ModelTableGoodsIssuance untuk memodelkan data tabel
        modelTableGoodsIssuance = new ModelTableGoodsIssuance(goodsIssuanceData);
        table = new JTable(modelTableGoodsIssuance);

        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateTableData(List<ModelGoodsIssuance> goodsIssuanceData) {
        modelTableGoodsIssuance.setIssuanceData(goodsIssuanceData);
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public Object getValueAt(int row, int column) {
        return modelTableGoodsIssuance.getValueAt(row, column);
    }
    
    public void setIssuanceController(ControllerGoodsIssuanceCRUD issuanceController) {
        this.issuanceController = issuanceController;
    }

    public JTable getTable() {
        return table;
    }
}
