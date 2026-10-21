/**
 * SinglyLinkedList
 *
 * A simple implementation of a singly linked list in Java.
 * Supports inserting at the beginning/end, deleting a value,
 * searching, and displaying the list.
 *
 * Time complexity:
 *   addFirst : O(1)
 *   addLast  : O(n)
 *   delete   : O(n)
 *   search   : O(n)
 */
public class SinglyLinkedList {

    // Inner class representing one node of the list
    private static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    // Insert a value at the beginning of the list
    public void addFirst(int value) {
        Node newNode = new Node(value);
        newNode.next = head;
        head = newNode;
        size++;
    }

    // Insert a value at the end of the list
    public void addLast(int value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Delete the first occurrence of a value; returns true if deleted
    public boolean delete(int value) {
        if (head == null) {
            return false;
        }
        if (head.data == value) {
            head = head.next;
            size--;
            return true;
        }
        Node current = head;
        while (current.next != null && current.next.data != value) {
            current = current.next;
        }
        if (current.next == null) {
            return false;
        }
        current.next = current.next.next;
        size--;
        return true;
    }

    // Check whether a value exists in the list
    public boolean contains(int value) {
        Node current = head;
        while (current != null) {
            if (current.data == value) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    // Print the list in the form: 10 -> 20 -> 30 -> null
    public void display() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    // Demo
    public static void main(String[] args) {
        SinglyLinkedList list = new SinglyLinkedList();

        list.addLast(10);
        list.addLast(20);
        list.addLast(30);
        list.addFirst(5);
        System.out.print("After insertions : ");
        list.display();

        list.delete(20);
        System.out.print("After deleting 20: ");
        list.display();

        System.out.println("Contains 30? " + list.contains(30));
        System.out.println("Contains 99? " + list.contains(99));
        System.out.println("Size: " + list.size());
    }
}