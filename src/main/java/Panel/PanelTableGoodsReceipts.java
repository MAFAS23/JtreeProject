/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

import DAO.DaoGoodsReceipts;
import Model.ModelGoodsReceipts;
import Model.ModelTableGoodsReceipts;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanelTableGoodsReceipts extends JPanel {

    private JTable table;
    private ModelTableGoodsReceipts tableModel;
    private JDateChooser fromDateChooser;
    private JDateChooser toDateChooser;
    private JTextField searchField;
    private JButton applyFilterButton;
    private DaoGoodsReceipts daoGoodsReceipts;

    public PanelTableGoodsReceipts(List<ModelGoodsReceipts> goodsReceipts) {
        this.tableModel = new ModelTableGoodsReceipts(goodsReceipts);
        this.daoGoodsReceipts = new DaoGoodsReceipts();
        initComponents();
    }

   private void initComponents() {
        setLayout(new BorderLayout());

        // Panel untuk filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Komponen filter tanggal
        fromDateChooser = new JDateChooser();
        toDateChooser = new JDateChooser();
        filterPanel.add(new JLabel("Dari:"));
        filterPanel.add(fromDateChooser);
        filterPanel.add(new JLabel("Sampai:"));
        filterPanel.add(toDateChooser);

        // Komponen pencarian
        searchField = new JTextField(20);
        filterPanel.add(new JLabel("Cari:"));
        filterPanel.add(searchField);

        // Tombol terapkan
        applyFilterButton = new JButton("Apply Filter");
        applyFilterButton.addActionListener(this::applyFilter);
        filterPanel.add(applyFilterButton);

        add(filterPanel, BorderLayout.NORTH);

        // Tabel
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

     private void applyFilter(ActionEvent e) {
        try {
            LocalDate fromDate = null;
            LocalDate toDate = null;
            
            if (fromDateChooser.getDate() != null) {
                fromDate = fromDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            if (toDateChooser.getDate() != null) {
                toDate = toDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
            
            String searchTerm = searchField.getText().trim();
            
            List<ModelGoodsReceipts> filteredList = daoGoodsReceipts.filterGoodsReceipts(fromDate, toDate, searchTerm);
            updateTableData(filteredList);
        } catch (SQLException ex) {
            Logger.getLogger(PanelTableGoodsReceipts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void updateTableData(List<ModelGoodsReceipts> goodsReceipts) {
        this.tableModel = new ModelTableGoodsReceipts(goodsReceipts);
        table.setModel(tableModel);
    }
    // Metode baru untuk mendapatkan baris yang dipilih
    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    // Metode baru untuk mendapatkan nilai pada baris dan kolom tertentu
    public Object getValueAt(int row, int column) {
        return table.getValueAt(row, column);
    }

    // Metode untuk mendapatkan ModelGoodsReceipts dari baris yang dipilih
    public ModelGoodsReceipts getSelectedGoodsReceipt() {
        int selectedRow = getSelectedRow();
        if (selectedRow != -1) {
            return tableModel.getGoodsReceiptAt(selectedRow);
        }
        return null;
    }

    // Metode untuk mendapatkan JTable jika diperlukan
    public JTable getTable() {
        return table;
    }
}