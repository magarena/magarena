package magic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("serial")
public class MagicCondensedDeck extends ArrayList<MagicCondensedCardDefinition> {

    private String name = "Unsaved Deck";

    private final HashMap<String, MagicCondensedCardDefinition> map = new HashMap<>();

    public MagicCondensedDeck() {}

    public MagicCondensedDeck(final MagicCondensedDeck deck) {
        super(deck);
        name = deck.getName();
    }

    public MagicCondensedDeck(final MagicDeck list) {
        this((List<MagicCardDefinition>) list);

        name = list.getFilename();
    }

    public MagicCondensedDeck(final List<MagicCardDefinition> list) {
        super();

        for (final MagicCardDefinition card : list) {
            addCard(card, true);
        }
    }

    private String getKey(final MagicCardDefinition card) {
        return card.getDistinctName();
    }

    public boolean addCard(final MagicCardDefinition card, final boolean ignoreCopiesLimit) {
        if (!map.containsKey(getKey(card))) {
            // add to end
            add(new MagicCondensedCardDefinition(card));
            map.put(getKey(card), get(size() - 1));

            return true;
        } else {
            final MagicCondensedCardDefinition existingCard = map.get(getKey(card));

            if (ignoreCopiesLimit || existingCard.getNumCopies() < MagicDeckConstructionRule.MAX_COPIES) {
                existingCard.incrementNumCopies();

                return true;
            } // otherwise card can't be added because of copies limit
        }

        return false;
    }

    public void setContent(final MagicCondensedDeck deck) {
        clear();
        addAll(deck);

        name = deck.getName();

        map.clear();
        map.putAll(deck.getMap());
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getNumCards() {
        return toMagicDeck().size();
    }

    public HashMap<String, MagicCondensedCardDefinition> getMap() {
        return map;
    }

    public MagicDeck toMagicDeck() {
        final MagicDeck deck = new MagicDeck();

        for (int i = 0; i < size(); i++) {
            final MagicCondensedCardDefinition card = get(i);

            for (int j = 0; j < card.getNumCopies(); j++) {
                deck.add(card.getCard());
            }
        }

        return deck;
    }
}
