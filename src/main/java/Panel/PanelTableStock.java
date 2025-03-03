/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Panel;

/**
 *
 * @author athif
 */
import Controller.ControllerStock;
import DAO.DaoStock;
import Model.ModelTableStock;
import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PanelTableStock extends JPanel {
    private final JTable table;
    private final JScrollPane scrollPane;
    private final JTextField searchField;
    private final JComboBox<String> categoryComboBox;
    private final JCheckBox inStockCheckBox;
    private final ModelTableStock modelTableStock;
    private final TableRowSorter<ModelTableStock> sorter;
    private ControllerStock controller;

    public PanelTableStock() {
        setLayout(new BorderLayout());
        modelTableStock = new ModelTableStock(new ArrayList<>());

        // Create filter components
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All Categories");
        inStockCheckBox = new JCheckBox("Only In Stock");

        filterPanel.add(new JLabel("Search: "));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category: "));
        filterPanel.add(categoryComboBox);
        filterPanel.add(inStockCheckBox);

        JButton applyFilterButton = new JButton("Apply Filter");
        filterPanel.add(applyFilterButton);

        add(filterPanel, BorderLayout.NORTH);

        // Create table
        table = new JTable(modelTableStock);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Set up sorter
        sorter = new TableRowSorter<>(modelTableStock);
        table.setRowSorter(sorter);

        // Add action listener to apply filter button
        applyFilterButton.addActionListener((ActionEvent e) -> {
            applyFilters();
        });
         // Inisialisasi controller dan load data awal
    controller = new ControllerStock(this, new DaoStock());
    controller.loadStockData("", "All Categories", false);
    }

    
   public void setController(ControllerStock controller) {
        this.controller = controller;
    }


   public void setModel(ModelTableStock modelTableStock) {
    table.setModel(modelTableStock);
    table.revalidate();
    table.repaint();
    System.out.println("Model set to table with " + modelTableStock.getRowCount() + " rows");
    
  
}

    public void setCategories(List<String> categories) {
        categoryComboBox.removeAllItems();
        categoryComboBox.addItem("All Categories");
        for (String category : categories) {
            categoryComboBox.addItem(category);
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void applyFilters() {
        if (controller != null) {
            String searchTerm = searchField.getText();
            String category = categoryComboBox.getSelectedItem().toString();
            boolean onlyInStock = inStockCheckBox.isSelected();

            controller.loadStockData(searchTerm, category, onlyInStock);
        }
    }
public JTable getTable() {
    return table;
}
  public ModelTableStock getModel() {
        return (ModelTableStock) table.getModel();
    }
}
