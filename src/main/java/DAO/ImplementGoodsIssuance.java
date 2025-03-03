/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.ModelGoodsIssuance;
import Model.ModelIssuanceItem;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author athif
 */
public interface ImplementGoodsIssuance {
    int insertGoodsIssuance(ModelGoodsIssuance issuance) throws SQLException;
    void insertIssuanceItem(ModelIssuanceItem item) throws SQLException ;
//    List<Object[]> getAllGoodsIssuance() throws SQLException;
    List<ModelGoodsIssuance> getAllGoodsIssuance() throws SQLException ;
    String generateIssuanceNumber() throws SQLException;
    void deleteGoodsIssuance(int issuanceId) throws SQLException;
    ModelGoodsIssuance getGoodsIssuanceById(int issuanceId) throws SQLException;
    
}
