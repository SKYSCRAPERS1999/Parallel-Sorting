package Utils;

public class Sortings {

    public static class MergeSort {
        public static void merge(int[] numbers, int l, int r) {
            int[] buffers = new int[r - l + 1];
            int m = (l + r) / 2;
            int i = l, j = m + 1, pos = 0;
            for (; i <= m && j <= r; pos++) {
                if (numbers[i] <= numbers[j]) {
                    buffers[pos] = numbers[i];
                    i++;
                } else {
                    buffers[pos] = numbers[j];
                    j++;
                }
            }
            for (; i <= m; i++, pos++) buffers[pos] = numbers[i];
            for (; j <= r; j++, pos++) buffers[pos] = numbers[j];
            for (int k = l; k <= r; k++) numbers[k] = buffers[k - l];
        }

        public static void sort(int[] numbers, int l, int r) {
            if (l < r) {
                int m = (l + r) / 2;
                sort(numbers, l, m);
                sort(numbers, m + 1, r);
                merge(numbers, l, r);
            }
        }

    }

    public static class InsertionSort {
        public static void sort(int[] numbers, int l, int r) {
            int i, val, j;
            for (i = l + 1; i <= r; i++) {
                val = numbers[i];
                j = i - 1;
                while (j >= l && numbers[j] > val) {
                    numbers[j + 1] = numbers[j];
                    j = j - 1;
                }
                numbers[j + 1] = val;
            }
        }
    }

    public static class QuickSort {

        public static void swap(int[] numbers, int l, int r) {
            int t = numbers[l];
            numbers[l] = numbers[r];
            numbers[r] = t;
        }

        public static int partition(int[] numbers, int l, int r) {
            int m = (l + r) / 2;
            int val = numbers[m];
            swap(numbers, m, r);
            int inter = l, end;
            for (end = l; end < r; end++) {
                if (numbers[end] < val) {
                    swap(numbers, inter, end);
                    ++inter;
                }
            }
            swap(numbers, inter, r);
            return inter;
        }

        public static void __put_median(int[] numbers, int l, int r) {
            int m = l + (r - l) / 2;
            if (numbers[l] > numbers[r]) swap(numbers, l, r);
            if (numbers[r] > numbers[m]) swap(numbers, r, m);
            if (numbers[l] < numbers[r]) swap(numbers, l, r);
        }

        public static int __partition(int[] numbers, int l, int r, int pivot) {
//            int i = l - 1, j = r + 1;
            int i = l - 1, j = r + 1;
            int pval = numbers[pivot];
            while (true) {
                ++i;
                while (numbers[i] < pval) ++i;
                --j;
                while (numbers[j] > pval) --j;
                if (!(i < j)) return i;
                swap(numbers, i, j);
            }
        }

        public static void sort(int[] numbers, int l, int r) {
            if (l < r) {
                if (r - l < 16) {
                    Sortings.InsertionSort.sort(numbers, l, r);
                } else {
                    __put_median(numbers, l, r);
                    int m = __partition(numbers, l + 1, r, l);
                    sort(numbers, l, m - 1);
                    sort(numbers, m, r);
//                    int m = partition(numbers, l, r);
//                    sort(numbers, l, m - 1);
//                    sort(numbers, m + 1, r);
                }
            }
        }

    }

    public static boolean isSorted(int[] numbers, int l, int r) {
        for (int i = l; i < r; i++) {
            if (numbers[i] > numbers[i + 1]) {
                // Output Error
                System.out.printf("Error sublist(error in %d): ", i);
                for (int j = Math.max(l, i - 3); j < Math.min(r, i + 3); j++) {
                    System.out.print(numbers[j] + " ");
                    if (j == i) System.out.print(" !!! ");
                }
                System.out.println();
                return false;
            }
        }
        return true;
    }

    public static boolean isSorted(int[] numbers) {
        return isSorted(numbers, 0, numbers.length - 1);
    }
}