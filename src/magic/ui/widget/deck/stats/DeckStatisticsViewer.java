package magic.ui.widget.deck.stats;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.data.CardStatistics;
import magic.data.MagicIcon;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.ActionButtonTitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckStatisticsViewer extends JPanel implements ChangeListener {

    public static final String CP_LAYOUT_CHANGED = "47395590-0c6a-4837-9dff-44a4ce19bb3c";

    // translatable strings
    private static final String _S1 = "Deck Statistics";
    private static final String _S2 = "%d card deck";

    public static final ImageIcon ALERT_ICON = MagicImages.getIcon(MagicIcon.ALERT);
    private static boolean isStatsVisible = true;

    private final ActionButtonTitleBar titleBar;
    private final ManaCurvePanel manaCurvePanel;
    private final ActionBarButton titlebarButton;
    private final StatsTable statsTable;
    private String pwl = "";
    private CardStatistics statistics;
    private MagicDeck thisDeck;

    public DeckStatisticsViewer() {

        setOpaque(false);

        titlebarButton = getLogViewActionButton();
        titleBar = new ActionButtonTitleBar(MText.get(_S1), getLogActionButtons());

        statsTable = new StatsTable();
        manaCurvePanel = new ManaCurvePanel();
        setStatsVisible(isStatsVisible);

        setDeck(new MagicDeck());

        setLayout(new MigLayout(
                "flowy, insets 0, gap 0",
                "[center]"
        ));
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        add(titleBar, "w 100%");
        add(statsTable, "hidemode 3");
        add(manaCurvePanel, "gaptop 2,  gapbottom 4, hidemode 3");
        revalidate();
    }

    private void setStatsVisible(boolean b) {
        statsTable.setVisible(b);
        manaCurvePanel.setVisible(b);
        titlebarButton.setIcon(b
            ? MagicImages.getIcon(MagicIcon.ARROW_DOWN)
            : MagicImages.getIcon(MagicIcon.ARROW_UP)
        );
    }

    private void switchStatsVisibility() {
        isStatsVisible = !isStatsVisible;
        setStatsVisible(isStatsVisible);
        refreshLayout();
        firePropertyChange(CP_LAYOUT_CHANGED, true, false);
    }

    private ActionBarButton getLogViewActionButton() {
        return new ActionBarButton(
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switchStatsVisibility();
                }
            }
        );
    }

    private List<JButton> getLogActionButtons() {
        final List<JButton> btns = new ArrayList<>();
        btns.add(titlebarButton);
        for (JButton btn : btns) {
            btn.setFocusable(false);
        }
        return btns;
    }

    private boolean isNewDeck(MagicDeck aDeck) {
        return thisDeck == null
            || !thisDeck.getName().equals(aDeck.getName())
            || thisDeck.getDeckFileChecksum() != aDeck.getDeckFileChecksum()
            || thisDeck.getDeckType() != aDeck.getDeckType();
    }

    private void setInvalidDeckIcon(MagicDeck deck) {
        if (deck.isNotValid() && deck.isNotEmpty()) {
            titleBar.setIcon(ALERT_ICON);
            titleBar.setToolTipText(
                String.format("<html><b>Invalid deck</b><br>%s</html>",
                    deck.getDescription().replace("\n", "<br>")
                )
            );
        } else {
            titleBar.setIcon(null);
            titleBar.setToolTipText(null);
        }
    }

    public void setDeck(MagicDeck aDeck) {
        setInvalidDeckIcon(aDeck);
        statistics = new CardStatistics(aDeck == null ? new MagicDeck() : aDeck);
        statsTable.setStats(statistics);
        manaCurvePanel.setStats(statistics);
        if (isNewDeck(aDeck)) {
            this.thisDeck = aDeck;
        }
        setPlayedWonLost(pwl);
    }

    public void setPlayedWonLost(String newPWL) {
        pwl = newPWL;
        titleBar.setText(MText.get(_S2, statistics.totalCards)
            + (!newPWL.isEmpty() ? "   •   " + newPWL : "")
        );
    }

    @Override
    public void stateChanged(final ChangeEvent event) {
        setDeck(((DuelPlayerConfig)event.getSource()).getDeck());
    }

    static JLabel getCaptionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FontsAndBorders.FONT0);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }
}
