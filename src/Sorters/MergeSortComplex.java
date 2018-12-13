package Sorters;

import Utils.Sortings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MergeSortComplex {

    private ExecutorService exec;
    private CompletionService<Void> completionService;
    private int parallelNumber;

    public MergeSortComplex() {
        this.parallelNumber = Runtime.getRuntime().availableProcessors();
        exec = Executors.newFixedThreadPool(parallelNumber);
        completionService = new ExecutorCompletionService<>(exec);
    }

    private class SubSorter implements Callable<Void> {
        private int[] numbers;
        private int l, r;

        public SubSorter(int[] numbers, int l, int r) {
            this.numbers = numbers;
            this.l = l;
            this.r = r;
        }

        public Void call() {
            Sortings.QuickSort.sort(numbers, l, r);
            return null;
        }
    }

    @SuppressWarnings("Duplicates")
    public void ParallelSort(int[] numbers) {

        int sliceLen = numbers.length / parallelNumber + 1;
        int pivotInterval = sliceLen / parallelNumber;

        int[] pivots = new int[parallelNumber * parallelNumber];
        int[] spliters = new int[parallelNumber];

        List<List<Integer>> segList = new ArrayList<>();
        int[][] segArray = new int[parallelNumber][];
        for (int i = 0; i < parallelNumber; i++) {
            segList.add(new ArrayList<>());
        }

        // SubSort
        for (int i = 0; i < parallelNumber; i++) {
            int start = i * sliceLen, end = Math.min(numbers.length, (i + 1) * sliceLen);
            SubSorter subCalc = new SubSorter(numbers, start, end - 1);
            completionService.submit(subCalc);
        }

        for (int i = 0; i < parallelNumber; i++) {
            try {
                completionService.take().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Select Pivots, then spliters
        for (int i = 0; i * pivotInterval < numbers.length && i < parallelNumber * parallelNumber; i++) {
            pivots[i] = numbers[i * pivotInterval];
        }
        Sortings.MergeSort.sort(pivots, 0, pivots.length - 1);

        for (int i = 0; i < parallelNumber - 1; i++) {
            // Add parallelNumber - 1 pivots as spliters
            spliters[i] = pivots[(i + 1) * parallelNumber];
        }
        // Padding the spliter by INT_MAX
        spliters[parallelNumber - 1] = Integer.MAX_VALUE;

        // Split by spliters
        for (int i = 0; i < parallelNumber; i++) {
            int start = i * sliceLen, end = Math.min(numbers.length, sliceLen * (i + 1));
            for (int j = start, k = 0; j < end && k < parallelNumber; ) {
                if (spliters[k] < numbers[j]) k++;
                else segList.get(k).add(numbers[j++]);
            }
        }

        // Change into array
        for (int i = 0; i < parallelNumber; i++) {
            segArray[i] = segList.get(i).stream().mapToInt(Integer::intValue).toArray();
        }

        for (int i = 0; i < parallelNumber; i++) {
            SubSorter subCalc = new SubSorter(segArray[i], 0, segArray[i].length - 1);
            completionService.submit(subCalc);
        }

        for (int i = 0; i < parallelNumber; i++) {
            try {
                completionService.take().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Concatenate the sorted segments
        int pos = 0;
        for (int i = 0; i < segArray.length; i++) {
            for (int j = 0; j < segArray[i].length; j++) {
                numbers[pos++] = segArray[i][j];
            }
        }

        assert pos == numbers.length;

        exec.shutdown();
    }
}