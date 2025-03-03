/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel.Dialog;

/**
 *
 * @author athif
 */
import Panel.Dialog.BaseDialog;
import Model.ModelProduct;
import Utilities.DialogUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

public class EditDialog extends BaseDialog {
    private final ModelProduct product;
    private boolean isConfirmed = false;

    public EditDialog(Frame parent, ModelProduct product) {
        super(parent, "Edit Product");
        this.product = product;
        initEditComponents();
    }

    private void initEditComponents() {
        addField("Product ID:", "productId");
        addField("Product Name:", "productName");
        addField("Price:", "price");

        setFieldValue("productId", String.valueOf(product.getProductId()));
        setFieldValue("productName", product.getProductName());
//        setFieldValue("price", product.getPrice().toString());
        setFieldValue("price", DialogUtils.formatNumberWithThousandsSeparator(product.getPrice().toString()));
        
        // Menambahkan KeyListener untuk memastikan format harga saat pengguna mengetik
        JTextField priceField = fields.get("price");
        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Memeriksa apakah kombinasi Command/Ctrl+A ditekan
                if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_A) {
                    return; // Jika Command/Ctrl+A, abaikan pemrosesan lebih lanjut
                }

                String text = priceField.getText().replaceAll("[^\\d.]", ""); // Hanya izinkan angka dan titik desimal
                
                // Pastikan hanya ada satu titik desimal
                int dotIndex = text.indexOf('.');
                if (dotIndex != -1) {
                    text = text.substring(0, dotIndex + 1) + text.substring(dotIndex + 1).replace(".", ""); // Hapus titik tambahan setelah titik desimal pertama
                }
                
                if (!text.isEmpty()) {
                    // Format angka dengan pemisah ribuan untuk tampilan
                    priceField.setText(DialogUtils.formatNumberWithThousandsSeparator(text));
                    priceField.setCaretPosition(priceField.getText().length());
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Memeriksa apakah Command/Ctrl+A ditekan
                if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0 && c == 'a') {
                    return; // Jika Command/Ctrl+A, abaikan pemrosesan lebih lanjut
                }

                // Cegah karakter non-digit kecuali backspace dan titik
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != '.') {
                    e.consume(); // Mencegah input jika bukan angka atau titik
                }
            }
        });

        fields.get("productId").setEditable(false);

        finalizeDialog();
    }

    @Override
    protected void onOkButtonClick() {
        if (validateInput()) {
            updateProduct();
            isConfirmed = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        return !getFieldValue("productName").isEmpty() &&
               isValidBigDecimal(getFieldValue("price").replaceAll(",", "")); // Hapus koma sebelum validasi
    }

     private void updateProduct() {
        product.setProductName(getFieldValue("productName"));

        // Hapus pemisah ribuan (koma) sebelum menyimpan ke BigDecimal
        product.setPrice(new BigDecimal(getFieldValue("price").replaceAll(",", "")));
    }

    public ModelProduct getUpdatedProduct() {
        return isConfirmed ? product : null;
    }

    private boolean isValidBigDecimal(String s) {
        try {
            new BigDecimal(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}