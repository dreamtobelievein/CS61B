package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> workingList = new AListNoResizing<>();
        BuggyAList<Integer> buggyList = new BuggyAList<>();
        for (int i = 4; i <= 6; i++) {
            workingList.addLast(i);
            buggyList.addLast(i);
        }

        assertEquals(workingList.removeLast(), buggyList.removeLast());
        assertEquals(workingList.removeLast(), buggyList.removeLast());
        assertEquals(workingList.removeLast(), buggyList.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int size2 = B.size();
                assertEquals(size, size2);
                System.out.println("size: " + size);
            } else if (L.size() > 0) {
                if (operationNumber == 2) {
                    int lastNum = L.getLast();
                    int lastNum2 = B.getLast();
                    assertEquals(lastNum, lastNum2);
                    System.out.println("last number: " + lastNum);
                } else if (operationNumber == 3) {
                    int lastNum = L.removeLast();
                    int lastNum2 = B.removeLast();
                    assertEquals(lastNum, lastNum2);
                    System.out.println("removeLast(" + lastNum + ")");
                }
            }
        }
    }
}
