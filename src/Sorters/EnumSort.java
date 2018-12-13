package Sorters;

import java.util.concurrent.*;

import static java.lang.Runtime.getRuntime;

public class EnumSort {
    private ExecutorService exec;
    private CompletionService<Rank> completionService;
    private static int cpuCoreNumber = getRuntime().availableProcessors();
    private boolean isParallel;

    public EnumSort(boolean isParallel) {
        this.isParallel = isParallel;
    }

    class Rank {
        public int pos, rank;

        Rank(int pos, int rank) {
            this.pos = pos;
            this.rank = rank;
        }
    }

    private class SubSorter implements Callable<Rank> {
        private int[] numbers;
        private int pos, rank;

        SubSorter(int[] numbers, int pos) {
            this.numbers = numbers;
            this.pos = pos;
            this.rank = 0;
        }

        public Rank call() {
            for (int i = 0; i < numbers.length; i++) {
                if (numbers[pos] > numbers[i] || (numbers[pos] == numbers[i] && pos > i)) {
                    rank++;
                }
            }
            return new Rank(pos, rank);
        }
    }

    void solve(int[] numbers) {
        int n = numbers.length;
        int[] results = new int[n];
        for (int i = 0; i < n; i++) {
            completionService.submit(new SubSorter(numbers, i));
        }
        for (int i = 0; i < n; i++) {
            try {
                Rank res = completionService.take().get();
                results[res.rank] = numbers[res.pos];
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();
        for (int i = 0; i < n; i++) numbers[i] = results[i];
    }

    public void run(int[] numbers){
        int parallelNumber = isParallel ? cpuCoreNumber : 1;
        exec = Executors.newFixedThreadPool(parallelNumber);
        completionService = new ExecutorCompletionService<>(exec);
        solve(numbers);
    }
}
