/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author athif
 */
package Model;

public class ModelReceiptItems {
    private int itemId;
    private int receiptId;
    private int productId;
    private int quantity;
    private String note;

    // Constructor
    public ModelReceiptItems(int itemId, int receiptId, int productId, int quantity) {
        this.itemId = itemId;
        this.receiptId = receiptId;
        this.productId = productId;
        this.quantity = quantity;
//        this.note = note;
    }

    // Constructor tanpa itemId
    public ModelReceiptItems(int receiptId, int productId, int quantity) {
        this.receiptId = receiptId;
        this.productId = productId;
        this.quantity = quantity;
//        this.note = note;
    }

    // Getter dan Setter
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
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
        return "ModelReceiptItems{" +
                "itemId=" + itemId +
                ", receiptId=" + receiptId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", note='" + note + '\'' +
                '}';
    }
}
