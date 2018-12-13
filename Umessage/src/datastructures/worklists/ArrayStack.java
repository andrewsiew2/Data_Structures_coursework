package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.LIFOWorkList;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {

	private E[] array;
	private int size;
	
    public ArrayStack() {
    		array = (E[])new Object[10];    
    		size = 0;
    	}

    @Override
    public void add(E work) {
    		if (size == array.length) {
    			E[] tempArray = (E[])new Object[array.length * 2];
    			for (int i = 0; i < array.length; i++) {
    				tempArray[i] = array[i];
    			}	
    			array = tempArray;
    		}
    		array[size] = work;
    		size++;
    }

    @Override
    public E peek() {
        if (!this.hasWork()) { // Correct style?
        		throw new NoSuchElementException();
        }
        return array[size - 1];
    }

    @Override
    public E next() {
	    	if (!this.hasWork()) { // Correct style?
	    		throw new NoSuchElementException();
	    }
    		size--;
        return array[size];
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
    		array = (E[])new Object[10];    
		size = 0;
    }
}
