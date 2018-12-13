import Utils.Test;
import com.sun.deploy.panel.ITreeNode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static String INPUT = "src/random.txt";
    public static List<Integer> vec = new ArrayList<>();
    public static int cpuCoreNumber = Runtime.getRuntime().availableProcessors();
    public static int BigInputSize = 100000000;
    public static boolean isBigInput = false;

    // Fix the seed as my birthday for generating big inputs
    public static Random rand = new Random(19990130);

    public static int IterNum = 0;

    public static int SortType = 0;
    public static int isParallel = 0;
    public static int ParallelType = 0;

    public static double SumNanoSecond = 0.0;
    public static double MinNanoSecond = 0.0;
    public static String[] MethodNames = {
            "",
            "EnumSort Sequential", "EnumSort Parallel",
            "QuickSort Sequential", "QuickSort Parallel",
            "MergeSort Sequential", "MergeSort Parallel Simple", "MergeSort Parallel Complex", "MergeSort Parallel Complex Too"
    };

    public static void Prepare() {
        System.out.printf("cpuCoreNumber: %d%n%n", cpuCoreNumber);
        if (isBigInput) {
            for (int i = 0; i < BigInputSize; i++) vec.add(rand.nextInt());
            IterNum = 10;
        } else {
            try {
                FileInputStream instream = new FileInputStream(INPUT);
                System.setIn(instream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Scanner cin = new Scanner(new BufferedInputStream(System.in));
            while (cin.hasNext()) vec.add(cin.nextInt());
            IterNum = 50;
        }
    }

    public static int[] Vec2Arr(List<Integer> vec) {
        return vec.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void OutputToFile(int id, int Iter) {

        String info = String.format("%s: %nAverage Time after warming of %d iterations: %.3f milliseconds%n" +
                        "Minimum Time after warming of %d iterations: %.3f milliseconds%n",
                MethodNames[id], Iter, (SumNanoSecond / (1.0 * Iter)) / 1000000.0,
                Iter, MinNanoSecond / 1000000.0);

        String OUTPUT_INFO = String.format("info%d%s.txt", id,
                isBigInput ? String.format("_%d", BigInputSize) : "");
        String OUTPUT_NUMBER = String.format("order%d%s.txt", id,
                isBigInput ? String.format("_%d", BigInputSize) : "");

        try (PrintWriter fp = new PrintWriter(OUTPUT_INFO)) {
            fp.println(info);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int[] numbers = Vec2Arr(vec);
        new Test(Vec2Arr(vec), false, SortType, isParallel, ParallelType);
        try (PrintWriter fp = new PrintWriter(OUTPUT_NUMBER)) {
            for (int x : numbers) {
                fp.print(x);
                fp.print(' ');
            }
            fp.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        isBigInput = false; // Have to set
        if (args.length == 0) {
            isBigInput = false;
        } else {
            isBigInput = true;
            BigInputSize = Integer.valueOf(args[0]);
        }
        Prepare();
        // Iterate for all types of Sorts
        for (SortType = isBigInput ? 1 : 0; SortType < 3; SortType++) {
            for (isParallel = 0; isParallel < 2; isParallel++) {
                for (ParallelType = 0; ParallelType < 1 + 2 * (SortType == 2 && isParallel == 1 ? 1 : 0); ParallelType++) {
                    SumNanoSecond = 0.0;
                    MinNanoSecond = Double.MAX_VALUE;

                    // First Iter iterations WarmUp, then Iter iterations calculate
                    int Iter = SortType == 0 ? IterNum / 10 : IterNum;
                    for (int i = 0; i < Iter + Iter; i++) {
                        new Test(Vec2Arr(vec), false, SortType, isParallel, ParallelType);
                        if (i >= Iter) {
                            SumNanoSecond += Test.TestNanoSeconds;
                            MinNanoSecond = Math.min(MinNanoSecond, Test.TestNanoSeconds);
                        }
                        System.out.println();
                    }

                    int id = 2 * SortType + isParallel + ParallelType + 1;

                    System.out.printf("Above is in %s: %n", MethodNames[id]);
                    System.out.printf("Average Time after warming of %d iterations: %.3f milliseconds%n",
                            Iter, (SumNanoSecond / (1.0 * Iter)) / 1000000.0);
                    System.out.printf("Minimum Time after warming of %d iterations: %.3f milliseconds%n",
                            Iter, (MinNanoSecond / 1000000.0));

                    OutputToFile(id, Iter);
                }
            }
        }

    }
}
