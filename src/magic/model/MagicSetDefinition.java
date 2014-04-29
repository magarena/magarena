package magic.model;

import java.util.HashSet;

@SuppressWarnings("serial")
public class MagicSetDefinition extends HashSet<String> {

    private final String name;

    public MagicSetDefinition(final String name) {
        this.name = name;
    }

    public boolean containsCard(final MagicSetDefinition setDefinition) {
        return contains(setDefinition.toString());
    }

    @Override
    public String toString() {
        return name;
    }
}
