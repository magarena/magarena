package magic.ui.deck.editor;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.duel.viewer.DeckEditorCardViewer;
import magic.ui.widget.TexturedPanel;
import magic.ui.deck.widget.DeckInfoPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckSideBar extends TexturedPanel {

    private final MigLayout migLayout = new MigLayout();
    private final JScrollPane cardScrollPane = new JScrollPane();
    private final DeckEditorCardViewer cardViewer = new DeckEditorCardViewer();
    private final DeckInfoPanel deckInfo = new DeckInfoPanel();

    public DeckSideBar() {
        setLookAndFeel();
        refreshLayout();
        deckInfo.addPropertyChangeListener(
            DeckInfoPanel.CP_LAYOUT_CHANGED,
            (e) -> { refreshLayout(); }
        );
    }

    private void setLookAndFeel() {

        cardScrollPane.setViewportView(cardViewer);
        cardScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        cardScrollPane.setOpaque(false);
        cardScrollPane.getViewport().setOpaque(false);
        cardScrollPane.getVerticalScrollBar().setUnitIncrement(10);

        final int BORDER_WIDTH = 1;
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, BORDER_WIDTH, Color.BLACK));
        setMinimumSize(new Dimension(cardViewer.getMinimumSize().width + BORDER_WIDTH, 0));
        setLayout(migLayout);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        migLayout.setColumnConstraints("[fill, grow]");
        migLayout.setRowConstraints("[][fill, grow]");
        add(cardScrollPane);
        add(deckInfo);
        revalidate();
    }

    public void setDeck(MagicDeck aDeck) {
        deckInfo.setDeck(aDeck);
    }

    public final void setCard(final MagicCardDefinition card) {
        cardViewer.setCard(card);
    }

    public void setCardCount(final int count) {
        cardViewer.setCardCount(count);
    }

    public CardViewer getCardViewer() {
        return cardViewer;
    }

}
