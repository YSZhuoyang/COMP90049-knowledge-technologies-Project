import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by oscar on 4/13/16.
 */
public class TitleFileLoader
{
    private TokenRetriever retriever;

    public TitleFileLoader()
    {
        retriever = new TokenRetriever();
    }

    public void readFile(String path)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));

            String line = br.readLine();

            while (line != null)
            {
                retriever.retrieveTokens(line);
                line = br.readLine();
            }

            br.close();
        }
        catch (Exception e)
        {
            System.err.println("Failed to read file from: " + path);
            System.exit(1);
        }

        retriever.filter();
        retriever.computeWeight();
    }

    public HashMap<String, ArrayList> getTokens()
    {
        return retriever.getTokens();
    }

    public int getTitleCount()
    {
        return retriever.getTokens().keySet().size();
    }

    public void printTokens()
    {
        retriever.printTokens();
    }

    public void printFrequencies()
    {
        retriever.printFrequencies();
    }

    public void printWeight()
    {
        retriever.printWeight();
    }

    public void printHighFrequencyTokens()
    {
        retriever.printHighFrequencyTokens();
    }
}
