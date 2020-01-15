package magic.ui.widget.deck;

import magic.model.MagicDeck;
import magic.ui.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.deck.stats.DeckStatisticsViewer;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckInfoPanel extends TexturedPanel {

    public static final String CP_LAYOUT_CHANGED = "9e33fc9e-e588-4b5f-86ad-66a2898f7d5e";

    private final DeckStatisticsViewer statsViewer;
    private final DeckDescriptionViewer descViewer;

    public DeckInfoPanel() {

        this.statsViewer = new DeckStatisticsViewer();
        this.descViewer = new DeckDescriptionViewer();

        final MigLayout mig = new MigLayout();
        setLayout(mig);
        mig.setLayoutConstraints("flowy, insets 0, gap 0");
        mig.setColumnConstraints("[fill, grow]");
        mig.setRowConstraints("[][fill, grow]");
        refreshLayout();

        statsViewer.addPropertyChangeListener(
            DeckStatisticsViewer.CP_LAYOUT_CHANGED,
            (e) -> {
                refreshLayout();
                firePropertyChange(CP_LAYOUT_CHANGED, true, false);
            }
        );

        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
    }

    private void refreshLayout() {
        removeAll();
        add(statsViewer);
        add(descViewer);
        revalidate();
    }

    public void setDeck(MagicDeck aDeck) {
        statsViewer.setDeck(aDeck);
        descViewer.setDeckDescription(aDeck);
    }

    public void setPlayedWonLost(String pwl) {
        statsViewer.setPlayedWonLost(pwl);
    }

}
