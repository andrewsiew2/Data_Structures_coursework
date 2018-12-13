package longestSequence;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class LongestSequence {
	private static final ForkJoinPool POOL = new ForkJoinPool();
    public static int getLongestSequence(int val, int[] arr, int sequentialCutoff) {
    		SequenceRange data = POOL.invoke(new countsSequence(arr, 0, arr.length, sequentialCutoff, val, true, true));
    		return data.longestRange;
    }


	public static SequenceRange sequential(int[] arr, int lo, int hi, int val, boolean isLeft) {
		int countLeft = 0;
		int count = 0;
		int max = 0;
		boolean leftCount = true;
	    for (int i = lo; i < hi; i++) {
	        if (arr[i] == val) {
	            count++;
	            if(count > max) {
	            		max = count;
	            }
	        }else {
	        		if(leftCount) {
	        			countLeft = count;
	        		}
	        		if(count > max) {
	            		max = count;
	            }
	        		leftCount = false;
	        		count = 0;
	        }
	    }
	    if(leftCount) {
	    		countLeft = count;
	    }
    		return new SequenceRange(countLeft, count, max, hi - lo);
	}
	
    private static class countsSequence extends RecursiveTask<SequenceRange> {
        int[] arr;
        int lo, hi;
        int cutoff;
        int value;
        int max;
        boolean isALeft;
        boolean isRoot;
        
        public countsSequence(int[] arr, int lo, int hi, int sequentialCutoff, int val, boolean isALeft, boolean isRoot) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
            this.value = val;
            this.cutoff = sequentialCutoff;
            this.max = 0;
            this.isALeft = isALeft;
            this.isRoot = isRoot;
        }
        
        protected SequenceRange compute() {
            if (hi - lo <= this.cutoff) {
                return sequential(arr, lo, hi, value, isALeft);
            }
            int mid = lo + (hi - lo) / 2;
            countsSequence left = new countsSequence(arr, lo, mid, cutoff, value, true, false);
            countsSequence right = new countsSequence(arr, mid, hi, cutoff, value, false, false);
            left.fork();
            SequenceRange rightSide = right.compute();
            SequenceRange leftSide = left.join();
            int max = leftSide.longestRange;
            if(rightSide.longestRange > max) {
            		max = rightSide.longestRange;
            }
            
            int specialSequence = leftSide.sequenceLength + rightSide.sequenceLength;
            int middle = leftSide.matchingOnRight + rightSide.matchingOnLeft;
            if(middle > max) {
            		max = middle;
            }
            int right1 = 0;
            int left1 = 0;
            if(rightSide.matchingOnRight == rightSide.sequenceLength) {
            		right1 = rightSide.matchingOnLeft + leftSide.matchingOnRight; 
            }else {
            		right1 = rightSide.matchingOnRight;
            }
            if(leftSide.matchingOnLeft == leftSide.sequenceLength) {
            		left1 = rightSide.matchingOnLeft + leftSide.matchingOnLeft;
            		
            }else {
            		left1 = leftSide.matchingOnLeft;
            }
            
            
            SequenceRange data = new SequenceRange(left1, right1, 
            									max, specialSequence);
            return data;
        }
        
    }

    private static void usage() {
        System.err.println("USAGE: LongestSequence <number> <array> <sequential cutoff>");
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
            System.out.println(getLongestSequence(val, arr, Integer.parseInt(args[2])));
        } catch (NumberFormatException e) {
            usage();
        }
    }
}