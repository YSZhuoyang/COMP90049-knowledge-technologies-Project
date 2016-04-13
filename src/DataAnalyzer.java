import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by oscar on 4/13/16.
 */
public class DataAnalyzer
{
    private ArrayList<String> tokens;

    public DataAnalyzer()
    {
        tokens = new ArrayList<>();
    }

    public void readFile(String path)
    {
        //Pattern wordPattern = Pattern.compile("[a - z A - Z 0 - 9]");
        String regTime = "[0-2][0-3]:[0-5][0-9]";
        String regName = "";

        Pattern wordPattern = Pattern.compile("(\\d|\\w|,)+");
        Matcher matcher;

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));

            //StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null)
            {
                // Retrieve matched tokens
                matcher = wordPattern.matcher(line);

                while (matcher.find())
                {
                    tokens.add(matcher.group());
                }


                // Build string
                //sb.append(line);
                //sb.append(System.lineSeparator());
                line = br.readLine();
            }

            //String everything = sb.toString();

            br.close();
        }
        catch (Exception e)
        {
            System.err.println("Failed to read file from: " + path);
            System.exit(1);
        }
    }

    public void printTokens()
    {
        for (String s : tokens)
        {
            System.out.println(s);
        }
    }

    public ArrayList<String> getTokens()
    {
        return tokens;
    }
}
