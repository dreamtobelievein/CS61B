package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        AList<Integer> Ns = new AList<>();
        AList<Integer> opCounts = new AList<>();
        int M = 10000;
        for (int i = 1000; i <= 128000; i *= 2) {
            Ns.addLast(i);
            opCounts.addLast(M);
        }

        AList<Double> times = getTimes(Ns, M);
        printTimingTable(Ns, times, opCounts);
    }

    private static AList<Double> getTimes(AList<Integer> Ns, int M) {
        AList<Double> times = new AList<>();
        int elem = 1;
        for (int i = 0; i < Ns.size(); i++) {
            int N = Ns.get(i);
            SLList<Integer> test = new SLList<>();
            for (int j = 0; j < N; j++) {
                test.addFirst(elem);
            }

            Stopwatch sw = new Stopwatch();
            for (int k = 0; k < M; k++) {
                test.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
        return times;
    }
}
