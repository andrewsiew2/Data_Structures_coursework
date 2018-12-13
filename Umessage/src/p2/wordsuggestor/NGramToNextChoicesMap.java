package p2.wordsuggestor;

import java.util.Comparator;
import java.util.Iterator; // Can I import?
import java.util.function.Supplier;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.Dictionary;
import cse332.misc.LargeValueFirstItemComparator;
import cse332.types.AlphabeticString;
import cse332.types.NGram;
import p2.sorts.*;

public class NGramToNextChoicesMap {
    private final Dictionary<NGram, Dictionary<AlphabeticString, Integer>> map;
    private final Supplier<Dictionary<AlphabeticString, Integer>> newInner;

    public NGramToNextChoicesMap(
            Supplier<Dictionary<NGram, Dictionary<AlphabeticString, Integer>>> newOuter,
            Supplier<Dictionary<AlphabeticString, Integer>> newInner) {
        this.map = newOuter.get();
        this.newInner = newInner;
    }

    /**
     * Increments the count of word after the particular NGram ngram.
     */
    public void seenWordAfterNGram(NGram ngram, String word) {
        AlphabeticString key = new AlphabeticString(word);
        Dictionary<AlphabeticString, Integer> innerHolder = this.map.find(ngram);
        if (innerHolder == null) {
            innerHolder = this.newInner.get();
            this.map.insert(ngram, innerHolder);
        }
        
        Integer count = innerHolder.find(key);
        
        if (count == null) {
            count = Integer.valueOf(0);
        }
        innerHolder.insert(key, Integer.sum(1, count));
    }

    /**
     * Returns an array of the DataCounts for this particular ngram. Order is
     * not specified.
     *
     * @param ngram
     *            the ngram we want the counts for
     * 
     * @return An array of all the Items for the requested ngram.
     */
    public Item<String, Integer>[] getCountsAfter(NGram ngram) {
        Dictionary<AlphabeticString, Integer> innerMap = this.map.find(ngram);
        if (innerMap == null) {
            return (Item<String, Integer>[]) new Item[0];
        } 
        Item<String, Integer>[] result = (Item<String, Integer>[]) new Item[innerMap.size()];
        Iterator<Item<AlphabeticString, Integer>> loopIterator = innerMap.iterator();  
        int i = 0;
        while (loopIterator.hasNext()) {
            Item<AlphabeticString, Integer> referenceItem = loopIterator.next();
            Item<String, Integer> element = new Item(referenceItem.key.toString(), referenceItem.value);
            result[i] = element;
            i++;
        }      
        return result;
    }

    
    public String[] getWordsAfter(NGram ngram, int k) {
        Item<String, Integer>[] afterNGrams = getCountsAfter(ngram);
        Comparator<Item<String, Integer>> comp = new NewComparator<String, Integer>();
        if (k < 0) {
            HeapSort.sort(afterNGrams, comp);
        }
        else {
            TopKSort.sort(afterNGrams, k, comp.reversed());
            HeapSort.sort(afterNGrams,comp);
        }

        String[] nextWords = new String[k < 0 ? afterNGrams.length : k];
        for (int l = 0; l < afterNGrams.length && l < nextWords.length
                && afterNGrams[l] != null; l++) {
            nextWords[l] = afterNGrams[l].key;
        }
        return nextWords;
    }
    
    public static class NewComparator<K extends Comparable<K>, V extends Comparable<V>> extends LargeValueFirstItemComparator<K,V>{
        @Override
        public int compare(Item<K, V> e1, Item<K, V> e2) {
            if (e1 != null && e2 != null) {
                return super.compare(e1, e2);
            } else if (e1 != null && e2 == null) {
                return -1;
            } else if (e1 == null && e2 != null) {
                return 1;
            }
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return this.map.toString();
    }
}
