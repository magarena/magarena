package magic.data;

import magic.model.MagicCardDefinition;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

public class TokenCardDefinitions {

    private static final ConcurrentMap<String,MagicCardDefinition> tokensMap = new ConcurrentHashMap<>();

    public static Collection<MagicCardDefinition> getAll() {
        return tokensMap.values();
    }

    public static MagicCardDefinition get(final String name) {
        final String key = name.toLowerCase();
        // lazy loading of token scripts
        if (tokensMap.containsKey(key) == false) {
            CardDefinitions.loadCardDefinition(name);
        }
        if (tokensMap.containsKey(key)) {
            return tokensMap.get(key);
        } else {
            throw new RuntimeException("unknown token: \"" + name + "\"");
        }
    }

    public static void add(final MagicCardDefinition token) {
        tokensMap.putIfAbsent(token.getFullName().toLowerCase(), token);
    }
}
