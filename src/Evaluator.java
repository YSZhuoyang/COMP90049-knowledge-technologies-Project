import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by oscar on 4/19/16.
 */
public class Evaluator
{
	private enum Evaluation
	{
		POSITIVE, NEGATIVE, NEUTRAL
	}

	private HashMap<String, Evaluation> evaluation;

	public Evaluator(HashMap<String, HashMap> matches)
	{
		Set<String> reviews = matches.keySet();
		evaluation = new HashMap<>(reviews.size());

		for (String review : reviews)
		{
			evaluation.put(review, Evaluation.NEUTRAL);
		}
	}

	public void evaluate()
	{
		for (Map.Entry<String, Evaluation> entry : evaluation.entrySet())
		{
			String review = entry.getKey();
			review = review.replaceAll("(<br /><br />)|\\s|[^,.\\d\\w-]", "+");

			try
			{
				HttpResponse<JsonNode> response = Unirest.get("https://loudelement-free-natural-language-processing-" +
				                                              "service.p.mashape.com/nlp-text/?text=" + review)
				                                         .header("X-Mashape-Key", "6ubrAVRPefmshJjTbkX2nbbsdEtMp1qtOzBjsn2vAFk4pFMONF")
				                                         .header("Accept", "application/json")
				                                         .asJson();

				String sentiment = response.getBody().getObject().getString("sentiment-text");

				if (sentiment.equals("positive"))
				{
					entry.setValue(Evaluation.POSITIVE);
				}
				else if (sentiment.equals("negative"))
				{
					entry.setValue(Evaluation.NEGATIVE);
				}
				else
				{
					entry.setValue(Evaluation.NEUTRAL);
				}
			}
			catch (UnirestException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void writeToAFile()
	{
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, Evaluation> entry : evaluation.entrySet())
		{
			Evaluation evaluation = entry.getValue();

			switch (evaluation)
			{
				case POSITIVE:
					sb.append(entry.getKey() + "\n\nEvaluation result: " + "Good!\n\n");
					break;

				case NEGATIVE:
					sb.append(entry.getKey() + "\n\nEvaluation result: " + "Bad!\n\n");
					break;

				case NEUTRAL:
					sb.append(entry.getKey() + "\n\nEvaluation result: " + "Neutral\n\n");
					break;

				default:
					break;
			}
		}

		// Write to a txt file under output directory
		PrintWriter writer = null;

		try
		{
			writer = new PrintWriter("./Output/evaluations.txt", "UTF-8");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

		writer.println(sb.toString());
		writer.close();
	}
}
