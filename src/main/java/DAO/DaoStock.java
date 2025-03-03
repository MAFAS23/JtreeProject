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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class DaoStock implements ImplementStock{
//    private final Connection conn;

   private final DataSource dataSource;


    public DaoStock() {
        this.dataSource = ConnectionDb.getInstance().getDataSource();
        
    }
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    
    @Override
    public List<Object[]> getStockData(String searchTerm, String category, boolean onlyInStock) throws SQLException {
        List<Object[]> stockData = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "p.product_id, " +
            "f.name as category, " +
            "p.product_name, " +
            "p.stock_quantity, " +
            "CASE WHEN p.stock_quantity > 0 THEN 'In Stock' ELSE 'Out of Stock' END as stock_status " +
            "FROM products p " +
            "JOIN folders f ON p.folder_id = f.folders_id " +
            "WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql.append("AND (LOWER(p.product_name) LIKE LOWER(?) OR CAST(p.product_id AS TEXT) LIKE ?) ");
            params.add("%" + searchTerm + "%");
            params.add("%" + searchTerm + "%");
        }

        if (category != null && !category.equals("All Categories")) {
            sql.append("AND f.name = ? ");
            params.add(category);
        }

        if (onlyInStock) {
            sql.append("AND p.stock_quantity > 0 ");
        }

        sql.append("ORDER BY p.product_id");

        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stockData.add(new Object[]{
                            rs.getInt("product_id"),
                            rs.getString("category"),
                            rs.getString("product_name"),
                            rs.getInt("stock_quantity"),
                            rs.getString("stock_status")
                    });
                }
            }
        }
        System.out.println("Retrieved " + stockData.size() + " rows from database");
        return stockData;
    }

    public List<String> getCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT name FROM folders ORDER BY name";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        }
        return categories;
    }
    


}