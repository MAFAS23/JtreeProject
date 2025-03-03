/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author athif
 */


import java.math.BigDecimal;

public class ModelProduct {
    private int productId;
    private int folderId;
    private String productName;
    private BigDecimal price;
    private int stockQuantity;


    public ModelProduct(int productId, int folderId, String productName, BigDecimal price) {
        this.productId = productId;
        this.folderId = folderId;
        this.productName = productName;
        this.price = price;
    }
    
    public ModelProduct(int folderId, String productName, BigDecimal price) {
//        this.productId = productId;
        this.folderId = folderId;
        this.productName = productName;
        this.price = price;
    }
    
    public ModelProduct(int productId,String productName, int stockQuantity, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.stockQuantity = stockQuantity;
        this.price = price;
    }
    public ModelProduct(int productId, String productName) {
        this.productId = productId;
        this.productName = productName;
        // Inisialisasi properti lainnya...
    }

    // Getter dan Setter

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
//        return price;
         return (price != null) ? price : BigDecimal.ZERO;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
