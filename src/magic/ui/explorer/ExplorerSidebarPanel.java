package magic.ui.explorer;

import magic.model.MagicCardDefinition;
import magic.ui.duel.viewer.DeckStatisticsViewer;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ExplorerSidebarPanel extends TexturedPanel {

    private final MigLayout migLayout = new MigLayout();
    private final DeckStatisticsViewer statsViewer;
    private final CardPanel cardPanel = new CardPanel();

    public ExplorerSidebarPanel(final boolean isDeckEditorMode) {
        statsViewer = isDeckEditorMode ? new DeckStatisticsViewer() : null;
        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setLayout(migLayout);
        setOpaque(true);
//        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG); // .IMENUOVERLAY_BACKGROUND_COLOR);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0");
        add(cardPanel, "w 100%, h 100%");
        if (statsViewer != null) {
            add(statsViewer, "w 100%, gap 6 6 6 6, aligny bottom, pushy");
        }
        revalidate();
    }

    public DeckStatisticsViewer getStatsViewer() {
        return statsViewer;
    }

    public void setCard(final MagicCardDefinition card) {
        cardPanel.setCard(card);
    }

}
