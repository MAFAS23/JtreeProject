/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author athif
 */
public class ModelTableFolder extends AbstractTableModel {
    
    private List<ModelFolder> lmf; // list model folder
    private final String[] columnNames = {"folders_id", "Name"};

    public ModelTableFolder(List<ModelFolder> lmf) {
        this.lmf = lmf;
    }
    
    @Override
    public int getRowCount() {
        return lmf.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        return switch (column) {
            case 0 -> lmf.get(row).getId();
            case 1 -> lmf.get(row).getName();
            default -> null;
        };
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    public void setFolders(List<ModelFolder> folders) {
        this.lmf = folders;
        fireTableDataChanged();
    }
    
    public ModelFolder getFolderAt(int row) {
        return lmf.get(row);
    }
}