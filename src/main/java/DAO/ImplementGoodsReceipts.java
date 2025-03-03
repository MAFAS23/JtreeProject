/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.ModelGoodsReceipts;
import Model.ModelReceiptItems;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author athif
 */
public interface ImplementGoodsReceipts {
//     void insertGoodsReceipt(ModelGoodsReceipts goodsReceipt) throws SQLException ;
    int insertGoodsReceipt(ModelGoodsReceipts goodsReceipt) throws SQLException;
    void updateGoodsReceipt(ModelGoodsReceipts goodsReceipt) throws SQLException;
    void deleteGoodsReceipt(int receiptId) throws SQLException;
    List<ModelGoodsReceipts> getAllGoodsReceipts() throws SQLException;
    ModelGoodsReceipts getGoodsReceiptById(int receiptId) throws SQLException;
    String generateReceiptNumber() throws SQLException ;
    int getNextReceiptId() throws SQLException ;
    List<ModelGoodsReceipts> getGoodsReceiptsWithItems() throws SQLException;
//    void createSequenceTableIfNotExists() throws SQLException ;
//    void updateGoodsReceiptForEdit(ModelGoodsReceipts receipt, List<ModelReceiptItems> items) throws SQLException;
    void updateGoodsReceiptForEdit(ModelGoodsReceipts receipt, List<ModelReceiptItems> items, Map<Integer, Integer> stockChanges) throws SQLException ;
    void deleteGoodsReceiptForDel(int receiptId) throws SQLException ;
    List<ModelGoodsReceipts> filterGoodsReceipts(LocalDate fromDate, LocalDate toDate, String searchTerm)throws SQLException ;
    ModelGoodsReceipts getGoodsReceiptByIdForEdit(int receiptId) throws SQLException;
    
    
}
