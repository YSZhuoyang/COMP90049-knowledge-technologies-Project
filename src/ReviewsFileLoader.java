import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by oscar on 4/14/16.
 */
public class ReviewsFileLoader
{
    private TokenRetriever retriever;

	// Settings
	private int numberOfReviewsLoaded;


    public ReviewsFileLoader(int numberOfReviewsLoaded, int tokenFrequencyLimit)
    {
        retriever = new TokenRetriever();
        retriever.setFrequencyLimit(tokenFrequencyLimit);
	    this.numberOfReviewsLoaded = numberOfReviewsLoaded;
    }

    public void readFile(String path)
    {
        StringBuilder sb = new StringBuilder();
        String pathToFile;

        for (int i = 1; i <= numberOfReviewsLoaded; i++)
        {
            pathToFile = path + i + ".txt";

            if (!Files.exists(Paths.get(pathToFile)))
            {
                continue;
            }

            try
            {
                BufferedReader br = new BufferedReader(new FileReader(pathToFile));

                String line = br.readLine();

                while (line != null)
                {
                    sb.append(line.toLowerCase());
                    line = br.readLine();
                }

                br.close();
            }
            catch (Exception e)
            {
                System.err.println("Failed to read file from: " + pathToFile);
                System.exit(1);
            }

            retriever.retrieveTokens(sb.toString());
            sb.setLength(0);
        }

        retriever.filter();
        retriever.computeWeight();
    }

    public HashMap<String, ArrayList> getTokens()
    {
        return retriever.getTokens();
    }

    public HashMap<String, Float> getTokenWeight()
    {
        return retriever.getTokenWeight();
    }

    public int getReviewCount()
    {
        return retriever.getTokens().size();
    }

    public void printTokens()
    {
        retriever.printTokens();
    }

    public void printFrequencies()
    {
        retriever.printFrequencies();
    }

    public void printHighFrequencyTokens()
    {
        retriever.printHighFrequencyTokens();
    }
}
