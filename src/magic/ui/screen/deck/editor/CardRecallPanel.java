package magic.ui.screen.deck.editor;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTable;
import magic.model.MagicCardDefinition;
import magic.ui.MagicSound;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.cards.table.BasicDeckTablePanel;
import magic.ui.widget.cards.table.CardTablePanelA;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CardRecallPanel extends JPanel implements IDeckEditorView, FocusListener {

    // fired when card selection changes
    public static final String CP_CARD_SELECTED = "5d3dc52f-b1b3-419e-a4ab-375c3c85d29c";

    private final DeckEditorController controller = DeckEditorController.instance;

    private final CardQuantityActionPanel quantityPanel;
    private final BasicDeckTablePanel deckPanel;
    private final CardTablePanelA recallTablePanel;
    private final MigLayout miglayout = new MigLayout();

    private final List<MagicCardDefinition> recallCards;
    private MagicCardDefinition selectedCard = MagicCardDefinition.UNKNOWN;
    private final IDeckEditorListener listener;

    CardRecallPanel(final IDeckEditorListener aListener, final CardQuantityActionPanel aPanel) {

        this.listener = aListener;
        this.quantityPanel = aPanel;

        deckPanel = new BasicDeckTablePanel();

        recallCards = new ArrayList<>();
        recallTablePanel = new CardTablePanelA(recallCards, "");
        recallTablePanel.setDeckEditorSelectionMode();
        recallTablePanel.setHeaderVisible(false);

        setPropertyChangeListeners();

        setLookAndFeel();
        refreshLayout();

    }

    private MagicCardDefinition getRecallSelectedCard() {
        return recallTablePanel.getSelectedCards().size() > 0
                ? recallTablePanel.getSelectedCards().get(0)
                : null;
    }

    private void clearRecallSelection() {
        recallTablePanel.clearSelection();
    }

    private void doDeckPanelSelectionAction() {
        if (deckPanel.getSelectedCard() != null) {
            selectedCard = deckPanel.getSelectedCard();
            if (selectedCard != getRecallSelectedCard()) {
                clearRecallSelection();
            }
            listener.cardSelected(getSelectedCard());
        }
    }

    private void doRecallPanelSelectionAction() {
        if (getRecallSelectedCard() != null) {
            selectedCard = getRecallSelectedCard();
            if (controller.getDeck().contains(selectedCard) == false) {
                deckPanel.clearSelection();
            } else {
                deckPanel.setSelectedCard(selectedCard);
            }
            listener.cardSelected(getSelectedCard());
        }
    }

    private void setPropertyChangeListeners() {
        deckPanel.addPropertyChangeListener(
                BasicDeckTablePanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doDeckPanelSelectionAction();
                    }
                });
        recallTablePanel.addPropertyChangeListener(CardTablePanelA.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doRecallPanelSelectionAction();
                    }
                });
        recallTablePanel.addPropertyChangeListener(CardTablePanelA.CP_CARD_LCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        addSelectedCardToDeck();
                    }
                });
        recallTablePanel.addPropertyChangeListener(CardTablePanelA.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        removeSelectedCardFromDeck();
                    }
                });
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 0, gap 0");
        add(deckPanel, "w 220!, h 100%");
        add(recallTablePanel, "w 100%, h 100%");
        revalidate();
    }

    @Override
    public MagicCardDefinition getSelectedCard() {
        return selectedCard;
    }

    private void addSelectedCardToDeck() {

        final MagicCardDefinition card = getSelectedCard();

        if (card == null || card == MagicCardDefinition.UNKNOWN) {
            return;
        }

        for (int i = 0; i < quantityPanel.getQuantity(); i++) {
            controller.getDeck().add(card);
        }

        listener.deckUpdated(controller.getDeck());

        // Ensures the count overlay is updated on card image.
        listener.cardSelected(card);

        deckPanel.getTableModel().setCards(controller.getDeck());
        deckPanel.getTable().repaint();
        deckPanel.setSelectedCard(card);

        MagicSound.ADD_CARD.play();

    }

    private void removeSelectedCardFromDeck() {

        final MagicCardDefinition card = getSelectedCard();

        if (card == null || card == MagicCardDefinition.UNKNOWN) {
            return;
        }

        if (controller.getDeck().contains(card) == false) {
            MagicSound.BEEP.play();
            return;
        }

        for (int i = 0; i < quantityPanel.getQuantity(); i++) {
            controller.getDeck().remove(card);
        }

        listener.deckUpdated(controller.getDeck());

        // Ensures the count overlay is updated on card image.
        listener.cardSelected(card);

        deckPanel.getTableModel().setCards(controller.getDeck());
        deckPanel.getTable().repaint();

        if (card != deckPanel.getSelectedCard() && getRecallSelectedCard() != null) {
            deckPanel.clearSelection();
        } if (getRecallSelectedCard() == null) {
            doDeckPanelSelectionAction();
        }

        MagicSound.REMOVE_CARD.play();

    }

    @Override
    public boolean requestFocusInWindow() {
        if (recallTablePanel.getSelectedCards().isEmpty()) {
            recallTablePanel.selectFirstRow();
        }
        return super.requestFocusInWindow();
    }

    void addCardToRecall(final MagicCardDefinition card) {
        if (!recallCards.contains(card)) {
            recallCards.add(card);
            recallTablePanel.setCards(recallCards);
        }
    }

    void setDeckTable(final JTable aDeckTable) {
        deckPanel.setDeckTable(aDeckTable);
        deckPanel.getTable().removeFocusListener(this);
        deckPanel.getTable().addFocusListener(this);
    }

    @Override
    public void doPlusButtonAction() {
        addSelectedCardToDeck();
    }

    @Override
    public void doMinusButtonAction() {
        removeSelectedCardFromDeck();
    }

    @Override
    public List<ActionBarButton> getActionButtons() {
        return new ArrayList<>();
    }

    private void doFocusLostAction(FocusEvent e) {
        if (e.getOppositeComponent() == deckPanel.getTable()) {
            clearRecallSelection();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        // do nothing.
    }

    @Override
    public void focusLost(FocusEvent e) {
        doFocusLostAction(e);
    }

}
