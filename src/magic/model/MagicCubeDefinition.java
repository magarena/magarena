package magic.model;

import java.util.Set;
import java.util.HashSet;

import magic.data.MagicFormat;
import magic.data.CardLegality;

public class MagicCubeDefinition extends MagicFormat {

    private final Set<String> legal = new HashSet<>();
    private final String name;

    public MagicCubeDefinition(final String name) {
        this.name=name;
    }

    public void add(final String name) {
        legal.add(name);
    }

    @Override
    public CardLegality getCardLegality(MagicCardDefinition card, int cardCount) {
        return (legal.contains(card.getName())) ? CardLegality.Legal : CardLegality.Illegal;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getMinimumDeckSize() {
        return 40;
    }

    public String getLabel() {
        return name + " (" + legal.size() + " cards)";
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
