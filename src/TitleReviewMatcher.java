import java.util.HashMap;
import java.util.Map;

/**
 * Created by oscar on 4/15/16.
 */
public class TitleReviewMatcher
{
	private NGramAnalyzer nGramAnalyzer;
	private LocalEditDistanceAnalyzer localEditDistanceAnalyzer;
	private HashMap<String, HashMap> matches;

	// Settings
	private boolean nGramAnalyzerEnabled;
	private boolean localEditDistanceAnalyzerEnabled;


	public TitleReviewMatcher(NGramAnalyzer nGramAnalyzer, LocalEditDistanceAnalyzer localEditDistanceAnalyzer)
	{
		this.nGramAnalyzer = nGramAnalyzer;
		this.localEditDistanceAnalyzer = localEditDistanceAnalyzer;

		// Enable nGram analyzer, disable local edit distance analyzer by default
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
			matches = nGramAnalyzer.getMatches();

			localEditDistanceAnalyzer.process();
			HashMap<String, HashMap> localEditDistanceMatches = localEditDistanceAnalyzer.getMatches();

			for (Map.Entry<String, HashMap> entry : matches.entrySet())
			{
				String review = entry.getKey();
				HashMap<String, Float> nGramScoreForEachTitle = entry.getValue();
				HashMap<String, Float> localScoreForEachTitle = localEditDistanceMatches.get(review);

				for (Map.Entry<String, Float> localTitleScoreEntry : localScoreForEachTitle.entrySet())
				{
					String title = localTitleScoreEntry.getKey();

					if (nGramScoreForEachTitle.containsKey(title))
					{
						nGramScoreForEachTitle.put(title, nGramScoreForEachTitle.get(title) + localTitleScoreEntry.getValue());
					}
					else
					{
						nGramScoreForEachTitle.put(title, localTitleScoreEntry.getValue());
					}
				}
			}
		}
		else if (localEditDistanceAnalyzerEnabled)
		{
			localEditDistanceAnalyzer.process();
			matches = localEditDistanceAnalyzer.getMatches();
		}
		else if (nGramAnalyzerEnabled)
		{
			nGramAnalyzer.process();
			matches = nGramAnalyzer.getMatches();
		}
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
}
