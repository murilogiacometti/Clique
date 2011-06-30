import org.apache.lucene.analysis.*;
import java.io.*;

import java.util.*;

class AnalyzerUtils { 
	public static Token[] tokensFromAnalysis(Analyzer analyzer, String text) throws IOException {
		TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));

		ArrayList tokenList = new ArrayList();

		while (true) {
			Token token = stream.next();
			if (token == null) break;

			tokenList.add(token);
		}

		return (Token[]) tokenList.toArray(new Token[0]);
	}

 	public static String readFileAsString(String filePath)
    	throws java.io.IOException{
        
		StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}

public class Brincadeirinha { 
	public static void main(String[] args)  throws Exception {
		File stop_words = new File("stop_words");
		StopAnalyzer stop = new StopAnalyzer(stop_words);

		String text = AnalyzerUtils.readFileAsString("input.txt");

		Token[] filtered = AnalyzerUtils.tokensFromAnalysis(stop, text);

		for (int i = 0; i < filtered.length; ++i) {
			System.out.println(filtered[i] + "\n");
		}
	}

}
