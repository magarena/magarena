package magic.data;

import magic.model.MagicCardDefinition;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class TokenCardDefinitions {

	private static final Map<String,MagicCardDefinition> tokensMap = new HashMap<String, MagicCardDefinition>();

	public static Collection<MagicCardDefinition> getAll() {
		return tokensMap.values();
	}
	
	public static MagicCardDefinition get(String name) {
        if (tokensMap.containsKey(name)) {
    		return tokensMap.get(name);
        } else {
            throw new RuntimeException("token " + name + " not found");
        }
	}
	
	public static void add(MagicCardDefinition token, String fullName) {		
		tokensMap.put(fullName, token);
	}
}
