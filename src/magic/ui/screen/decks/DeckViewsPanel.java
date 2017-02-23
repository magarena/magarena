package magic.ui.screen.decks;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.helpers.MouseHelper;
import magic.ui.deck.games.DeckStatsPanel;
import magic.ui.widget.deck.legality.LegalityPanel;
import magic.ui.widget.deck.stats.IPwlWorkerListener;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckViewsPanel extends JPanel implements IPwlWorkerListener {

    // translatable strings
    private static final String _S1 = "Deck";
    private static final String _S4 = "Legality";

    public static final int DECK_ACTION_PANEL_WIDTH = 40;

    private final MigLayout miglayout = new MigLayout();

    private final ToggleButtonsPanel toggleButtonsPanel = new ToggleButtonsPanel();
    private final DeckPanel deckPanel;
    private final LegalityPanel legalityPanel;
    private final DeckStatsPanel statsPanel;
    private JToggleButton statsToggleButton;

    private IDeckView activeView;
    private MagicDeck deck = new MagicDeck();

    public DeckViewsPanel() {

        deckPanel = new DeckPanel();
        legalityPanel = new LegalityPanel();
        statsPanel = new DeckStatsPanel(new MagicDeck());

        legalityPanel.setVisible(false);
        statsPanel.setVisible(false);

        activeView = deckPanel;

        setLookAndFeel();
        refreshLayout();

        addToggleButtons();
    }

    private void addToggleButtons() {

        toggleButtonsPanel.addToggleButton(MText.get(_S1), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor((Component) e.getSource());
                setView(deckPanel);
                MouseHelper.showHandCursor((Component) e.getSource());
            }
        });

        toggleButtonsPanel.addToggleButton(MText.get(_S4), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor((Component) e.getSource());
                setView(legalityPanel);
                MouseHelper.showHandCursor((Component) e.getSource());
            }
        });

        statsToggleButton = toggleButtonsPanel.addToggleButton("", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor((Component) e.getSource());
                setView(statsPanel);
                MouseHelper.showHandCursor((Component) e.getSource());
            }
        });
        statsToggleButton.setVisible(GeneralConfig.getInstance().isGameStatsEnabled());

        toggleButtonsPanel.setSelectedToggleButton(MText.get(_S1));
        toggleButtonsPanel.refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 0, gap 0, flowy");
        add(toggleButtonsPanel, "w 100%, h 34!");
        add(deckPanel, "w 100%, h 100%, hidemode 3");
        add(legalityPanel, "w 100%, h 100%, hidemode 3");
        add(statsPanel, "w 100%, h 100%, hidemode 3");
        revalidate();
    }

    private void setView(IDeckView aView) {
        aView.notifyShowing();
        if (activeView != null) {
            activeView.setVisible(false);
        }
        aView.setVisible(true);
        activeView = aView;
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
    }

    private void doRefreshViews(MagicCardDefinition selectCard) {
        deckPanel.setDeck(deck, selectCard);
        legalityPanel.setDeck(deck);
        statsPanel.setDeck(deck);
        setView(activeView);
    }

    public void setDeck(MagicDeck deck, MagicCardDefinition selectCard) {
        this.deck = deck;
        doRefreshViews(selectCard);
    }

    public void setDeck(MagicDeck deck) {
        setDeck(deck, null);
    }

    @Override
    public void setPlayedWonLost(String pwl) {
        statsToggleButton.setText(pwl);
    }

    public void setCardsTableListeners(ICardsTableListener... listeners) {
        deckPanel.setCardsTableListeners(listeners);
        legalityPanel.setCardsTableListeners(listeners);
    }

    public void setSelectedCard(MagicCardDefinition card) {
        deckPanel.setSelectedCard(card);
    }

    public MagicCardDefinition getSelectedCard() {
        return deckPanel.getSelectedCard();
    }
}
