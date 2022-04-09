package deque;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    /* Test linked list deque iterator */
    public void iteratorTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<Integer>();
        for (int i = 0; i < 10000; i++) {
            lld.addLast(i);
        }
        for (int ignored : lld) {
            lld.removeLast();
        }
        assertEquals("Should have the same value", 5000, lld.size());
    }

    @Test
    /* Test linked list deque equals */
    public void notEqualTest() {

        LinkedListDeque<Integer> deque1 = new LinkedListDeque<>();
        deque1.addFirst(15);
        deque1.addFirst(20);
        deque1.addLast(10);

        LinkedListDeque<Integer> deque2 = new LinkedListDeque<>();
        deque1.addFirst(15);
        deque1.addLast(20);
        deque1.addLast(26);

        boolean res = deque1.equals(deque2);
        assertNotEquals("Should not be equal, ", res, true);
    }

    @Test
    public void equalTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(15);
        lld1.addLast(20);
        lld1.addLast(10);

        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();
        lld2.addFirst(15);
        lld2.addLast(20);
        lld2.addLast(10);

        boolean res = lld1.equals(lld2);
        assertEquals("Should be equal, ", res, true);
    }

    @Test
    /* Test getRecursive */
    public void getRecursiveTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();

        for (int i = 0; i < 1000; i++) {
            lld.addLast(i);
        }

        for (int i = 0; i < lld.size(); i++) {
            int val = (int) lld.getRecursive(i);
            assertEquals(i, val);
        }

        System.out.println("Check curNode position");
    }

    @Test
    /* Random addLast removeFirst test */
    public void AGAddRemoveTest1() {

        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        ArrayList<Integer> aList = new ArrayList<>();

        /* Generate random float point numbers between 0 and 1 */
        for (int i = 0; i < 1000; i++) {
            int num = (int) Math.round(100 * Math.random());
            aList.add(num);
        }

        /* Add numbers to linkedListDeque */
        for (int j = 0; j < aList.size(); j++) {
            lld.addLast(aList.get(j));
        }

        /* Check if all numbers are equal */
        assertEquals(lld.size(), aList.size());
        for (int k = 0; k < lld.size(); k++) {
            assertEquals(lld.get(k), aList.get(k));
        }

        /* Remove all numbers from the back of queue */
        for (int l = 0; l < aList.size(); l++) {
            int lldItem = lld.removeLast();
            assert lldItem == aList.get(aList.size() - l - 1);
        }

        /* Check size of linkedListDeque */
        int lldSize = lld.size();
        assertEquals(0, lldSize);
    }

    @Test
    /* Random addFirst removeLast test */
    public void AGAddRemoveTest2() {

        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        ArrayList<Integer> aList = new ArrayList<>();

        /* Generate random float point numbers between 0 and 1 */
        for (int i = 0; i < 1000; i++) {
            int num = (int) Math.round(100 * Math.random());
            aList.add(num);
        }

        /* Add numbers to linkedListDeque */
        for (int j = 0; j < aList.size(); j++) {
            lld.addFirst(aList.get(j));
        }

        /* Check if all numbers are equal */
        assertEquals(lld.size(), aList.size());
        for (int k = 0; k < lld.size(); k++) {
            assertEquals(lld.get(k), aList.get(aList.size() - k - 1));
        }

        /* Remove all numbers from the back of queue */
        for (int l = 0; l < aList.size(); l++) {
            int lldItem = lld.removeLast();
            assert lldItem == aList.get(l);
        }

        /* Check size of linkedListDeque */
        int lldSize = lld.size();
        assertEquals(0, lldSize);
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

        boolean isEqual = lld.equals(ad);
        assertTrue(isEqual);
    }
}
