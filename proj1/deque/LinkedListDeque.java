package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private Node sentinel;
    private Node curNode; // this is just for getRecursive
    private int size;

    private class Node {
        private Node prev;
        private T item;
        private Node next;

        private Node(Node p, T i, Node n) {
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
        curNode = sentinel; // initialize curNode to sentinel
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
        sentinel.next.prev = sentinel;
        if (size > 0) {
            size--;
        }
        return oldFirst.item;
    }

    public T removeLast() {
        Node oldLast = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        if (size > 0) {
            size--;
        }
        return oldLast.item;
    }

    public T get(int index) {
        if (index > size) {
            return null;
        }
        Node ptr = sentinel;
        for (int i = 0; i <= index; ++i) {
            ptr = ptr.next;
        }
        return ptr.item;
    }

    /* Same as get, but uses recursion */
    public T getRecursive(int index) {
        if (index > size) {
            return null;
        }
        curNode = curNode.next; // Move curNode to the next pos
        if (index == 0) {
            T item = curNode.item;
            curNode = curNode.prev; // Retrieve curNode back
            return item;
        }
        T item = getRecursive(index - 1);
        curNode = curNode.prev;
        return item;
    }

    /* Implementation: Return a new iterator */
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private int pos;

        // Constructor
        LinkedListIterator() {
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
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        // Check instance type
        if (other.getClass() != this.getClass() && !(other instanceof Deque)) {
            return false;
        }
        Deque<T> o = (Deque<T>) other;
        // Check size
        if (o.size() != this.size()) {
            return false;
        }
        // Check if sequence of items are the same
        for (int i = 0; i < this.size(); i++) {
            T otherItem = o.get(i);
            T item = this.get(i);
            if (!(item.equals(otherItem))) {
                return false;
            }
        }
        return true;
    }

}
