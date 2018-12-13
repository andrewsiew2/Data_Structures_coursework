package datastructures.dictionaries;

import java.util.NoSuchElementException;

import java.util.Iterator;
import java.util.function.Supplier;

import cse332.datastructures.containers.*;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;

/**
 * TODO: Replace this comment with your own as appropriate.
 * 1. You must implement a generic chaining hashtable. You may not
 *    restrict the size of the input domain (i.e., it must accept 
 *    any key) or the number of inputs (i.e., it must grow as necessary).
 * 3. Your HashTable should rehash as appropriate (use load factor as
 *    shown in class).
 * 5. HashTable should be able to grow at least up to 200,000 elements. 
 * 6. We suggest you hard code some prime numbers. You can use this
 *    list: http://primes.utm.edu/lists/small/100000.txt 
 *    NOTE: Do NOT copy the whole list!
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>> newChain;  
    private Dictionary<K,V>[] array;
    private int[] primeArray = {211, 431, 839, 1609, 3259, 6563, 13553, 29531, 61981, 136963, 270031, 550279, 1294753};
    private int sizeChooser;
    
    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        sizeChooser = 0;
        size = 0;
        this.array = new Dictionary[101];
    }
    
    // Rehash when necessary
    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        
        if (((float)this.size / (float)this.array.length) > 1.0) {
            Dictionary<K,V>[] newArray;  
            int newArraySize;
            if (sizeChooser < this.primeArray.length) {
                newArraySize = primeArray[sizeChooser];
            } else {
                newArraySize = this.array.length * 2;
            }
            
            newArray = new Dictionary[newArraySize];
            for (int i = 0; i < this.array.length; i++) {
                if (this.array[i] != null) {
                    java.util.Iterator<Item<K, V>> iterator = (this.array[i]).iterator();
                    while (iterator.hasNext()) {
                        Item<K,V> item = iterator.next();
                        rehash(item.key, item.value, newArray);
                    }
                }
            }
            this.array = newArray;
            sizeChooser++;
        }
        
        int hashCode = key.hashCode();
        int position = Math.abs(hashCode % this.array.length);        
        
        if (this.array[position] != null) {
            if (this.array[position].find(key) == null) {
                size++;
            }
            return this.array[position].insert(key, value);
        }
        
        size++;
        Dictionary<K, V> newItem = newChain.get();
        array[position] = newItem;
        return newItem.insert(key, value);
    }
    
    private V rehash(K key, V value, Dictionary<K,V>[] intoArray) {
        int hashCode = key.hashCode();
        int position = Math.abs(hashCode % intoArray.length);
        
        if  (intoArray[position] != null) {
            V result = intoArray[position].insert(key, value);
            return result;
        }
        
        Dictionary<K, V> newItem = newChain.get();
        intoArray[position] = newItem;
        return newItem.insert(key, value);
    }
    
    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        int hashCode = key.hashCode();
        int position = Math.abs(hashCode % this.array.length);
//        System.err.println("Hash Code is " + hashCode);
//        System.err.println("Array length " + this.array.length);
//        System.err.println("Position is " + position);
        if (this.array[position] == null) {
            return null;
        }
        return this.array[position].find(key);
    } 
    
    private class Iterator<E> extends SimpleIterator<E>{
        
        private Dictionary<K,V>[] innerArray = array;
        private int currentPosition = 0;
        private Dictionary<K,V> currentChain = null;
        private java.util.Iterator<Item<K, V>> innerIterator = null;
        
        
        public Iterator() {
            hasNext();
        }
        
        public E next() {
            if (currentChain == null) {
                throw new NoSuchElementException();
                //hasNext();
            }
            return (E) innerIterator.next();
        }
        
        public boolean hasNext() {
            if (innerIterator != null && innerIterator.hasNext()) {
                return true;
            }
            for (int i = currentPosition; i < this.innerArray.length; i++) {
                if (this.innerArray[i] != null) {
                    currentChain = this.innerArray[i];
                    innerIterator = currentChain.iterator();
                    currentPosition = i + 1;
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new Iterator<Item<K,V>>();
    }
}
