/**
 * Created by oscar on 4/13/16.
 */
public class TestDriver
{

	public static void main(String[] args)
	{
		TitleFileLoader titleFileLoader = new TitleFileLoader(60);
		ReviewsFileLoader reviewsFileLoader = new ReviewsFileLoader(10, 60);

		titleFileLoader.readFile("./Data/film_titles.txt");
		//titleFileLoader.printTokens();
		//titleFileLoader.printFrequencies();
		//titleFileLoader.printWeight();
		//titleFileLoader.printHighFrequencyTokens();

		reviewsFileLoader.readFile("./Data/revs/");
		//reviewsFileLoader.printTokens();
		//reviewsFileLoader.printHighFrequencyTokens();
		//reviewsFileLoader.printFrequencies();

		NGramAnalyzer nGramAnalyzer = new NGramAnalyzer(titleFileLoader, reviewsFileLoader, 2);
		//nGramAnalyzer.process();
		//nGramAnalyzer.printScore();

		LocalEditDistanceAnalyzer localEditDistanceAnalyzer =
				new LocalEditDistanceAnalyzer(titleFileLoader, reviewsFileLoader);
		//localEditDistanceAnalyzer.process();
		//localEditDistanceAnalyzer.printScore();

		TitleReviewMatcher titleReviewMatcher = new TitleReviewMatcher(nGramAnalyzer, localEditDistanceAnalyzer);
		titleReviewMatcher.enableLocalEditDistanceAnalyzer(true);
		titleReviewMatcher.process();
		titleReviewMatcher.printScore();
	}
}
