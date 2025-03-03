/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
//
//public class ModelGoodsReceipts {
//    private int receiptId;
//    private String receiptNumber;
//    private int vendorId;
//    private String vendorName;
//    private LocalDate receiptDate;
//    private String note;
//    private String productName;
//    private int productId;      // Tambahan
//    private int quantity;       // Tambahan
//    private String itemNote;
//    private List<ModelReceiptItems> receiptItems;
//
//    
//    public ModelGoodsReceipts(){
//        
//    }
//    
//    // Constructor pertama (untuk kasus tanpa produk dan quantity)
//    public ModelGoodsReceipts(int receiptId, String receiptNumber, int vendorId, String vendorName, LocalDate receiptDate, String note) {
//        this.receiptId = receiptId;
//        this.receiptNumber = receiptNumber;
//        this.vendorId = vendorId;
//        this.vendorName = vendorName;
//        this.receiptDate = receiptDate;
//        this.note = note;
//        this.receiptItems = new ArrayList<>(); // Inisialisasi dengan ArrayList kosong
//    }
//    public ModelGoodsReceipts( String receiptNumber, int vendorId, String vendorName, LocalDate receiptDate, String note) {
//        this.receiptNumber = receiptNumber;
//        this.vendorId = vendorId;
//        this.vendorName = vendorName;
//        this.receiptDate = receiptDate;
//        this.note = note;
//        this.receiptItems = new ArrayList<>(); // Inisialisasi dengan ArrayList kosong
//    }
//    
//    public ModelGoodsReceipts(int receiptId, String receiptNumber, String vendorName, 
//                              String productName, int quantity, LocalDate receiptDate, String note) {
//        this.receiptId = receiptId;
//        this.receiptNumber = receiptNumber;
//        this.vendorName = vendorName;
//        this.productName = productName;
//        this.quantity = quantity;
//        this.receiptDate = receiptDate;
//        this.note = note;
//    }
//
//    // Constructor kedua (untuk kasus dengan receiptItems)
//    public ModelGoodsReceipts(int receiptId, String receiptNumber, int vendorId, String vendorName, LocalDate receiptDate, String note, List<ModelReceiptItems> receiptItems) {
//        this.receiptId = receiptId;
//        this.receiptNumber = receiptNumber;
//        this.vendorId = vendorId;
//        this.vendorName = vendorName;
//        this.receiptDate = receiptDate;
//        this.note = note;
//        this.receiptItems = receiptItems != null ? receiptItems : new ArrayList<>(); // Inisialisasi dengan ArrayList kosong jika null
//    }
//
//    // Constructor ketiga (untuk kasus dengan produk dan quantity)
//    public ModelGoodsReceipts(int receiptId, String receiptNumber, int vendorId, String vendorName, LocalDate receiptDate, String note, String productName, int productId, int quantity, String itemNote) {
//        this.receiptId = receiptId;
//        this.receiptNumber = receiptNumber;
//        this.vendorId = vendorId;
//        this.vendorName = vendorName;
//        this.receiptDate = receiptDate;
//        this.note = note;
//        this.productName = productName;
//        this.productId = productId;
//        this.quantity = quantity;
//        this.itemNote = itemNote;
//        this.receiptItems = new ArrayList<>(); // Inisialisasi dengan ArrayList kosong
//    }
//
//    // Getter dan Setter untuk field tambahan
//    public int getProductId() {
//        return productId;
//    }
//
//    public void setProductId(int productId) {
//        this.productId = productId;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getItemNote() {
//        return itemNote;
//    }
//
//    public void setItemNote(String itemNote) {
//        this.itemNote = itemNote;
//    }
//
//    // Getter dan Setter lainnya
//    public String getVendorName() {
//        return vendorName;
//    }
//
//    public void setVendorName(String vendorName) {
//        this.vendorName = vendorName;
//    }
//
//    public int getReceiptId() {
//        return receiptId;
//    }
//
//    public void setReceiptId(int receiptId) {
//        this.receiptId = receiptId;
//    }
//
//    public String getReceiptNumber() {
//        return receiptNumber;
//    }
//
//    public void setReceiptNumber(String receiptNumber) {
//        this.receiptNumber = receiptNumber;
//    }
//
//    public int getVendorId() {
//        return vendorId;
//    }
//
//    public void setVendorId(int vendorId) {
//        this.vendorId = vendorId;
//    }
//
//    public LocalDate getReceiptDate() {
//        return receiptDate;
//    }
//
//    public void setReceiptDate(LocalDate receiptDate) {
//        this.receiptDate = receiptDate;
//    }
//
//    public String getNote() {
//        return note;
//    }
//
//    public void setNote(String note) {
//        this.note = note;
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//    // Getter dan Setter untuk receiptItems
//    public List<ModelReceiptItems> getReceiptItems() {
//        return receiptItems;
//    }
//
//    public void setReceiptItems(List<ModelReceiptItems> receiptItems) {
//        this.receiptItems = receiptItems != null ? receiptItems : new ArrayList<>(); // Hindari null dengan inisialisasi
//    }
//
//    @Override
//    public String toString() {
//        return "ModelGoodsReceipts{" +
//                "receiptId=" + receiptId +
//                ", receiptNumber='" + receiptNumber + '\'' +
//                ", vendorId=" + vendorId +
//                ", vendorName='" + vendorName + '\'' +
//                ", receiptDate=" + receiptDate +
//                ", note='" + note + '\'' +
//                ", productName='" + productName + '\'' +
//                ", productId=" + productId +
//                ", quantity=" + quantity +
//                ", itemNote='" + itemNote + '\'' +
//                '}';
//    }
//}

