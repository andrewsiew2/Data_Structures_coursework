package p2.sorts;

import java.util.Comparator;
import cse332.exceptions.NotYetImplementedException;

public class QuickSort {
    
    
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        if(array.length > 1) {
            QuickSort.sort(array, comparator, 0, array.length - 1);
        }
    }
    public static <E> void sort(E[] array, Comparator<E> comparator, int leftMostIndex, int rightMostIndex) { 
        if (leftMostIndex < rightMostIndex) {

                int middleIndex = (leftMostIndex + rightMostIndex) >>> 1;
                E a = array[leftMostIndex];
                E b = array[rightMostIndex];
                E c = array[middleIndex];
                
                int medianIndex;
                if (comparator.compare(b, a) > 0) {
                    if (comparator.compare(c, b) > 0) {
                        medianIndex = rightMostIndex;
                    } else if (comparator.compare(a, c) > 0) {
                        medianIndex = leftMostIndex;
                    } else {
                        medianIndex = middleIndex;
                    }
                } else {
                    if (comparator.compare(c, b) < 0) {
                        medianIndex = rightMostIndex;
                    } else if (comparator.compare(a, c) < 0) {
                        medianIndex = leftMostIndex;
                    } else { 
                        medianIndex = middleIndex;
                    }
                }
                
                E pivot = array[medianIndex];
                swap(array, leftMostIndex, medianIndex);
                int leftPointer = leftMostIndex;
                int rightPointer = rightMostIndex;
               
                while (leftPointer < rightPointer) {
                    while (comparator.compare(array[leftPointer], pivot) <= 0 && leftPointer < rightPointer) {
                        leftPointer++;
                    }
                    
                    while (comparator.compare(array[rightPointer], pivot) > 0) {
                        rightPointer--;
                    }
                    
                    if (leftPointer < rightPointer) {
                        swap(array, leftPointer, rightPointer);
                    }
                }
                swap(array, leftMostIndex, rightPointer);
                sort(array, comparator, leftMostIndex, rightPointer - 1);
                sort(array, comparator, rightPointer + 1, rightMostIndex);
        }
    }

    private static <E> void swap(E[] array, int position1, int position2) {
        E temp = array[position1];
        array[position1] = array[position2];
        array[position2] = temp;
    }
}
