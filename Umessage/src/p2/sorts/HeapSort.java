package p2.sorts;

import java.util.Comparator;
import cse332.exceptions.NotYetImplementedException;
import datastructures.worklists.MinFourHeap;

public class HeapSort {
    
    
    public static <E extends Comparable<E>> void sort(E[] array) {
        sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        int size = array.length;
        MinFourHeap heap = new MinFourHeap(comparator);
        for(int i = 0; i < size; i++) {
            heap.add(array[i]);
        }
        
        for(int i = 0; i < size; i++) {
            array[i] = (E) heap.next();
        }
        
        
    }
}
