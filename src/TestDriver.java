/**
 * Created by oscar on 4/13/16.
 */
public class TestDriver
{

	public static void main(String[] args)
	{
		// Create file loaders reading all titles and reviews,
		// set the token frequency limit to be 60 for titles,
		// set the token frequency limit to be 100 for titles,
		// tokens of which the frequency exceeds the limit will
		// be filtered (frequency is calculated based on all files)
		TitleFileLoader titleFileLoader = new TitleFileLoader(60);
		ReviewsFileLoader reviewsFileLoader = new ReviewsFileLoader(50000, 100);

		titleFileLoader.readFile("./Data/film_titles.txt");
		reviewsFileLoader.readFile("./Data/revs/");

		// Create ngram analyzer, set the threshold to be 0.2.
		// Create local distance analyzer, set the threshold to be 0.7.
		// Reviews of which the matching score is less than threshold will be filtered.
		// Process 20 reviews for the purpose of speed, which can be changed by modifying
		// the third input param of nGramAnalyzer and localEditDistanceAnalyzer.
		NGramAnalyzer nGramAnalyzer = new NGramAnalyzer(titleFileLoader, reviewsFileLoader, 20, 2, 0.2f);
		LocalEditDistanceAnalyzer localEditDistanceAnalyzer =
				new LocalEditDistanceAnalyzer(titleFileLoader, reviewsFileLoader, 20, 0.7f);

		TitleReviewMatcher titleReviewMatcher = new TitleReviewMatcher(nGramAnalyzer, localEditDistanceAnalyzer);
		// Enable local edit distance analyzer and disable ngram analyzer
		titleReviewMatcher.enableLocalEditDistanceAnalyzer(true);
		titleReviewMatcher.enableNGramAnalyzer(false);
		titleReviewMatcher.process();
		titleReviewMatcher.writeToAFile("Local edit distance matches");

		// Enable ngram analyzer and disable local edit distance analyzer
		titleReviewMatcher.reset();
		titleReviewMatcher.enableLocalEditDistanceAnalyzer(false);
		titleReviewMatcher.enableNGramAnalyzer(true);
		titleReviewMatcher.process();
		titleReviewMatcher.writeToAFile("NGram matches");

		// Judge whether the reviews reflect positive or negative sentiment
		Evaluator evaluator = new Evaluator(titleReviewMatcher.getMatches());
		evaluator.evaluate();
		evaluator.writeToAFile();
	}
}
