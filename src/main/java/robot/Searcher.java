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
				String word = (String) (((Map.Entry) it.next()).getKey());
				Integer relevance = ((Integer)(Utilities.getWordFrequency(word).get(word))).intValue();

				keywords.put(word, relevance);
			}
		
		} catch (Exception e) {
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
}
