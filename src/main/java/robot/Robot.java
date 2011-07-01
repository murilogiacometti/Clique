package clique.robot;

import java.io.*;
import java.util.*;
import java.net.*;

import org.json.*;

public class Robot {

	public static HashMap search(String query, int results, File stop_words) throws Exception {

		String buffer = "";
		String urlSanitized = query.replaceAll(" ", "+");
		URL url = new URL("http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=YahooDemo&query="
						+ urlSanitized + "&results=" + (new Integer(results)).toString() + "&output=json");
		
		URLConnection uconn = url.openConnection();
	        		
		InputStream is = uconn.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		String str = null;
		
		while ((str = br.readLine()) != null) {
			buffer += str;
		}
		
		JSONObject jresponse = new JSONObject(buffer);
		JSONArray jarray ;
		jresponse = jresponse.getJSONObject("ResultSet");
		jarray = jresponse.getJSONArray("Result");
		int count = jarray.length();

		HashMap keywords = new HashMap();
		HashMap search = null;
		
		for (int i = 0; i < count; i++){
			JSONObject resultObject = jarray.getJSONObject(i);
			String webSearch = (String) resultObject.get("Url");
			search = Searcher.search(webSearch, stop_words);

			System.out.println("Searched website " + webSearch + ". Found " + Integer.toString(search.size()) + "keys");

			Set keys = search.keySet();
			Iterator it = keys.iterator();
			int max = 0;

			while (it.hasNext()) {
				String word = (String) it.next();
				Integer relevance = null;

				if (keywords.containsKey(word)) {
					relevance = (Integer) keywords.get(word);
					relevance = new Integer(relevance.intValue() + ((Integer)(search.get(word))).intValue());

					keywords.put(word, relevance);
				
				} else {
					relevance = ((Integer) (search.get(word))).intValue();
					keywords.put(word, relevance);

				}
				
				if (relevance.intValue() > max) {
						max = relevance.intValue();
				}
			}

			System.out.println("max occurs: " + Integer.toString(max));

			keys = keywords.keySet();
			it = keys.iterator();

			while (it.hasNext()) {
				String word = (String)it.next(); 

				Integer relevance = (Integer) keywords.get(word);
				System.out.println("Normalized relevance: "+ relevance.toString()); 

				keywords.put(word, relevance);
			}
		}
		System.out.println("Robot is finished. Keywords found: " + Integer.toString(keywords.size()));
		return keywords;
	
	}
}

