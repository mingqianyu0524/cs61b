package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> myComparator;
    /* Creates a MaxArrayDeque with the given Comparator. */
    public MaxArrayDeque(Comparator<T> c) {
        myComparator = c;
    }

    /* Returns the maximum element in the deque as governed by the previously given Comparator.
     * If the MaxArrayDeque is empty, simply return null.
     * */
    public T max() {
        if (this == null) {
            return null;
        }
        T curMax = this.get(0);
        for (int i = 0; i < this.size(); i++) {
            T curItem = this.get(i);
            if (myComparator.compare(curItem, curMax) > 0) {
                curMax = curItem;
            }
        }
        return curMax;
    }

    /* Returns the maximum element in the deque as governed by the parameter Comparator c.
     * If the MaxArrayDeque is empty, simply return null.
     * */
    public T max(Comparator<T> c) {
        if (this == null) {
            return null;
        }
        T curMax = this.get(0);
        for (int i = 0; i < this.size(); i++) {
            T curItem = this.get(i);
            if (c.compare(curItem, curMax) > 0) {
                curMax = curItem;
            }
        }
        return curMax;
    }

    public Iterator<T> iterator() {
        return new MaxArrayDequeIterator();
    }

    /* MaxArrayDeque iterator object */
    private class MaxArrayDequeIterator implements Iterator<T> {
        int pos;

        public MaxArrayDequeIterator() {
            pos = 0;
        }

        @Override
        public boolean hasNext() {
            return pos < size();
        }

        @Override
        public T next() {
            T returnItem = get(pos);
            pos++;
            return returnItem;
        }
    }
}
