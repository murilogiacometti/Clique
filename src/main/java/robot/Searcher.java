package clique.robot;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.util.*;
import net.sf.classifier4J.*;
import java.io.*;
import java.util.*;

import java.net.*;

public class Searcher { 
	public static HashMap search(String address, File stop_words)  {
		HashMap keywords = new HashMap();
		try {

			StringBuffer buffer = new StringBuffer();

			URL url = new URL(address);
			URLConnection conn = url.openConnection();

			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String str = null;

			while ((str = reader.readLine()) != null) {
				buffer.append(str);	
			}

			String text = buffer.toString();
			
			text = text.replaceAll("\\<.*?\\>", "");

			StopAnalyzer stop = new StopAnalyzer(Version.LUCENE_32, stop_words);

			String tokens = tokensFromAnalysis(stop, text);

			Set words = Utilities.getMostFrequentWords(10, Utilities.getWordFrequency(tokens));

			Iterator it = words.iterator();

			while (it.hasNext()) {
				String word = (String)it.next(); 

				int r = numberOccurrences(word, tokens);
				if (r > 100) {
					r = 10;
				} else {
					r = r/10;
				}
				Integer relevance = new Integer(r);
				
				System.out.println("Primitive is " + Integer.toString(r));
				keywords.put(word, relevance);
				System.out.println("Got: "+ ((Integer) keywords.get(word)).intValue());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not search URL " + address);
		
		}

		return keywords;
	}

	public static String tokensFromAnalysis(Analyzer analyzer, String text)  {
		StringBuffer buffer = new StringBuffer();
		try {
			
			TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));

			ArrayList tokenList = new ArrayList();
			OffsetAttribute offAttr = stream.getAttribute(OffsetAttribute.class);
			TermAttribute termAttr = stream.getAttribute(TermAttribute.class);
			
			while (stream.incrementToken()) {
				int startOff = offAttr.startOffset();
				int endOff = offAttr.endOffset();
				buffer.append(termAttr.term() + " ");
			}
		} catch (Exception e) {
			System.out.println("Could not analyze (stop words) from search result.");
		}

		return buffer.toString();
	}
	
	public static int numberOccurrences(String findStr, String str)  {
		
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {

                lastIndex = str.indexOf(findStr, lastIndex);

                if (lastIndex != -1) {
                        count++;
                        lastIndex += findStr.length();
                }
        }

		return count;
	}

}
