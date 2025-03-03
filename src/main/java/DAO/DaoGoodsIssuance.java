/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conn.ConnectionDb;
import Model.ModelGoodsIssuance;
import Model.ModelIssuanceItem;
import Model.ModelProduct;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.sql.DataSource;

/**
 *
 * @author athif
 */
public class DaoGoodsIssuance implements ImplementGoodsIssuance{
    
    private final DataSource dataSource;
    private static final String PREFIX = "IN";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmm");

    public DaoGoodsIssuance() {
        this.dataSource = ConnectionDb.getInstance().getDataSource();
    }
    
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    // Insert new goods issuance
 
    @Override
    public int insertGoodsIssuance(ModelGoodsIssuance issuance) throws SQLException {
    String sql = "INSERT INTO goods_issuance (issuance_number, issuance_date, total_price, note) VALUES (?, ?, ?, ?) RETURNING issuance_id";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, issuance.getIssuanceNumber());
        stmt.setDate(2, java.sql.Date.valueOf(issuance.getIssuanceDate()));
        stmt.setBigDecimal(3, issuance.getTotalPrice()); // Ganti total_amount menjadi total_price
        stmt.setString(4, issuance.getNote());

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("issuance_id");
        }
    }
    return 0;
}

    // Insert issuance items
    @Override
    public void insertIssuanceItem(ModelIssuanceItem item) throws SQLException {
    String sql = "INSERT INTO issuance_items (issuance_id, product_id, quantity, total_price) VALUES (?, ?, ?, ?)";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
        stmt.setInt(1, item.getIssuanceId());
        stmt.setInt(2, item.getProductId());
        stmt.setInt(3, item.getQuantity());
        
        // Hitung total_price dengan mengalikan harga satuan dengan kuantitas
        BigDecimal totalPrice = item.getSalePrice().multiply(new BigDecimal(item.getQuantity()));
        stmt.setBigDecimal(4, totalPrice);

        stmt.executeUpdate();
    }
}
    
    @Override
    public List<ModelGoodsIssuance> getAllGoodsIssuance() throws SQLException {
    List<ModelGoodsIssuance> goodsIssuanceList = new ArrayList<>();
    String sql = "SELECT gi.issuance_id, gi.issuance_number, gi.issuance_date, gi.note, " +
                 "ii.product_id, ii.quantity, ii.total_price, p.product_name " +
                 "FROM goods_issuance gi " +
                 "JOIN issuance_items ii ON gi.issuance_id = ii.issuance_id " +
                 "JOIN products p ON ii.product_id = p.product_id";

    try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql))  {

        Map<Integer, ModelGoodsIssuance> issuanceMap = new HashMap<>(); // Untuk mengelompokkan pengeluaran berdasarkan issuance_id

        while (rs.next()) {
            int issuanceId = rs.getInt("issuance_id");
            String issuanceNumber = rs.getString("issuance_number");
            LocalDate issuanceDate = rs.getDate("issuance_date") != null ? rs.getDate("issuance_date").toLocalDate() : null;
            String note = rs.getString("note");

            // Jika pengeluaran dengan issuance_id ini belum ada, buat objek baru
            if (!issuanceMap.containsKey(issuanceId)) {
                ModelGoodsIssuance issuance = new ModelGoodsIssuance(
                    issuanceId,
                    issuanceNumber,
                    issuanceDate,
                    note, 
                    BigDecimal.ZERO, // Inisialisasi totalPrice dengan BigDecimal.ZERO
                    new ArrayList<>() // Daftar item pengeluaran kosong
                );
                issuanceMap.put(issuanceId, issuance);
                goodsIssuanceList.add(issuance);
            }

            // Ambil pengeluaran yang sesuai dengan issuance_id
            ModelGoodsIssuance issuance = issuanceMap.get(issuanceId);

            // Ambil data produk dari ResultSet
            int productId = rs.getInt("product_id");
            String productName = rs.getString("product_name");
            int quantity = rs.getInt("quantity");
            BigDecimal totalPrice = rs.getBigDecimal("total_price");

            // Buat objek ModelProduct dengan product_id dan nama produk
            ModelProduct product = new ModelProduct(productId, productName);

            // Buat objek item pengeluaran
            ModelIssuanceItem issuanceItem = new ModelIssuanceItem(
                issuanceId,
                productId,
                product, // Gunakan objek ModelProduct
                quantity,
                totalPrice
            );

            // Tambahkan item ke daftar item pada pengeluaran
            issuance.getIssuanceItems().add(issuanceItem);

            // Update totalPrice pada ModelGoodsIssuance
            BigDecimal currentTotalPrice = issuance.getTotalPrice().add(totalPrice);
            issuance.setTotalPrice(currentTotalPrice);
        }
    } catch (SQLException e) {
        System.out.println("Error executing SQL: " + e.getMessage());
        e.printStackTrace();
    }
    return goodsIssuanceList;
}

    
    @Override
    public void deleteGoodsIssuance(int issuanceId) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Menggunakan transaksi manual

            try {
                // Hapus item pengeluaran terlebih dahulu karena mereka memiliki referensi ke pengeluaran
                String deleteItemsSql = "DELETE FROM issuance_items WHERE issuance_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteItemsSql)) {
                    stmt.setInt(1, issuanceId);
                    stmt.executeUpdate();
                }

                // Hapus data pengeluaran
                String deleteIssuanceSql = "DELETE FROM goods_issuance WHERE issuance_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteIssuanceSql)) {
                    stmt.setInt(1, issuanceId);
                    stmt.executeUpdate();
                }

                conn.commit(); // Commit transaksi setelah semua operasi berhasil
            } catch (SQLException e) {
                conn.rollback(); // Rollback jika terjadi kesalahan
                throw e;
            } finally {
                conn.setAutoCommit(true); // Kembali ke mode auto-commit
            }
        }
    }
    
    @Override
    public ModelGoodsIssuance getGoodsIssuanceById(int issuanceId) throws SQLException {
    String sql = "SELECT gi.issuance_id, gi.issuance_number, gi.issuance_date, gi.note, ii.product_id, ii.quantity, ii.total_price " +
                 "FROM goods_issuance gi " +
                 "LEFT JOIN issuance_items ii ON gi.issuance_id = ii.issuance_id " +
                 "WHERE gi.issuance_id = ?";
    
    ModelGoodsIssuance issuance = null;
    List<ModelIssuanceItem> issuanceItems = new ArrayList<>();

    try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, issuanceId);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                if (issuance == null) {
                    // Initialize issuance object only once
                    issuance = new ModelGoodsIssuance(
                        rs.getInt("issuance_id"),
                        rs.getString("issuance_number"),
                        rs.getDate("issuance_date").toLocalDate(),
                        rs.getString("note"),
                        BigDecimal.ZERO, // Initialize total price, will be calculated later
                        issuanceItems
                    );
                }

                // Create and add each issuance item to the list
                ModelIssuanceItem item = new ModelIssuanceItem(
                    0,
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getBigDecimal("total_price"),
                    ""
                );
                issuanceItems.add(item);
            }

            if (issuance != null) {
                // Calculate total price for all items in this issuance
                BigDecimal totalPrice = issuanceItems.stream()
                        .map(ModelIssuanceItem::getSalePrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                issuance.setTotalPrice(totalPrice);
            }
        }
    }
    return issuance;
}

    @Override
    public String generateIssuanceNumber() throws SQLException {
        
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DATE_FORMATTER);
        String uuidPart = UUID.randomUUID().toString().substring(0, 4); // Menggunakan 8 karakter UUID
        
        return String.format("%s-%s-%s", PREFIX, datePart, uuidPart);
    }
    
    

    
}
