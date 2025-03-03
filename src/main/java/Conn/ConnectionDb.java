package Conn;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

//
///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//
///**
// *
// * @author athif
// */
//public class ConnectionDb {
//    
//    private final String dbname = "Jtree_DB";
//    private final String user = "postgres";
//    private final String pass = "123456";
//
//    public Connection connect_to_db() {
//        Connection conn = null;
//        try {
//            // Mendaftarkan driver PostgreSQL
//            Class.forName("org.postgresql.Driver");
//            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
//            if (conn != null) {
//                System.out.println("Connection established");
//            } else {
//                System.out.println("Connection failed");
//            }
//        } catch (ClassNotFoundException e) {
//            System.out.println("PostgreSQL JDBC Driver not found. Include it in your library path.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return conn;
//}
//    
//}

public class ConnectionDb {
    private static HikariDataSource dataSource;

    private ConnectionDb() {
        // Konfigurasi HikariCP
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/coba");
        config.setUsername("postgres");
        config.setPassword("postgres");

        // Mengatur parameter pool
        config.setMinimumIdle(5); // Jumlah minimum koneksi yang akan tetap idle dalam pool
        config.setMaximumPoolSize(10); // Jumlah maksimum koneksi yang dapat ada dalam pool
        config.setIdleTimeout(1200); // Waktu (ms) untuk idle koneksi sebelum ditutup

        dataSource = new HikariDataSource(config);
        System.out.println("conn established");
    }

    // Static inner class untuk singleton
    private static class Holder {
        private static final ConnectionDb INSTANCE = new ConnectionDb();
    }

    public static ConnectionDb getInstance() {
        return Holder.INSTANCE;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}


