package deque;

public interface Deque<T> extends Iterable<T> {

    public void addFirst(T item);

    public void addLast(T item);

    /* Default implementation, returns true if deque is empty, false otherwise */
    public default boolean isEmpty() {
        return size() == 0;
    }

    /* Returns the size of the deque */
    public int size();

    /* Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque();

    /* Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     */
    public T removeFirst();

    /* Removes and returns the item at the end of the deque.
     * If no such item exists, returns null.
     */
    public T removeLast();

    /* Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * Use iterative, not recursive
     */
    public T get(int index);
}
