/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myfilezilla;

/**
 *
 * @author MURAT KARATAŞ
 */
public class MyStack<T> {
    private Node<T> first;
    private int N;

    public boolean isEmpty() { return first == null; }
    public int size() { return N; }

    public void push(T item) {
        Node<T> oldfirst = first;
        first = new Node<>(item);
        first.next = oldfirst;
        N++;
    }

    public T pop() {
        if (isEmpty()) return null;
        T item = first.data;
        first = first.next;
        N--;
        return item;
    }

    public T peek() {
        if (isEmpty()) return null;
        return first.data;
    }
}