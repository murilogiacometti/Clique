package clique.robot;

import java.io.*;
import java.util.*;
import java.net.*;

import org.json.*;

public class Robot {

	public static HashMap search(String query, int results, File stop_words) throws Exception {

		String buffer = "";
		URL url = new URL("http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=YahooDemo&query="
				+ query + "&results=" + results + "&output=json");
		
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
		int count = jresponse.length();
		String[] title = null;
		String[] summary = null;
		String[] site = null;

		HashMap keywords = new HashMap();
		HashMap search = null;
		
		for (int i = 0; i < count; i++){
			JSONObject resultObject = jarray.getJSONObject(i);
			title[i] = (String) resultObject.get("Title");
			summary[i] = (String) resultObject.get("Summary");
			site[i] = (String) resultObject.get("Url");
			search = Searcher.search(site[i], stop_words);

			Set keys = search.keySet();
			Iterator it = keys.iterator();
			int max = 0;

			while (it.hasNext()) {
				String word = (String)(((Map.Entry)it.next()).getKey());
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

			keys = keywords.keySet();
			it = keys.iterator();

			int factor = 1;

			if (max > 0) {
				factor = 10/max;
			}

			while (it.hasNext()) {
				String word = (String) (((Map.Entry) it.next()).getKey());

				Integer relevance = (Integer) keywords.get(word);
				int normalized = relevance.intValue() * factor;
				relevance = new Integer(normalized);

				keywords.put(word, relevance);
			}
		}
		
		return keywords;
	
	}
}

