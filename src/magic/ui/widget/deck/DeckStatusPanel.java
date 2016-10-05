package magic.ui.widget.deck;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.model.MagicDeck;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckStatusPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "%d cards";

    private final MigLayout migLayout = new MigLayout();
    private final JLabel deckNameLabel = new JLabel();
    private final JLabel deckSizeLabel = new JLabel();

    public DeckStatusPanel(final MagicDeck deck) {
        setLookAndFeel();
        setDeck(deck, false);
    }
    public DeckStatusPanel() {
        setLookAndFeel();
    }

    public void setDeck(final MagicDeck deck, final boolean showDeckSize) {
        deckNameLabel.setText(deck != null ? deck.getName() : "");
        deckSizeLabel.setText(showDeckSize && deck != null ? UiString.get(_S1, deck.size()): "");
        refreshLayout();
        revalidate();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(migLayout);
        // deck name label
        deckNameLabel.setForeground(Color.WHITE);
        deckNameLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        deckNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // deck size label
        deckSizeLabel.setForeground(Color.WHITE);
        deckSizeLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        deckSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 2, flowy");
        add(deckNameLabel, "w 100%");
        add(deckSizeLabel, "w 100%");
    }

}
