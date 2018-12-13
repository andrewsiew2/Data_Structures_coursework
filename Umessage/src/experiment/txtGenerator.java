package experiment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

import cse332.datastructures.trees.*;

// this randomly generates strings and numbers in a txt file
public class txtGenerator {
    public static final String fileName1 = "numbersFinder.txt"; 
    public static final String fileName2 = "stringsFinder.txt"; 
    public static final int MAXLINES = 100000;
    public static final int MAXLENGTH = 7;
    
    
    public static void main(String[] args) throws FileNotFoundException {
        FileOutputStream number = new FileOutputStream(fileName1);
        FileOutputStream strings = new FileOutputStream(fileName2);
        PrintStream outNumber = new PrintStream(new File(fileName1));
        PrintStream outStrings = new PrintStream(new File(fileName2));
        
        String[] array = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m",
                                        "n","o","p","q","r","s","t","u","v","w","x","y","z"};
        Random rand = new Random();
        int line = 0;
        int genNumber = 0;
        int multiplier = 1;
        
        // randomly generates numbers
        for(int i = 0; i < MAXLINES; i++) {
            line = 0;
            multiplier = 1;
            for(int j = 0; j < MAXLENGTH; j++) {
                genNumber = rand.nextInt(10);
                multiplier = 1;
                for(int k = 0; k < j - 1; k++) {
                    multiplier *= 10;
                }
                line += genNumber * multiplier;
            } 
            outNumber.println(line);
        }
        
        String stringLine = "";
        for(int i = 0; i < MAXLINES; i++) {
            stringLine = "";
            for(int j = 0; j < MAXLENGTH; j++) {
                genNumber = rand.nextInt(26);   
                stringLine += array[genNumber];
            } 
            outStrings.println(stringLine);
        }
        
        
    }

}