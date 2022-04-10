package deque;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        ArrayDeque<String> ad1 = new ArrayDeque<String>();

        assertTrue("A newly initialized ADeque should be empty", ad1.isEmpty());
        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad1.size());
        assertFalse("ad1 should now contain 1 item", ad1.isEmpty());

        ad1.addLast("middle");
        assertEquals(2, ad1.size());

        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("Printing out deque: ");
        ad1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue("ad1 should be empty upon initialization", ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());

        ad1.removeFirst();
        // should be empty
        assertTrue("ad1 should be empty after removal", ad1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(3);

        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();

        int size = ad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String>  ad1 = new ArrayDeque<String>();
        ArrayDeque<Double>  ad2 = new ArrayDeque<Double>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<Boolean>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

        String s = ad1.removeFirst();
        double d = ad2.removeFirst();
        boolean b = ad3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, ad1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigADequeTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            ad1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) ad1.removeLast(), 0.0);
        }
    }

    @Test
    /* Simulation of AG test b01 */
    public void AGGetTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 6; i++) {
            ad1.addLast(i);
        }
        assertEquals("Should have the same value", 0, (double) ad1.get(0), 0.0);
    }

    @Test
    /* Simulation of AG test b03 */
    public void AGFillUpTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // Fill up
        for (int i = 0; i < 10000; i++) {
            ad1.addLast(i);
        }
        // Empty
        for (int i = 0; i < 10000; i++) {
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }
        // Fill up again
        for (int i = 0; i < 10000; i++) {
            ad1.addLast(i);
        }
        assertEquals("Should have the same value", 9999, ad1.get(9999), 0.0);
    }

    @Test
    public void equalTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(15);
        ad1.addLast(20);
        ad1.addLast(10);

        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        ad2.addFirst(15);
        ad2.addLast(20);
        ad2.addLast(10);

        boolean res = ad1.equals(ad2);
        assertEquals("Should be equal, ", res, true);
    }

    @Test
    public void notEqualTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(15);
        ad1.addLast(20);
        ad1.addLast(11);

        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        ad2.addFirst(15);
        ad2.addLast(20);
        ad2.addLast(10);

        boolean res = ad1.equals(ad2);
        assertNotEquals("Should not be equal, ", res, true);
    }

    @Test
    public void iteratorTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 10000; i++) ad1.addLast(i);
        for (int ignored : ad1) ad1.removeFirst();
        for (int ignored : ad1) ad1.removeLast();

        assertEquals("Should be equal ", 2500, ad1.size());
    }

    @Test
    public void equalCrossTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();

        for (int i = 0; i < 100; i++) {
            int num = (int) Math.round(Math.random() * 100);
            ad.addLast(num);
            lld.addLast(num);
        }

        boolean isEqual = ad.equals(lld);
        assertTrue(isEqual);
    }

    @Test
    /* Ensure that you resize down after removals */
    public void resizeTest() {
        int N = 64;
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        // Insert N items
        for (int i = 0; i < N; i++) {
            int num = (int) Math.round(Math.random() * 100);
            ad.addLast(num);
        }

        // Remove all but one
        for (int i = 0; i < N - 1; i++) {
            ad.removeLast();
        }
    }

    @Test
    /* Random addLast removeLast test */
    public void AGAddRemoveTest1() {

        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ArrayList<Integer> aList = new ArrayList<>();

        /* Generate random float point numbers between 0 and 1 */
        for (int i = 0; i < 5000; i++) {
            int num = (int) Math.round(100 * Math.random());
            aList.add(num);
        }

        /* Add numbers to array deque */
        for (Integer integer : aList) {
            ad.addLast(integer);
        }

        /* Check if all numbers are equal */
        assertEquals(ad.size(), aList.size());
        for (int k = 0; k < ad.size(); k++) {
            assertEquals(ad.get(k), aList.get(k));
        }

        /* Remove all numbers from the back of array deque */
        for (int l = 0; l < aList.size(); l++) {
            int lldItem = ad.removeLast();
            assert lldItem == aList.get(aList.size() - l - 1);
        }

        /* Check size of linkedListDeque */
        int lldSize = ad.size();
        assertEquals(0, lldSize);
    }

    @Test
    public void AGTestD006() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        ad.addLast(0);
        ad.addFirst(1);
        ad.isEmpty();
        ad.addFirst(3);
        ad.addLast(4);
        ad.removeLast();
        ad.removeFirst();
        ad.addFirst(7);

        int ans = ad.removeLast();
        assert ans == 0;
    }

    @Test
    public void AGTestD005() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        ad.addLast(0);
        ad.addLast(1);
        ad.addLast(2);
        ad.removeFirst();
        ad.addLast(4);
        ad.addLast(5);
        ad.addLast(6);
        ad.addLast(7);
        ad.addLast(8);
        ad.addLast(9);

        int ans = ad.removeFirst();
        assert ans == 1;
    }

    @Test
    public void AGTestD011() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        ad.addLast(0);
        ad.get(0);
        ad.addFirst(2);
        ad.removeFirst();
        ad.addLast(7);
        ad.addFirst(9);
        ad.removeFirst();
        int ans = ad.get(1);
        assert ans == 7;
        ad.addFirst(12);
        ad.removeLast();
        ad.addFirst(14);
        ans = ad.get(1);
        assert ans == 12;
        ans = ad.get(2);
        assert ans == 0;
    }

    @Test
    public void testRandomGet() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        ad.addLast(0);
        ad.addLast(1);
        ad.addFirst(2);
        int ans = ad.removeLast();
        assert ans == 1;
        ans = ad.removeFirst();
        assert ans == 2;
        ad.addLast(5);
        ans = ad.removeLast();
        assert ans == 5;

        ans = ad.removeLast();
        assert ans == 0;
        ad.addLast(8);
        ans = ad.removeFirst();
        assert ans == 8;
        ad.addFirst(10);
        ad.addLast(11);
        ad.addFirst(12);

        ans = ad.removeFirst();
        assert ans == 12;
        ad.addFirst(14);
        ans = ad.get(2);
        assert ans == 11;
        ad.addLast(16);
        ans = ad.removeLast();
        assert ans == 16;
        ans = ad.get(0);
        assert ans == 14;
        ans = ad.removeLast();
        assert ans == 11;
        ad.addLast(20);
        ad.addFirst(21);
        ans = ad.removeFirst();
        assert ans == 21;
        ans = ad.removeLast();
        assert ans == 20;
    }

    /* TODO simulate random test */
    /* Goal: make 500 random calls to addFirst removeLast and isEmpty */
    @Test
    public void simulateRandomTest() {
        // Step 1: create a matrix of probability
        double m[] = {0.5, 0.4, 0.1};
        int a[] = randomSimulator(m);

        ArrayDeque<Integer> ad = new ArrayDeque<>();

        // Step 2: call functions accordingly
        for (int i = 0; i < 20; i++) {
            int randomIndex = (int) Math.round(Math.random() * 10); // TODO fix array index out of bound error
            int funcIndex = (int) a[randomIndex];

            if (funcIndex == 0) {
                // call addFirst
                int numToInsert = (int) Math.round(Math.random() * 100);
                ad.addFirst(numToInsert);
                System.out.println("Inserted " + numToInsert + " into the deque");
            } else if (funcIndex == 1) {
                // call removeLast
                if (ad.isEmpty()) {
                    continue;
                }
                int numRemoved = ad.removeLast();
                System.out.println("Removed " + numRemoved + " from the deque");
            } else {
                // call isEmpty
                ad.isEmpty();
                System.out.println("isEmpty? ");
            }
        }
    }

    private int[] randomSimulator(double m[]) {
        int[] a = new int[10];
        // Make sure the sum of m is 1
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        assert (int) Math.round(sum) == 1;
        // insert 0, 1, 2 into the array by probability distribution
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < m.length; j++) {
                int numIns = (int) Math.round(10 * m[j]);
                for (int k = 0; k < numIns; k++) {
                    a[i++] = j;
                }
            }
        }
        return a;
    }
}
