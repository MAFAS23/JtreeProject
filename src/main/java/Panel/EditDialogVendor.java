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

public class EditDialogVendor extends BaseDialog {
    private final ModelVendor vendor;
    private boolean isConfirmed = false;

    public EditDialogVendor(Frame owner, ModelVendor vendor) {
        super(owner, "Edit Vendor");
        this.vendor = vendor;

        addField("Vendor ID:", "vendorId");
        addField("Name:", "name");
        addField("Address:", "address");

        setFieldValue("vendorId", String.valueOf(vendor.getVendorId()));
        setFieldValue("name", vendor.getName());
        setFieldValue("address", vendor.getAddress());

        fields.get("vendorId").setEditable(false);

        finalizeDialog();
    }

    @Override
    protected void onOkButtonClick() {
        if (validateInput()) {
            updateVendor();
            isConfirmed = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        return !getFieldValue("name").isEmpty() &&
               !getFieldValue("address").isEmpty();
    }

    private void updateVendor() {
        vendor.setName(getFieldValue("name"));
        vendor.setAddress(getFieldValue("address"));
    }

    public ModelVendor getUpdatedVendor() {
        return isConfirmed ? vendor : null;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}
