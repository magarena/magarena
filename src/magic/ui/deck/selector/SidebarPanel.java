package magic.ui.deck.selector;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import magic.model.MagicDeck;
import magic.ui.deck.widget.DeckInfoPanel;
import magic.ui.deck.widget.DeckPicker;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.screen.interfaces.IDeckConsumer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class SidebarPanel extends TexturedPanel {

    private final DeckPicker deckPicker;
    private final DeckInfoPanel deckInfo;

    SidebarPanel(IDeckConsumer aConsumer) {

        deckPicker = new DeckPicker();
        deckPicker.addListener(aConsumer);

        deckInfo = new DeckInfoPanel();
        deckInfo.addPropertyChangeListener(
            DeckInfoPanel.CP_LAYOUT_CHANGED,
            (e) -> { refreshLayout(); }
        );

        final int BORDER_WIDTH = 1;
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, BORDER_WIDTH, Color.BLACK));
        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        setMinimumSize(new Dimension(CardViewer.getSidebarImageSize().width + BORDER_WIDTH, 0));

        MigLayout mig = new MigLayout("flowy, insets 0, gap 0");
        mig.setColumnConstraints("[fill, grow]");
        mig.setRowConstraints("[][fill, grow]");
        setLayout(mig);
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        add(deckPicker);
        add(deckInfo);
        revalidate();
    }

    void setDeck(MagicDeck deck) {
        deckInfo.setDeck(deck);
    }

}
