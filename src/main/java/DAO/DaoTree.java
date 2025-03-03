/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conn.ConnectionDb;
import Model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author athif
 */
public class DaoTree implements ImplementTree {
//   private static final int ROOT_ID = 1;
//    private final Connection conn;
       private final DataSource dataSource;

    final String insertFolder = "INSERT INTO folders (name) VALUES (?)";
    final String deleteFolder = "DELETE FROM folders WHERE folders_id = ?";
    final String updateFolder = "UPDATE folders SET name = ? WHERE folders_id = ?";
    final String selectFolders = "SELECT * FROM folders";
    final String getFolderById = "SELECT * FROM folders WHERE folders_id = ?";

    final String insertRelation = "INSERT INTO folder_relations (parent_id, child_id, depth) VALUES (?, ?, ?)";
    final String deleteRelation = "DELETE FROM folder_relations WHERE parent_id = ? OR child_id = ?";
    final String selectRelationsByFolderId = "SELECT * FROM folder_relations WHERE parent_id = ?";
    final String getParentDepth = "SELECT depth FROM folder_relations WHERE child_id = ?";
    
   
    
      public DaoTree() {
        this.dataSource = ConnectionDb.getInstance().getDataSource();
        
    }
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    @Override
    public void insertFolder(ModelFolder folder) {
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(insertFolder, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, folder.getName());
        int affectedRows = stmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating folder failed, no rows affected.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                folder.setId(generatedId);
                System.out.println("Generated ID: " + generatedId);
            } else {
                System.out.println("No ID obtained after insert.");
                throw new SQLException("Creating folder failed, no ID obtained.");
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


    @Override
    public void deleteFolder(int folderId) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(deleteFolder)) {
            stmt.setInt(1, folderId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateFolder(ModelFolder folder) {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(updateFolder)) {
            stmt.setString(1, folder.getName());
            stmt.setInt(2, folder.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<ModelFolder> getAllFolders() {
    List<ModelFolder> folders = new ArrayList<>();

    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(selectFolders);
         ResultSet rs = stmt.executeQuery()) {
        
        System.out.println("Executing query to fetch all folders...");
        
        while (rs.next()) {
            int folderId = rs.getInt("folders_id");
            String folderName = rs.getString("name");
            
            // Log setiap folder yang diambil dari database
            System.out.println("Retrieved Folder: ID = " + folderId + ", Name = " + folderName);
            
            ModelFolder folder = new ModelFolder(folderId, folderName);
            folders.add(folder);
        }
        
        System.out.println("Total folders retrieved: " + folders.size());
        
    } catch (SQLException ex) {
        // Log detail error jika terjadi SQLException
        System.err.println("Error while fetching folders from database: " + ex.getMessage());
        ex.printStackTrace();
    }
    
    return folders;
}

    @Override
    public ModelFolder getFolderById(int folderId) {
        ModelFolder folder = null;
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(getFolderById)) {
            stmt.setInt(1, folderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    folder = new ModelFolder(rs.getInt("folders_id"), rs.getString("name"));
                System.out.println("Folder found: ID = " + folder.getId() + ", Name = " + folder.getName());
                } else {
                    System.out.println("Folder not found for ID: " + folderId);
                }
            }
        } catch (SQLException ex) {
             System.err.println("Error fetching folder by ID: " + ex.getMessage());
            ex.printStackTrace();
        }
        return folder;
    }


    @Override
   public void insertRelation(ModelFolderRelations relation) {
    try {
        if (relation.getParentId() == relation.getChildId()) {
            throw new SQLException("A folder cannot be a parent of itself.");
        }

        // Insert the relation
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(insertRelation)) {
            stmt.setInt(1, relation.getParentId());
            stmt.setInt(2, relation.getChildId());
            stmt.setInt(3, relation.getDepth());
            stmt.executeUpdate();
            System.out.println("Inserted relation: Parent ID = " + relation.getParentId() + ", Child ID = " + relation.getChildId());
        }
    } catch (SQLException ex) {
        System.err.println("Error inserting relation: " + ex.getMessage());
        ex.printStackTrace();
    }
}

