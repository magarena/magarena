package magic.ui.screen.deck.editor;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JTable;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class MainViewsPanel extends JPanel implements IDeckEditorListener {

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

    private IDeckEditorView activeView;
    private final JTable deckTable;
    private final IDeckEditorListener listener;

    MainViewsPanel(IDeckEditorListener aListener) {

        this.listener = aListener;

        deckActionPanel = new DeckActionPanel(getPlusButtonAction(), getMinusButtonAction());

        deckPanel = new DeckPanel(this, deckActionPanel.getQuantityPanel());
        cardPoolPanel = new CardPoolViewPanel(this, deckActionPanel.getQuantityPanel());
        recallPanel = new CardRecallPanel(this, deckActionPanel.getQuantityPanel());
        legalityPanel = new LegalityPanel();

        this.deckTable = deckPanel.getDeckTable();

        cardPoolPanel.setVisible(false);
        recallPanel.setVisible(false);
        legalityPanel.setVisible(false);

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
                deckPanel.setDeckTable(deckTable);
                setView(deckPanel);
            }
        });
        toggleButtonsPanel.addToggleButton(MText.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardPoolPanel.setDeckTable(deckTable);
                setView(cardPoolPanel);
            }
        });
        toggleButtonsPanel.addToggleButton(MText.get(_S3), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recallPanel.setDeckTable(deckTable);
                setView(recallPanel);
            }
        });
        if (ScreenController.isDuelActive() == false) {
            toggleButtonsPanel.addToggleButton(MText.get(_S4), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setView(legalityPanel);
                }
            });
        }

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
        revalidate();
    }

    private void setView(final IDeckEditorView aView) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        deckActionPanel.setView(aView);
        if (activeView != null) {
            activeView.setVisible(false);
        }
        aView.setVisible(true);
        deckActionPanel.setVisible(aView instanceof LegalityPanel == false);
        activeView = aView;
        refreshLayout();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
    }

    MagicCardDefinition getSelectedCard() {
        return activeView.getSelectedCard();
    }

    void doRefreshView() {
        deckPanel.doRefreshView();
        legalityPanel.setDeck(controller.getDeck());
    }

    MagicDeck getDeck() {
        return controller.getDeck();
    }

    boolean isUpdatingExistingDeck() {
        return deckPanel.isUpdatingExistingDeck();
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
        doRefreshView();
    }

}
