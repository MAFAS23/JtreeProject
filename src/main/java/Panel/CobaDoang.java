/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

/**
 *
 * @author athif
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Vector;

public class CobaDoang {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JTable Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Data produk dari database (misalnya)
        Vector<String> produk = new Vector<>();
        produk.add("Produk A");
        produk.add("Produk B");
        produk.add("Produk C");

        // Buat model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Produk");
        
        // Tambahkan beberapa baris untuk contoh
        model.addRow(new Object[]{1, "Produk A"});
        model.addRow(new Object[]{2, "Produk B"});

        // Buat JTable
        JTable table = new JTable(model);

        // Buat JComboBox dan tambahkan sebagai editor kolom
        JComboBox<String> comboBox = new JComboBox<>(produk);
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setCellEditor(new DefaultCellEditor(comboBox));

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
