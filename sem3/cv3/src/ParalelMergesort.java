import java.util.Random;
import java.util.concurrent.*;

public class ParalelMergesort {

    private int[] arr;
    private final ForkJoinPool pool;

    public ParalelMergesort(int x, int y) {
        this.arr = generateRandomArray(x);
        this.pool = new ForkJoinPool(y);
    }

    private int[] generateRandomArray(int x) {
        Random random = new Random();
        int[] arr = new int[x];
        for(int i = 0; i < x; i++ )
            arr[i] = random.nextInt(100);
        return arr;
    }

    public void sort() {
        if (arr.length < 10)
            singleThreadMergeSort(arr, 0, arr.length - 1);
        else {
            MergeSortAction msa = new MergeSortAction(arr, 0, arr.length - 1);
            pool.invoke(msa);
        }
    }

    private static void singleThreadMergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            singleThreadMergeSort(arr, l, m);
            singleThreadMergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private static void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        System.arraycopy(arr, l, leftArr, 0, n1);

        for(int i = 0; i < n2; i++)
            rightArr[i] = arr[m + i + 1];

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i];
                i++;
            } else {
                arr[k] = rightArr[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = leftArr[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = rightArr[j];
            j++;
            k++;
        }
    }

    private static class MergeSortAction extends RecursiveAction {
        private int[] arr;
        private int l, r;

        public MergeSortAction(int[] arr, int l, int r) {
            this.arr = arr;
            this.l = l;
            this.r = r;
        }

        @Override
        protected void compute() {
            if(arr.length < 10)
                singleThreadMergeSort(arr, 0, arr.length - 1);
            else {
                if (l < r) {
                    int m = (l + r) / 2;

                    MergeSortAction left = new MergeSortAction(arr, l, m);
                    MergeSortAction right = new MergeSortAction(arr, m + 1, r);
                    left.fork();
                    right.compute();
                    left.join();
                    merge(arr, l, m, r);
                }
            }
        }
    }

    public int[] getArr() {
        return arr;
    }
}
