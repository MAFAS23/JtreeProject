/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author athif
 */
public class ModelFolderRelations {
    private int folderRelationsId;
    private int depth;
    private int parentId;
    private int childId;
    

    public ModelFolderRelations(int folderRelationsId, int parentId, int childId, int depth) {
        this.folderRelationsId = folderRelationsId;
        this.parentId = parentId;
        this.childId = childId;
        this.depth = depth;
    }

    public int getFolderRelationsId() {
        return folderRelationsId;
    }

    public void setFolderRelationsId(int folderRelationsId) {
        this.folderRelationsId = folderRelationsId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }


    @Override
    public String toString() {
        return "FolderRelation{folderRelationsId=" + folderRelationsId + ", parentId=" + parentId + ", childId=" + childId + ", depth=" + depth+ "}";
    }
}
