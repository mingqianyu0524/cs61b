package deque;

public class LinkedListDeque<T> {
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

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /* Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        Node ptr = sentinel;
        while (ptr.next != sentinel) {
            ptr = ptr.next;
            System.out.print(ptr.item.toString() + " ");
        }
        System.out.print(ptr.item.toString() + "\n");
    }

    /* Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
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

    /* Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * Use iterative, not recursive
     */
    public T get(int index) {
        if (index > size) return null;
        Node ptr = sentinel;
        for (int i = 0; i < index; ++i) {
            ptr = ptr.next;
        }
        return ptr.item;
    }

    /* public Iterator<T> iterator() {} */

    @Override
    // Need to watch the lectures first before implementing this method
    public boolean equals(Object o) {
        return (o instanceof LinkedListDeque);
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> deque1 = new LinkedListDeque<>();
        deque1.addFirst(15);
        deque1.addFirst(20);
        deque1.addLast(10);
        LinkedListDeque<Integer> deque2 = new LinkedListDeque<>();
        deque1.addFirst(10);
        deque1.addFirst(15);
        deque1.addLast(20);
        deque1.addLast(25);
        if (deque1.equals(deque2)) {
            System.out.println("deque1 and deque2 are equal");
        } else {
            System.out.println("deque1 and deque2 are not equal");
        }
    }
}
