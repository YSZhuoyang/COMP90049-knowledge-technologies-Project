import java.util.HashMap;
import java.util.Map;

/**
 * Created by oscar on 4/15/16.
 */
public class TitleReviewMatcher
{
	private NGramAnalyzer nGramAnalyzer;
	private LocalEditDistanceAnalyzer localEditDistanceAnalyzer;
	private HashMap<String, HashMap> matchingScores;

	// Settings
	private boolean nGramAnalyzerEnabled;
	private boolean localEditDistanceAnalyzerEnabled;

	public TitleReviewMatcher(NGramAnalyzer nGramAnalyzer, LocalEditDistanceAnalyzer localEditDistanceAnalyzer)
	{
		this.nGramAnalyzer = nGramAnalyzer;
		this.localEditDistanceAnalyzer = localEditDistanceAnalyzer;

		nGramAnalyzerEnabled = true;
		localEditDistanceAnalyzerEnabled = false;
	}

	public void enableNGramAnalyzer(boolean nGramSwitch)
	{
		nGramAnalyzerEnabled = nGramSwitch;
	}

	public void enableLocalEditDistanceAnalyzer(boolean localEditDistanceSwitch)
	{
		localEditDistanceAnalyzerEnabled = localEditDistanceSwitch;
	}

	public void process()
	{
		if (nGramAnalyzerEnabled && localEditDistanceAnalyzerEnabled)
		{
			nGramAnalyzer.process();
			matchingScores = nGramAnalyzer.getScores();

			localEditDistanceAnalyzer.process();
			HashMap<String, HashMap> localEditDistanceScores = localEditDistanceAnalyzer.getScores();

			for (Map.Entry<String, HashMap> entry : matchingScores.entrySet())
			{
				String review = entry.getKey();
				HashMap<String, Float> nGramScoreForEachTitle = entry.getValue();
				HashMap<String, Float> localScoreForEachTitle = localEditDistanceScores.get(review);

				for (Map.Entry<String, Float> titleScoreEntry : nGramScoreForEachTitle.entrySet())
				{
					String title = titleScoreEntry.getKey();
					titleScoreEntry.setValue(titleScoreEntry.getValue() + localScoreForEachTitle.get(title));
				}
			}
		}
		else if (localEditDistanceAnalyzerEnabled)
		{
			localEditDistanceAnalyzer.process();
			matchingScores = localEditDistanceAnalyzer.getScores();
		}
		else if (nGramAnalyzerEnabled)
		{
			nGramAnalyzer.process();
			matchingScores = nGramAnalyzer.getScores();
		}
	}

	public void printScore()
	{
		String match = "";
		float scoreMatch = 0;

		for (Map.Entry<String, HashMap> entry : matchingScores.entrySet())
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
}
