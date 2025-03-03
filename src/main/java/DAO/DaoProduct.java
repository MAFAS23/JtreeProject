/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conn.ConnectionDb;
import Model.ModelProduct;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author athif
 */
public class DaoProduct implements ImplementProduct {

//    private final Connection conn;
     private final DataSource dataSource;

    public DaoProduct() {
        this.dataSource = ConnectionDb.getInstance().getDataSource();
        
    }
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void insertProduct(ModelProduct product) throws SQLException {
    String sql = "INSERT INTO products (folder_id, product_name, price) VALUES (?, ?, ?)";
    try (Connection conn = getConnection()) { // Gunakan try-with-resources untuk koneksi
        // Nonaktifkan auto-commit untuk memulai transaksi
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, product.getFolderId());
            stmt.setString(2, product.getProductName());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.executeUpdate();

            // Mendapatkan product_id yang dihasilkan oleh database
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setProductId(generatedKeys.getInt(1)); // Mengatur nilai product_id ke dalam objek ModelProduct
                }
            }

            // Commit transaksi setelah operasi berhasil
            conn.commit();
        } catch (SQLException e) {
            // Rollback transaksi jika terjadi kesalahan
            conn.rollback();
            throw e;
        } finally {
            // Kembalikan ke mode auto-commit
            conn.setAutoCommit(true);
        }
    }
}


    @Override
    public void updateProduct(ModelProduct product) throws SQLException {
    String selectSql = "SELECT product_name, price FROM products WHERE product_id = ? FOR UPDATE";
    String updateSql = "UPDATE products SET product_name = ?, price = ? WHERE product_id = ?";

    try (Connection conn = getConnection()) { // Menggunakan try-with-resources untuk koneksi
        // Nonaktifkan auto-commit untuk memulai transaksi
        conn.setAutoCommit(false);

        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, product.getProductId());
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("Data tidak ditemukan atau sudah dihapus.");
                }
            }

            // Lanjutkan dengan pembaruan
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, product.getProductName());
                updateStmt.setBigDecimal(2, product.getPrice());
                updateStmt.setInt(3, product.getProductId());
                updateStmt.executeUpdate();
            }

            // Commit transaksi setelah operasi berhasil
            conn.commit();

        } catch (SQLException e) {
            // Rollback transaksi jika terjadi kesalahan
            conn.rollback();
            throw e;
        } finally {
            // Kembalikan ke mode auto-commit
            conn.setAutoCommit(true);
        }
    }
}


    @Override
    public void deleteProduct(int productId) throws SQLException {
    String sql = "DELETE FROM products WHERE product_id = ?";

    try (Connection conn = getConnection()) { // Menggunakan try-with-resources untuk koneksi
        // Nonaktifkan auto-commit untuk memulai transaksi
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();

            // Commit transaksi setelah operasi berhasil
            conn.commit();
        } catch (SQLException e) {
            // Rollback transaksi jika terjadi kesalahan
            conn.rollback();
            throw e;
        } finally {
            // Kembalikan ke mode auto-commit
            conn.setAutoCommit(true);
        }
    }
}

    @Override
    public List<ModelProduct> getProductsByFolderId(int folderId) throws SQLException {
    List<ModelProduct> products = new ArrayList<>();
    String sql = "SELECT product_id, folder_id, product_name, price FROM products WHERE folder_id = ?";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, folderId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ModelProduct product = new ModelProduct(
                    rs.getInt("product_id"),
                    rs.getInt("folder_id"),
                    rs.getString("product_name"),
                    rs.getBigDecimal("price")
                );
                products.add(product);
            }
        }
    }
    return products;
    }

    @Override
    public int getNextProductId() throws SQLException {
    String sql = "SELECT COALESCE(MAX(product_id), 0) + 1 AS next_id FROM products";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            return rs.getInt("next_id");
        }
        throw new SQLException("Unable to generate next product ID");
    }
}
    
    @Override
    public List<ModelProduct> getAllProducts() throws SQLException {
    List<ModelProduct> products = new ArrayList<>();
    String sql = "SELECT * FROM products";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            ModelProduct product = new ModelProduct(
                rs.getInt("product_id"),
                rs.getInt("folder_id"),
                rs.getString("product_name"),
                rs.getBigDecimal("price")
            );
            products.add(product);
        }
    }
    return products;
}
    
    @Override
    public List<ModelProduct> getAllProductsForGoodsReceipt() throws SQLException {
    List<ModelProduct> products = new ArrayList<>();
    String sql = "SELECT product_id, folder_id, product_name, price FROM products";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            ModelProduct product = new ModelProduct(
                rs.getInt("product_id"),
                rs.getInt("folder_id"),
                rs.getString("product_name"),
                rs.getBigDecimal("price")
            );
            products.add(product);
        }
    }
    return products;
}
    
    @Override
    public List<ModelProduct> getAllProductsForGoodsIssuance() throws SQLException {
        List<ModelProduct> products = new ArrayList<>();
        String query = "SELECT product_id, product_name, stock_quantity, price FROM products WHERE stock_quantity > 0";

        try (Connection conn = getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                int stockQuantity = resultSet.getInt("stock_quantity");
                BigDecimal price = resultSet.getBigDecimal("price");

                ModelProduct product = new ModelProduct(productId, productName, stockQuantity, price);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new SQLException("Error fetching products for goods issuance: " + e.getMessage(), e);
        }

        return products;
    }
    
    @Override
     public List<String> getAllProductNames() {
        List<String> productNames = new ArrayList<>();
        String sql = "SELECT product_name FROM products";
        
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productNames.add(rs.getString("product_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return productNames;
    }

     
    @Override
    public List<String> getAllProductCodes() {
        List<String> productCodes = new ArrayList<>();
        String sql = "SELECT product_id FROM products";
        
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productCodes.add(String.valueOf(rs.getInt("product_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return productCodes;
    }
    
    
    @Override
    public void updateProductStock(int productId, int quantityChange) throws SQLException {
    String sql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
    
    try (Connection conn = getConnection()) { // Menggunakan try-with-resources untuk koneksi
        // Simpan status auto-commit sebelumnya dan nonaktifkan auto-commit untuk memulai transaksi
        boolean previousAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityChange);
            stmt.setInt(2, productId);
            int affectedRows = stmt.executeUpdate(); // Jumlah baris yang diperbarui

            // Commit transaksi setelah operasi berhasil
            conn.commit();
            
            System.out.println("Stok produk berhasil diperbarui. Jumlah baris yang diperbarui: " + affectedRows);
        } catch (SQLException e) {
            // Rollback transaksi jika terjadi kesalahan
            conn.rollback();
            throw e;
        } finally {
            // Kembalikan ke status auto-commit sebelumnya
            conn.setAutoCommit(previousAutoCommit);
        }
    }
}
    
    @Override
    public List<ModelProduct> searchProduct(String kueri) throws SQLException {
    List<ModelProduct> result = new ArrayList<>();
    String sql = "SELECT * FROM products WHERE CAST(product_id AS TEXT) ILIKE ? OR product_name ILIKE ?";
    
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        String wildcardKueri = "%" + kueri + "%";
        stmt.setString(1, wildcardKueri);
        stmt.setString(2, wildcardKueri);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ModelProduct product = new ModelProduct(
                    rs.getInt("product_id"),
                    rs.getInt("folder_id"),
                    rs.getString("product_name"),
                    rs.getBigDecimal("price")
                );
                result.add(product);
            }
        }
    }
    
    return result;
}
   
    
    @Override
    public synchronized void decreaseProductStock(int productId, int quantity) throws SQLException {
    String checkStockSql = "SELECT stock_quantity FROM products WHERE product_id = ?";
    String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";

    try (Connection conn = getConnection()) { // Menggunakan try-with-resources untuk koneksi
        // Nonaktifkan auto-commit untuk memulai transaksi
        conn.setAutoCommit(false);

        // Memeriksa jumlah stok saat ini
        try (PreparedStatement checkStmt = conn.prepareStatement(checkStockSql)) {
            checkStmt.setInt(1, productId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    int currentStock = rs.getInt("stock_quantity");

                    // Periksa apakah stok cukup untuk pengurangan
                    if (currentStock < quantity) {
                        throw new SQLException("Stok tidak cukup untuk produk dengan ID: " + productId);
                    }

                    // Lanjutkan pengurangan stok jika cukup
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateStockSql)) {
                        updateStmt.setInt(1, quantity);
                        updateStmt.setInt(2, productId);
                        updateStmt.executeUpdate();

                        // Commit transaksi setelah operasi berhasil
                        conn.commit();
                        System.out.println("Stok produk berhasil diperbarui. Jumlah baris yang diperbarui: 1");
                    }
                } else {
                    throw new SQLException("Produk dengan ID: " + productId + " tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            // Rollback transaksi jika terjadi kesalahan
            conn.rollback();
            throw e;
        } finally {
            // Kembalikan ke mode auto-commit
            conn.setAutoCommit(true);
        }
    }
}
    
    @Override
    public List<ModelProduct> searchProductsByName(String productName) throws SQLException {
    List<ModelProduct> productList = new ArrayList<>();
    String query = "SELECT * FROM products WHERE LOWER(product_name) LIKE ?";
    
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, "%" + productName.toLowerCase() + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("product_id");
            String name = rs.getString("product_name");
            // Sesuaikan jika ada field lain
            productList.add(new ModelProduct(id, 0, name, null)); // Sesuaikan konstruktor
        }
    }
    return productList;
}
    
    @Override
    public List<ModelProduct> selectProductsByName(String productName) throws SQLException {
    List<ModelProduct> productList = new ArrayList<>();
    String query = "SELECT * FROM products WHERE LOWER(product_name) LIKE ?";
    
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, "%" + productName.toLowerCase() + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("product_id");
            String name = rs.getString("product_name");
            int stockQuantity = rs.getInt("stock_quantity");
            BigDecimal price = rs.getBigDecimal("price");
            // Sesuaikan jika ada field lain
            productList.add(new ModelProduct(id, name,stockQuantity,price)); // Sesuaikan konstruktor
        }
    }
    return productList;
}
    
    public int getCurrentStock(int productId) throws SQLException {
        String sql = "SELECT stock_quantity FROM products WHERE product_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock_quantity");
                } else {
                    throw new SQLException("Produk dengan ID: " + productId + " tidak ditemukan.");
                }
            }
        }
    }
    
}
