/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author athif
 */
public class ModelVendor {

    private int vendorId;
    private String name;
    private String address;
    
    public ModelVendor() {
    }

    public ModelVendor(int vendorId, String name, String address) {
        this.vendorId = vendorId;
        this.name = name;
        this.address = address;
    }
    public ModelVendor(int vendorId, String name) {
        this.vendorId = vendorId;
        this.name = name;
    }

    public ModelVendor(String name,String address) {
        this.name = name;
        this.address =address;
    }
    
    // Getter dan Setter
    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
