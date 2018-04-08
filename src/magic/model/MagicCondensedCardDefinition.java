package magic.model;

import java.util.Comparator;

// basically a wrapper for MagicCardDefinition that adds number of copies of card
public class MagicCondensedCardDefinition {
    private int copies;
    private final MagicCardDefinition card;

    public MagicCondensedCardDefinition(final MagicCardDefinition card) {
        this.card = card;
        copies = 1;
    }

    public MagicCardDefinition getCard() {
        return card;
    }

    public void incrementNumCopies() {
        copies++;
    }

    public void decrementNumCopies() {
        if (copies > 0) {
            copies--;
        }
    }

    public void setNumCopies(final int i) {
        copies = i;
    }

    public int getNumCopies() {
        return copies;
    }

    public static final Comparator<MagicCondensedCardDefinition> RATING_COMPARATOR_DESC =
            Comparator.comparingDouble(cardDefinition -> cardDefinition.getCard().getValue());

    public static final Comparator<MagicCondensedCardDefinition> RATING_COMPARATOR_ASC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCondensedCardDefinition.RATING_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);

    public static final Comparator<MagicCondensedCardDefinition> NUM_COPIES_COMPARATOR_DESC =
            Comparator.comparingInt(MagicCondensedCardDefinition::getNumCopies);

    public static final Comparator<MagicCondensedCardDefinition> NUM_COPIES_COMPARATOR_ASC=
            (cardDefinition1, cardDefinition2) ->
                    MagicCondensedCardDefinition.NUM_COPIES_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);

    public static final Comparator<MagicCondensedCardDefinition> NAME_COMPARATOR_DESC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.NAME_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> NAME_COMPARATOR_ASC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.NAME_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> CONVERTED_COMPARATOR_DESC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.CONVERTED_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> CONVERTED_COMPARATOR_ASC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.CONVERTED_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> SUBTYPE_COMPARATOR_ASC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.SUBTYPE_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> SUBTYPE_COMPARATOR_DESC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.SUBTYPE_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> TYPE_COMPARATOR_DESC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.TYPE_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> TYPE_COMPARATOR_ASC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.TYPE_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> RARITY_COMPARATOR_DESC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.RARITY_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> RARITY_COMPARATOR_ASC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.RARITY_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> POWER_COMPARATOR_DESC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.POWER_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> POWER_COMPARATOR_ASC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.POWER_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> TOUGHNESS_COMPARATOR_DESC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.TOUGHNESS_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());

    public static final Comparator<MagicCondensedCardDefinition> TOUGHNESS_COMPARATOR_ASC =
            (cardDefinition1, cardDefinition2) ->
                    MagicCardDefinition.TOUGHNESS_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
}