    @Override
    public void deleteRelation(int folderId) {
    try (Connection conn = getConnection()) {
        // Memulai transaksi
        conn.setAutoCommit(false);

        // Langkah 1: Hapus semua relasi yang berkaitan dengan folder
        deleteAllRelationsByFolderId(conn, folderId);

        // Langkah 2: Hapus folder
        try (PreparedStatement stmt = conn.prepareStatement(deleteFolder)) {
            stmt.setInt(1, folderId);
            stmt.executeUpdate();
        }

        // Commit transaksi setelah penghapusan berhasil
        conn.commit();
        System.out.println("Folder dengan ID " + folderId + " berhasil dihapus.");
    } catch (SQLException ex) {
        ex.printStackTrace();
        System.err.println("Gagal menghapus folder: " + ex.getMessage());
    }
}

private void deleteAllRelationsByFolderId(Connection conn, int folderId) throws SQLException {
    // Hapus semua relasi dimana folder ini adalah parent
    String deleteRelationsSql = "DELETE FROM folder_relations WHERE parent_id = ? OR child_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(deleteRelationsSql)) {
        stmt.setInt(1, folderId);
        stmt.setInt(2, folderId);
        stmt.executeUpdate();
    }
}
//    public void deleteRelation(int folderId) {
//        try (Connection conn = getConnection();
//                PreparedStatement stmt = conn.prepareStatement(deleteRelation)) {
//            stmt.setInt(1, folderId);
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }

    @Override
   public List<ModelFolderRelations> getRelationsByFolderId(int folderId) {
    List<ModelFolderRelations> relations = new ArrayList<>();
    
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(selectRelationsByFolderId)) {
        // Mengatur nilai parameter untuk placeholder '?'
        stmt.setInt(1, folderId);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ModelFolderRelations relation = new ModelFolderRelations(
                    rs.getInt("folder_relations_id"),          // folder_relations_id
                    rs.getInt("parent_id"),   // parent_id
                    rs.getInt("child_id"),    // child_id
                    rs.getInt("depth")        // depth
                );
                relations.add(relation);
                System.out.println("Loaded relation: " + relation.toString());
            }
        }
    } catch (SQLException ex) {
        System.err.println("Error fetching relations for folder ID: " + folderId + ", Message: " + ex.getMessage());
        ex.printStackTrace();
    }
    
    return relations;
    
}
   @Override
     public List<ModelFolderRelations> getRelationsByParentId(int parentId) {
        List<ModelFolderRelations> relations = new ArrayList<>();
        String selectRelationsByParentId = "SELECT * FROM folder_relations WHERE parent_id = ?";
        
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(selectRelationsByParentId)) {
            stmt.setInt(1, parentId);
            System.out.println("Executing query: " + selectRelationsByParentId + " with parentId = " + parentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ModelFolderRelations relation = new ModelFolderRelations(
                        rs.getInt("folder_relations_id"),
                        rs.getInt("parent_id"),
                        rs.getInt("child_id"),
                        rs.getInt("depth")
                    );
                    relations.add(relation);
                    System.out.println("Found relation: " + relation);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching relations by parent ID: " + ex.getMessage());
            ex.printStackTrace();
        }
        System.out.println("Total relations found for parentId " + parentId + ": " + relations.size());
        return relations;
    }
     @Override
    public List<ModelFolderRelations> getRelationsByChildId(int childId) {
    List<ModelFolderRelations> relations = new ArrayList<>();
    String sql = "SELECT * FROM folder_relations WHERE child_id = ?";
    
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, childId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int parentId = rs.getInt("parent_id");
                int depth = rs.getInt("depth");
                
                // Tambahkan log untuk memastikan tidak ada relasi dengan dirinya sendiri
                if (parentId == childId) {
                    System.out.println("Invalid relation: Folder cannot be its own parent.");
                    continue;
                }
                
                ModelFolderRelations relation = new ModelFolderRelations(
                    rs.getInt("folder_relations_id"),
                    parentId,
                    childId,
                    depth
                );
                relations.add(relation);
            }
        }
    } catch (SQLException ex) {
        System.err.println("Error fetching relations by child ID: " + ex.getMessage());
        ex.printStackTrace();
    }
    return relations;
}
   
    @Override
    
    public void addNewFolder(String folderName, int parentId) {
    try (Connection conn = getConnection()) {
        // Memulai transaksi
        conn.setAutoCommit(false);

        // Langkah 1: Tambahkan folder baru
        int newFolderId;
        try (PreparedStatement insertFolderStmt = conn.prepareStatement(insertFolder, Statement.RETURN_GENERATED_KEYS)) {
            insertFolderStmt.setString(1, folderName);
            int affectedRows = insertFolderStmt.executeUpdate(); // Menggunakan executeUpdate()

            if (affectedRows == 0) {
                throw new SQLException("Gagal menambahkan folder baru. Tidak ada baris yang terpengaruh.");
            }

            try (ResultSet rs = insertFolderStmt.getGeneratedKeys()) { // Menggunakan getGeneratedKeys untuk mendapatkan ID yang dihasilkan
                if (rs.next()) {
                    newFolderId = rs.getInt(1); // Menggunakan indeks kolom yang benar
                } else {
                    throw new SQLException("Gagal mendapatkan ID folder yang baru.");
                }
            }
        }

        // Langkah 2: Tentukan parentId, jika parentId == 0, gunakan ROOT_ID
        if (parentId == 0) {
            parentId = 1; // Root ID asumsikan sebagai 1
        }

        // Langkah 3: Hitung depth (kedalaman) berdasarkan parent
        int depth;
        try (PreparedStatement getDepthStmt = conn.prepareStatement(getParentDepth)) {
            getDepthStmt.setInt(1, parentId);
            try (ResultSet rs = getDepthStmt.executeQuery()) {
                if (rs.next()) {
                    depth = rs.getInt("depth") + 1;
                } else {
                    // Jika parent tidak ditemukan, depth diatur ke 1 (root depth)
                    depth = 1;
                }
            }
        }

        // Langkah 4: Buat relasi antara parent dan child (folder baru)
        try (PreparedStatement insertRelationStmt = conn.prepareStatement(insertRelation)) {
            insertRelationStmt.setInt(1, parentId);
            insertRelationStmt.setInt(2, newFolderId);
            insertRelationStmt.setInt(3, depth);
            insertRelationStmt.executeUpdate();
        }

        // Langkah 5: Commit transaksi
        conn.commit();
        System.out.println("New folder added successfully: " + folderName + " with ID: " + newFolderId);
    } catch (SQLException ex) {
        ex.printStackTrace();
        System.err.println("Transaction failed due to an error: " + ex.getMessage());
    }
}
    
