package magic.ui.screen.deck.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.deck.editor.stats.DeckStatsPanel;
import magic.ui.widget.cards.table.CardsJTable;
import magic.ui.widget.deck.stats.IPwlWorkerListener;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class MainViewsPanel extends JPanel
    implements IDeckEditorListener, IPwlWorkerListener  {

    // translatable strings
    private static final String _S1 = "Deck";
    private static final String _S2 = "Card Pool";
    private static final String _S3 = "Card Recall";
    private static final String _S4 = "Legality";

    public static final int DECK_ACTION_PANEL_WIDTH = 40;

    private final DeckEditorController controller = DeckEditorController.instance;

    private final MigLayout miglayout = new MigLayout();

    private final ToggleButtonsPanel toggleButtonsPanel = new ToggleButtonsPanel();
    private final DeckActionPanel deckActionPanel;

    private final DeckPanel deckPanel;
    private final CardPoolViewPanel cardPoolPanel;
    private final CardRecallPanel recallPanel;
    private final LegalityPanel legalityPanel;
    private final DeckStatsPanel statsPanel;

    private IDeckEditorView activeView;
    private final CardsJTable deckTable;
    private final IDeckEditorListener listener;
    private JToggleButton statsToggleButton;

    MainViewsPanel(IDeckEditorListener aListener) {

        this.listener = aListener;

        deckActionPanel = new DeckActionPanel(getPlusButtonAction(), getMinusButtonAction());

        deckPanel = new DeckPanel(this, deckActionPanel.getQuantityPanel());
        cardPoolPanel = new CardPoolViewPanel(this, deckActionPanel.getQuantityPanel());
        recallPanel = new CardRecallPanel(this, deckActionPanel.getQuantityPanel());
        legalityPanel = new LegalityPanel();
        statsPanel = new DeckStatsPanel(controller.getDeck());

        this.deckTable = deckPanel.getDeckTable();

        cardPoolPanel.setVisible(false);
        recallPanel.setVisible(false);
        legalityPanel.setVisible(false);
        statsPanel.setVisible(false);

        setLookAndFeel();
        refreshLayout();
        setView(deckPanel);

        addPropertyChangeListeners();
        addToggleButtons();

    }

    private AbstractAction getPlusButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activeView.doPlusButtonAction();
            }
        };
    }

    private AbstractAction getMinusButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activeView.doMinusButtonAction();
            }
        };
    }

    private void addToggleButtons() {

        toggleButtonsPanel.addToggleButton(MText.get(_S1), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor((Component) e.getSource());
                deckPanel.setDeckTable(deckTable);
                setView(deckPanel);
                MouseHelper.showHandCursor((Component) e.getSource());
            }
        });
        toggleButtonsPanel.addToggleButton(MText.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor((Component) e.getSource());
                cardPoolPanel.setDeckTable(deckTable);
                setView(cardPoolPanel);
                MouseHelper.showHandCursor((Component) e.getSource());
            }
        });
        toggleButtonsPanel.addToggleButton(MText.get(_S3), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MouseHelper.showBusyCursor((Component) e.getSource());
                recallPanel.setDeckTable(deckTable);
                setView(recallPanel);
                MouseHelper.showHandCursor((Component) e.getSource());
            }
        });
        if (ScreenController.isDuelActive() == false) {
            toggleButtonsPanel.addToggleButton(MText.get(_S4), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MouseHelper.showBusyCursor((Component) e.getSource());
                    setView(legalityPanel);
                    MouseHelper.showHandCursor((Component) e.getSource());
                }
            });
        }

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

    private void addPropertyChangeListeners() {

        // DeckPanel
        deckPanel.addPropertyChangeListener(
            DeckPanel.CP_REMOVE_FROM_DECK,
            evt -> doRemoveCardFromDeck(deckPanel.getSelectedCard())
        );
        deckPanel.addPropertyChangeListener(
            DeckPanel.CP_ADD_TO_DECK,
            evt -> doAddCardToDeck(deckPanel.getSelectedCard())
        );
        deckPanel.addPropertyChangeListener(
            DeckPanel.CP_CARD_SELECTED,
            evt -> listener.cardSelected(getSelectedCard())
        );

        // CardRecallPanel
        recallPanel.addPropertyChangeListener(
            CardRecallPanel.CP_CARD_SELECTED,
            evt -> listener.cardSelected(getSelectedCard())
        );

        // LegalityPanel
        legalityPanel.addPropertyChangeListener(
            LegalityPanel.CP_CARD_SELECTED,
            evt -> deckPanel.setSelectedCard(legalityPanel.getSelectedCard())
        );
        legalityPanel.addPropertyChangeListener(
            LegalityPanel.CP_CARD_DCLICKED,
            evt -> deckPanel.setSelectedCard(legalityPanel.getSelectedCard())
        );
    }

    private void doAddCardToDeck(final MagicCardDefinition card) {
        if (card != null && card != MagicCardDefinition.UNKNOWN) {
            deckPanel.addCardToDeck(card);
            recallPanel.addCardToRecall(card);
            listener.cardSelected(getSelectedCard());
            MagicSound.ADD_CARD.play();
        }
    }

    private void doRemoveCardFromDeck(final MagicCardDefinition card) {
        if (card != null && card != MagicCardDefinition.UNKNOWN) {
            deckPanel.removeCardFromDeck(card);
            recallPanel.addCardToRecall(card);
            listener.cardSelected(getSelectedCard());
            MagicSound.REMOVE_CARD.play();
        }
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 0, gap 0, flowx, wrap 2");
        add(toggleButtonsPanel, "w 100%, h 34!, spanx 2");
        add(deckActionPanel, "w " + DECK_ACTION_PANEL_WIDTH + "!, h 100%, hidemode 3");
        add(deckPanel, "w 100%, h 100%, hidemode 3");
        add(cardPoolPanel, "w 100%, h 100%, hidemode 3");
        add(recallPanel, "w 100%, h 100%, hidemode 3");
        add(legalityPanel, "w 100%, h 100%, hidemode 3");
        add(statsPanel, "w 100%, h 100%, hidemode 3");
        revalidate();
    }

    private void setView(IDeckEditorView aView) {
        aView.notifyShowing();
        deckActionPanel.setView(aView);
        if (activeView != null) {
            activeView.setVisible(false);
        }
        aView.setVisible(true);
        deckActionPanel.setVisible(
            aView instanceof LegalityPanel == false
            && aView instanceof DeckStatsPanel == false
        );
        activeView = aView;
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
    }

    MagicCardDefinition getSelectedCard() {
        return activeView.getSelectedCard();
    }

    void doRefreshViews() {
        deckPanel.doRefreshView();
        legalityPanel.setDeck(controller.getDeck());
        statsPanel.setDeck(controller.getDeck());
        activeView.notifyShowing();
    }

    @Override
    public void deckUpdated(MagicDeck deck) {
        legalityPanel.setDeck(deck);
        listener.deckUpdated(deck);
    }

    @Override
    public void cardSelected(MagicCardDefinition card) {
        listener.cardSelected(card);
    }

    @Override
    public void addCardToRecall(MagicCardDefinition card) {
        recallPanel.addCardToRecall(card);
    }

    @Override
    public void setDeck(MagicDeck deck) {
        doRefreshViews();
    }

    @Override
    public void setPlayedWonLost(String pwl) {
        statsToggleButton.setText(pwl);
    }

}
