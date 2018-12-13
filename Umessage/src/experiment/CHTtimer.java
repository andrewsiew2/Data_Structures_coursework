package experiment;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cse332.datastructures.trees.BinarySearchTree;
import datastructures.dictionaries.AVLTree;
import datastructures.dictionaries.ChainingHashTable;
import datastructures.dictionaries.MoveToFrontList;

public class CHTtimer {
    // each file has 100000 lines of data
    // a number can vary from 1 to 7 digits
    public static final String fileName = "alice.txt"; 
    public static final String fileName2 = "stringsFinder.txt"; 
    
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(fileName);
        File finder = new File(fileName2);
        Scanner scan = new Scanner(file);
        Scanner scanCopy = new Scanner(finder);
        Scanner scan1 = new Scanner(file);
        Scanner scanCopy1 = new Scanner(finder);
        Scanner scan2 = new Scanner(file);
        Scanner scanCopy2 = new Scanner(finder);
        
        ChainingHashTable<String, Object> avlChain = new ChainingHashTable<String, Object>(() -> new AVLTree<>());
        ChainingHashTable<String, Object> bstChain = new ChainingHashTable<String, Object>(() -> new BinarySearchTree<>());
        ChainingHashTable<String, Object> mtfChain = new ChainingHashTable<String, Object>(() -> new MoveToFrontList<>());
        // this is inserting numbers as keys with an arbitrary object
        // we will time how long it takes
        long startTime = System.currentTimeMillis();
        while(scan.hasNext()) {
            avlChain.insert(scan.next(), new Object());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time for insert AVL: " + (endTime - startTime));
        
        // times and tries to find the values with all the previous keys
        startTime = System.currentTimeMillis();
        while(scanCopy.hasNext()) {
            avlChain.find(scanCopy.next());
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for find AVL: " + (endTime - startTime));
        
        startTime = System.currentTimeMillis();
        while(scan1.hasNext()) {
            bstChain.insert(scan1.next(), new Object());
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for insert BST: " + (endTime - startTime));
        
        // times and tries to find the values with all the previous keys
        startTime = System.currentTimeMillis();
        while(scanCopy1.hasNext()) {
            bstChain.find(scanCopy1.next());
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for find BST: " + (endTime - startTime));
        
        startTime = System.currentTimeMillis();
        while(scan2.hasNext()) {
            mtfChain.insert(scan2.next(), new Object());
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for insert MTF: " + (endTime - startTime));
        
        // times and tries to find the values with all the previous keys
        startTime = System.currentTimeMillis();
        while(scanCopy2.hasNext()) {
            mtfChain.find(scanCopy2.next());
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for find MTF: " + (endTime - startTime));
        
        
    }

}