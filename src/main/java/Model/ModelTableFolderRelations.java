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
public class ModelTableFolderRelations extends AbstractTableModel {
    
    private List<ModelFolderRelations> folderRelations;
    private final String[] columnNames = {"folders_relations_id", "parent_id", "child_id","depth"};

    public ModelTableFolderRelations(List<ModelFolderRelations> folderRelations) {
        this.folderRelations = folderRelations;
    }

    @Override
    public int getRowCount() {
        return folderRelations.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int column) {
        ModelFolderRelations relation = folderRelations.get(row);
        return switch (column) {
            case 0 -> relation.getFolderRelationsId();
            case 1 -> relation.getParentId();
            case 2 -> relation.getChildId();
            case 3 -> relation.getDepth();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setFolderRelations(List<ModelFolderRelations> folderRelations) {
        this.folderRelations = folderRelations;
        fireTableDataChanged();
    }

    public ModelFolderRelations getFolderRelationAt(int row) {
        return folderRelations.get(row);
    }
}