import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oscar on 4/14/16.
 */
public class TokenRetriever
{
    private final static String REGULAR_TIME = "([0-9][0-9]:[0-9][0-9])";
    private final static String REGULAR_NUMBER = "([0-9]+,[0-9]+)";
    private final static String REGULAR_ORG_NAME = "([A-Z]\\w*[.][A-Z]\\w*([.][A-Z]\\w*)*)";
    private final static String REGULAR_PERSON_NAME = "([A-Z]\\w*[.] [A-Z]\\w*)";
    private final static String REGULAR_AND_COMB = "([\\w]+\\s*&\\s*[\\w]+)";
    private final static String REGULAR_SLASH_COMB = "([\\w\\d]+\\s*-\\s*[\\w\\d]+)";
    private final static String REGULAR_STAR_COMB = "([\\w\\d]+[*][\\w\\d]+)";
    private final static String REGULAR_NOUN = "(([Aa]|(An)|(an)|(One)|(one)|(Two)|(two)|" +
            "(Three)|(three)|(Four)|(four)|(Five)|(five)|(Six)|(six)|(Seven)|(seven)|(Eight)|" +
            "(eight)|(Nine)|(nine)|(Ten)|(ten)|(Eleven)|(Eleven)|(Twelve)|(twelve)|(\\d+)) \\w+)";

    private final static String REGULAR_OTHER = "([^\\s\"()?:.,*!&-]+)";
    private final static String REGULAR =
            REGULAR_TIME + "|" +
                    REGULAR_ORG_NAME + "|" +
                    REGULAR_PERSON_NAME + "|" +
                    REGULAR_NUMBER + "|" +
                    REGULAR_AND_COMB + "|" +
                    REGULAR_STAR_COMB + "|" +
                    REGULAR_NOUN + "|" +
                    REGULAR_SLASH_COMB + "|" +
                    REGULAR_OTHER;


    private HashMap<String, ArrayList> tokens;
    private HashMap<String, Integer> tokenFrequency;
    private HashMap<String, Float> tokenWeight;
    private HashSet<String> filtered;
    private Pattern wordPattern;

    // Settings
    private int frequencyLimit;


    public TokenRetriever()
    {
        tokens = new HashMap<>();
        tokenFrequency = new HashMap<>();
        tokenWeight = new HashMap<>();
        filtered = new HashSet<>();
        wordPattern = Pattern.compile(REGULAR);

        frequencyLimit = 50;
    }

    public void setFrequencyLimit(int limit)
    {
        frequencyLimit = limit;
    }

    public void retrieveTokens(String line)
    {
        ArrayList<String> tokensInEachItem = new ArrayList<>();

        // Retrieve tokens matching the reg
        Matcher matcher = wordPattern.matcher(line);

        while (matcher.find())
        {
            String token = matcher.group();

            if (filtered.contains(token))
            {
                continue;
            }

            tokensInEachItem.add(token);

            // Added frequencies
            if (tokenFrequency.containsKey(token))
            {
                if (tokenFrequency.get(token) >= frequencyLimit)
                {
                    // Add to filtering list
                    filtered.add(token);
                    tokenFrequency.remove(token);

                    //System.out.println("Filtered: " + token);
                }
                else
                {
                    tokenFrequency.put(token, tokenFrequency.get(token) + 1);
                }
            }
            else
            {
                tokenFrequency.put(token, 1);
            }
        }

        tokens.put(line, tokensInEachItem);
    }

    private void removeCommonWords()
    {

    }

    public void filter()
    {
        removeCommonWords();

        for (Map.Entry<String, ArrayList> entry: tokens.entrySet())
        {
            // Consider the case where all tokens retrieved are filtered
            if (filtered.containsAll(entry.getValue()))
            {
                entry.getValue().removeAll(filtered);
                entry.getValue().add(entry.getKey());
                tokenFrequency.put(entry.getKey(), 1);

                //System.out.println("Filtering: " + entry.getValue().get(0));
            }
            else
            {
                entry.getValue().removeAll(filtered);
            }
        }
    }

    public void computeWeight()
    {
        for (Map.Entry<String, Integer> entry : tokenFrequency.entrySet())
        {
            tokenWeight.put(entry.getKey(), 50f / (float) entry.getValue());
        }

        // Won't be used anymore
        //tokenFrequency.clear();
    }

    public void printTokens()
    {
        for (Map.Entry<String, ArrayList> entry : tokens.entrySet())
        {
            System.out.print(entry.getKey() + ": ");

            for (String token : (ArrayList<String>) entry.getValue())
            {
                System.out.print("'" + token + "' ");
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

    public void printWeight()
    {
        for (Map.Entry<String, Float> entry : tokenWeight.entrySet())
        {
            System.out.println("Weight of " + entry.getKey() + ": " + entry.getValue());
        }
    }

    public void printHighFrequencyTokens()
    {
        int counter = 0;
        int counterOfTokenInItems = 0;

        for (Map.Entry<String, Integer> entry : tokenFrequency.entrySet())
        {
            if (entry.getValue() > frequencyLimit)
            {
                counter++;
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }

        for (Map.Entry<String, ArrayList> entry : tokens.entrySet())
        {
            counterOfTokenInItems += entry.getValue().size();
        }

        System.out.println("Frequency limit: " + frequencyLimit);
        System.out.println("Number of tokens: " + tokenFrequency.size());
        System.out.println("Number of tokens in items: " + counterOfTokenInItems);
        System.out.println("Number of tokens with high frequencies: " + counter);
    }

    public HashMap<String, ArrayList> getTokens()
    {
        return tokens;
    }
}
