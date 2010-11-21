package magic.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class KeywordDefinitions {

	private static final KeywordDefinitions INSTANCE=new KeywordDefinitions();

	private static final String KEYWORDS_FILENAME="keywords.txt";
	
	private final List<KeywordDefinition> keywordDefinitions;
	
	private KeywordDefinitions() {

		keywordDefinitions=new ArrayList<KeywordDefinition>();
	}
	
	public void loadKeywordDefinitions() throws IOException {
		
		keywordDefinitions.clear();
		final InputStream stream=this.getClass().getResourceAsStream(KEYWORDS_FILENAME);
		final BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
		KeywordDefinition current=null;
		while (true) {
			
			final String line=reader.readLine();
			if (line==null) {
				break;
			}
			if (line.startsWith("*")) {
				current=new KeywordDefinition();
				current.name=line.substring(1).trim();
				keywordDefinitions.add(current);
			} else {
				if (current.description.length()>0) {
					current.description=current.description+"<br>"+line.trim();
				} else {
					current.description=line.trim();
				}
			}
		}
 		reader.close();
	}
	
	public List<KeywordDefinition> getKeywordDefinitions() {
		
		return keywordDefinitions;
	}
	
	public static KeywordDefinitions getInstance() {
		
		return INSTANCE;
	}
	
	public static class KeywordDefinition {

		public String name;
		public String description="";
	}
}