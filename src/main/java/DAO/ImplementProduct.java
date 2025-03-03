/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author athif
 */
import Model.ModelProduct;
import java.sql.SQLException;
import java.util.List;

public interface ImplementProduct {
    void insertProduct(ModelProduct product) throws SQLException;
    void updateProduct(ModelProduct product) throws SQLException;
    void deleteProduct(int productId) throws SQLException;
    List<ModelProduct> getProductsByFolderId(int folderId) throws SQLException;
    int getNextProductId() throws SQLException ;
    List<ModelProduct> getAllProducts() throws SQLException;
    List<ModelProduct> getAllProductsForGoodsReceipt() throws SQLException;
    List<String> getAllProductNames() ;
    List<String> getAllProductCodes();
    void updateProductStock(int productId, int quantityChange) throws SQLException;
    List<ModelProduct> searchProduct(String kueri) throws SQLException;
    List<ModelProduct> getAllProductsForGoodsIssuance() throws SQLException;
    void decreaseProductStock(int productId, int quantity) throws SQLException;
    List<ModelProduct> searchProductsByName(String productName) throws SQLException ;
    List<ModelProduct> selectProductsByName(String productName) throws SQLException;
}
