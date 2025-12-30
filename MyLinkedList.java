/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myfilezilla;

/**
 *
 * @author MURAT KARATAŞ
 */
public class MyLinkedList<T> {
    public Node<T> first;
    public Node<T> last;

    public void insertFirst(T x) {
        Node<T> newNode = new Node<>(x);
        newNode.next = first;
        if (last == null) {
            last = newNode;
        }
        first = newNode;
    }

    public T search(T x) {
        Node<T> tmp = first;
        while (tmp != null) {
            if (tmp.data.equals(x)) {
                return tmp.data;
            }
            tmp = tmp.next;
        }
        return null;
    }

    public void remove(T x) {
        if (first == null) return;

        if (first.data.equals(x)) {
            first = first.next;
            if (first == null) last = null;
            return;
        }

        Node<T> prev = first;
        Node<T> curr = first.next;

        while (curr != null) {
            if (curr.data.equals(x)) {
                prev.next = curr.next;
                if (curr == last) last = prev;
                return;
            }
            prev = curr;
            curr = curr.next;
        }
    }
}