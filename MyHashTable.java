/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myfilezilla;

/**
 *
 * @author MURAT KARATAŞ
 */
public class MyHashTable<T> {
    private int M;
    private MyLinkedList<T>[] table;

    @SuppressWarnings("unchecked")
    public MyHashTable(int M) {
        this.M = M;
        table = new MyLinkedList[M];
        for (int i = 0; i < M; i++) {
            table[i] = new MyLinkedList<>();
        }
    }

    private int hash(T t) {
        return Math.abs(t.hashCode()) % M;
    }

    public void insert(T t) {
        int ix = hash(t);
        table[ix].insertFirst(t);
    }

    public T get(T t) {
        int ix = hash(t);
        return table[ix].search(t);
    }

    public void remove(T t) {
        int ix = hash(t);
        table[ix].remove(t);
    }

    public MyLinkedList<T> getAllElements() {
        MyLinkedList<T> all = new MyLinkedList<>();
        for (int i = 0; i < M; i++) {
            Node<T> tmp = table[i].first;
            while (tmp != null) {
                all.insertFirst(tmp.data);
                tmp = tmp.next;
            }
        }
        return all;
    }
}