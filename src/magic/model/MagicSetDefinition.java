package magic.model;

import java.util.Set;
import java.util.HashSet;

public class MagicSetDefinition {

    private final Set<String> cards = new HashSet<>();
    private final String name;

    public MagicSetDefinition(final String name) {
        this.name = name;
    }

    public void add(final String name) {
        cards.add(name);
    }

    public boolean containsCard(final MagicCardDefinition cardDefinition) {
        return cards.contains(cardDefinition.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
