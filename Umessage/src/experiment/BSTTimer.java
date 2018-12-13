package experiment;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import cse332.datastructures.trees.*;

public class BSTTimer {
    // each file has 100000 lines of data
    // a number can vary from 1 to 7 digits
    public static final String fileName1 = "numbers.txt"; 
    public static final String fileName2 = "strings.txt"; 
    public static final int MAX = 100000;
    
    
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(fileName1);
        File file2 = new File(fileName2);
        File file3 = new File("stringsFinder.txt");
        File file4 = new File("numbersFinder.txt");
        Scanner scanNumbers = new Scanner(file);
        Scanner scanNumbersCopy = new Scanner(file4);
        Scanner scanStrings = new Scanner(file2);
        Scanner scanStringsCopy = new Scanner(file3);
        
        BinarySearchTree bstWithNumbers = new BinarySearchTree();
        BinarySearchTree bstWithStrings = new BinarySearchTree();
        
        int counter = 0;
        // this is inserting numbers as keys with an arbitrary object
        // we will time how long it takes
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        while(scanNumbers.hasNextInt() && counter <= MAX) {
            bstWithNumbers.insert(scanNumbers.next(), new Object());
            counter++;
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for insert BST: " + (endTime - startTime) + " for " + MAX + " lines of int data.");
        counter = 0;
        
        // times and tries to find the values with all the previous keys
        startTime = System.currentTimeMillis();
        while(scanNumbersCopy.hasNextInt() && counter <= MAX) {
            bstWithNumbers.find(scanNumbersCopy.next());
            counter++;
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for find BST: " + (endTime - startTime) + " for " + MAX + " lines of int data.");
        
        
        
        counter = 0;
        startTime = System.currentTimeMillis();
        // the same thing with strings
        while(scanStrings.hasNext() && counter <= MAX) {
            bstWithStrings.insert(scanStrings.next(), new Object());
            counter++;
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for insert BST: " + (endTime - startTime) + " for " + MAX + " lines of string data.");
        counter = 0;
        
        // times and tries to find the values with all the previous keys
        startTime = System.currentTimeMillis();
        while(scanStringsCopy.hasNext() && counter <= MAX) {
            bstWithStrings.find(scanStringsCopy.next());
            counter++;
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for find BST: " + (endTime - startTime) + " for " + MAX + " lines of string data.");
        counter = 0;
        
        
        
    }

}
