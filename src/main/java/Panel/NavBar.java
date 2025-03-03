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
import java.awt.event.ActionListener;

public class NavBar {
    private final JMenuBar menuBar;
    private final JMenuItem stockItem;
    private final JMenuItem penerimaanBarangItem;
    private final JMenuItem vendorItem;
    private final JMenuItem homeItem;  // Tambahkan ini
    private final JMenuItem issuesItem;  // Tambahkan ini

    public NavBar() {
        // Membuat menu bar
        menuBar = new JMenuBar();

        // Membuat menu "Menu"
        JMenu menu = new JMenu("Menu");

        // Membuat item-item menu
        stockItem = new JMenuItem("Stock");
        penerimaanBarangItem = new JMenuItem("Penerimaan Barang");
        vendorItem = new JMenuItem("Vendor");
        homeItem = new JMenuItem("Home");  // Tambahkan ini
        issuesItem = new JMenuItem("Pengeluaran");  // Tambahkan ini

        // Menambahkan item-item ke menu
        menu.add(stockItem);
        menu.add(penerimaanBarangItem);
        menu.add(vendorItem);
        menu.add(homeItem);
        menu.add(issuesItem);

        // Menambahkan menu ke menu bar
        menuBar.add(menu);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
    public void setStockMenuActionListener(ActionListener listener) {
        stockItem.addActionListener(listener);
    }

    public void setVendorMenuActionListener(ActionListener listener) {
        vendorItem.addActionListener(listener);
    }
    public void setHomeMenuActionListener(ActionListener listener) {  // Tambahkan ini
        homeItem.addActionListener(listener);
    }
    public void setGoodsReceiptsMenuActionListener(ActionListener listener) {  // Tambahkan ini
        penerimaanBarangItem.addActionListener(listener);
    }
    public void setIssuesItemMenuListener(ActionListener listener) {  // Tambahkan ini
        issuesItem.addActionListener(listener);
    }
}
