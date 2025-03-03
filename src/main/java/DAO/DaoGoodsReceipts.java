/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conn.ConnectionDb;
import Model.ModelGoodsReceipts;
import Model.ModelReceiptItems;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;

public class DaoGoodsReceipts implements ImplementGoodsReceipts {

//    private final Connection conn;
    private final DataSource dataSource;
    private static final String PREFIX = "RN";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmm");


    public DaoGoodsReceipts() {
         this.dataSource = ConnectionDb.getInstance().getDataSource();
    }
    
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public int insertGoodsReceipt(ModelGoodsReceipts goodsReceipt) throws SQLException {
    String sql = "INSERT INTO goods_receipts (receipt_number, vendor_id, receipt_date, note) VALUES (?, ?, ?, ?)";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, goodsReceipt.getReceiptNumber());
        stmt.setInt(2, goodsReceipt.getVendorId());
        stmt.setDate(3, Date.valueOf(goodsReceipt.getReceiptDate()));
        stmt.setString(4, goodsReceipt.getNote());
        stmt.executeUpdate();

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating goods receipt failed, no ID obtained.");
            }
        }
    }
}


    @Override
    public void updateGoodsReceipt(ModelGoodsReceipts goodsReceipt) throws SQLException {
        String sql = "UPDATE goods_receipts SET receipt_number = ?, vendor_id = ?, receipt_date = ?, note = ? WHERE receipt_id = ?";
        try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, goodsReceipt.getReceiptNumber());
            stmt.setInt(2, goodsReceipt.getVendorId());
            stmt.setDate(3, java.sql.Date.valueOf(goodsReceipt.getReceiptDate()));
            stmt.setString(4, goodsReceipt.getNote());
            stmt.setInt(5, goodsReceipt.getReceiptId());
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void updateGoodsReceiptForEdit(ModelGoodsReceipts receipt, List<ModelReceiptItems> items, Map<Integer, Integer> stockChanges) throws SQLException {
    try (Connection conn = getConnection()) {
        // Nonaktifkan auto-commit untuk memulai transaksi
        conn.setAutoCommit(false);

        // Update goods_receipts table
        String updateReceiptSql = "UPDATE goods_receipts SET receipt_number = ?, vendor_id = ?, receipt_date = ?, note = ? WHERE receipt_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateReceiptSql)) {
            pstmt.setString(1, receipt.getReceiptNumber());
            pstmt.setInt(2, receipt.getVendorId());
            pstmt.setDate(3, java.sql.Date.valueOf(receipt.getReceiptDate()));
            pstmt.setString(4, receipt.getNote());
            pstmt.setInt(5, receipt.getReceiptId());
            pstmt.executeUpdate();
        }

        // Delete existing receipt items
        String deleteItemsSql = "DELETE FROM receipt_items WHERE receipt_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteItemsSql)) {
            pstmt.setInt(1, receipt.getReceiptId());
            pstmt.executeUpdate();
        }

        // Insert updated receipt items
        String insertItemSql = "INSERT INTO receipt_items (receipt_id, product_id, quantity, note) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertItemSql)) {
            for (ModelReceiptItems item : items) {
                pstmt.setInt(1, receipt.getReceiptId());
                pstmt.setInt(2, item.getProductId());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setString(4, item.getNote());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        // Update product stock
        String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateStockSql)) {
            for (Map.Entry<Integer, Integer> entry : stockChanges.entrySet()) {
                pstmt.setInt(1, entry.getValue());
                pstmt.setInt(2, entry.getKey());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        // Commit transaksi setelah semua operasi berhasil
        conn.commit();
    } catch (SQLException e) {
        // Rollback jika terjadi kesalahan
        try (Connection conn = getConnection()) {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw e;
    } finally {
        // Kembalikan ke mode auto-commit
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
    

    @Override
    public void deleteGoodsReceipt(int receiptId) throws SQLException {
        String sql = "DELETE FROM goods_receipts WHERE receipt_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, receiptId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<ModelGoodsReceipts> getAllGoodsReceipts() throws SQLException {
    List<ModelGoodsReceipts> goodsReceipts = new ArrayList<>();
    String sql = "SELECT gr.receipt_id, gr.receipt_number, gr.vendor_id, v.name AS vendor_name, gr.receipt_date, gr.note " +
                 "FROM goods_receipts gr " +
                 "JOIN vendors v ON gr.vendor_id = v.vendor_id";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            ModelGoodsReceipts goodsReceipt = new ModelGoodsReceipts.Builder()
                            .receiptId(rs.getInt("receipt_id"))
                            .receiptNumber(rs.getString("receipt_number"))
                            .vendorId(rs.getInt("vendor_id"))
                            .vendorName(rs.getString("vendor_name"))
                            .receiptDate(rs.getDate("receipt_date").toLocalDate())
                            .note(rs.getString("note"))
                            .build();
//            ModelGoodsReceipts goodsReceipt = new ModelGoodsReceipts(
//                rs.getInt("receipt_id"),
//                rs.getString("receipt_number"),
//                rs.getInt("vendor_id"),
//                rs.getString("vendor_name"),  // Mengambil nama vendor
//                rs.getDate("receipt_date").toLocalDate(),
//                rs.getString("note")
//            );
            goodsReceipts.add(goodsReceipt);
        }
    }
    return goodsReceipts;
}

    @Override
    public ModelGoodsReceipts getGoodsReceiptById(int receiptId) throws SQLException {
    String sql = "SELECT gr.receipt_id, gr.receipt_number, gr.vendor_id, v.name AS vendor_name, gr.receipt_date, gr.note " +
                 "FROM goods_receipts gr " +
                 "JOIN vendors v ON gr.vendor_id = v.vendor_id " +
                 "WHERE gr.receipt_id = ?";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, receiptId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                ModelGoodsReceipts goodsReceipt = new ModelGoodsReceipts.Builder()
                        .receiptId(rs.getInt("receipt_id"))
                        .receiptNumber(rs.getString("receipt_number"))
                        .vendorId(rs.getInt("vendor_id"))
                        .vendorName(rs.getString("vendor_name"))
                        .receiptDate(rs.getDate("receipt_date").toLocalDate())
                        .note(rs.getString("note"))
                        .build();
//                return new ModelGoodsReceipts(
//                    rs.getInt("receipt_id"),
//                    rs.getString("receipt_number"),
//                    rs.getInt("vendor_id"),
//                    rs.getString("vendor_name"),  // Ambil vendor_name dari query
//                    rs.getDate("receipt_date").toLocalDate(),
//                    rs.getString("note")
//                );
            }
        }
    }
    return null;
}
    
   
    @Override
    public ModelGoodsReceipts getGoodsReceiptByIdForEdit(int receiptId) throws SQLException {
    String sql = "SELECT gr.receipt_id, gr.receipt_number, gr.vendor_id, v.name AS vendor_name, gr.receipt_date, gr.note " +
                 "FROM goods_receipts gr " +
                 "JOIN vendors v ON gr.vendor_id = v.vendor_id " +
                 "WHERE gr.receipt_id = ?";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, receiptId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                ModelGoodsReceipts goodsReceipt = new ModelGoodsReceipts.Builder()
                            .receiptId(rs.getInt("receipt_id"))
                            .receiptNumber(rs.getString("receipt_number"))
                            .vendorId(rs.getInt("vendor_id"))
                            .vendorName(rs.getString("vendor_name"))
                            .receiptDate(rs.getDate("receipt_date").toLocalDate())
                            .note(rs.getString("note"))
                            .build();
                            
//                ModelGoodsReceipts goodsReceipt = new ModelGoodsReceipts(
//                    rs.getInt("receipt_id"),
//                    rs.getString("receipt_number"),
//                    rs.getInt("vendor_id"),
//                    rs.getString("vendor_name"),
//                    rs.getDate("receipt_date").toLocalDate(),
//                    rs.getString("note")
//                );

                // Ambil item-item penerimaan
                goodsReceipt.setReceiptItems(getReceiptItemsForReceipt(receiptId));

                return goodsReceipt;
            }
        }
    }
    return null;
}