//    boolean originalAutoCommit = false;
//    try {
//        originalAutoCommit = conn.getAutoCommit();
//        conn.setAutoCommit(false);
//
//        // Langkah 1: Tambahkan folder baru
//        ModelFolder newFolder = new ModelFolder(0, folderName);
//        insertFolder(newFolder);  // Setelah ini, ID baru akan diset di newFolder
//
//        // Langkah 2: Tentukan parentId, jika parentId == 0, gunakan ROOT_ID
//        if (parentId == 0) {
//            parentId = 1; // Root ID asumsikan sebagai 1
//        }
//
//        // Langkah 3: Hitung depth (kedalaman) berdasarkan parent
//        int depth = (parentId == 1) ? 1 : getParentDepth(parentId) + 1;
//
//        // Langkah 4: Buat relasi antara parent dan child (folder baru)
//        ModelFolderRelations newRelation = new ModelFolderRelations(0, parentId, newFolder.getId(), depth);
//        insertRelation(newRelation);
//
//        // Langkah 5: Commit transaksi
//        conn.commit();
//        System.out.println("New folder added successfully: " + newFolder.getName() + " with ID: " + newFolder.getId());
//    } catch (SQLException ex) {
//        try {
//            conn.rollback();
//            System.err.println("Transaction rolled back due to an error: " + ex.getMessage());
//        } catch (SQLException e) {
//            System.err.println("Error rolling back transaction: " + e.getMessage());
//        }
//        ex.printStackTrace();
//    } finally {
//        try {
//            conn.setAutoCommit(originalAutoCommit);
//        } catch (SQLException e) {
//            System.err.println("Error resetting auto-commit: " + e.getMessage());
//        }
//    }
//}


    private int getParentDepth(int parentId) throws SQLException {
        String sql = "SELECT depth FROM folder_relations WHERE child_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, parentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("depth");
                }
            }
        }
        return -1; // or throw an exception if parent not found
    }
}


