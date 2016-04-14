import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oscar on 4/14/16.
 */
public class NGramAnalyzer
{
    private TitleFileLoader titleFileLoader;
    private ReviewsFileLoader reviewsFileLoader;
    private HashMap<String, String> matches;
    private int subLength;

    public NGramAnalyzer(TitleFileLoader titleFileLoader, ReviewsFileLoader reviewsFileLoader)
    {
        matches = new HashMap<>(titleFileLoader.getTitleCount());
        subLength = 2;

        this.titleFileLoader = titleFileLoader;
        this.reviewsFileLoader = reviewsFileLoader;
    }

    public void process()
    {
        HashMap<String, ArrayList> tokensInTitle = titleFileLoader.getTokens();
        HashMap<String, ArrayList> tokensInReview = reviewsFileLoader.getTokens();

        for (Map.Entry<String, ArrayList> entryReview : tokensInReview.entrySet())
        {
            ArrayList tokensInEachReview = entryReview.getValue();

            // Reviews that already has been matched should not be considered
            for (Map.Entry<String, ArrayList> entryTitle : tokensInTitle.entrySet())
            {
                ArrayList tokensInEachTitle = entryTitle.getValue();

                if (titleMatchesReview(tokensInEachTitle, tokensInEachReview))
                {
                    matches.put(entryReview.getKey(), entryTitle.getKey());

                    // Should be replaced by comparing with existing matches
                    break;
                }
            }
        }
    }

    private boolean titleMatchesReview(ArrayList<String> titleTokens, ArrayList<String> reviewTokens)
    {
        float ngramDistance;

        for (String titleToken : titleTokens)
        {
            for (String reviewToken : reviewTokens)
            {
                ngramDistance = computeNGramDistance(titleToken, reviewToken);

                if (titleToken.length() - reviewToken.length() < 4 &&
                    ngramDistance / (float) Math.min(titleToken.length(), reviewToken.length()) < 0.2f)
                {
                    System.out.println("Title token: " + titleToken + " matches review token: " + reviewToken);

                    return true;
                }
            }
        }

        return false;
    }

    private int computeNGramDistance(String tokenA, String tokenB)
    {
        if (tokenA.length() == 1 || tokenB.length() == 1)
        {
            if (tokenA.equals(tokenB))
            {
                return 0;
            }
            else
            {
                return 10;
            }
        }

        char[][] subCombsA = new char[tokenA.length() - 1][subLength];
        char[][] subCombsB = new char[tokenB.length() - 1][subLength];
        int s1 = subCombsA.length;
        int s2 = subCombsB.length;
        int intersection = 0;

        // Generate character combinations
        for (int i = 0; i < s1; i++)
        {
            subCombsA[i][0] = tokenA.charAt(i);
            subCombsA[i][1] = tokenA.charAt(i + 1);
        }

        for (int i = 0; i < s2; i++)
        {
            subCombsB[i][0] = tokenB.charAt(i);
            subCombsB[i][1] = tokenB.charAt(i + 1);
        }

        // Compute distance
        for (int i = 0; i < s1; i++)
        {
            for (int j = 0; j < s2; j++)
            {
                if (compareSub(subCombsA[i], subCombsB[j]))
                {
                    intersection++;
                }
            }
        }

        return s1 + s2 - 2 * intersection;
    }

    private boolean compareSub(char[] combA, char[] combB)
    {
        for (int i = 0; i < subLength; i++)
        {
            if (combA[i] != combB[i])
            {
                return false;
            }
        }

        return true;
    }

    public void printMatches()
    {
        for (Map.Entry<String, String> entry : matches.entrySet())
        {
            System.out.println(entry.getKey() + " ==> " + entry.getValue() + "\n");
        }
    }
}
