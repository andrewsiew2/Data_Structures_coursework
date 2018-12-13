package hasOver;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class HasOver {
	private static final ForkJoinPool POOL = new ForkJoinPool();

    public static boolean hasOver(int val, int[] arr, int sequentialCutoff) {
    		return POOL.invoke(new checkForHasOver(arr, 0, arr.length, sequentialCutoff, val));
    }


	public static boolean sequential(int[] arr, int lo, int hi, int val) {
	    for (int i = lo; i < hi; i++) {
	        if (arr[i] > val) {
	            return true;
	        }
	    }
	    return false;
	}
	
    private static class checkForHasOver extends RecursiveTask<Boolean> {
        int[] arr;
        int lo, hi;
        int cutoff;
        int value;
        
        public checkForHasOver(int[] arr, int lo, int hi, int sequentialCutoff, int val) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
            this.value = val;
            this.cutoff = sequentialCutoff;
        }
        
        protected Boolean compute() {
            if (hi - lo <= this.cutoff) {
                return sequential(arr, lo, hi, value);
            }
            int mid = lo + (hi - lo) / 2;
            checkForHasOver left = new checkForHasOver(arr, lo, mid, cutoff, value);
            checkForHasOver right = new checkForHasOver(arr, mid, hi, cutoff, value);
            left.fork();
            return right.compute() || left.join();
        }
        
    }
    
    private static void usage() {
        System.err.println("USAGE: HasOver <number> <array> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        int val = 0;
        int[] arr = null;

        try {
            val = Integer.parseInt(args[0]); 
            String[] stringArr = args[1].replaceAll("\\s*",  "").split(",");
            arr = new int[stringArr.length];
            for (int i = 0; i < stringArr.length; i++) {
                arr[i] = Integer.parseInt(stringArr[i]);
            }
            System.out.println(hasOver(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
        
    }
}
