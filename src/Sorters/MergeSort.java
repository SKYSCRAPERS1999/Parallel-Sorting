package Sorters;

import Utils.Sortings;

import java.util.concurrent.RecursiveAction;

public class MergeSort extends RecursiveAction {
    private int[] numbers;
    private int l, r;
    public static boolean isParallel;
    private static final int bound = 1000;

    public MergeSort(int[] numbers, int l, int r) {
        this.numbers = numbers;
        this.l = l;
        this.r = r;
    }

    @Override
    protected void compute() {
        if (isParallel) {
            if (l < r) {
                if (r - l <= bound) {
                    Sortings.MergeSort.sort(numbers, l, r);
                    return;
                }
                int m = (l + r) / 2;
                RecursiveAction lSort = new MergeSort(numbers, l, m);
                RecursiveAction rSort = new MergeSort(numbers, m + 1, r);
                invokeAll(lSort, rSort);
                Sortings.MergeSort.merge(numbers, l, r);
            }
        } else {
            Sortings.MergeSort.sort(numbers, l, r);
        }
    }
}

