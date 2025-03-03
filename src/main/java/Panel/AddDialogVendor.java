/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

/**
 *
 * @author athif
 */
import Panel.Dialog.BaseDialog;
import Model.ModelVendor;
import java.awt.Frame;
import javax.swing.JOptionPane;

public class AddDialogVendor extends BaseDialog {
    private final int nextVendorId;
    private boolean isConfirmed = false;

    public AddDialogVendor(Frame owner, int nextVendorId) {
        super(owner, "Add Vendor");
        this.nextVendorId = nextVendorId;

//        addField("Vendor ID:", "vendorId");
        addField("Vendor Name:", "vendorName");
        addField("Address:", "vendorAddress");

//        setFieldValue("vendorId", String.valueOf(nextVendorId));
//        fields.get("vendorId").setEditable(false);

        finalizeDialog();
    }

    @Override
    protected void onOkButtonClick() {
        if (validateInput()) {
            isConfirmed = true;
            System.out.println("Creating ModelVendor with ID: " + nextVendorId);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        return !getFieldValue("vendorName").isEmpty() &&
               !getFieldValue("vendorAddress").isEmpty() ;
    }

    private boolean isValidEmail(String email) {
        // Simple email validation, you might want to use a more robust method
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public ModelVendor getNewVendor() {
        if (!isConfirmed) {
            return null;
        }
        try {
//            int vendorId = Integer.parseInt(getFieldValue("vendorId"));
            String vendorName = getFieldValue("vendorName");
            String vendorAddress = getFieldValue("vendorAddress");
            return new ModelVendor(vendorName, vendorAddress);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing input: " + e.getMessage());
            return null;
        }
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}
