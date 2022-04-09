package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {
    @Test
    public void testIntegerMaxArrayDeque() {
        /* Compare integer */
        Comparator<Integer> c = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(c);
        mad.addFirst(15);
        mad.addLast(32);
        mad.addLast(24);

        boolean ans = mad.max() == 32;
        assertEquals("Should be equal", true, ans);
    }

    @Test
    public void testStringMaxArrayDeque() {
        /* Compare string letter by letter */
        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        MaxArrayDeque<String> mad = new MaxArrayDeque<>(c);
        mad.addFirst("Alissa");
        mad.addLast("Bob");
        mad.addLast("Curt");

        assertEquals("Should be equal", "Curt", mad.max());
    }

    @Test
    public void testMaxArrayDequeComparator() {
        /* Comparator c compares letters of string */
        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        /* Comparator cPrime compares length of string */
        Comparator<String> cPrime = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        };

        MaxArrayDeque<String> mad = new MaxArrayDeque<>(c);

        mad.addFirst("Ali");
        mad.addLast("Alice");
        mad.addLast("Bob");

        assertEquals("Should be equal", "Alice", mad.max(cPrime)); // Should return the longest string
    }

    @Test
    public void testNullMaxArrayDeque() {
        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        MaxArrayDeque<String> mad = new MaxArrayDeque<>(c);

        boolean ans = mad.max() == null; // Should return null when MaxArrayDeque is empty
        assertEquals("Should be equal", true, ans);
    }

    @Test
    public void testIterator() {
        Comparator<Integer> c = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(c);

        for (int i = 0; i < 1000; i++) {
            mad.addFirst(i);
        }
        for (int item : mad) {
            mad.removeLast();
        }

        assertEquals(500, mad.size());
    }
}
