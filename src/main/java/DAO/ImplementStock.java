/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author athif
 */
public interface ImplementStock {
//    List<Object[]> getStockData() throws SQLException;
    List<Object[]> getStockData(String searchTerm, String category, boolean onlyInStock) throws SQLException ;
    
}
