/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

 
package myfilezilla;

/**
 *
 * @author MURAT KARATAŞ
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileSystemManager {
    private DirectoryNode root;
    private DirectoryNode currentDirectory;
    private Scanner inputScanner;

    public FileSystemManager() {
        inputScanner = new Scanner(System.in);
    }

    public void loadFileSystem(String filePath) {
        try {
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            MyStack<DirectoryNode> directoryStack = new MyStack<>();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.trim().isEmpty()) continue;

                int depth = 0;
                int spaces = 0;
                int i = 0;
                while (i < line.length()) {
                    char c = line.charAt(i);
                    if (c == '\t') {
                        depth++;
                    } else if (c == ' ') {
                        spaces++;
                        if (spaces == 4) {
                            depth++;
                            spaces = 0;
                        }
                    } else {
                        break;
                    }
                    i++;
                }
                
                String content = line.trim();
                
                if (content.equals("\\root")) {
                    root = new DirectoryNode("root", "01.01.2025", "SYSTEM", null);
                    currentDirectory = root;
                    directoryStack.push(root);
                    continue;
                }

                while (directoryStack.size() > depth) {
                    directoryStack.pop();
                }
                
                DirectoryNode parent = directoryStack.peek();
                if (parent == null) continue;

                FileSystemNode newNode;
                if (content.startsWith("\\")) {
                    String dirName = content.substring(1);
                    newNode = new DirectoryNode(dirName, "01.01.2025", "USER", parent);
                    parent.addChild(newNode);
                    directoryStack.push((DirectoryNode) newNode);
                } else {
                    String[] parts = content.split("##");
                    if (parts.length >= 4) {
                        try {
                            long size = Long.parseLong(parts[2]);
                            newNode = new FileNode(parts[0], parts[1], size, parts[3], parent);
                            parent.addChild(newNode);
                        } catch (Exception e) {}
                    }
                }
            }
            reader.close();
            if (root != null) root.updateStats();
            System.out.println(">> File system loaded successfully from 'filesystem.txt'.");
            
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: 'filesystem.txt' not found!");
        }
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Current Directory: \\" + getFullPath(currentDirectory) + " ===");
            System.out.println("1. Change Directory (cd)");
            System.out.println("2. List Contents (ls)");
            System.out.println("3. Create Directory (mkdir)");
            System.out.println("4. Create File (touch)");
            System.out.println("5. Delete (rm)");
            System.out.println("6. Search (Recursive)");
            System.out.println("7. Exit");
            System.out.print("Select: ");
            
            String cmd = inputScanner.nextLine();
            
            switch (cmd) {
                case "1": changeDirectory(); break;
                case "2": listContents(); break;
                case "3": createDirectory(); break;
                case "4": createFile(); break;
                case "5": deleteItem(); break;
                case "6": search(); break;
                case "7": running = false; break;
                default: System.out.println("Invalid command.");
            }
        }
    }

    private String getFullPath(FileSystemNode node) {
        if (node == root) return "root";
        if (node.parent == null) return node.name;
        return getFullPath(node.parent) + "\\" + node.name;
    }

    private void changeDirectory() {
        System.out.print("Directory name (or ..): ");
        String target = inputScanner.nextLine();
        
        if (target.equals("..")) {
            if (currentDirectory.parent != null) {
                if (currentDirectory.getAccessLevel().equals("SYSTEM")) {
                    System.out.print("WARNING: You are leaving a SYSTEM directory. Confirm? (y/n): ");
                    if (!inputScanner.nextLine().equalsIgnoreCase("y")) return;
                }
                currentDirectory = currentDirectory.parent;
            } else {
                System.out.println("Already at Root.");
            }
        } else {
            FileSystemNode node = currentDirectory.getChild(target);
            if (node != null && node instanceof DirectoryNode) {
                if (node.getAccessLevel().equals("SYSTEM")) {
                    System.out.print("WARNING: You are entering a SYSTEM directory. Confirm? (y/n): ");
                    if (!inputScanner.nextLine().equalsIgnoreCase("y")) return;
                }
                currentDirectory = (DirectoryNode) node;
            } else {
                System.out.println("Error: Directory not found.");
            }
        }
    }

    private void listContents() {
        System.out.println("\n-------------------------------------------------------------");
        System.out.printf("%-25s %-12s %-10s %-10s\n", "NAME", "DATE", "SIZE", "ACCESS");
        System.out.println("-------------------------------------------------------------");
        
        MyLinkedList<FileSystemNode> children = currentDirectory.getChildrenList();
        Node<FileSystemNode> tmp = children.first;
        while (tmp != null) {
            System.out.println(tmp.data);
            tmp = tmp.next;
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("Total Directory Size: " + currentDirectory.getSize() + " bytes");
    }

    private void createDirectory() {
       
        if (currentDirectory.getAccessLevel().equals("SYSTEM")) {
            System.out.println("Error: Permission denied. Cannot create directory in a SYSTEM folder.");
            return;
        }

        System.out.print("Directory Name: ");
        String name = inputScanner.nextLine();
        if (currentDirectory.getChild(name) != null) {
            System.out.println("Error: Name conflict. Item already exists.");
            return;
        }
        DirectoryNode newDir = new DirectoryNode(name, "27.11.2025", "USER", currentDirectory);
        currentDirectory.addChild(newDir);
        System.out.println("Directory created.");
    }

    private void createFile() {
        
        if (currentDirectory.getAccessLevel().equals("SYSTEM")) {
            System.out.println("Error: Permission denied. Cannot create file in a SYSTEM folder.");
            return;
        }

        System.out.print("File Name (with extension): ");
        String name = inputScanner.nextLine();
        if (currentDirectory.getChild(name) != null) {
            System.out.println("Error: Name conflict. Item already exists.");
            return;
        }
        System.out.print("Size (bytes): ");
        long size = 0;
        try { size = Long.parseLong(inputScanner.nextLine()); } catch (Exception e) {}
        
        FileNode newFile = new FileNode(name, "27.11.2025", size, "USER", currentDirectory);
        currentDirectory.addChild(newFile);
        System.out.println("File created.");
    }

    private void deleteItem() {
        System.out.print("Item name to delete: ");
        String name = inputScanner.nextLine();
        FileSystemNode node = currentDirectory.getChild(name);
        
        if (node == null) {
            System.out.println("Not found.");
            return;
        }
        
        if (hasSystemContent(node)) {
            System.out.println("ERROR: Items containing SYSTEM content cannot be deleted!");
            return;
        }
        
        currentDirectory.removeChild(node);
        System.out.println("Item deleted.");
    }
    
    private boolean hasSystemContent(FileSystemNode node) {
        if (node.getAccessLevel().equals("SYSTEM")) return true;
        if (node instanceof DirectoryNode) {
            MyLinkedList<FileSystemNode> children = ((DirectoryNode) node).getChildrenList();
            Node<FileSystemNode> tmp = children.first;
            while (tmp != null) {
                if (hasSystemContent(tmp.data)) return true;
                tmp = tmp.next;
            }
        }
        return false;
    }

    private void search() {
        System.out.print("Search Query (name or extension): ");
        String query = inputScanner.nextLine();
        
       
        FileSystemNode fast = currentDirectory.getChild(query);
        if (fast != null) {
            System.out.println("-> [Current Dir] " + getFullPath(fast));
        }
        
        
        searchRecursive(currentDirectory, query);
    }
    
    private void searchRecursive(DirectoryNode dir, String query) {
        MyLinkedList<FileSystemNode> children = dir.getChildrenList();
        Node<FileSystemNode> tmp = children.first;
        
        while (tmp != null) {
            FileSystemNode node = tmp.data;
            boolean match = node.getName().equals(query);
            if (node instanceof FileNode && ((FileNode)node).getExtension().equals(query)) {
                match = true;
            }
            
          
            if (match && (dir != currentDirectory || !node.getName().equals(query))) {
                System.out.println("-> Found: " + getFullPath(node));
            }
            
            if (node instanceof DirectoryNode) {
                searchRecursive((DirectoryNode) node, query);
            }
            tmp = tmp.next;
        }
    }
}