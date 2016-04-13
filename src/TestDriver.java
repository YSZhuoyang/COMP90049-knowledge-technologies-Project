/**
 * Created by oscar on 4/13/16.
 */
public class TestDriver
{

    public static void main(String[] args)
    {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        dataAnalyzer.readFile("./Data/film_titles.txt");
        dataAnalyzer.printTokens();
    }
}
