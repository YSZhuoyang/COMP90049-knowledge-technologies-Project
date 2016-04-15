import java.util.*;

/**
 * Created by oscar on 4/15/16.
 */
public class LocalEditDistanceAnalyzer
{
	private TitleFileLoader titleFileLoader;
	private ReviewsFileLoader reviewsFileLoader;

	private int numAppearances;
	private float maxLocalDistance;
	private HashMap<String, HashMap> reviewScoreHash;

	// Settings
	private float bottomLimitOfLocalMatch = 0.8f;
	private float maxLocalDistanceWeight = 1f;
	private float numAppearanceWeight = 0.2f;


	public LocalEditDistanceAnalyzer(TitleFileLoader titleFileLoader, ReviewsFileLoader reviewsFileLoader)
	{
		this.titleFileLoader = titleFileLoader;
		this.reviewsFileLoader = reviewsFileLoader;

		int titleCount = titleFileLoader.getTitleCount();
		int reviewCount = reviewsFileLoader.getReviewCount();
		Set<String> reviewsStrArray = reviewsFileLoader.getTokens().keySet();

		reviewScoreHash = new HashMap<>(reviewCount);

		for (String review : reviewsStrArray)
		{
			HashMap<String, Float> scoreForEachTitle = new HashMap<>(titleCount);
			reviewScoreHash.put(review, scoreForEachTitle);
		}
	}

	public void process()
	{
		int titleLen;
		int reviewLen;

		Set<String> titleArray = titleFileLoader.getTokens().keySet();

		int count = 0;

		for (Map.Entry<String, HashMap> entry : reviewScoreHash.entrySet())
		{
			if (count % 2 == 0)
			{
				System.out.println("Processing: " + count + "th review");
			}

			count++;

			HashMap scoreForEachTitle = entry.getValue();
			String review = entry.getKey();
			reviewLen = review.length();

			for (String title : titleArray)
			{
				titleLen = title.length();

				if (titleLen > 10)
				{
					computeLocalEditDistance(titleLen, reviewLen, title, review);
					scoreForEachTitle.put(title, mark());
				}
				else
				{
					scoreForEachTitle.put(title, 0f);
				}
			}
		}
	}

	private void computeLocalEditDistance(int titleLen, int reviewLen, String title, String review)
	{
		int[][] alignmentTable = new int[titleLen + 1][reviewLen + 1];
		maxLocalDistance = 0;
		numAppearances = 0;

		for (int i = 0; i <= titleLen; i++)
		{
			alignmentTable[i][0] = 0;
		}

		for (int i = 0; i <= reviewLen; i++)
		{
			alignmentTable[0][i] = 0;
		}

		// Get maximum local edit distance
		for (int i = 1; i <= titleLen; i++)
		{
			for (int j = 1; j <= reviewLen; j++)
			{
				alignmentTable[i][j] = maxOfFour(
						0,
						alignmentTable[i - 1][j] - 2, // Insertion
						alignmentTable[i][j - 1] - 2, // Deletion
						alignmentTable[i - 1][j - 1] +
						equal(title.charAt(i - 1), review.charAt(j - 1))); // Match/miss match

				if (alignmentTable[i][j] > maxLocalDistance)
				{
					maxLocalDistance = alignmentTable[i][j];
				}
			}
		}

		maxLocalDistance /= (float) titleLen;

		// Get Number of local matches that meet requirements
		/*for (int i = 1; i <= titleLen; i++)
		{
			for (int j = 1; j < reviewLen; j++)
			{
				if ((float) alignmentTable[i][j] > bottomLimitOfLocalMatch * (float) maxLocalDistance)
				{
					numAppearances++;
				}
			}
		}*/
	}

	private int equal(char a, char b)
	{
		return (a == b) ? 1 : 0;
	}

	private int maxOfFour(int a, int b, int c, int d)
	{
		return Math.max(Math.max(Math.max(a, b), c), d);
	}

	public void setBottomLimitOfLocalMatch(float limit)
	{
		bottomLimitOfLocalMatch = limit;
	}

	private float mark()
	{
		return maxLocalDistance * maxLocalDistanceWeight + numAppearances * numAppearanceWeight;
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
