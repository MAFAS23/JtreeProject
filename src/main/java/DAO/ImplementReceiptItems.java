/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import Model.ModelReceiptItems;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author athif
 */
public interface ImplementReceiptItems {
    void insertReceiptItem(ModelReceiptItems receiptItem) throws SQLException;
    void updateReceiptItem(ModelReceiptItems receiptItem) throws SQLException;
    void deleteReceiptItem(int itemId) throws SQLException;
    List<ModelReceiptItems> getReceiptItemsByReceiptId(int receiptId) throws SQLException;
    List<ModelReceiptItems> getAllReceiptItems() throws SQLException;
}
