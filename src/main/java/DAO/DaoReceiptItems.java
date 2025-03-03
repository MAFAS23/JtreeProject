/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author athif
 */

import Conn.ConnectionDb;
import Model.ModelReceiptItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class DaoReceiptItems implements ImplementReceiptItems {

//    private final Connection conn;
    private final DataSource dataSource;


    public DaoReceiptItems() {
        this.dataSource = ConnectionDb.getInstance().getDataSource();
        
    }
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Metode untuk menambahkan item penerimaan baru ke database
    @Override
    public void insertReceiptItem(ModelReceiptItems receiptItem) throws SQLException {
    String sql = "INSERT INTO receipt_items (receipt_id, product_id, quantity) VALUES (?, ?, ?)";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, receiptItem.getReceiptId());
        stmt.setInt(2, receiptItem.getProductId());
        stmt.setInt(3, receiptItem.getQuantity());
//        stmt.setString(4, receiptItem.getNote());
        stmt.executeUpdate();
    }
}

    // Metode untuk memperbarui item penerimaan yang sudah ada
    @Override
    public void updateReceiptItem(ModelReceiptItems receiptItem) throws SQLException {
        String sql = "UPDATE receipt_items SET product_id = ?, quantity = ? WHERE item_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, receiptItem.getProductId());
            stmt.setInt(2, receiptItem.getQuantity());
//            stmt.setString(3, receiptItem.getNote());
            stmt.setInt(4, receiptItem.getItemId());
            stmt.executeUpdate();
        }
    }

    // Metode untuk menghapus item penerimaan berdasarkan item_id
    @Override
    public void deleteReceiptItem(int itemId) throws SQLException {
        String sql = "DELETE FROM receipt_items WHERE item_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        }
    }

    // Metode untuk mengambil semua item penerimaan berdasarkan receipt_id
    @Override
    public List<ModelReceiptItems> getReceiptItemsByReceiptId(int receiptId) throws SQLException {
        List<ModelReceiptItems> receiptItems = new ArrayList<>();
        String sql = "SELECT * FROM receipt_items WHERE receipt_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, receiptId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ModelReceiptItems item = new ModelReceiptItems(
                            rs.getInt("item_id"),
                            rs.getInt("receipt_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity")
                           
                    );
                    receiptItems.add(item);
                }
            }
        }
        return receiptItems;
    }

    
    // Metode untuk mengambil semua item penerimaan dari database
    @Override
    public List<ModelReceiptItems> getAllReceiptItems() throws SQLException {
        List<ModelReceiptItems> receiptItems = new ArrayList<>();
        String sql = "SELECT * FROM receipt_items";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ModelReceiptItems item = new ModelReceiptItems(
                        rs.getInt("item_id"),
                        rs.getInt("receipt_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity")
//                        rs.getString("note")
                );
                receiptItems.add(item);
            }
        }
        return receiptItems;
    }
}

