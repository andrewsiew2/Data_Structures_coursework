package p2.sorts;

import java.util.Comparator;
import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        int size = array.length;
        if(k <= size && k >= 0) {
            MinFourHeap heap = new MinFourHeap(comparator);
            for(int i = 0; i < k; i++) {
                heap.add(array[i]);
            }
            
            for(int i = k; i < size; i++) {
                if(comparator.compare(array[i], (E) heap.peek()) > 0) {
                    heap.next();
                    heap.add(array[i]);
                }
            }
            
            for(int i = 0; i < k; i++) {
                array[i] = (E) heap.next();
            }
            for(int i = k; i < size; i++) {
                array[i] = null;
            }
        }   
    }
}
