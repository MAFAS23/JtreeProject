/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.ModelFolder;
import Model.ModelFolderRelations;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author athif
 */
public interface ImplementTree {
    // Folder methods
    void insertFolder(ModelFolder folder);
    void deleteFolder(int folderId);
    void updateFolder(ModelFolder folder);
    List<ModelFolder> getAllFolders();
    ModelFolder getFolderById(int folderId);
    void addNewFolder(String folderName, int parentId);

    // Relations methods
    void insertRelation(ModelFolderRelations relation);
    void deleteRelation(int folderId);
    List<ModelFolderRelations> getRelationsByFolderId(int folderId);
    List<ModelFolderRelations> getRelationsByParentId(int parentId);
    List<ModelFolderRelations> getRelationsByChildId(int childId);
    
    
    
}
