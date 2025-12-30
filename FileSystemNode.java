/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myfilezilla;

/**
 *
 * @author MURAT KARATAŞ
 */
public abstract class FileSystemNode {
    protected String name;
    protected DirectoryNode parent;
    protected String accessLevel;
    protected long size;
    protected String date;

    public FileSystemNode(String name, String date, String accessLevel, DirectoryNode parent) {
        this.name = name;
        this.date = date;
        this.accessLevel = accessLevel;
        this.parent = parent;
        this.size = 0;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + name.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj instanceof FileSystemNode) {
            return this.name.equals(((FileSystemNode) obj).name);
        }
        if (obj instanceof String) {
            return this.name.equals(obj);
        }
        return false;
    }

    protected int compareDates(String d1, String d2) {
        try {
            String[] p1 = d1.split("\\.");
            String[] p2 = d2.split("\\.");
            
            int y1 = Integer.parseInt(p1[2]);
            int y2 = Integer.parseInt(p2[2]);
            if (y1 != y2) return y1 - y2;
            
            int m1 = Integer.parseInt(p1[1]);
            int m2 = Integer.parseInt(p2[1]);
            if (m1 != m2) return m1 - m2;
            
            int day1 = Integer.parseInt(p1[0]);
            int day2 = Integer.parseInt(p2[0]);
            return day1 - day2;
        } catch (Exception e) {
            return 0; 
        }
    }

    public String getName() { return name; }
    public long getSize() { return size; }
    public String getAccessLevel() { return accessLevel; }
    public String getDate() { return date; }
    
    public abstract void updateStats();
}