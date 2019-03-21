import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.tartarus.snowball.ext.PorterStemmer;
import java.lang.Math;

/**
 * This is the text-processing module of the search engine.
 *
 * @author Kaihan Xie
 * @version v2.0
 */

public class Index
{

    private static ArrayList<String> listOfDocs = new ArrayList<String>();
    private static ArrayList<String> docContainer = new ArrayList<String>();
    private String document;

    /**
     * Constructor for objects of class Index
     */
    public Index()
    {
        // initialise instance variables
        //docNo = 0;
        //document = "";
    }

    public static String readFile(String fileName)
    {
        StringBuilder fullDoc = new StringBuilder();
        String filename = (".\\collection\\" + fileName);
        try
        {
            FileReader inputFile = new FileReader(filename);
            try
            {
                Scanner parser = new Scanner(inputFile);
                do{
                    fullDoc.append(parser.nextLine());
                    fullDoc.append("\n");
                }while (parser.hasNextLine());
            }
            finally
            {
                //System.out.println(fullDoc);
                System.out.println("The data has been loaded to the system!");
                inputFile.
                close();
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
        String docs = fullDoc.toString();
        return docs;
    }

    private static String delimeter(String docs)
    {
        StringBuilder fullDoc = new StringBuilder();
        String regex =
            "((?<=\\w)s?'(?:s\\b)?)"//1 Tokenize apostrophes as 's and s'
            + "|'([^']*)'"//2 Tokenize single quote
            + "|(\"([^\"]*)\")"//3 Tokenize double quote
            + "|(\\b\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}\\b)"//4 Tokenize IP address
            + "|(\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b)"//5 Tokenize E-mail address
            + "|(\\b\\w*-\\w*\\b)"//7 Tokenize hyphenated words
            + "|(\\bhttps?://[A-Za-z]{2,4}.\\w+\\.[A-Za-z]{2,4}\\b)"//8 Tokenize URL
            + "|(\\b\\S.\\S.\\S\\b)"//9 Tokenize Acronym
            + "|((?:[A-Z]\\w*\\s*)+)" //10 Tokenize book title
            + "|(\\w+)";

        int count = 0;
        Matcher pat = Pattern.compile(regex).matcher(docs);
        while (pat.find()) {
            if (pat.group(1) != null) {
                fullDoc.append(pat.group(1).trim());
                fullDoc.append("\n");
                System.out.println("#1  "+pat.group(1).trim());
            }
            else if (pat.group(2) != null)
            {
                fullDoc.append(pat.group(2).trim());
                fullDoc.append("\n");
                System.out.println("#2  "+pat.group(2).replace("\'","").trim());
            }
            else if (pat.group(3) != null)
            {
                fullDoc.append(pat.group(3).trim());
                fullDoc.append("\n");
                System.out.println("#3  "+pat.group(3).replace("\"","").trim());
            }
            else if (pat.group(4) != null)
            {
                fullDoc.append(pat.group(4).trim());
                fullDoc.append("\n");
                System.out.println("#4  "+pat.group(4).trim());
            }
            else if (pat.group(5) != null)
            {
                fullDoc.append(pat.group(5).replaceAll("-","").trim());
                fullDoc.append("\n");
                System.out.println("#5  "+pat.group(5).trim());
            }
            else if (pat.group(6) != null)
            {
                fullDoc.append(pat.group(6).trim());
                fullDoc.append("\n");
                System.out.println("#6  "+pat.group(6).trim());
            }
            else if (pat.group(7) != null)
            {
                fullDoc.append(pat.group(7).trim());
                fullDoc.append("\n");
                System.out.println("#7  "+pat.group(7).replaceAll("-","").trim());
            }
            else if (pat.group(8) != null)
            {
                fullDoc.append(pat.group(8).trim());
                fullDoc.append("\n");
                System.out.println("#8  "+pat.group(8).trim());
            }
            else if (pat.group(9) != null)
            {
                fullDoc.append(pat.group(9).trim());
                fullDoc.append("\n");
                System.out.println("#9  "+pat.group(9).trim());
            }
            else if (pat.group(10) != null)
            {
                fullDoc.append(pat.group(10).trim());
                fullDoc.append("\n");
                System.out.println("#10  "+pat.group(10).trim());
            }
            else if (pat.group(11) != null)
            {
                fullDoc.append(pat.group(11).replaceAll("\\d+","").trim());
                fullDoc.append("\n");
                System.out.println("#11  "+pat.group(11).replaceAll("\\d","").trim());
            }
            else if (pat.group(12) != null)
            {
                fullDoc.append(pat.group(12).replaceAll("[_.:\',;:!?]","").trim());
                fullDoc.append("\n");
                System.out.println("#11  "+pat.group(12).replaceAll("\\d","").trim());
            }
        }
        writeToFile(fullDoc.toString(),"tokenList");
        return fullDoc.toString();
    }

    private static String stopList(String fullDoc, String filename)
    {
        try
        {
            FileReader inputFile = new FileReader(filename);
            try
            {
                Scanner parser = new Scanner(inputFile);
                do{
                    //regular expression(?i) ignore the case and \\b set the word boundary
                    String stopWord = "(?i)\\b" + parser.nextLine() + "\\b\n";
                    fullDoc = fullDoc.replaceAll(stopWord,"");
                }while (parser.hasNextLine());
            }
            finally
            {
                System.out.println("Words in the stop word list has been replaced!");
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
        //fullDoc = fullDoc.replaceAll("\n+", "\n");
        //System.out.println(fullDoc);
        System.out.println("This is the end");
        return fullDoc;
    }

    private static String SpaceRm(String docs)
    {
        docs = docs.replaceAll("\\s+"," ");
        //remove all extra wthitespaces
        docs = docs.replace('\ufeff',' ').trim();
        //as the Filereader above reads a UTF encoded text with a UTF-16 BOM, a zero width no-break space(0xFEFF)
        //shows up in the front of string, so it need to be replaced with the normal whitespace.
        return docs;
    }

    private static String DuplicateRm(String docs)
    {
        String[] tokens = docs.split("\n");
        StringBuilder forDup = new StringBuilder();
        tokens = new HashSet<String>(Arrays.asList(tokens)).toArray(new String[0]);
        for(String token : tokens) {
            forDup.append(token + "\n");
        }
        String result = forDup.toString();
        return result;
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
            docsForSte.append(stemmer.getCurrent().replaceAll("[->_<(),\";*|=+#!?]","").trim());
            docsForSte.append("\n");
        }
        fullDoc = docsForSte.toString().replaceAll("\n+", "\n");;
        //System.out.println(fullDoc);
        return fullDoc;
    }

    private static int countTf(String word, String fullDoc)
    {
        int tf = 0;
        for(String line : fullDoc.split("\n"))
        {
            if(word.equals(line))
            {
                tf+=1;
            }
        }
        return tf;
    }

    public static int docList(String indexDir)
    {
        int docNum = 0;
        File file = new File(".\\"+indexDir+"\\");
        for(File f : file.listFiles())
        {
            System.out.println(f.getName());
            listOfDocs.add(f.getName());
            docNum++;
        }
        
        System.out.println(listOfDocs);
        return docNum;
    }

    private static String getIdf(String line,int docNum)
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

    private static void textProcess(String colDir, String indexDir, String stopWord)
    {
        StringBuilder tempDic = new StringBuilder();
        StringBuilder completeDic = new StringBuilder();
        int docNum = docList(colDir);
        for(int i = 0;i < listOfDocs.size();i++)
        {
            String fullDoc = stemmer(stopList(delimeter(SpaceRm(readFile(listOfDocs.get(i)))), stopWord));
            docContainer.add(fullDoc);
            tempDic.append(fullDoc);
        }
        String dic = DuplicateRm(tempDic.toString());
        //String dic = tempDic.toString().replaceAll("(?m)^\\s", "");
        System.out.println(dic);
        //String fullDoc = stemmer(stopList(delimeter(SpaceRm(readFile()))));
        // StringBuilder docs = new StringBuilder();
        int num = 0;
        for(String line : dic.split("\n"))
        {
            int tf = 0;
            int idf = 0;
            int docId = 0;
            StringBuilder tfCom = new StringBuilder();
            completeDic.append(line + ",");
            System.out.println("Process Complete "+num);num++;
            for(docId = 0;docId < docContainer.size();docId++)
            {
                String curDoc = docContainer.get(docId);
                innerLop: for(String line2 : curDoc.split("\n"))
                {

                    if(line.equals(line2))
                    {
                        tf = countTf(line2,curDoc);
                        tfCom.append("d"+ listOfDocs.get(docId).replaceAll(".txt","") +","+tf+",");
                        break innerLop;
                    }

                }
            }

            completeDic.append(tfCom.toString()+getIdf(tfCom.toString(),docNum)+"\n");
        }
        //System.out.println(completeDic.toString());
        writeToFile(completeDic.toString(), indexDir);
        System.out.println("Process Completed");
    }

    private static void writeToFile(String result, String indexDir)
    {
        try
        {
            PrintWriter outputFile = new PrintWriter(".\\" + indexDir + "\\myindex.txt");
            System.out.println("Writing index to the file..");
            for (String line : result.split("\n"))
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
        textProcess(args[0], args[1], args[2]);
    }
}

