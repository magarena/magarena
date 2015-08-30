package magic.data;

import java.util.List;
import java.util.ArrayList;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.utility.DeckUtils;

public abstract class MagicFormat {
    
    public abstract String getName();
    
    public abstract CardLegality getCardLegality(MagicCardDefinition card, int cardCount);
    
    public abstract int getMinimumDeckSize();
   
    public boolean isCardLegal(MagicCardDefinition card, int cardCount) {
        return getCardLegality(card, cardCount) == CardLegality.Legal;
    }
    
    public boolean isCardLegal(MagicCardDefinition card) {
        return isCardLegal(card, 1);
    }
    
    public String getLabel() {
        return getName();
    }
    
    public boolean isDeckLegal(final MagicDeck aDeck) {
        if (aDeck.size() < getMinimumDeckSize()) {
            return false;
        }
        for (final MagicCardDefinition card : DeckUtils.getDistinctCards(aDeck)) {
            final int cardCountCheck = card.isLand() ? 1 : aDeck.getCardCount(card);
            if (isCardLegal(card, cardCountCheck) == false) {
                return false;
            }
        }
        return true;
    }

    //
    // static members
    //

    public static final MagicFormat ALL = new MagicFormat() {
        @Override
        public String getName() {
            return "All";
        }
        
        @Override
        public CardLegality getCardLegality(MagicCardDefinition card, int cardCount) {
            return CardLegality.Legal;
        }
        
        @Override
        public int getMinimumDeckSize() {
            return 40;
        }
    };

    private static List<String> getFormatLabels(final List<MagicFormat> formats) {
        final List<String> fmts = new ArrayList<>();
        for (final MagicFormat fmt : formats) {
            fmts.add(fmt.getLabel());
        }
        return fmts;
    }

    public static List<MagicFormat> getDuelFormats() {
        final List<MagicFormat> fmts = new ArrayList<>();
        fmts.add(MagicFormat.ALL);
        fmts.add(MagicPredefinedFormat.STANDARD);
        fmts.add(MagicPredefinedFormat.MODERN);
        fmts.add(MagicPredefinedFormat.LEGACY);
        fmts.addAll(MagicCustomFormat.values());
        return fmts;
    }

    public static List<MagicFormat> getCubeFilterFormats() {
        return MagicCustomFormat.values();
    }
    
    public static MagicFormat[] getDuelFormatsArray() {
        return getDuelFormats().toArray(new MagicFormat[0]);
    }
    
    public static String[] getDuelLabels() {
        return getFormatLabels(getDuelFormats()).toArray(new String[0]);
    }

    public static String[] getCubeFilterLabels() {
        return getFormatLabels(getCubeFilterFormats()).toArray(new String[0]);
    }
}
