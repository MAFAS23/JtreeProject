///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package Model;
//
//import java.util.List;
//import javax.swing.table.AbstractTableModel;
//
///**
// *
// * @author athif
// */
//public class ModelTableFolderStructure extends AbstractTableModel {
//    
//    private List<ModelFolder> folders;
//    private final String[] columnNames = {"ID", "Name", "Depth"};
//
//    public ModelTableFolderStructure(List<ModelFolder> folders) {
//        this.folders = folders;
//    }
//
//    @Override
//    public int getRowCount() {
//        return folders.size();
//    }
//
//    @Override
//    public int getColumnCount() {
//        return columnNames.length;
//    }
//
//    @Override
//    public Object getValueAt(int row, int column) {
//        ModelFolder folder = folders.get(row);
//        return switch (column) {
//            case 0 -> folder.getId();
//            case 1 -> folder.getName();
//            //case 2 -> folder.getDepth();  // Assuming we've added a depth field to Folder
//            default -> null;
//        };
//    }
//
//    @Override
//    public String getColumnName(int column) {
//        return columnNames[column];
//    }
//
//    public void setFolders(List<ModelFolder> folders) {
//        this.folders = folders;
//        fireTableDataChanged();
//    }
//
//    public ModelFolder getFolderAt(int row) {
//        return folders.get(row);
//    }
//}
