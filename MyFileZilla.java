/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package myfilezilla;

/**
 *
 * @author MURAT KARATAŞ
 */
public class MyFileZilla { 

    public static void main(String[] args) {
        System.out.println("### COMP2112 File System Project ###");
        
        FileSystemManager fsm = new FileSystemManager();
        
        
        fsm.loadFileSystem("filesystem.txt");
        
        fsm.start();
        
        System.out.println("System terminated.");
    }
}