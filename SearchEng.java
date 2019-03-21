import java.io.*;
import java.lang.Math;
import java.util.*;
import org.tartarus.snowball.ext.PorterStemmer;
/**
 * Write a description of class SearchEng here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class SearchEng
{
    // instance variables - replace the example below with your own
    private static Vector[] vsmModel;
    private static double[] queryVector;
    /**
     * Constructor for objects of class SearchEng
     */
    public SearchEng()
    {
        // initialise instance variables

    }

    private static void keyList()
    {
//         StringBuilder keyWord = new StringBuilder();
// //         String filename = ("keywords.txt");
// //         try
// //         {
// //             FileReader inputFile = new FileReader(filename);
// //             try
// //             {
// //                 Scanner parser = new Scanner(inputFile);
// //                 do{
// //                     //regular expression(?i) ignore the case and \\b set the word boundary
// //                     keyWord.append(parser.nextLine()+"\n");
// //                 }while (parser.hasNextLine());
// //             }
// //             finally
// //             {
// //                 System.out.println("Key word list has been loaded!");
// //                 inputFile.close();
// //             }
// //         }
// //         catch(FileNotFoundException exception)
// //         {
// //             System.out.println(filename + " not found");
// //         }
// //         catch(IOException exception)
// //         {
// //             System.out.println("Unexpected I/O exception occurs");
// //         }
//         return keyWord.toString();
    }

    private static String readIndex(String indexDir)
    {
        StringBuilder indexList = new StringBuilder();
        String filename = (".\\" + indexDir + "\\myindex.txt");
        try
        {
            FileReader inputFile = new FileReader(filename);
            try
            {
                Scanner parser = new Scanner(inputFile);
                do{
                    //regular expression(?i) ignore the case and \\b set the word boundary
                    indexList.append(parser.nextLine()+"\n");
                }while (parser.hasNextLine());
            }
            finally
            {
                System.out.println("Key word list has been loaded!");
                inputFile.close();
            }
        }
        catch(FileNotFoundException exception)
        {
            System.out.println(filename + " not found");
        }
        catch(IOException exception)
        {
            System.out.println("Unexpected I/O exception occurs");
        }
        return indexList.toString();
    }

    private static String stemmer(String fullDoc)
    {
        StringBuilder docsForSte = new StringBuilder();
        PorterStemmer stemmer = new PorterStemmer();
        //PorterStemmer calss is from package org.tartarus.snowball.ext
        for(String line : fullDoc.split("\n"))
        {
            stemmer.setCurrent(line);
            stemmer.stem();
            docsForSte.append(stemmer.getCurrent().trim());
            docsForSte.append("\n");
        }
        fullDoc = docsForSte.toString();
        System.out.println(fullDoc);
        return fullDoc;
    }

    private static double calWeight(int tf,double idf)
    {
        return tf * idf;
    }

    private static void createVector(String keyword, String index) 
    {
        int keyCount = keyword.split("\n").length;
        queryVector = new double[keyCount];
        docList(keyCount);
        for(String key : keyword.split("\n"))
        {
            for(String indexLine : index.split("\n"))
            {
                if(indexLine.toLowerCase().contains(key.toLowerCase()))
                {
                    String[] elements = indexLine.split(",");
                    String term = elements[0];
                    int docId = 0;
                    int tf = 0;
                    double idf = 0.0;
                    double weight = 0.0;
                    for(int i = 1;i < elements.length;i+=2)
                    {
                        if(i != (elements.length - 1))
                        {
                            Vector myVector = new Vector();
                            docId = toInteger(elements[i]);
                            tf = toInteger(elements[i+1]);
                            idf = Double.parseDouble(elements[elements.length-1]);
                            weight = calWeight(tf,idf);
                            vsmModel[docId-1].setModel(Arrays.asList(keyword.split("\n")).indexOf(key), weight);
                            queryVector[Arrays.asList(keyword.split("\n")).indexOf(key)] += weight;
                        }
                    }
                }
            }
            //ArrayList<String> items = Arrays.asList(str.split(","));
        }
    }

    public static void getConsine()
    {
        double molecule = 0;
        double deno1 = 0;
        double deno2 = 0;
        double cosine = 0;
        for(int i = 0;i < vsmModel.length;i++)
        {
            for(int j = 0;j < queryVector.length;j++)
            {
                molecule += vsmModel[i].getElement(j) * queryVector[j];
                deno1 += Math.pow(vsmModel[i].getElement(j),2);
                deno2 += Math.pow(queryVector[j],2);
            }
            double deno = Math.sqrt(deno1) * Math.sqrt(deno2);
            if(deno != 0) 
                cosine = molecule / deno;
            else
                cosine = 0;
            vsmModel[i].setScore(cosine);
        }
    }

    public static void sortScore()
    {
        Vector temp = new Vector();
        //use bubblesort here
    ;    for (int i = 0;i < vsmModel.length;i++)
        {
            for (int j = 0;j < vsmModel.length- 1 - i;j++)
            {
                if (vsmModel[j].getScore() <= vsmModel[j+1].getScore())
                {
                    temp = vsmModel[j];
                    vsmModel[j] = vsmModel[j+1];
                    vsmModel[j+1] = temp;
                }
            }
        }
    }

    private static void docList(int keyCount)
    {
        int docNum = 0;
        int docCount = 0;
        File file = new File(".\\collection\\");
        //File[] files = file.listFiles();
        for(File f : file.listFiles())
        {
            System.out.println(f.getName());
            //vsmModel[docNum].setToken("#" + docNum +" " + f.getName());
            docNum++;
        }

        vsmModel = new Vector[docNum];
        //StringBuilder vectorLine = new StringBuilder();
        //int[] weightVec = new int[keyNo];
        for(int i = 0;i < docNum;i++)
        {
            double[] initialArray = new double[keyCount];
            vsmModel[i] = new Vector(initialArray);
        }
        
        for(File f : file.listFiles())
        {
            System.out.println(f.getName());
            vsmModel[docCount].setToken(f.getName());
            docCount++;
        }
        //return docNum;
        //System.out.println(listOfDocs);
    }

    public static void displayQuery()
    {
        for(int i = 0;i < queryVector.length;i++)
            System.out.println(queryVector[i]);
    }

    public static void displayVector()
    {
        for(int i = 0;i < vsmModel.length;i++)
            System.out.println(Arrays.toString(vsmModel[i].getVectorModel()));
    }

    public static void disScore()
    {
        for(int i = 0;i < vsmModel.length;i++)
            System.out.println(vsmModel[i].getScore());
    }

    public static void disName(int topNum)
    {
        for(int i = 0;i < topNum;i++)
            System.out.println(vsmModel[i].getToken());
    }

    public static int toInteger(String line)
    {
        return Integer.parseInt(line.replaceAll("d",""));
    }

    public static void main(String args[])
    {
        StringBuilder keyword = new StringBuilder();
        String index = readIndex(args[0]);
        int topNum = Integer.valueOf(args[1]);
        //String keyword = stemmer(keyList());

        for(int i = 2;i < args.length;i++)
        {
            keyword.append(args[i]+"\n");
        }
        createVector(keyword.toString(), index);

        getConsine();
        displayVector();//
        //disName(topNum);//
        //disScore();//
        sortScore();
        disName(topNum);//
        
 
    }
}
