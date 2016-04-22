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
	private HashMap<String, HashMap> matches;

	// Settings
	private float bottomLimitOfLocalMatch = 0.8f;
	private float maxLocalDistanceWeight = 1f;
	private float numAppearanceWeight = 0.2f;
	private float threshold;
	private int numberOfReviewsProcessed;


	public LocalEditDistanceAnalyzer(TitleFileLoader titleFileLoader,
	                                 ReviewsFileLoader reviewsFileLoader,
	                                 int numberOfReviewsProcessed,
	                                 float threshold)
	{
		this.titleFileLoader = titleFileLoader;
		this.reviewsFileLoader = reviewsFileLoader;
		this.threshold = threshold;

		setNumberOfReviewsProcessed(numberOfReviewsProcessed);
		initData();
	}

	public void initData()
	{
		matches = new HashMap<>(numberOfReviewsProcessed);

		int titleCount = titleFileLoader.getTitleCount();
		Set<String> reviewsStrArray = reviewsFileLoader.getTokens().keySet();

		// Initialize hash table of matches of each review
		int count = 0;

		for (String review : reviewsStrArray)
		{
			if (count >= numberOfReviewsProcessed)
			{
				break;
			}

			HashMap<String, Float> scoreForEachTitle = new HashMap<>(titleCount);
			matches.put(review, scoreForEachTitle);
			count++;
		}
	}

	public void process()
	{
		int titleLen;
		int reviewLen;

		Set<String> titleArray = titleFileLoader.getTokens().keySet();

		int count = 0;

		for (Map.Entry<String, HashMap> entry : matches.entrySet())
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

				//if (titleLen > 10)
				{
					computeLocalEditDistance(titleLen, reviewLen, title, review);
					float score = mark();

					if (score > threshold)
					{
						scoreForEachTitle.put(title, score);
					}
				}
			}
		}
	}

	public void setNumberOfReviewsProcessed(int num)
	{
		numberOfReviewsProcessed = num;
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

	public void printMatches()
	{
		for (Map.Entry<String, HashMap> entry : matches.entrySet())
		{
			String match = "";
			float highestScore = 0;

			HashMap<String, Float> titleScores = entry.getValue();

			System.out.println(entry.getKey());

			for (Map.Entry<String, Float> score : titleScores.entrySet())
			{
				if (score.getValue() > highestScore)
				{
					match = score.getKey();
					highestScore = score.getValue();
				}

				System.out.print(score.getKey() + ": " + score.getValue() + "\n");
			}

			System.out.println("\nBest Match: <<" + match + ">> score: " + highestScore + "\n\n");
		}
	}

	public HashMap<String, HashMap> getMatches()
	{
		return matches;
	}
}
