package deque;

import org.junit.Test;

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

        int cap = ad.getCapacity();
        assertEquals(2, cap); // There are some problems with the resize method
    }
}
