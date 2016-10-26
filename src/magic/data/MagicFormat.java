package magic.data;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.utility.DeckUtils;

public abstract class MagicFormat {

    private static final String _S1 = "All cards";

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
            final int cardCountCheck = card.canHaveAnyNumberInDeck() ? 1 : aDeck.getCardCount(card);
            if (!isCardLegal(card, cardCountCheck)) {
                return false;
            }
        }
        return true;
    }

    public static final MagicFormat ALL = new MagicFormat() {
        @Override
        public String getName() {
            return MText.get(_S1);
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
        return formats
            .stream()
            .map(MagicFormat::getLabel)
            .collect(Collectors.toList());
    }

    public static List<MagicFormat> getDuelFormats() {
        final List<MagicFormat> fmts = new ArrayList<>();
        fmts.add(ALL);
        fmts.addAll(MagicPredefinedFormat.values());
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