public class ModelGoodsReceipts {
    private int receiptId;
    private String receiptNumber;
    private int vendorId;
    private String vendorName;
    private LocalDate receiptDate;
    private String note;
    private String productName;
    private int productId;
    private int quantity;
    private String itemNote;
    private List<ModelReceiptItems> receiptItems;

    // Private constructor to enforce usage of Builder
    private ModelGoodsReceipts(Builder builder) {
        this.receiptId = builder.receiptId;
        this.receiptNumber = builder.receiptNumber;
        this.vendorId = builder.vendorId;
        this.vendorName = builder.vendorName;
        this.receiptDate = builder.receiptDate;
        this.note = builder.note;
        this.productName = builder.productName;
        this.productId = builder.productId;
        this.quantity = builder.quantity;
        this.itemNote = builder.itemNote;
        this.receiptItems = builder.receiptItems != null ? builder.receiptItems : new ArrayList<>();
    }

    // Getters and Setters...

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }

    public List<ModelReceiptItems> getReceiptItems() {
        return receiptItems;
    }

    public void setReceiptItems(List<ModelReceiptItems> receiptItems) {
        this.receiptItems = receiptItems;
    }
    

    public static class Builder {
        private int receiptId;
        private String receiptNumber;
        private int vendorId;
        private String vendorName;
        private LocalDate receiptDate;
        private String note;
        private String productName;
        private int productId;
        private int quantity;
        private String itemNote;
        private List<ModelReceiptItems> receiptItems;

        public Builder receiptId(int receiptId) {
            this.receiptId = receiptId;
            return this;
        }

        public Builder receiptNumber(String receiptNumber) {
            this.receiptNumber = receiptNumber;
            return this;
        }

        public Builder vendorId(int vendorId) {
            this.vendorId = vendorId;
            return this;
        }

        public Builder vendorName(String vendorName) {
            this.vendorName = vendorName;
            return this;
        }

        public Builder receiptDate(LocalDate receiptDate) {
            this.receiptDate = receiptDate;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder productId(int productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder itemNote(String itemNote) {
            this.itemNote = itemNote;
            return this;
        }

        public Builder receiptItems(List<ModelReceiptItems> receiptItems) {
            this.receiptItems = receiptItems;
            return this;
        }

        public ModelGoodsReceipts build() {
            return new ModelGoodsReceipts(this);
        }
    }
}