private List<ModelReceiptItems> getReceiptItemsForReceipt(int receiptId) throws SQLException {
    List<ModelReceiptItems> items = new ArrayList<>();
    String sql = "SELECT ri.*, p.product_name FROM receipt_items ri " +
                 "JOIN products p ON ri.product_id = p.product_id " +
                 "WHERE ri.receipt_id = ?";
    try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, receiptId);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ModelReceiptItems item = new ModelReceiptItems(
                    rs.getInt("item_id"),
                    rs.getInt("receipt_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity")
//                    rs.getString("note")
                );
                // Kita tidak menyimpan product_name di ModelReceiptItems
                items.add(item);
            }
        }
    }
    return items;
}
   
    @Override
    public List<ModelGoodsReceipts> getGoodsReceiptsWithItems() throws SQLException {
    List<ModelGoodsReceipts> goodsReceiptsList = new ArrayList<>();
    String sql = "SELECT gr.receipt_id, gr.receipt_number, gr.vendor_id, v.name as vendor_name, gr.receipt_date, gr.note, "
               + "ri.product_id, p.product_name, ri.quantity, ri.note as item_note "
               + "FROM goods_receipts gr "
               + "JOIN vendors v ON gr.vendor_id = v.vendor_id "
               + "JOIN receipt_items ri ON gr.receipt_id = ri.receipt_id "
               + "JOIN products p ON ri.product_id = p.product_id";
    
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            // Membuat objek ModelGoodsReceipts dengan data yang relevan
             ModelGoodsReceipts receipt = new ModelGoodsReceipts.Builder()
            .receiptId(rs.getInt("receipt_id"))
            .receiptNumber(rs.getString("receipt_number"))
            .vendorId(rs.getInt("vendor_id"))
            .vendorName(rs.getString("vendor_name"))
            .productName(rs.getString("product_name"))
            .productId(rs.getInt("product_id"))
            .quantity(rs.getInt("quantity"))
            .receiptDate(rs.getDate("receipt_date").toLocalDate())
            .note(rs.getString("note"))
            .itemNote(rs.getString("item_note"))
            .build();
//            ModelGoodsReceipts receipt = new ModelGoodsReceipts.Builder()
//                    rs.getInt("receipt_id"),
//                    rs.getString("receipt_number"),
//                    rs.getInt("vendor_id"),
//                    rs.getString("vendor_name"),
//                    rs.getDate("receipt_date").toLocalDate(),
//                    rs.getString("note"),
//                    rs.getString("product_name"),
//                    rs.getInt("product_id"),
//                    rs.getInt("quantity"),
//                    rs.getString("item_note")
//            );
            goodsReceiptsList.add(receipt);
        }
    }
    return goodsReceiptsList;
}
    
    @Override
    public String generateReceiptNumber() throws SQLException {
         LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DATE_FORMATTER);
        String uuidPart = UUID.randomUUID().toString().substring(0, 4); // Menggunakan 8 karakter UUID
        
        return String.format("%s-%s-%s", PREFIX, datePart, uuidPart);
    }


    @Override
    public int getNextReceiptId() throws SQLException {
        String sql = "SELECT nextval('goods_receipts_receipt_id_seq')";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to get next receipt_id");
        }
    }
    
    @Override
    public void deleteGoodsReceiptForDel(int receiptId) throws SQLException {
    try (Connection conn = getConnection()) {
        // Nonaktifkan auto-commit untuk memulai transaksi
        conn.setAutoCommit(false);

        try {
            // Hapus item penerimaan terlebih dahulu
            String deleteItemsSql = "DELETE FROM receipt_items WHERE receipt_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteItemsSql)) {
                pstmt.setInt(1, receiptId);
                pstmt.executeUpdate();
            }

            // Kemudian hapus penerimaan barang
            String deleteReceiptSql = "DELETE FROM goods_receipts WHERE receipt_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteReceiptSql)) {
                pstmt.setInt(1, receiptId);
                pstmt.executeUpdate();
            }

            // Commit transaksi setelah operasi berhasil
            conn.commit();
        } catch (SQLException e) {
            // Rollback jika terjadi kesalahan
            conn.rollback();
            throw e;
        } finally {
            // Kembalikan ke mode auto-commit
            conn.setAutoCommit(true);
        }
    }
}
    
    
    @Override
    public List<ModelGoodsReceipts> filterGoodsReceipts(LocalDate fromDate, LocalDate toDate, String searchTerm) throws SQLException {
        List<ModelGoodsReceipts> filteredList = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT gr.receipt_id, gr.receipt_number, v.name AS vendor_name, " +
            "p.product_name, ri.quantity, gr.receipt_date, gr.note " +
            "FROM goods_receipts gr " +
            "JOIN vendors v ON gr.vendor_id = v.vendor_id " +
            "JOIN receipt_items ri ON gr.receipt_id = ri.receipt_id " +
            "JOIN products p ON ri.product_id = p.product_id " +
            "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (fromDate != null) {
            queryBuilder.append("AND gr.receipt_date >= ? ");
            params.add(java.sql.Date.valueOf(fromDate));
        }
        if (toDate != null) {
            queryBuilder.append("AND gr.receipt_date <= ? ");
            params.add(java.sql.Date.valueOf(toDate));
        }
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            queryBuilder.append("AND (gr.receipt_number LIKE ? OR v.name ILIKE ? OR p.product_name ILIKE ? OR gr.note LIKE ?) ");
            String likePattern = "%" + searchTerm.trim() + "%";
            params.add(likePattern);
            params.add(likePattern);
            params.add(likePattern);
            params.add(likePattern);
        }

        queryBuilder.append("ORDER BY gr.receipt_date DESC");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ModelGoodsReceipts receipt = new ModelGoodsReceipts.Builder()
                            .receiptId(rs.getInt("receipt_id"))
                            .receiptNumber(rs.getString("receipt_number"))
                            .vendorName(rs.getString("vendor_name"))
                            .productName(rs.getString("product_name"))
                            .quantity(rs.getInt("quantity"))
                            .receiptDate(rs.getDate("receipt_date").toLocalDate())
                            .note(rs.getString("note"))
                            .build();
                            
                            
//                    ModelGoodsReceipts receipt = new ModelGoodsReceipts
//                        rs.getInt("receipt_id"),
//                        rs.getString("receipt_number"),
//                        rs.getString("vendor_name"),
//                        rs.getString("product_name"),
//                        rs.getInt("quantity"),
//                        rs.getDate("receipt_date").toLocalDate(),
//                        rs.getString("note")
//                    );
                    filteredList.add(receipt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }

        return filteredList;
    }
     
     

}
