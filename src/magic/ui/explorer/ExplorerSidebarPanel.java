package magic.ui.explorer;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.ui.duel.viewer.CardViewer;
import magic.ui.duel.viewer.DeckStatisticsViewer;
import magic.ui.utility.GraphicsUtils;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ExplorerSidebarPanel extends TexturedPanel {

    private final MigLayout migLayout = new MigLayout();
    private final JScrollPane cardScrollPane = new JScrollPane();
    private final CardViewer cardViewer = new CardViewer();
    private final DeckStatisticsViewer statsViewer;

    public ExplorerSidebarPanel(final boolean isDeckEditorMode) {
        statsViewer = isDeckEditorMode ? new DeckStatisticsViewer() : null;
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setLayout(migLayout);
        setBackground(FontsAndBorders.IMENUOVERLAY_BACKGROUND_COLOR);
        // card image viewer
        cardViewer.setPreferredSize(GraphicsUtils.getMaxCardImageSize());
        cardViewer.setMaximumSize(GraphicsUtils.getMaxCardImageSize());
        // card image scroll pane
        cardScrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        cardScrollPane.setOpaque(false);
        cardScrollPane.getViewport().setOpaque(false);
        cardScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        cardScrollPane.setHorizontalScrollBarPolicy(
                GeneralConfig.getInstance().isHighQuality()
                        ? ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
                        : ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0");
        cardScrollPane.setViewportView(cardViewer);
        add(cardScrollPane);
        if (statsViewer != null) {
            add(statsViewer, "w 100%, gap 6 6 6 6, aligny bottom, pushy");
        }
    }

    public DeckStatisticsViewer getStatsViewer() {
        return statsViewer;
    }

    public void setCard(final MagicCardDefinition card) {
        cardViewer.setCard(card);
    }

    public CardViewer getCardViewer() {
        return cardViewer;
    }

}
