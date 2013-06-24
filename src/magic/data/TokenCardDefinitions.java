package magic.data;

import magic.model.MagicCardDefinition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TokenCardDefinitions {

    private static final Map<String,MagicCardDefinition> tokensMap = new HashMap<String, MagicCardDefinition>();

    public static Collection<MagicCardDefinition> getAll() {
        return tokensMap.values();
    }

    public static MagicCardDefinition get(final String name) {
        if (tokensMap.containsKey(name)) {
            return tokensMap.get(name);
        } else {
            throw new RuntimeException("token " + name + " not found");
        }
    }

    public static void add(final MagicCardDefinition token) {
        tokensMap.put(token.getFullName(), token);
    }
}
