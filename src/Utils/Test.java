package Utils;

import Sorters.*;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Test {
    private int[] numbers;
    private boolean outArray;
    private Random rand;
    private boolean isParallel;
    public static double TestNanoSeconds = 0.0;

    public Test(int[] numbers, boolean outArray, int SortType, int isParallel, int ParallelType) {
        this.numbers = numbers;
        this.outArray = outArray;
        this.rand = new Random(System.currentTimeMillis());
        // Random change
        numbers[Math.abs(rand.nextInt() % numbers.length)] = rand.nextInt();
        this.isParallel = (isParallel != 0);
        switch (SortType) {
            case 0:
                TestEnum(this.isParallel);
                break;
            case 1:
                TestQuick(this.isParallel);
                break;
            case 2:
                TestMerge(this.isParallel, ParallelType);
                break;
        }
    }

    private void printTime(double TotalTime) {
        this.TestNanoSeconds = TotalTime;
        System.out.printf("Time: %.3f milliseconds%n", TotalTime / 1000000.0);
    }

    private void printArray(int[] array) {
        if (outArray) {
            for (int x : array) System.out.printf("%d ", x);
        } else {
            // Random output
            System.out.printf("Random output: %d", array[Math.abs(rand.nextInt() % array.length)]);
        }
        System.out.println();
        if (Sortings.isSorted(array)) System.out.println("Is Sorted.");
        else System.out.println("Not Sorted Yet!");
    }

    private void TestMerge(Boolean isParallel, int ParallelType) {
        long startTime = System.nanoTime();
        if (!isParallel) {
            MergeSort.isParallel = false;
            new ForkJoinPool().invoke(new MergeSort(numbers, 0, numbers.length - 1));
        } else {
            switch (ParallelType) {
                case 0:
                    MergeSort.isParallel = true;
                    new ForkJoinPool().invoke(new MergeSort(numbers, 0, numbers.length - 1));
                    break;
                case 1:
                    new MergeSortComplex().ParallelSort(numbers);
                    break;
                case 2:
                    new MergeSortComplexToo().ParallelSort(numbers);
                    break;
            }
        }
        long TotalTime = System.nanoTime() - startTime;
        printTime(TotalTime);
        printArray(numbers);
    }

    public void TestMergeSortParallel() {
        System.out.printf("%s: %n", Thread.currentThread().getStackTrace()[1].getMethodName());
        TestMerge(true, 0);
    }

    public void TestMergeSortComplex() {
        System.out.printf("%s: %n", Thread.currentThread().getStackTrace()[1].getMethodName());
        TestMerge(true, 1);
    }

    public void TestMergeSortComplexToo() {
        System.out.printf("%s: %n", Thread.currentThread().getStackTrace()[1].getMethodName());
        TestMerge(true, 2);
    }

    public void TestMergeSortSequential() {
        System.out.printf("%s: %n", Thread.currentThread().getStackTrace()[1].getMethodName());
        TestMerge(false, 0);
    }

    private void TestEnum(Boolean isParallel) {

        long startTime = System.nanoTime();
        new EnumSort(isParallel).run(numbers);
        long TotalTime = System.nanoTime() - startTime;

        printTime(TotalTime);
        printArray(numbers);
    }

    public void TestEnumSortParallel() {
        System.out.printf("%s: %n", Thread.currentThread().getStackTrace()[1].getMethodName());
        TestEnum(true);
    }

    public void TestEnumSortSequential() {
        System.out.printf("%s: %n", Thread.currentThread().getStackTrace()[1].getMethodName());
        TestEnum(false);
    }

    private void TestQuick(Boolean isParallel) {

        long startTime = System.nanoTime();
        QuickSort.isParallel = isParallel;
        new ForkJoinPool().invoke(new QuickSort(numbers, 0, numbers.length - 1));
        long TotalTime = System.nanoTime() - startTime;

        printTime(TotalTime);
        printArray(numbers);
    }

    public void TestQuickSortParallel() {
        System.out.printf("%s: %n", Thread.currentThread().getStackTrace()[1].getMethodName());
        TestQuick(true);
    }

    public void TestQuickSortSequential() {
        System.out.printf("%s: %n", Thread.currentThread().getStackTrace()[1].getMethodName());
        TestQuick(false);
    }
}


