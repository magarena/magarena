package magic.ui.deck.widget;

import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.UiString;

public class DeckFilter {

    // translatable strings
    private static final String _S1 = "any";
    private static final String _S2 = "equals";
    private static final String _S3 = "less than";
    private static final String _S4 = "greater than";
    private static final String _S5 = "between";

    public enum NumericFilter {
        Any(UiString.get(_S1)),
        Equals(UiString.get(_S2), 1),
        LessThan(UiString.get(_S3), 1),
        GreaterThan(UiString.get(_S4), 1),
        Between(UiString.get(_S5), 2)
        ;

        private final String filterCaption;
        private final int spinnersRequired;

        private NumericFilter(String caption, int spinnersRequired) {
            this.filterCaption = caption;
            this.spinnersRequired = spinnersRequired;
        }
        private NumericFilter(final String caption) {
            this(caption, 0);
        }

        @Override
        public String toString() {
            return filterCaption;
        }

        public int getSpinnersRequired() {
            return spinnersRequired;
        }
    }

    private NumericFilter deckSizeFilter = NumericFilter.Any;
    private int deckSizeFilterValue1 = 40;
    private int deckSizeFilterValue2 = 60;

    private String deckNameFilterText = "";
    private String deckDescFilterText = "";
    private String cardNameFilterText = "";

    public DeckFilter(final DeckFilter filter) {
        if (filter != null) {
            deckSizeFilter = filter.getDeckSizeFilterType();
            deckSizeFilterValue1 = filter.getDeckSizeFilterValue1();
            deckSizeFilterValue2 = filter.getDeckSizeFilterValue2();
            deckNameFilterText = filter.getDeckNameFilterText();
            deckDescFilterText = filter.getDeckDescFilterText();
            cardNameFilterText = filter.getCardNameFilterText();
        }
    }
    public DeckFilter() {
        this(null);
    }

    public boolean isDeckValid(final MagicDeck deck) {
        if (!isDeckSizeValid(deck)) {
            return false;
        }
        if (!isDeckNameValid(deck)) {
            return false;
        }
        if (!isDeckDescValid(deck)) {
            return false;
        }
        if (!isCardInDeck(deck)) {
            return false;
        }
        return true;
    }

    private boolean isDeckDescValid(final MagicDeck deck) {
        final String searchText = deckDescFilterText.trim();
        if (!searchText.isEmpty()) {
            if (deck.getDescription() != null) {
                return deck.getDescription().toLowerCase().contains(searchText);
            } else {
                return false;
            }
        } else {
            return true ;
        }
    }

    private boolean isDeckNameValid(final MagicDeck deck) {
        final String searchText = deckNameFilterText.trim();
        if (!searchText.isEmpty()) {
            return deck.getName().toLowerCase().contains(searchText);
        } else {
            return true;
        }
    }

    private boolean isCardInDeck(final MagicDeck deck) {
        final String searchText = cardNameFilterText.trim();
        if (!searchText.isEmpty()) {
            for (MagicCardDefinition card : deck) {
                if (card.getName().toLowerCase().contains(searchText)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean isDeckSizeValid(final MagicDeck deck) {
        if (deckSizeFilter != NumericFilter.Any) {
            switch (deckSizeFilter) {
                case Equals:
                    return deck.size() == deckSizeFilterValue1;
                case GreaterThan:
                    return deck.size() > deckSizeFilterValue1;
                case LessThan:
                    return deck.size() < deckSizeFilterValue1;
                case Between:
                    return (deck.size() >= deckSizeFilterValue1) && (deck.size() <= deckSizeFilterValue2);
                default:
                    throw new RuntimeException("Unhandled deck size filter: " + deckSizeFilter);
            }
        } else {
            return true;
        }
    }

    public void setDeckSizeFilterValues(final NumericFilter filter, final int filterValue1, final int filterValue2) {
        this.deckSizeFilter = filter;
        this.deckSizeFilterValue1 = filterValue1;
        this.deckSizeFilterValue2 = filterValue2;
    }

    public NumericFilter getDeckSizeFilterType() {
        return deckSizeFilter;
    }

    public int getDeckSizeFilterValue1() {
        return deckSizeFilterValue1;
    }

    public int getDeckSizeFilterValue2() {
        return deckSizeFilterValue2;
    }

    public String getCardNameFilterText() {
        return cardNameFilterText;
    }

    public void setCardNameFilterText(String filterText) {
        this.cardNameFilterText = filterText;
    }

    public String getDeckNameFilterText() {
        return deckNameFilterText;
    }

    public void setDeckNameFilterText(String filterText) {
        this.deckNameFilterText = filterText;
    }

    public String getDeckDescFilterText() {
        return deckDescFilterText;
    }

    public void setDeckDescFilterText(String filterText) {
        this.deckDescFilterText = filterText;
    }

}
