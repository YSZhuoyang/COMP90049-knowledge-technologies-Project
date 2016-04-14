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

    public ReviewsFileLoader()
    {
        retriever = new TokenRetriever();
        retriever.setFrequencyLimit(80);
    }

    public void readFile(String path)
    {
        StringBuilder sb = new StringBuilder();
        String pathToFile;

        for (int i = 1; i <= 30000; i++)
        {
            /*if (i % 100 == 0)
            {
                System.out.println("Processing: " + i);
            }*/

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
                    sb.append(line);
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
    }

    public HashMap<String, ArrayList> getTokens()
    {
        return retriever.getTokens();
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
