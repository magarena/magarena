package magic.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            if (countedCard.getNumCopies() > 4 && !countedCard.getCard().isBasic() && !isUnlimitedCard(countedCard.getCard().getName())) {
                brokenRules.add(FourCopyLimit);
                break;
            }
        }

        return brokenRules;
    }

    private static final Set<String> UNLIMITED_CARDS = new HashSet<>(Arrays.asList(
            "Shadowborn Apostle", "Rat Colony", "Relentless Rats"
    ));

    /**
     * @return true if card does have "A deck can have any number of this card" feature
     */
    public static boolean isUnlimitedCard(String countedCard) {
        return UNLIMITED_CARDS.contains(countedCard);
    }

    /**
     * @return list of cards that can be in deck any number of times, separated by given string
     */
    public static String unlimitedCardList(String separator) {
        StringBuilder out = new StringBuilder();
        boolean first = true;
        for (String i : UNLIMITED_CARDS) {
            if (!first) {
                out.append(separator);
            }
            out.append(i);
            first = false;
        }
        return out.toString();
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
