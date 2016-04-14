import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by oscar on 4/13/16.
 */
public class DataAnalyzer
{
    private final static String REGULAR_TIME = "([0-2][0-3]:[0-5][0-9])";
    private final static String REGULAR_NUMBER = "([0-9]+,[0-9]+)";
    private final static String REGULAR_AND_COMB = "([\\w]+\\s*&\\s*[\\w]+)";
    private final static String REGULAR_SLASH_COMB = "([\\w]+\\s*-\\s*[\\w]+)";
    private final static String REGULAR_NOUN = "(([Aa123456789]|(Two)|(two)|(Three)|" +
            "(three)|(Four)|(four)|(Five)|(five)|(Six)|(six)|(Seven)|(seven)|(Eight)|" +
            "(eight)|(Nine)|(nine)|(Ten)|(ten)|(Eleven)|(Eleven)|(Twelve)|(twelve)) \\w)";

    private final static String REGULAR_OTHER = "([^\\s\"()?:.,&-]+)";
    private final static String REGULAR =
            REGULAR_TIME + "|" +
            REGULAR_OTHER + "|" +
            REGULAR_NUMBER + "|" +
            REGULAR_AND_COMB + "|" +
            REGULAR_NOUN + "|" +
            REGULAR_SLASH_COMB;

    private HashMap<String, ArrayList> tokens;
    private HashMap<String, Integer> tokenFrequency;
    private Pattern wordPattern;


    public DataAnalyzer()
    {
        tokens = new HashMap<>();
        tokenFrequency = new HashMap<>();
        wordPattern = Pattern.compile(REGULAR);
    }

    private void retrieveTokens(String line)
    {
        ArrayList<String> tokensInEachTitle = new ArrayList<>();

        // Retrieve tokens matching the reg
        Matcher matcher = wordPattern.matcher(line);

        while (matcher.find())
        {
            String token = matcher.group();
            tokensInEachTitle.add(token);

            // Added frequencies
            if (tokenFrequency.containsKey(token))
            {
                tokenFrequency.put(token, tokenFrequency.get(token) + 1);
            }
            else
            {
                tokenFrequency.put(token, 1);
            }
        }

        tokens.put(line, tokensInEachTitle);
    }

    private void removeCommonWords()
    {

    }

    private void filter()
    {
        removeCommonWords();
        ArrayList<String> keysToBeRemoved = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : tokenFrequency.entrySet())
        {
            if (entry.getValue() > 20)
            {
                keysToBeRemoved.add(entry.getKey());
            }
        }

        for (String key : keysToBeRemoved)
        {
            tokenFrequency.remove(key);
        }
    }

    public void readFile(String path)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));

            String line = br.readLine();

            while (line != null)
            {
                retrieveTokens(line);
                line = br.readLine();
            }

            br.close();
        }
        catch (Exception e)
        {
            System.err.println("Failed to read file from: " + path);
            System.exit(1);
        }

        filter();
    }

    public void printTokens()
    {
        for (Map.Entry<String, ArrayList> entry : tokens.entrySet())
        {
            System.out.print(entry.getKey() + ": ");

            for (String token : (ArrayList<String>) entry.getValue())
            {
                System.out.print(token + " ");
            }

            System.out.println("");
        }
    }

    public void printFrequencies()
    {
        for (Map.Entry<String, Integer> entry : tokenFrequency.entrySet())
        {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public void printHighFrequencyTokens()
    {
        for (Map.Entry<String, Integer> entry : tokenFrequency.entrySet())
        {
            if (entry.getValue() > 5)
            {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    /*public ArrayList<String> getTokens()
    {
        return tokens;
    }*/
}
