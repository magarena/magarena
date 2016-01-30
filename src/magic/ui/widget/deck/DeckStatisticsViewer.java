package magic.ui.widget.deck;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
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
import magic.translate.UiString;
import magic.ui.MagicImages;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.ActionButtonTitleBar;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckStatisticsViewer extends JPanel implements ChangeListener {

    public static final Dimension PREFERRED_SIZE = new Dimension(300, 190);

    public static final String CP_LAYOUT_CHANGED = "47395590-0c6a-4837-9dff-44a4ce19bb3c";

    // translatable strings
    private static final String _S1 = "Deck Statistics";
    private static final String _S2 = "Deck Statistics : %d cards";

    private final ActionButtonTitleBar titleBar;
    private final ManaCurvePanel manaCurvePanel;
    private final CardTypeStatsPanel cardTypesPanel;
    private final CardColorStatsPanel colorsPanel;
    private boolean isVisible = true;

    public DeckStatisticsViewer() {
        
        setOpaque(false);

        titleBar = new ActionButtonTitleBar(UiString.get(_S1), getLogActionButtons());
        cardTypesPanel = new CardTypeStatsPanel();
        manaCurvePanel = new ManaCurvePanel();
        colorsPanel = new CardColorStatsPanel();

        setLayout(new MigLayout("flowy, insets 0, gap 0"));
        refreshLayout();
    }
    
    private void refreshLayout() {
        removeAll();
        add(titleBar, "w 100%");
        add(cardTypesPanel, "alignx center, gaptop 2, hidemode 3");
        add(manaCurvePanel, "alignx center, gaptop 2, hidemode 3");
        add(colorsPanel, "w 100%, gaptop 2, hidemode 3");
        revalidate();
    }
    
    private void switchStatsVisibility() {
        isVisible = !isVisible;
        cardTypesPanel.setVisible(isVisible);
        manaCurvePanel.setVisible(isVisible);
        colorsPanel.setVisible(isVisible);        
        refreshLayout();
        firePropertyChange(CP_LAYOUT_CHANGED, true, false);
    }

    private JButton getLogViewActionButton(MagicIcon aIcon) {
        return new ActionBarButton(
            MagicImages.getIcon(aIcon),
            null, null,
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
        btns.add(getLogViewActionButton(MagicIcon.DOWNARROW_ICON));
        for (JButton btn : btns) {
            btn.setFocusable(false);
        }
        return btns;
    }

    public void setDeck(final MagicDeck aDeck) {

        final CardStatistics statistics = new CardStatistics(aDeck);

        titleBar.setText(UiString.get(_S2, statistics.totalCards));
        cardTypesPanel.setStats(statistics);
        colorsPanel.setStats(statistics);
        manaCurvePanel.setStats(statistics);

        revalidate();
        repaint();
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
