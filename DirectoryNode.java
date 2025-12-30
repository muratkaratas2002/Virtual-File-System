/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myfilezilla;

/**
 *
 * @author MURAT KARATAŞ
 */
public class DirectoryNode extends FileSystemNode {
    private MyHashTable<FileSystemNode> children;

    public DirectoryNode(String name, String date, String accessLevel, DirectoryNode parent) {
        super(name, date, accessLevel, parent);
        this.children = new MyHashTable<>(20);
    }

    public void addChild(FileSystemNode node) {
        children.insert(node);
        updateStats();
    }

    public void removeChild(FileSystemNode node) {
        children.remove(node);
        updateStats();
    }

    public FileSystemNode getChild(String childName) {
        FileSystemNode tempSearchNode = new FileSystemNode(childName, "", "", null) {
            @Override public void updateStats() {}
        };
        return children.get(tempSearchNode);
    }
    
    public MyLinkedList<FileSystemNode> getChildrenList() {
        return children.getAllElements();
    }

    @Override
    public void updateStats() {
        long totalSize = 0;
        String latestDate = this.date;
        boolean allSystem = true;
        boolean isEmpty = true;

        MyLinkedList<FileSystemNode> allKids = children.getAllElements();
        Node<FileSystemNode> current = allKids.first;
        
        while (current != null) {
            isEmpty = false;
            FileSystemNode child = current.data;
            
            totalSize += child.getSize();
            
            if (compareDates(child.getDate(), latestDate) > 0) {
                latestDate = child.getDate();
            }
            
            if (!child.getAccessLevel().equals("SYSTEM")) {
                allSystem = false;
            }
            
            current = current.next;
        }
        
        this.size = totalSize;
        this.date = latestDate;
        
        if (isEmpty || !allSystem) {
            this.accessLevel = "USER";
        } else {
            this.accessLevel = "SYSTEM";
        }
        
        if (parent != null) {
            parent.updateStats();
        }
    }
    
    @Override
    public String toString() {
        return String.format("\\%-24s %-12s %-10d %-10s", name, date, size, accessLevel);
    }
}