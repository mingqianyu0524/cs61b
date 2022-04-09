package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T> {
    private Node sentinel;
    private int size;

    public class Node {
        public Node prev;
        public T item;
        public Node next;

        public Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    /* Creates an empty linked list deque, first item is always sentinel.next */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public void addFirst(T item) {
        Node first = new Node(sentinel, item, sentinel.next);
        sentinel.next.prev = first;
        sentinel.next = first;
        size++;
    }

    public void addLast(T item) {
        Node last = new Node(sentinel.prev, item, sentinel);
        sentinel.prev.next = last;
        sentinel.prev = last;
        size++;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node ptr = sentinel;
        while (ptr.next != sentinel) {
            ptr = ptr.next;
            System.out.print(ptr.item.toString() + " ");
        }
        System.out.print(ptr.item.toString() + "\n");
    }

    public T removeFirst() {
        Node oldFirst = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.next.prev = sentinel;
        if (size > 0) size--;
        return oldFirst.item;
    }

    public T removeLast() {
        Node oldLast = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.prev.next = sentinel;
        if (size > 0) size--;
        return oldLast.item;
    }

    public T get(int index) {
        if (index > size) return null;
        Node ptr = sentinel;
        for (int i = 0; i <= index; ++i) {
            ptr = ptr.next;
        }
        return ptr.item;
    }

    /* Implementation: Return a new iterator */
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    public class LinkedListIterator implements Iterator<T> {
        private int pos;

        // Constructor
        public LinkedListIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public T next() {
            T returnItem = get(pos);
            pos++;
            return returnItem;
        }
    }

    @Override
    // Compare if two linked list deque equals
    public boolean equals(Object other) {
        if (other == this ) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        // Cast to linked list deque
        LinkedListDeque<T> o = (LinkedListDeque<T>) other;
        // Check size
        if (o.size() != this.size()) return false;
        // Check if sequence of items are the same
        for (int i = 0; i < this.size(); i++) {
            T otherItem = o.get(i);
            T item = this.get(i);
            if (!(item.equals(otherItem))) return false;
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
