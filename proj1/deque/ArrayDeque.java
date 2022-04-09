package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int first;
    private int last;
    private int size;
    private int capacity;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        first = 4;
        last = 3;
        size = 0;
        capacity = 8;
    }

    private void resize(int new_capacity) {
        // Create a new array of size new_capacity
        T[] a = (T[]) new Object[new_capacity];
        // Copy items into new array
        if (new_capacity > capacity) {
            System.arraycopy(items, first, a, 0, capacity - first);
            System.arraycopy(items, 0, a, capacity - first, first);
            last = capacity - 1;
        } else {
            int j = 0;
            for (int i = 0; i < capacity; ++i) if (items[i] != null) a[j++] = items[i];
            last = new_capacity - 1;
        }
        capacity = new_capacity;
        first = 0;
        items = a;
    }

    public void addFirst(T item) {
        // Update first
        if (--first < 0) first += capacity;
        // Add item to items[first]
        items[first] = item;
        // Double the size of the array if capacity is reached, and aupdate size
        if (++size == capacity) resize(capacity * 2);
    }

    public void addLast(T item) {
        // Update last
        if (++last >= capacity) last -= capacity;
        // Add item to the last index
        items[last] = item;
        // Double the size of the array if capacity is reached, and update size
        if (++size == capacity) resize(capacity * 2);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int ptr = first - 1;
        for (int i = 0; i < size; i++) {
            if (++ptr >= capacity) ptr -= capacity;
            System.out.print(items[ptr].toString() + " ");
        }
        System.out.print("\n");
    }

    public T removeFirst() {
        if (size == 0) return null;
        // Remove item from items[first]
        T temp = items[first];
        items[first] = null;
        // Update first
        if (++first >= capacity) first -= capacity;
        // Shrink the array if size = 1/4 of the capacity, and update size
        if (--size == 1/4 * capacity && size > 1) resize(1/4 * capacity);
        return temp;
    }

    public T removeLast() {
        if (size == 0) return null;
        // Remove item from items[last]
        T temp = items[last];
        items[last] = null;
        // Update last
        if (--last < 0 ) last += capacity;
        // Shrink the array if size = 1/4 of the capacity, and update size
        if (--size == 1/4 * capacity && size > 1) resize(1/4 * capacity);
        return temp;
    }

    public T get(int index) {
        if (first + index < capacity) return items[first + index];
        return items[first + index - capacity];
    }

    /* Implement iterator */
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    public class ArrayDequeIterator implements Iterator {
        int pos;

        public ArrayDequeIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public Object next() {
            T returnItem = get(pos);
            pos++;
            return returnItem;
        }
    }

    @Override
    /* Implement equals */
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        ArrayDeque<T> o = (ArrayDeque<T>) other;
        if (o.size() != this.size()) return false;
        for (int i = 0; i < size; i++) {
            T thisItem = this.get(i);
            T otherItem = o.get(i);
            if (!thisItem.equals(otherItem)) return false;
        }
        return true;
    }
}
