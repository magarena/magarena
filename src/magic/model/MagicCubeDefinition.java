package magic.model;

import java.util.HashSet;

@SuppressWarnings("serial")
public class MagicCubeDefinition extends HashSet<String> {

    private final String name;

    public MagicCubeDefinition(final String name) {
        this.name=name;
    }

    public boolean containsCard(final MagicCardDefinition cardDefinition) {
        return contains(cardDefinition.getName());
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return name + " (" + size() + " cards)";
    }

    @Override
    public String toString() {
        return getLabel();
    }
    
}
