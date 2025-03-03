/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;

/**
 *
 * @author athif
 */

public class ModelIssuanceItem {

    private int issuanceId;  // ID pengeluaran yang terkait
    private int issuanceItemId;
    private int productId;
    private int quantity;
    private BigDecimal salePrice;  
    private String note;
    private BigDecimal totalPrice;
    private ModelProduct productName;

    public ModelProduct getProductName() {
        return productName;
    }

    public void setProductName(ModelProduct product) {
        this.productName = product;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    
    
    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }
    
    
   

    // Constructor tanpa ID (untuk saat membuat record baru)
    
    
       
    public ModelIssuanceItem(int issuanceId,int productId, ModelProduct productName, int quantity, BigDecimal totalPrice) {
        this.issuanceId = issuanceId;
        this.productId = productId;
        this.productName=productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    
    
    public ModelIssuanceItem(int issuanceId, int productId, int quantity, BigDecimal salePrice, String note) {
        this.issuanceId = issuanceId;
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.note = note;
    }

    // Constructor dengan ID (untuk saat mengedit record)
    public ModelIssuanceItem(int issuanceItemId, int issuanceId, int productId, int quantity,  BigDecimal salePrice, String note) {
        this.issuanceItemId = issuanceItemId;
        this.issuanceId = issuanceId;
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.note = note;
    }

    // Getters dan Setters
    public int getIssuanceItemId() {
        return issuanceItemId;
    }

    public void setIssuanceItemId(int issuanceItemId) {
        this.issuanceItemId = issuanceItemId;
    }

    public int getIssuanceId() {
        return issuanceId;
    }

    public void setIssuanceId(int issuanceId) {
        this.issuanceId = issuanceId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

   

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ModelIssuanceItem{" +
                "issuanceItemId=" + issuanceItemId +
                ", issuanceId=" + issuanceId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", salePrice=" + salePrice +
                ", note='" + note + '\'' +
                '}';
    }
}

