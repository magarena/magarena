package magic.data;

import magic.model.MagicCardDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenCardDefinitions {

	private static final TokenCardDefinitions INSTANCE = new TokenCardDefinitions();
	
	public static final String TOKEN_FILENAME = "tokens.txt";
	
	private final List<MagicCardDefinition> tokens;
	private final Map<String,MagicCardDefinition> tokensMap;
	
	private TokenCardDefinitions() {
		tokens = new ArrayList<MagicCardDefinition>();
		tokensMap = new HashMap<String, MagicCardDefinition>();
	}
	
	public static TokenCardDefinitions getInstance() {
		return INSTANCE;
	}
	
	public List<MagicCardDefinition> getTokenDefinitions() {
		return tokens;
	}
	
	public MagicCardDefinition getTokenDefinition(String name) {
		return tokensMap.get(name);
	}
	
	public MagicCardDefinition addTokenDefinition(MagicCardDefinition token, String fullName) {		
		token.setToken();
		token.setFullName(fullName);
		
		tokens.add(token);
		tokensMap.put(fullName, token);
		
		return token;
	}
}
