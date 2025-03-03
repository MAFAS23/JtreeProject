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
import Utilities.DialogUtils;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class DelDialog extends BaseDialog {
    private boolean isConfirmed = false;

    public DelDialog(Frame parent, int productId, String productName, BigDecimal price) {
        super(parent, "Delete Product");
        initDeleteComponents(productId, productName, price);
    }

    private void initDeleteComponents(int productId, String productName, BigDecimal price) {
        addField("Product ID:", "productId");
        addField("Product Name:", "productName");
        addField("Price:", "price");

        setFieldValue("productId", String.valueOf(productId));
        setFieldValue("productName", productName);
//        setFieldValue("price", price.toString());
        setFieldValue("price", DialogUtils.formatNumberWithThousandsSeparator(price.toString()));

        for (JTextField field : fields.values()) {
            field.setEditable(false);
        }

        JLabel confirmationLabel = new JLabel("Are you sure you want to delete this product?", SwingConstants.CENTER);
        contentPanel.add(confirmationLabel, BorderLayout.NORTH);

        okButton.setText("Delete");

        finalizeDialog();
    }

    @Override
    protected void onOkButtonClick() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this product?",
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