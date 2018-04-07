package magic.ui.deck.games;

import java.awt.Dimension;
import javax.swing.JLabel;
import magic.data.CardStatistics;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicManaType;
import magic.ui.MagicImages;
import net.miginfocom.swing.MigLayout;

/**
 * Displays a string of {@link MagicColor} char symbols as one or more 16 x 16 mana icons.
 * <p>
 * Normally used to display the color of a deck.
 */
@SuppressWarnings("serial")
class DeckColorLabel extends JLabel {

    private static final int ICON_GAPX = 1;

    /**
     * Returns a string of characters as defined in {@link MagicColor} where each
     * character represents a color associated with the given deck.
     */
    public static String getDeckColorSymbols(final MagicDeck deck) {
        final CardStatistics statistics = new CardStatistics(deck);
        StringBuilder deckColor = new StringBuilder();
        for (int i = 0; i < statistics.colorCount.length; i++) {
            if (statistics.colorCount[i] > 0) {
                final MagicColor color = MagicColor.values()[i];
                deckColor.append(color.getSymbol());
            }
        }
        return deckColor.toString();
    }

    // CTR
    DeckColorLabel(final String deckColorSymbols) {

        final int colorCount = deckColorSymbols.length();

        if (colorCount > 0) {
            setLayout(new MigLayout("insets 0, gapx " + ICON_GAPX));
            for (int i = 0; i < colorCount; i++) {
                final MagicManaType manaType = MagicColor.getColor(deckColorSymbols.charAt(i)).getManaType();
                final JLabel iconLabel = new JLabel(MagicImages.getIcon(manaType, true));
                add(iconLabel, "w 16!, h 16!");
            }
        }

        final int preferredWidth = (colorCount * 16) + (colorCount * ICON_GAPX);
        setPreferredSize(new Dimension(preferredWidth, 16));
    }

}
