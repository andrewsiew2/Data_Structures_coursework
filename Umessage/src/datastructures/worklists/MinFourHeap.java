package datastructures.worklists;

import cse332.interfaces.worklists.PriorityWorkList;
import java.util.NoSuchElementException;
import java.util.Comparator;


/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeap<E> extends PriorityWorkList<E> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;
    private Comparator<E> comparator;
    
    public MinFourHeap(Comparator<E> comparator) {
    		size = 0;
    		data = (E[])new Object[10]; 
    		this.comparator = comparator;
    	}

    @Override
    public boolean hasWork() {
        return size > 0;
    }

    @Override
    public void add(E array) {
    		if (size == data.length) {
    			resize();
    		}
    		int i = percolateUp(size, array);
    		data[i] = array;
	    size++;
    }
     
    private void resize() {
		E[] tempData = (E[])new Object[data.length * 4];
    		for (int i = 0; i < data.length; i++) {
    			tempData[i] = data[i];
    		}
    		data = tempData;
    }
    
    private int percolateUp(int hole, E work) {
    		while (hole > 0 && comparator.compare(work, data[(hole - 1)/4]) < 0) {
    			data[hole] = data[(hole - 1)/4];
    			hole = (hole - 1)/4;
    		}
    		return hole;
    }

    @Override
    public E peek() {
    		if (!hasWork()) {
    			throw new NoSuchElementException();
    		}
    		return data[0];
    }

    @Override
    public E next() {
    		if (!hasWork()) {
    			throw new NoSuchElementException();
    		}
    		E workHolder = data[0];
    		size--;
    		int i = percolateDown(0, data[size]);
    		data[i] = data[size];
    		return workHolder;
    }

    private int percolateDown(int hole, E work) {
    		while (1 + 4 * hole < size) { 
    			int left = 1 + 4 * hole;
    			int right = 4 +  4 * hole;
    			int target = left;
    			E minimum = data[left];
    			int maxRange = Math.min(size + 1, right + 1);
    			for (int i = left + 1; i < maxRange; i++) {
    				if (comparator.compare(data[i],minimum) < 0) {
    					minimum = data[i];
    					target = i;
    				}
    			}
    			if (comparator.compare(data[target], work) < 0) {
    				data[hole] = data[target];
    				hole = target;
    			} else {
    				break;
    			}
    		}
    		return hole;
    	}
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
		size = 0;
		data = (E[])new Object[10];
	}
}
