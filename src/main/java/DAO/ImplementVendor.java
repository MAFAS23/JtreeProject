/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.ModelVendor;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author athif
 */
public interface ImplementVendor {
    void insertVendor(ModelVendor vendor) throws SQLException;
    void updateVendor(ModelVendor vendor) throws SQLException;
    void deleteVendor(int vendorId) throws SQLException;
    List<ModelVendor> getAllVendors() throws SQLException;
    int getNextVendorId() throws SQLException ;
    List<String> getAllVendorNames() throws SQLException;
    int getVendorIdByName(String vendorName);
    List<ModelVendor> searchVendorByName(String name) throws SQLException;
    List<ModelVendor> searchVendorByName2(String vendorName) throws SQLException ;
    String getVendorNameById(int vendorId) throws SQLException;
}
