/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myfilezilla;

/**
 *
 * @author MURAT KARATAŞ
 */
public class FileNode extends FileSystemNode {
    private String extension;

    public FileNode(String name, String date, long size, String accessLevel, DirectoryNode parent) {
        super(name, date, accessLevel, parent);
        this.size = size;
        this.extension = extractExtension(name);
        if (parent != null) parent.updateStats();
    }

    private String extractExtension(String name) {
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) return name.substring(dotIndex + 1);
        return "";
    }

    @Override
    public void updateStats() {
        if (parent != null) parent.updateStats();
    }

    public String getExtension() { return extension; }

    @Override
    public String toString() {
        return String.format("%-25s %-12s %-10d %-10s", name, date, size, accessLevel);
    }
}