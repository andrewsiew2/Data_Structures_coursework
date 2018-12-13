package experiment;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cse332.datastructures.trees.*;
import cse332.interfaces.misc.BString;
import cse332.types.AlphabeticString;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.HashTrieMap;
import datastructures.dictionaries.MoveToFrontList;

public class HTMtimer {
    public static final String fileName = "alice.txt"; 
    
    
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(fileName);
        File file2 = new File("stringsFinder.txt");
        Scanner scanStrings = new Scanner(file);
        Scanner scanStringsCopy = new Scanner(file2);
        Scanner scanStrings2 = new Scanner(file);
        Scanner scanStringsCopy2 = new Scanner(file2);
        HashTrieMap map = new HashTrieMap(String.class);
        ChainingHashTable<String, Object> hTable = new ChainingHashTable<String, Object>(() -> new MoveToFrontList<>());
        
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        
        startTime = System.currentTimeMillis();
        // the same thing with strings
        while(scanStrings.hasNext()) {
            map.insert(new AlphabeticString(scanStrings.next()), new Object());
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for insert HTM: " + (endTime - startTime));

        // times and tries to find the values with all the previous keys
        startTime = System.currentTimeMillis();
        while(scanStringsCopy.hasNext()) {
            map.find(new AlphabeticString(scanStringsCopy.next()));
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for find HTM: " + (endTime - startTime));
        
        startTime = System.currentTimeMillis();
        // the same thing with strings
        while(scanStrings2.hasNext()) {
            hTable.insert(scanStrings2.next(), new Object());
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for insert Hashtable: " + (endTime - startTime));

        // times and tries to find the values with all the previous keys
        startTime = System.currentTimeMillis();
        while(scanStringsCopy2.hasNext()) {
            hTable.find(scanStringsCopy2.next());
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for find HashTable: " + (endTime - startTime));
        
        
        
        
    }

}