package magic.ui.deck.editor;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import magic.model.MagicCardDefinition;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.duel.viewer.DeckEditorCardViewer;
import magic.ui.duel.viewer.DeckStatisticsViewer;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckEditorSideBarPanel extends TexturedPanel {

    private final MigLayout migLayout = new MigLayout();
    private final JScrollPane cardScrollPane = new JScrollPane();
    private final DeckEditorCardViewer cardViewer = new DeckEditorCardViewer();
    private final DeckStatisticsViewer statsViewer = new DeckStatisticsViewer();

    public DeckEditorSideBarPanel() {
        setLookAndFeel();
        refreshLayout();
        setCard(MagicCardDefinition.UNKNOWN);
    }

    private void setLookAndFeel() {
        setMinimumSize(new Dimension(cardViewer.getMinimumSize().width, 0));
        setLayout(migLayout);
        cardScrollPane.setViewportView(cardViewer);
        cardScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        cardScrollPane.setOpaque(false);
        cardScrollPane.getViewport().setOpaque(false);
        cardScrollPane.getVerticalScrollBar().setUnitIncrement(10);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0");
        add(cardScrollPane);
        if (statsViewer != null) {
            add(statsViewer, "w 100%, gap 6 6 6 6, aligny bottom, pushy");
        }
    }

    public DeckStatisticsViewer getStatsViewer() {
        return statsViewer;
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
