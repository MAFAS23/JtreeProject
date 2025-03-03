/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author athif
 */
public class ModelFolderStructure {
    
    private final ModelFolder folder;
    private final List<ModelFolderStructure> children;

    public ModelFolderStructure(ModelFolder folder) {
        this.folder = folder;
        this.children = new ArrayList<>();
    }

    public ModelFolder getFolder() {
        return folder;
    }

    public List<ModelFolderStructure> getChildren() {
        return children;
    }

    public void addChild(ModelFolderStructure child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    private String toString(int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ".repeat(depth)).append(folder.getName()).append("\n");
        for (ModelFolderStructure child : children) {
            sb.append(child.toString(depth + 1));
        }
        return sb.toString();
    }
    
}
