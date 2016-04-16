/**
 * Created by oscar on 4/13/16.
 */
public class TestDriver
{

	public static void main(String[] args)
	{
		TitleFileLoader titleFileLoader = new TitleFileLoader(60);
		ReviewsFileLoader reviewsFileLoader = new ReviewsFileLoader(50000, 100);

		titleFileLoader.readFile("./Data/film_titles.txt");
		reviewsFileLoader.readFile("./Data/revs/");

		NGramAnalyzer nGramAnalyzer = new NGramAnalyzer(titleFileLoader, reviewsFileLoader, 2, 0.2f);
		LocalEditDistanceAnalyzer localEditDistanceAnalyzer =
				new LocalEditDistanceAnalyzer(titleFileLoader, reviewsFileLoader, 0.7f);

		TitleReviewMatcher titleReviewMatcher = new TitleReviewMatcher(nGramAnalyzer, localEditDistanceAnalyzer);
		titleReviewMatcher.enableLocalEditDistanceAnalyzer(false);
		titleReviewMatcher.enableNGramAnalyzer(true);
		titleReviewMatcher.process();
		titleReviewMatcher.printMatches();
	}
}
