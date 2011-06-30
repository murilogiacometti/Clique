package clique.robot;

import java.net.*;
import java.io.*;
import java.util.regex.*;
import java.util.*;



public class Robot {

	private ArrayList<String>  download(String adress, String word, int limits ){
		
		String buffer = "";
		
		URL url = new URL(address);
		
		URLConnection con = url.openConnection();

		InputStream os = con.getInputStream();

		BufferedReader cr = new BufferedReader(new InputStreamReader(os));

		String str = null;

		while ((str = cr.readline()) != null) {
			buffer += str;
		}
		
		Pattern pt = Pattern.compile(word);
		Matcher m = pt.matcher(buffer);
		int times = 0;
	       	while (m.find()) times++;
		
	
			
			
		}

		


	}


	public static ArrayList<String> search(String query, int results) throws Exception {

		String buffer = "";
		URL url = new URL("http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=YahooDemo&query="
				+ query + "&results=" + results + "&output=json");
		
		URLConnection uccon = url.openConnection();
	        		
		InputStream is = uconn.getInputStream();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		String str = null;
		
		while ((str = br.readline()) != null) {
			
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

		for (i = 0; i < count; i++){
			JSONObject resultObject = jresponse.getJSONObject(i);
			title[i] = resultObject.get("Title");
			summary[i] = resultObject.get("Summary");
			site[i] = resultObject.get("Url");	
		}
	
	
	}

