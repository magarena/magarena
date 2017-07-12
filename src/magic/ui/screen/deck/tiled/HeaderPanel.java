package magic.ui.screen.deck.tiled;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.translate.StringContext;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class HeaderPanel extends JPanel {

    // translatable strings
    @StringContext(eg = "Creatures (28 cards, 46%)")
    private static final String _S11 = "%s (%d cards, %d%%)";
    @StringContext(eg = "All cards (60 cards)")
    private static final String _S12 = "%s (%d cards)";

    private final JLabel deckNameLabel = new JLabel();
    private final JLabel filterLabel = new JLabel();

    HeaderPanel() {
        setLookAndFeel();
    }

    private void setLookAndFeel() {

        setOpaque(false);
        setLayout(new MigLayout(
                "insets 0, gapy 2, flowy, aligny center",
                "[fill, grow]")
        );

        // deck name label
        deckNameLabel.setForeground(Color.WHITE);
        deckNameLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        deckNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // filter label
        filterLabel.setForeground(Color.WHITE);
        filterLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        filterLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void refreshLayout() {
        removeAll();
        add(deckNameLabel);
        add(filterLabel);
        revalidate();
    }

    private int getValidCardsCount(List<MagicCardDefinition> cards) {
        return (int) cards.stream().filter(card -> card.isValid()).count();
    }

    private String getFilterCaption(CardTypeFilter filter, MagicDeck deck, List<MagicCardDefinition> cards) {
        int cardCount = cards == null ? 0 : getValidCardsCount(cards);
        if (filter != CardTypeFilter.ALL) {
            final int percentage = (int)((cardCount / (double) deck.size()) * 100);
            return MText.get(_S11, filter, cardCount, percentage);
        } else {
            return MText.get(_S12, filter, cardCount);
        }
    }

    void setContent(MagicDeck deck, CardTypeFilter filter, List<MagicCardDefinition> cards) {
        deckNameLabel.setText(deck.getName());
        filterLabel.setText(getFilterCaption(filter, deck, cards));
        refreshLayout();
    }
}
