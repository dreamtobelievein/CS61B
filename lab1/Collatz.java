/** Class that prints the Collatz sequence starting from a given number.
 *  @author Jibing Yang
 */
public class Collatz {

    /** Generate the next number of a Collatz sequence. */
    public static int nextNumber(int n) {
        if (n % 2 == 0) {
            return n / 2;
        } else {
            return n * 3 + 1;
        }
    }

    /** Print the Collatz sequence starting at 5. */
    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

