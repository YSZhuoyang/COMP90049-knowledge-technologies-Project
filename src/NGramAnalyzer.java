import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by oscar on 4/14/16.
 */
public class NGramAnalyzer
{
	private TitleFileLoader titleFileLoader;
	private ReviewsFileLoader reviewsFileLoader;
	private HashMap<String, HashMap> reviewScoreHash;
	private int combLength;

	public NGramAnalyzer(TitleFileLoader titleFileLoader,
	                     ReviewsFileLoader reviewsFileLoader,
	                     int lengthOfCombination)
	{
		this.titleFileLoader = titleFileLoader;
		this.reviewsFileLoader = reviewsFileLoader;

		int titleCount = titleFileLoader.getTitleCount();
		int reviewCount = reviewsFileLoader.getReviewCount();
		Set<String> reviewsStrArray = reviewsFileLoader.getTokens().keySet();

		reviewScoreHash = new HashMap<>(reviewCount);
		combLength = lengthOfCombination;

		for (String review : reviewsStrArray)
		{
			HashMap<String, Float> scoreForEachTitle = new HashMap<>(titleCount);
			reviewScoreHash.put(review, scoreForEachTitle);
		}
	}

	public void process()
	{
		HashMap<String, ArrayList> tokensInTitle = titleFileLoader.getTokens();
		HashMap<String, ArrayList> tokensInReview = reviewsFileLoader.getTokens();
		HashMap<String, Float> titleTokenWeight = titleFileLoader.getTokenWeight();
		HashMap<String, Float> reviewTokenWeight = reviewsFileLoader.getTokenWeight();
		Set<String> titleArray = tokensInTitle.keySet();

		//int count = 0;

		for (Map.Entry<String, HashMap> entryReview : reviewScoreHash.entrySet())
		{
			/*if (count % 1000 == 0)
			{
				System.out.println("Processing: " + count + "th review");
			}

			count++;*/

			String review = entryReview.getKey();
			ArrayList tokensInEachReview = tokensInReview.get(review);
			HashMap scoreForEachTitle = entryReview.getValue();

			for (String title : titleArray)
			{
				ArrayList<String> tokensInEachTitle = tokensInTitle.get(title);

				scoreForEachTitle.put(title, computeNGramScore(tokensInEachTitle,
				                                               tokensInEachReview,
				                                               titleTokenWeight,
				                                               reviewTokenWeight));
			}
		}
	}

	private float computeNGramScore(ArrayList<String> titleTokens,
	                                ArrayList<String> reviewTokens,
	                                HashMap<String, Float> titleTokenWeight,
	                                HashMap<String, Float> reviewTokenWeight)
	{
		float nGramDistance;
		int tokenTitleLen;
		int tokenReviewLen;
		float score = 0f;

		for (String titleToken : titleTokens)
		{
			tokenTitleLen = titleToken.length();

			for (String reviewToken : reviewTokens)
			{
				nGramDistance = computeNGramDistance(titleToken, reviewToken);
				tokenReviewLen = reviewToken.length();

				// Consider the length of words when judging whether nGram distance meets the criteria
				if (nGramDistance / (float) Math.min(tokenTitleLen, tokenReviewLen) < 0.2f)
				{
					//System.out.println("Title token: " + titleToken + " matches review token: " + reviewToken);

					score += (titleTokenWeight.get(titleToken) + reviewTokenWeight.get(reviewToken)) * 0.5f;
				}
			}
		}

		if (score > 0f)
		{
			return score;
		}
		else
		{
			return -0.5f;
		}
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

		char[][] subCombsA = new char[tokenA.length() - 1][combLength];
		char[][] subCombsB = new char[tokenB.length() - 1][combLength];
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
		for (int i = 0; i < combLength; i++)
		{
			if (combA[i] != combB[i])
			{
				return false;
			}
		}

		return true;
	}

	public void printScore()
	{
		String match = "";
		float scoreMatch = 0;

		for (Map.Entry<String, HashMap> entry : reviewScoreHash.entrySet())
		{
			HashMap<String, Float> titleScores = entry.getValue();

			System.out.println(entry.getKey());

			for (Map.Entry<String, Float> score : titleScores.entrySet())
			{
				if (score.getValue() > scoreMatch)
				{
					scoreMatch = score.getValue();
					match = score.getKey();
				}

				System.out.print(score.getKey() + ": " + score.getValue() + "\n");
			}

			System.out.println("\n");

			break;
		}

		System.out.println("Match: " + match + ": " + scoreMatch);
	}

	public HashMap<String, HashMap> getScores()
	{
		return reviewScoreHash;
	}
}
