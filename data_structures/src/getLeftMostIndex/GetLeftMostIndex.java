package getLeftMostIndex;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class GetLeftMostIndex {
	
	private static final ForkJoinPool POOL = new ForkJoinPool();
	
    public static int getLeftMostIndex(char[] needle, char[] haystack, int sequentialCutoff) {
    		return POOL.invoke(new getLeftMost(needle, haystack, 0, haystack.length, sequentialCutoff));
    }
	public static Integer sequential(char[] needle, char[] haystack, int lo, int hi) {
	    for(int i = lo; i < hi; i++) {
	    		if(haystack[i] == needle[0]) {
	    			for(int j = 0; j < needle.length ; j++) {
	    				if(i + j >= haystack.length) {
	    					return -1;
	    				}
	    				if(needle[j] != haystack[i + j]) {
	    					break;
	    				}
	    				if(j == needle.length - 1) {
	    					return i;
	    				}
	    			}
	    		}
	    }
	    return -1;
	}
	
    private static class getLeftMost extends RecursiveTask<Integer> {
        char[] needle;
        char[] haystack;
        int lo, hi;
        int cutoff;
        public getLeftMost(char[] needle, char[] haystack, int lo, int hi, int sequentialCutoff) {
            this.lo = lo;
            this.hi = hi;
            this.cutoff = sequentialCutoff;
            this.needle = needle;
            this.haystack = haystack;
        }
        
        protected Integer compute() {
            if (hi - lo <= this.cutoff) {
                return sequential(needle, haystack, lo, hi);
            }
            int mid = lo + (hi - lo) / 2;
            getLeftMost left = new getLeftMost(needle, haystack,lo, mid, cutoff);
            getLeftMost right = new getLeftMost(needle, haystack,mid, hi, cutoff);
            left.fork();
            int rightInt = right.compute();
        		int leftInt = left.join();

        		if(leftInt != -1 && rightInt != -1) {
        			return Math.min(leftInt, rightInt);
        		}
    			return Math.max(leftInt, rightInt);
        		
        }
        
    }
    
    
    
    
    private static void usage() {
        System.err.println("USAGE: GetLeftMostIndex <needle> <haystack> <sequential cutoff>");
        System.exit(2);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            usage();
        }

        char[] needle = args[0].toCharArray();
        char[] haystack = args[1].toCharArray();
        try {
            System.out.println(getLeftMostIndex(needle, haystack, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}
