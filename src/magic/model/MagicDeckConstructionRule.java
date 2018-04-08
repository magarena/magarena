package magic.model;

import java.util.ArrayList;
import java.util.List;

public enum MagicDeckConstructionRule {

    MinDeckSize("Decks must have a least 40 cards."),
    FourCopyLimit("With the exception of basic lands, a deck must have no more than 4 copies of a card.")
    ;

    public static final int MIN_DECK_SIZE = 40;
    public static final int MAX_COPIES = 4;

    private final String text;

    private MagicDeckConstructionRule(final String text) {
        this.text = text;
    }

    private String getRuleText() {
        return text;
    }

    public static List<MagicDeckConstructionRule> checkDeck(final MagicDeck deck) {
        final ArrayList<MagicDeckConstructionRule> brokenRules = new ArrayList<>();

        if (deck.size() < MIN_DECK_SIZE) {
            brokenRules.add(MinDeckSize);
        }

        final MagicCondensedDeck countedDeck = new MagicCondensedDeck(deck);
        for (final MagicCondensedCardDefinition countedCard : countedDeck) {
            if (countedCard.getNumCopies() > 4 && !countedCard.getCard().isBasic() && !"Shadowborn Apostle".equals(countedCard.getCard().getName()) && !"Relentless Rats".equals(countedCard.getCard().getName())) {
                brokenRules.add(FourCopyLimit);
                break;
            }
        }

        return brokenRules;
    }

    public static String getRulesText(final List<MagicDeckConstructionRule> rules) {
        final StringBuilder sb = new StringBuilder();

        for (final MagicDeckConstructionRule rule : rules) {
            sb.append(rule.getRuleText());
            sb.append("\n");
        }

        return sb.toString();
    }
}
