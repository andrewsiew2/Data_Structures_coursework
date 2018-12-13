package p2.wordcorrector;

import cse332.datastructures.containers.Item;
import cse332.types.AlphabeticString;
import datastructures.dictionaries.HashTrieMap;
import java.util.AbstractMap.SimpleEntry;


public class AutocompleteTrie extends HashTrieMap<Character, AlphabeticString, Integer> {

    public AutocompleteTrie() {
        super(AlphabeticString.class);
    }

    public String autocomplete(String key) {
        @SuppressWarnings("unchecked")
        HashTrieNode current = (HashTrieNode) this.root;
        for (Character item : key.toCharArray()) {
//            if (!current.pointers.containsKey(item)) {
//                return null;
//            }
            if (current.pointers.find(item) == null) {
                return null;
            }
            else {
                //current = current.pointers.get(item);
                current = current.pointers.find(item);
            }
        }

        String result = key;

        while (current.pointers.size() == 1) {
            if (current.value != null) {
                return null;
            }
            
            //result += current.pointers.keySet().iterator().next();
            //current = current.pointers.values().iterator().next();
            
//            Item<Character, HashTrieMap<Character, AlphabeticString, Integer>.HashTrieNode> currentItem = current.pointers.iterator().next();
//            result += currentItem.key;
//            current = currentItem.value;
//            
            result += current.pointers.iterator().next().key;
            current = current.pointers.iterator().next().value;
            

        }

        // Switch this to return null to only complete if we're at the end of
        // the word
        if (current.pointers.size() != 0) {
            return result;
        }
        return result;
    }
}
