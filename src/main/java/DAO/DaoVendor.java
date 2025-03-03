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
import Model.ModelProduct;
import Model.ModelVendor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class DaoVendor implements ImplementVendor{

//    private final Connection conn;
  private final DataSource dataSource;

    public DaoVendor() {
        this.dataSource = ConnectionDb.getInstance().getDataSource();
        
    }
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void insertVendor(ModelVendor vendor) throws SQLException {
    String sql = "INSERT INTO vendors (name, address) VALUES (?, ?)";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, vendor.getName());
        stmt.setString(2, vendor.getAddress());
        stmt.executeUpdate();

        // Mendapatkan dan mengatur vendor_id yang dihasilkan secara otomatis
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                vendor.setVendorId(generatedKeys.getInt(1));
            }
        }
    }
}

    @Override
    public void updateVendor(ModelVendor vendor) throws SQLException {
        String sql = "UPDATE vendors SET name = ?, address = ? WHERE vendor_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vendor.getName());
            stmt.setString(2, vendor.getAddress());
            stmt.setInt(3, vendor.getVendorId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteVendor(int vendorId) throws SQLException {
        String sql = "DELETE FROM vendors WHERE vendor_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vendorId);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<ModelVendor> getAllVendors() throws SQLException {
        List<ModelVendor> vendors = new ArrayList<>();
        String sql = "SELECT * FROM vendors";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ModelVendor vendor = new ModelVendor(
                    rs.getInt("vendor_id"),
                    rs.getString("name"),
                    rs.getString("address")
                );
                vendors.add(vendor);
            }
        }
        return vendors;
    }
    
    @Override
    public List<String> getAllVendorNames() throws SQLException {
    List<String> vendorNames = new ArrayList<>();
    List<ModelVendor> vendors = getAllVendors(); // Panggil metode getAllVendors untuk mendapatkan semua vendor

    for (ModelVendor vendor : vendors) {
        vendorNames.add(vendor.getName()); // Ambil nama vendor dari setiap ModelVendor dan tambahkan ke daftar vendorNames
    }

    return vendorNames;
}
    
    @Override
    public int getNextVendorId() throws SQLException {
        String query = "SELECT MAX(vendor_id) FROM vendors";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return maxId + 1;
            } else {
                return 1; // Jika tabel kosong, mulai dari 1
            }
        }
    }
    
    @Override
    public int getVendorIdByName(String vendorName) {
    String sql = "SELECT vendor_id FROM vendors WHERE name = ?";
    
    // Mendapatkan koneksi dari DataSource
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, vendorName);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("vendor_id");
            } else {
                System.err.println("Vendor not found for name: " + vendorName);
                return -1; // Mengembalikan -1 jika vendor tidak ditemukan
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return -1; // Mengembalikan -1 jika terjadi kesalahan
    }
}
    
  @Override
    public String getVendorNameById(int vendorId) throws SQLException {
    String sql = "SELECT name FROM vendors WHERE vendor_id = ?";
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, vendorId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("name");
            }
        }
    }
    throw new SQLException("Vendor with ID " + vendorId + " not found.");
}
    
    
    @Override
    public List<ModelVendor> searchVendorByName(String name) throws SQLException {
    String sql = "SELECT * FROM vendors WHERE LOWER(name) LIKE ?";
    List<ModelVendor> vendors = new ArrayList<>();

    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, "%" + name.toLowerCase() + "%");
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ModelVendor vendor = new ModelVendor();
                vendor.setVendorId(rs.getInt("vendor_id"));
                vendor.setName(rs.getString("name"));
                vendor.setAddress(rs.getString("address"));
                vendors.add(vendor);
            }
        }
    }
    return vendors;
}
    
    @Override
    public List<ModelVendor> searchVendorByName2(String vendorName) throws SQLException {
    List<ModelVendor> vendorList = new ArrayList<>();
    String query = "SELECT * FROM vendors WHERE LOWER(name) LIKE ?";
    
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, "%" + vendorName.toLowerCase() + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            int id = rs.getInt("vendor_id");
            String name = rs.getString("name");
            // Sesuaikan jika ada field lain
            vendorList.add(new ModelVendor(id,name, null)); // Sesuaikan konstruktor
        }
    }
    return vendorList;
}
}