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
import java.time.LocalDate;
import java.util.List;

public class ModelGoodsIssuance {

    private int issuanceId;
    private String issuanceNumber;
    private LocalDate issuanceDate;
    private String note;
    private BigDecimal totalPrice;
    private List<ModelIssuanceItem> issuanceItems;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public ModelGoodsIssuance(){
        
    }
    
    public ModelGoodsIssuance(int issuanceId, String issuanceNumber, LocalDate issuanceDate, String note, BigDecimal totalPrice, List<ModelIssuanceItem> issuanceItems) {
    this.issuanceId = issuanceId;
    this.issuanceNumber = issuanceNumber;
    this.issuanceDate = issuanceDate;
    this.note = note;
    this.totalPrice = totalPrice;
    this.issuanceItems = issuanceItems;
}
    // Constructor tanpa ID (untuk saat membuat record baru)
    public ModelGoodsIssuance(String issuanceNumber, LocalDate issuanceDate, String recipient, String note) {
        this.issuanceNumber = issuanceNumber;
        this.issuanceDate = issuanceDate;
        this.note = note;
    }
    public ModelGoodsIssuance(int issuanceId,String issuanceNumber, LocalDate issuanceDate, BigDecimal totalPrice, String note) {
        this.issuanceId = issuanceId;
        this.issuanceNumber = issuanceNumber;
        this.issuanceDate = issuanceDate;
        this.totalPrice = totalPrice;
        this.note = note;
    }

    // Constructor dengan ID (untuk saat mengedit record)
    public ModelGoodsIssuance(int issuanceId, String issuanceNumber, LocalDate issuanceDate, String recipient, String note, List<ModelIssuanceItem> issuanceItems) {
        this.issuanceId = issuanceId;
        this.issuanceNumber = issuanceNumber;
        this.issuanceDate = issuanceDate;
        this.note = note;
        this.issuanceItems = issuanceItems;
    }
    


    // Getters dan Setters
    public int getIssuanceId() {
        return issuanceId;
    }

    public void setIssuanceId(int issuanceId) {
        this.issuanceId = issuanceId;
    }

    public String getIssuanceNumber() {
        return issuanceNumber;
    }

    public void setIssuanceNumber(String issuanceNumber) {
        this.issuanceNumber = issuanceNumber;
    }

    public LocalDate getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(LocalDate issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<ModelIssuanceItem> getIssuanceItems() {
        return issuanceItems;
    }

    public void setIssuanceItems(List<ModelIssuanceItem> issuanceItems) {
        this.issuanceItems = issuanceItems;
    }

    @Override
    public String toString() {
        return "ModelGoodsIssuance{" +
                "issuanceId=" + issuanceId +
                ", issuanceNumber='" + issuanceNumber + '\'' +
                ", issuanceDate=" + issuanceDate +
                ", note='" + note + '\'' +
                ", issuanceItems=" + issuanceItems +
                '}';
    }
}

