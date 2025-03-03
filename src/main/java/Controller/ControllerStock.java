/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.DaoProduct;
import DAO.DaoStock;
import Model.ModelTableStock;
import Panel.PanelTableStock;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author athif
 */
public class ControllerStock {
    private final PanelTableStock panelTableStock;
    private final DaoStock daoStock;

   public ControllerStock(PanelTableStock panelTableStock, DaoStock daoStock) {
        this.panelTableStock = panelTableStock;
        this.daoStock = daoStock;
        initializeCategories();
    }

    private void initializeCategories() {
        try {
            List<String> categories = daoStock.getCategories();
            panelTableStock.setCategories(categories);
        } catch (SQLException e) {
            panelTableStock.showError("Error loading categories: " + e.getMessage());
        }
    }

    public void loadStockData(String searchTerm, String category, boolean onlyInStock) {
    try {
        List<Object[]> stockData = daoStock.getStockData(searchTerm, category, onlyInStock);

        if (stockData.isEmpty()) {
            System.out.println("No data retrieved from database");
        } else {
            System.out.println("Retrieved " + stockData.size() + " rows from database");
        }

        ModelTableStock modelTableStock = panelTableStock.getModel();
        modelTableStock.setStockData(stockData);
        
        panelTableStock.setModel(modelTableStock);
    } catch (SQLException e) {
        e.printStackTrace();
        panelTableStock.showError("Error loading stock data: " + e.getMessage());
    }
}
    

}