import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.tartarus.snowball.ext.PorterStemmer;
import java.lang.Math;
/**
 * Write a description of class Test here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Test
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class Test
     */
    public Test()
    {
        // initialise instance variables
        x = 0;
    }

    public static double calWeight(int tf,double idf)
    {
        return tf * idf;
    }
    
    public static String getIdf(String line,int docNum)
    {
        double df = 0;
        int i = 0;
        for(i=0;i < line.length();i++)
        {
            if(line.charAt(i) == 'd')
            {
                df+=1;
            }
        }
        return String.format("%.6f",Math.log(docNum/(df)));
    }
    private static String DuplicateRm(String docs)
    {
        String[] tokens = docs.split("\n");
        StringBuilder forDup = new StringBuilder();
        tokens = new HashSet<String>(Arrays.asList(tokens)).toArray(new String[0]);
        for(String token : tokens) {
            forDup.append(token + "\n++");
        }
        String result = forDup.toString();
        //docs = docs.replaceAll("(?i)\\b([a-z]+)\\b(?:\\s+\\1\\b)+", "$1");
        //docs = docs.replaceAll("_+", " ");
        return result;
    }
    //save the data to file
    public void writeLinesToFile(String result) 
    { 
        try      
        { 
            PrintWriter outputFile = new PrintWriter("statistics.txt"); 
            System.out.println("Writing a message to the file.."); 
            for (String line : result.split(","))
                outputFile.println(line);
            outputFile.close(); 
        }
        catch(IOException e)
        {
            System.out.println("Error! Can't write to the file!");
        }
    }
    
    public static void main(String args[])
    {
        System.out.println(DuplicateRm("thank you\nthank you\nthanks\nthank")); 
    }
}
