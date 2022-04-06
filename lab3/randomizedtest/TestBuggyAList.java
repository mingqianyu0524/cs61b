package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> BL = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);

            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                BL.addLast(randVal);

                assertEquals(L.size(), BL.size());
                assertEquals(L.getLast(), BL.getLast());

                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                assertEquals(L.size(), BL.size());
                if (L.size() > 0) {
                    int LLast = L.getLast();
                    int BLLast = BL.getLast();

                    assertEquals(LLast, BLLast);
                    System.out.println("getLast(" + LLast + ")");
                }
            } else if (operationNumber == 2) {
                assertEquals(L.size(), BL.size());
                if (L.size() > 0) {
                    int LLast = L.removeLast();
                    int BLLast = BL.removeLast();

                    assertEquals(LLast, BLLast);
                    System.out.println("removeLast(" + LLast + ")");
                }
            } else {
                int LSize = L.size();
                int BLSize = BL.size();

                assertEquals(L.size(), BL.size());
                System.out.println("size: " + LSize);
            }
        }
    }
}
