package datastructures.dictionaries;

import java.util.Iterator;

import cse332.datastructures.containers.*;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.*;
import java.util.NoSuchElementException;
//test!
/**
 * TODO: Replace this comment with your own as appropriate.
 * 1. The list is typically not sorted.
 * 2. Add new items to the front oft he list.
 * 3. Whenever find is called on an item, move it to the front of the 
 *    list. This means you remove the node from its current position 
 *    and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 *    elements to the front.  The iterator should return elements in
 *    the order they are stored in the list, starting with the first
 *    element in the list.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {
    
    private Node front;
    
    private class Node {
        public K key;
        public V value;
        public Node next;
        
        public Node(K key, V value) {
            this(key, value, null);
        }
        
        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
    }
    
    private class Iterator<E> extends SimpleIterator<E>{
        Node temp = front;
        @Override
        public E next() {
            if (temp == null) {
                throw new NoSuchElementException();
            }
            K key = temp.key;
            V value = temp.value;
            temp = temp.next;
            return (E) new Item(key, value);
        }

        @Override
        public boolean hasNext() {
            return temp != null;
        }

    }
    
    
    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node(key, value, front);
        front = newNode;
        Node tempNode = front;
        while (tempNode.next != null) {
            if (tempNode.next.key.equals(key)) {
                V result = tempNode.next.value;
                tempNode.next = tempNode.next.next;
                return result;
            }
            tempNode = tempNode.next;
        }
        size++;
        return null;
    }

    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        if (front != null) {
            // Check if its at the front already if not use front as the reference
            Node tempNode = front;
            if (tempNode.key.equals(key)) {
                return tempNode.value;
            }
            
            while (tempNode.next != null) {
                if (tempNode.next.key.equals(key)) {
                    Node toBeFront = tempNode.next;
                    tempNode.next = toBeFront.next;
                    toBeFront.next = front;
                    front = toBeFront;
                    return front.value;
                }
                tempNode = tempNode.next;
            }
        }
        return null;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new Iterator<Item<K,V>>();
    }
}
