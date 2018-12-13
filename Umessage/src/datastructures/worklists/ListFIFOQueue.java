package datastructures.worklists;
import java.util.NoSuchElementException;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FIFOWorkList;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {
	private int listCount;
	private Node<E> head, tail;
	
	public class Node<E> {
		private E element;
		private Node<E> next;
	}
	
    public ListFIFOQueue() {
    		head = null;
    		tail = null;
        listCount = 0;
    }

    @Override
    public void add(E work) {
    		Node<E> temp = new Node<E>();
		temp.element = work;
        if(listCount == 0) {
        		head = temp;
        		tail = temp;
        }else{
        		tail.next = temp;
        		tail = tail.next;
        }
        listCount++;
    }

    @Override
    public E peek() {
    		if(!hasWork()) {
			throw new NoSuchElementException();
		}
        return head.element;
    }

    @Override
    public E next() {
    		if(!hasWork()) {
    			throw new NoSuchElementException();
    		}
    		E temp = head.element;
    		head = head.next;
    		listCount--;
        return temp;
    }

    @Override
    public int size() {
        return listCount;
    }

    @Override
    public void clear() {
        	head = null;
        tail = null;
        listCount = 0;
    }
}
