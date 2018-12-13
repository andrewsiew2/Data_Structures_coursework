package experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;
import cse332.types.*;


// this code compares a chaining hash table and a regular
// hash function in terms of time to insert  
// the data inserted are randomly generate 7 letter long strings
public class HashComparer {
    public static final String fileName = "strings.txt";  
    public static final int MAX = 100000;
    private static Scanner scanStrings;
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(fileName);
        scanStrings = new Scanner(file);
        
        double totalTime = 0;
        int NUM_TESTS = 10;
        int NUM_WARMUP = 3;
        int counter = 0;
        for (int j = 10000; j <= MAX; j += 10000) {
            for (int i = 0; i < NUM_TESTS; i++) {
                ChainingHashTable<AlphabeticString, Object> CHT = new ChainingHashTable<AlphabeticString, Object>(() -> new MoveToFrontList<>());
                long startTime = System.currentTimeMillis();
                while (scanStrings.hasNext() && counter <= j) {
                    CHT.insert(new AlphabeticString(scanStrings.next()), new Object());
                    counter++;
                }
                long endTime = System.currentTimeMillis();
                counter = 0;
                scanStrings = new Scanner(file);
                
                if (NUM_WARMUP <= i) {
                    totalTime += (endTime - startTime);
                }
                
                CHT = null;
                
            }
            double averageRunTime = totalTime / (NUM_TESTS - NUM_WARMUP);
            
            System.out.println("Time taken to insert  " + j + " amount of items are " + averageRunTime);
        }

        
//        counter = 0;
//        long startTime = System.currentTimeMillis();
//        while(scanStrings.hasNext() && counter <= MAX) {
//            CHT.insert(scanStrings.next(), new Object());
//            counter++;
//        }
//        long endTime = System.currentTimeMillis();
//        System.out.println("Total execution time for insert CHT: " + (endTime - startTime));
//        
//        startTime = System.currentTimeMillis();
//        counter = 0;
//        while(scanStrings2.hasNext() && counter <= MAX) {
//            CHT.insert(scanStrings2.next(), new Object());
//            counter++;
//        }
//        endTime = System.currentTimeMillis();
//        System.out.println("Total execution time for insert Regular Hash: " + (endTime - startTime));
        
    }
    
    
    
    
    
}
