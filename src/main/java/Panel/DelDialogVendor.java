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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.JTextField;

public class DelDialogVendor extends BaseDialog {
    private final ModelVendor vendor;
    private boolean isConfirmed = false;

    public DelDialogVendor(Frame owner, ModelVendor vendor) {
        super(owner, "Delete Vendor");
        this.vendor = vendor;

        addField("Vendor ID:", "vendorId");
        addField("Name:", "name");
        addField("Address:", "address");

        setFieldValue("vendorId", String.valueOf(vendor.getVendorId()));
        setFieldValue("name", vendor.getName());
        setFieldValue("address", vendor.getAddress());

        for (JTextField field : fields.values()) {
            field.setEditable(false);
        }

        JLabel confirmationLabel = new JLabel("Are you sure you want to delete this vendor?", SwingConstants.CENTER);
        contentPanel.add(confirmationLabel, BorderLayout.NORTH);

        okButton.setText("Delete");

        finalizeDialog();
    }

    @Override
    protected void onOkButtonClick() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this vendor?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            isConfirmed = true;
            dispose();
        }
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}