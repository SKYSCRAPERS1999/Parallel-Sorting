package Sorters;

import Utils.Sortings;

import java.util.concurrent.RecursiveAction;

public class QuickSort extends RecursiveAction {
    private int l, r;
    private int[] numbers;
    public static boolean isParallel;
    private static final int bound = 1000;

    public QuickSort(int[] numbers, int l, int r) {
        this.numbers = numbers;
        this.l = l;
        this.r = r;
    }

    @Override
    protected void compute() {
        if (isParallel) {
            if (l < r) {
                if (r - l <= bound) {
                    Sortings.QuickSort.sort(numbers, l, r);
                } else {
                    int m = Sortings.QuickSort.partition(numbers, l, r);
                    RecursiveAction lSort = new QuickSort(numbers, l, m - 1);
                    RecursiveAction rSort = new QuickSort(numbers, m + 1, r);
                    invokeAll(lSort, rSort);
                }
            }
        } else {
            Sortings.QuickSort.sort(numbers, 0, numbers.length - 1);
        }
    }

}
