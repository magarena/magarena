package magic.data;

import java.util.Collection;
import magic.generator.RandomDeckGenerator;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;

public class DeckGenerator {

    public int deckSize = MagicDeck.DEFAULT_SIZE;
    // percentage of deck size allocated to non-land cards.
    public int spellsPercent = 60;
    // maximum percentage of spells allocated to creature cards.
    public int maxCreaturesPercent = 66;
    // maximum random colors to use in deck.
    public int maxColors = 2;

    private MagicDeckProfile deckProfile;
    private MagicDeck deck;

    public int getSpellsCount() {
        return (int)(deckSize * ((double)spellsPercent / 100));
    }

    public int getMaxCreaturesCount() {
        return (int)(getSpellsCount() * ((double)maxCreaturesPercent / 100));
    }

    public int getLandsCount() {
        return deckSize - getSpellsCount();
    }

    public void setDeckProfile(final MagicDeckProfile profile) {
        this.deckProfile = profile;
    }
    public MagicDeckProfile getDeckProfile() {
        return deckProfile;
    }

    public void setDeck(final MagicDeck deck) {
        this.deck = deck;
    }
    public MagicDeck getDeck() {
        return deck;
    }

    public MagicDeck getRandomDeck(final Collection<MagicCardDefinition> cardPool) {
        final MagicFormat cubeDefinition = MagicCustomFormat.create(cardPool);
        final RandomDeckGenerator generator = new RandomDeckGenerator(cubeDefinition);
        deck = new MagicDeck();
        deckProfile = MagicDeckProfile.getDeckProfile(getColorText());
        generator.generateDeck(this);
        addBasicLandsToDeck(deck, deckProfile, deckSize);
        return deck;
    }

    private String getColorText() {
        switch (maxColors) {
            case 1:
                return MagicDeckProfile.ANY_ONE;
            case 2:
                return MagicDeckProfile.ANY_TWO;
            case 3:
                return MagicDeckProfile.ANY_THREE;
            default:
                throw new IndexOutOfBoundsException("maxColors = " + maxColors);
        }
    }

    public static void addBasicLandsToDeck(final MagicDeck newDeck, final MagicDeckProfile deckProfile, final int deckSize) {

        final int MIN_SOURCE = 16;
        // Calculate statistics per color.
        final int[] colorCount = new int[MagicColor.NR_COLORS];
        final int[] colorSource = new int[MagicColor.NR_COLORS];
        for (final MagicCardDefinition cardDefinition : newDeck) {
            if (cardDefinition.isLand()) {
                for (final MagicColor color : MagicColor.values()) {
                    colorSource[color.ordinal()] += cardDefinition.getManaSource(color);
                }
            } else {
                final int colorFlags = cardDefinition.getColorFlags();
                for (final MagicColor color : deckProfile.getColors()) {
                    if (color.hasColor(colorFlags)) {
                        colorCount[color.ordinal()]++;
                    }
                }
            }
        }
        // Add optimal basic lands to deck.
        while (newDeck.size() < deckSize) {
            MagicColor bestColor = null;
            int lowestRatio = Integer.MAX_VALUE;
            for (final MagicColor color : MagicColor.values()) {
                final int index = color.ordinal();
                final int count = colorCount[index];
                if (count > 0) {
                    final int source = colorSource[index];
                    final int ratio;
                    if (source < MIN_SOURCE) {
                        ratio = source - count;
                    } else {
                        ratio = source * 100 / count;
                    }
                    if (ratio < lowestRatio) {
                        lowestRatio = ratio;
                        bestColor = color;
                    }
                }
            }
            // fix for issue 446 (http://code.google.com/p/magarena/issues/detail?id=446).
            if (bestColor == null) {
                bestColor = MagicColor.getColor(MagicColor.getRandomColors(1).charAt(0));
            }
            final MagicCardDefinition landCard = CardDefinitions.getBasicLand(bestColor);
            colorSource[bestColor.ordinal()] += landCard.getManaSource(bestColor);
            newDeck.add(landCard);
        }
    }
}
