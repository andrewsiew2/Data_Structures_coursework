package filterEmpty;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class FilterEmpty {
    static ForkJoinPool POOL = new ForkJoinPool();

    public static int[] filterEmpty(String[] arr) {
        int[] bitset = mapToBitSet(arr);
        //System.err.println(java.util.Arrays.toString(bitset));
        int[] bitsum = ParallelPrefixSum.parallelPrefixSum(bitset);
        //System.err.println(java.util.Arrays.toString(bitsum));
        int[] result = mapToOutput(arr, bitsum);
        return result;
    }


    public static int[] mapToOutput(String[] input, int[] bitsum) {
    		if(bitsum.length <= 0) {
    			return new int[0];
    		}
    		int[] output = new int[bitsum[bitsum.length - 1]];
    		if(output.length > 0) {
    			POOL.invoke(new mapOutput(input, 0, input.length, output, bitsum));
    		}
		return output;
    }

    private static class mapOutput extends RecursiveAction {
        int lo, hi;
        String[] input;
        int[] output;
        int[] bitSum;
        public mapOutput(String[] input, int lo, int hi, int[] output, int[] bitSum) {
            this.lo = lo;
            this.hi = hi;
            this.input = input;
            this.output = output;
            this.bitSum = bitSum;
        }
        
        protected void compute() {
            if (hi - lo <= 1) {
        			String word = input[lo];
        			if(word.length() > 0) {
        				output[bitSum[lo] - 1] = word.length();
        			}
            }else {
	            int mid = lo + (hi - lo) / 2;
	            mapOutput left = new mapOutput(input, lo, mid, output, bitSum);
	            mapOutput right = new mapOutput(input, mid, hi, output, bitSum);
	            left.fork();
	            right.compute();
	            left.join();
            }
        }
        
    }
    public static int[] mapToBitSet(String[] arr) {
    		int[] bitSet = new int[arr.length];
    		if(arr.length > 0) {
    			POOL.invoke(new MapBit(arr, 0, arr.length, bitSet));
    		}
    		return bitSet;
    }

    
    private static class MapBit extends RecursiveAction {
        String[] arr;
        int lo, hi;
        int[] bitArray;
        public MapBit(String[] arr, int lo, int hi, int[] bitArray) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
            this.bitArray = bitArray;
        }
        
        protected void compute() {
            if (hi - lo <= 1) {
                if(arr[lo].length() > 0) {
                		bitArray[lo] = 1;
                }else {
                		bitArray[lo] = 0;
                }
            }else {
	            int mid = lo + (hi - lo) / 2;
	            MapBit left = new MapBit(arr, lo, mid, bitArray);
	            MapBit right = new MapBit(arr, mid, hi, bitArray);
	            left.fork();
	            right.compute();
	            left.join();
            }
        }
        
    }
    private static void usage() {
        System.err.println("USAGE: FilterEmpty <String array>");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        }

        String[] arr = args[0].replaceAll("\\s*", "").split(",");
        System.out.println(Arrays.toString(filterEmpty(arr)));
    }
}