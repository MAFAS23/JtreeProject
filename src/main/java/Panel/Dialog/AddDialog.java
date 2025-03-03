

package Panel.Dialog;

import Model.ModelProduct;
import Utilities.DialogUtils;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddDialog extends BaseDialog {
    private final int folderId;
    private boolean isConfirmed = false;
//    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0");

    public AddDialog(Frame owner, int nextProductId, int folderId) {
        super(owner, "Add Product");
        this.folderId = folderId;

//        addField("Product ID:", "productId");
        addField("Product Name:", "productName");
        addField("Price:", "price");
        
//        setFieldValue("productId", String.valueOf(nextProductId));
//        fields.get("productId").setEditable(false);
        
        // Set InputVerifier untuk field "price" agar hanya menerima angka
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

                    // Setel kursor ke posisi akhir setelah pemformatan
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


        finalizeDialog();
    }

    @Override
    protected void onOkButtonClick() {
        if (validateInput()) {
            isConfirmed = true;
            System.out.println("Creating ModelProduct with folderId: " + folderId);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   private boolean validateInput() {
        String priceText = getFieldValue("price").replaceAll(",", ""); // Hapus koma jika ada

        // Validasi field kosong dan format angka
        return !getFieldValue("productName").isEmpty() &&
               !priceText.isEmpty() &&
               isValidBigDecimal(priceText);
    }


    
    private boolean isValidBigDecimal(String s) {
        try {
         
            new BigDecimal(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
     public ModelProduct getUpdatedProduct() {
        if (!isConfirmed) {
            return null;
        }
        try {
//            int productId = Integer.parseInt(getFieldValue("productId"));
            String productName = getFieldValue("productName");

            // Hapus pemisah ribuan (koma) untuk mengubah ke BigDecimal
            String cleanPrice = getFieldValue("price").replaceAll(",", ""); // Hapus koma
            BigDecimal price = new BigDecimal(cleanPrice);
            return new ModelProduct(folderId, productName, price);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing input: " + e.getMessage());
            return null;
        }
    }


}