package magic.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.utility.DeckUtils;
import magic.utility.MagicResources;

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

    protected static List<MagicFormat> duelFormats = new ArrayList<>();

    public static List<MagicFormat> getDuelFormats() {
        return duelFormats;
    }
    
    public static MagicFormat[] getDuelFormatsArray() {
        return duelFormats.toArray(new MagicFormat[0]);
    }
    
    public static String[] getDuelLabels() {
        final List<String> values = new ArrayList<>();
        for (final MagicFormat cube : duelFormats) {
            values.add(cube.getLabel());
        }
        return values.toArray(new String[0]);
    }
}
