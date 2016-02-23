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

    public static final Comparator<MagicCondensedCardDefinition> RATING_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return Double.compare(cardDefinition1.getCard().getValue(), cardDefinition2.getCard().getValue());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> RATING_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCondensedCardDefinition.RATING_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> NUM_COPIES_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return cardDefinition1.getNumCopies() - cardDefinition2.getNumCopies();
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> NUM_COPIES_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCondensedCardDefinition.NUM_COPIES_COMPARATOR_DESC.compare(cardDefinition2, cardDefinition1);
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> NAME_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.NAME_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> NAME_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.NAME_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> CONVERTED_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.CONVERTED_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> CONVERTED_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.CONVERTED_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };
    public static final Comparator<MagicCondensedCardDefinition> SUBTYPE_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.SUBTYPE_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };
    public static final Comparator<MagicCondensedCardDefinition> SUBTYPE_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.SUBTYPE_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };
    public static final Comparator<MagicCondensedCardDefinition> TYPE_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.TYPE_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };


    public static final Comparator<MagicCondensedCardDefinition> TYPE_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.TYPE_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> RARITY_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.RARITY_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> RARITY_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.RARITY_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> POWER_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.POWER_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> POWER_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.POWER_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> TOUGHNESS_COMPARATOR_DESC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.TOUGHNESS_COMPARATOR_DESC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };

    public static final Comparator<MagicCondensedCardDefinition> TOUGHNESS_COMPARATOR_ASC=new Comparator<MagicCondensedCardDefinition>() {
        @Override
        public int compare(final MagicCondensedCardDefinition cardDefinition1,final MagicCondensedCardDefinition cardDefinition2) {
            return MagicCardDefinition.TOUGHNESS_COMPARATOR_ASC.compare(cardDefinition1.getCard(), cardDefinition2.getCard());
        }
    };
}
