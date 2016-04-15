import java.util.HashMap;

/**
 * Created by oscar on 4/15/16.
 */
public class Review
{
	private String title;
	private String review;

	private float localEditDistanceScore;
	private float nGramScore;

	private HashMap<String, Float> scores;


	public Review(String r, int titleCount)
	{
		scores = new HashMap<>(titleCount);

		title = "";
		review = r;

		localEditDistanceScore = 0f;
		nGramScore = 0f;
	}

	public void setTitle(String t)
	{
		title = t;
	}

	public void setReview(String r)
	{
		review = r;
	}

	public void setLocalEditDistanceScore(float score)
	{
		localEditDistanceScore = score;
	}

	public void setnGramScore(float score)
	{
		nGramScore = score;
	}

	public float getLocalEditDistanceScore()
	{
		return localEditDistanceScore;
	}

	public float getNGramScore()
	{
		return nGramScore;
	}
}
