package experiment;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;
import java.util.NoSuchElementException;



/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularNew<E extends Comparable<E>> extends FixedSizeFIFOWorkList<E> {
    
    private E[] array;
    
    private int front;
    private int back;
    private int size;
    
    
    public CircularNew(int capacity) {
        super(capacity);
        array = (E[])new Comparable[capacity];
        front = 0;
        back = 0;
        size = 0;
    }

    @Override
    public void add(E work) {
            if (isFull()) {
                throw new IllegalStateException();
            }
            size++;
            array[back] = work;
            back = (back + 1) % capacity();
    }

    @Override
    public E peek() {
            return peek(0);
    }
   
    @Override
    public E peek(int i) {
        if (!hasWork()) {
                throw new NoSuchElementException(); 
        } else if (i < 0 || i >= capacity()) { 
                throw new IndexOutOfBoundsException();
        }
        return array[(front + i) % capacity()];         
    }
    
    @Override
    public E next() {
            if (!hasWork()) {
                throw new NoSuchElementException();
            }
            size--;
            E temp = array[front];
            front = (front + 1) % capacity(); 
            return temp;
    }
    
    @Override
    public void update(int i, E value) {
            if (!hasWork()) {
                throw new NoSuchElementException();
            } else if (i < 0 || i >= capacity()) { 
                throw new IndexOutOfBoundsException();
            }
            array[(front + i) % capacity()] = value; 
    }
    
    @Override
    public int size() {
            return size;
    }
    
    @Override
    public void clear() {
        array = (E[])new Comparable[capacity()];
        front = 0;
        back = 0;
        size = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in p2. Leave this method unchanged for p1.
        if (this.equals(other)) return 0;
        for (int i = 0; i < Math.min(this.size(), other.size()); i++) {
            int result = this.peek(i).compareTo(other.peek(i));
            if (result != 0) {
                return result;
            }
        }
        return this.size() - other.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        // You will finish implementing this method in p2. Leave this method unchanged for p1.
        if (this == obj) {
            return true;
        }
        else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        }
        else {
            FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
            if (!(other instanceof CircularNew<?>)) return false;
            CircularNew<E> test = (CircularNew<E>) other;
            if (test.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < this.size(); i++) {
                if (!(this.peek(i).equals(test.peek(i)))) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        // You will implement this method in p2. Leave this method unchanged for p1.
        int result = 0;
        for (int i = 0; i < size; i++) {
            E element = peek(i);
            result = (i + 1) * 7 + element.hashCode();
        }
        // Test!
        return result;
    }
}