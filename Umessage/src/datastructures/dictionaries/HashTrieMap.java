package datastructures.dictionaries;

import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Map.Entry;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.BString;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import cse332.interfaces.trie.TrieMap;


/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    
    public class HashTrieNode extends TrieNode<Dictionary<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<A, HashTrieNode>(() -> new MoveToFrontList<>());
            this.value = value;
        }

        @Override
        public java.util.Iterator<java.util.Map.Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            return new NewIterator();
        }
        
        
        public class NewIterator extends SimpleIterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> {
            java.util.Iterator<Item<A, HashTrieMap<A, K, V>.HashTrieNode>> innerIterator = pointers.iterator();
            
            public Entry next() {
                Item<A, HashTrieMap<A, K, V>.HashTrieNode> item = (innerIterator.next());
                SimpleEntry result = new SimpleEntry(item.key, item.value);
                return (Entry) result;
            }
            
            public boolean hasNext() {
                return innerIterator.hasNext();
            }
        }
    }
    
    
    

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }

    @Override
    public V insert(K key, V value) {
    		if (value == null || key == null) {
    			throw new IllegalArgumentException();
    		}
    		
    		HashTrieNode tempNode = (HashTrieNode) this.root;
    		Iterator<A> iterator = key.iterator();
    		while (iterator.hasNext()) {
    			A keyComp = iterator.next();
    			if (tempNode.pointers.find(keyComp) != null) {
    			    tempNode = tempNode.pointers.find(keyComp);
    			} else {
                    HashTrieNode newNode = new HashTrieNode();
                    tempNode.pointers.insert(keyComp, newNode);
                    tempNode = newNode;
    			}
    		}	
    		V oldVal = tempNode.value;
    		if (oldVal == null) {
    			size++;
    		}
    		tempNode.value = value;
    		return oldVal;
    }

    @Override
    public V find(K key) {
    		if (key == null) {
    			throw new IllegalArgumentException();
    		}
    		
    		Iterator<A> iterator = key.iterator();
    		HashTrieNode tempNode = (HashTrieNode)this.root;
    		
    		while (iterator.hasNext()) {
    			A keyComp = iterator.next();
    			if (tempNode.pointers.find(keyComp) == null) {
    			    return null;
            } else {
                tempNode = tempNode.pointers.find(keyComp);
            }
    		}
    		return tempNode.value;
    }

    @Override
    public boolean findPrefix(K key) {
    		if (key == null) {
        		throw new IllegalArgumentException();
    		}
    		HashTrieNode tempNode = (HashTrieNode)this.root;
		Iterator<A> iterator = key.iterator();
	    while (iterator.hasNext()) {
            A keyComp = iterator.next();
            if (tempNode.pointers.find(keyComp) == null) {
                return false;
            }
            tempNode = tempNode.pointers.find(keyComp);
        }
		return true;
    }

    @Override
    public void delete(K key) {
        if (key == null) {
        		throw new IllegalArgumentException();
        }
		HashTrieNode oldNode = (HashTrieNode)this.root;
		HashTrieNode newNode = oldNode;
		Iterator<A> iterator = key.iterator();
		boolean shouldStoreDeletionKey = true;
		A deletionKey = null;
		while (iterator.hasNext()) {
			A keyComp = iterator.next();
	        if (newNode.pointers.find(keyComp) == null) {
	                return;
	        }
			
			if (shouldStoreDeletionKey) {
				deletionKey = keyComp;
				shouldStoreDeletionKey = false;
			}
			
			newNode = newNode.pointers.find(keyComp);
			if (newNode.pointers.size() > 1 || (newNode.value != null && newNode.pointers.size() > 0)) {
				oldNode = newNode;
				shouldStoreDeletionKey = true;
			}
		}
		
		if (newNode.value != null) {
			size--;
			newNode.value = null;
			if (!(newNode.pointers.size() > 0)) {
				oldNode.pointers.delete(deletionKey);
			}
		}
    }

    @Override
    public void clear() {
    		size = 0;
    		this.root = new HashTrieNode();
    }
}
