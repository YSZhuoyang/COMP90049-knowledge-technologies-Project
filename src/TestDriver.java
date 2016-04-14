/**
 * Created by oscar on 4/13/16.
 */
public class TestDriver
{

    public static void main(String[] args)
    {
        TitleFileLoader titleFileLoader = new TitleFileLoader();
        ReviewsFileLoader reviewsFileLoader = new ReviewsFileLoader();

        titleFileLoader.readFile("./Data/film_titles.txt");
        //titleFileLoader.printTokens();
        //titleFileLoader.printFrequencies();
        //titleFileLoader.printWeight();
        //titleFileLoader.printHighFrequencyTokens();

        reviewsFileLoader.readFile("./Data/revs/");
        //reviewsFileLoader.printHighFrequencyTokens();
        //reviewsFileLoader.printFrequencies();

        NGramAnalyzer nGramAnalyzer = new NGramAnalyzer(titleFileLoader, reviewsFileLoader);
        nGramAnalyzer.process();
        nGramAnalyzer.printMatches();
    }
}