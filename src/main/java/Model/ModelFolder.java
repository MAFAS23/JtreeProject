/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author athif
 */
public class ModelFolder {
    private int foldersId;
    private String name;

    public ModelFolder(int foldersId, String name) {
        this.foldersId = foldersId;
        this.name = name;
    }

    public int getId() {
        return foldersId;
    }

    public void setId(int id) {
        this.foldersId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Folder{id=" + foldersId + ", name='" + name + "'}";
    }
}
