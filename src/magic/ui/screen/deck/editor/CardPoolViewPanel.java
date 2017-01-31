package magic.ui.screen.deck.editor;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JTable;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.MagicImages;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.dialog.RandomDeckGeneratorDialog;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.cards.table.BasicDeckTablePanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class CardPoolViewPanel extends JPanel implements IDeckEditorView, FocusListener {

    // translatable strings
    private static final String _S1 = "Random Deck";
    private static final String _S2 = "Generate a random deck using current set of cards in card pool.";

    private final DeckEditorController controller = DeckEditorController.instance;

    private final FilteredCardPoolPanel cardPoolPanel;
    private final CardQuantityActionPanel quantityPanel;
    private final BasicDeckTablePanel deckPanel;
    private final MigLayout miglayout = new MigLayout();

    private MagicCardDefinition selectedCard = MagicCardDefinition.UNKNOWN;
    private final IDeckEditorListener listener;
    private final List<ActionBarButton> actionButtons = new ArrayList<>();

    CardPoolViewPanel(final IDeckEditorListener aListener, final CardQuantityActionPanel aActionBar) {

        this.listener = aListener;
        this.quantityPanel = aActionBar;

        deckPanel = new BasicDeckTablePanel();
        cardPoolPanel = new FilteredCardPoolPanel();

        actionButtons.add(getRandomDeckActionButton());

        setLookAndFeel();
        refreshLayout();

        setListeners();

        cardPoolPanel.getTable().addFocusListener(this);
    }

    private void setListeners() {

        deckPanel.addPropertyChangeListener(
                BasicDeckTablePanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doDeckPanelSelectionAction();
                    }
                });

        cardPoolPanel.addPropertyChangeListener(
                FilteredCardPoolPanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doCardPoolPanelSelectionAction();
                    }
                });
        cardPoolPanel.addPropertyChangeListener(
                FilteredCardPoolPanel.CP_CARD_LCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        addSelectedCardToDeck();
                    }
                });
        cardPoolPanel.addPropertyChangeListener(
                FilteredCardPoolPanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        removeSelectedCardFromDeck();
                    }
                });

    }

    private void doCardPoolPanelSelectionAction() {
        if (cardPoolPanel.getSelectedCard() != null) {
            selectedCard = cardPoolPanel.getSelectedCard();
            if (controller.getDeck().contains(selectedCard) == false) {
                deckPanel.clearSelection();
            } else {
                deckPanel.setSelectedCard(selectedCard);
            }
            listener.cardSelected(getSelectedCard());
        }
    }

    private void doDeckPanelSelectionAction() {
        if (deckPanel.getSelectedCard() != null) {
            selectedCard = deckPanel.getSelectedCard();
            if (selectedCard != cardPoolPanel.getSelectedCard()) {
                cardPoolPanel.clearSelection();
            }
            listener.cardSelected(getSelectedCard());
        }
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
        listener.addCardToRecall(card);

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
        listener.addCardToRecall(card);

        deckPanel.getTableModel().setCards(controller.getDeck());
        deckPanel.getTable().repaint();

        if (card != deckPanel.getSelectedCard() && cardPoolPanel.getSelectedCard() != null) {
            deckPanel.clearSelection();
        } if (cardPoolPanel.getSelectedCard() == null) {
            doDeckPanelSelectionAction();
        }

        MagicSound.REMOVE_CARD.play();

    }

    private ActionBarButton getRandomDeckActionButton() {
        return new ActionBarButton(
                MagicImages.getIcon(MagicIcon.RANDOM),
                MText.get(_S1),
                MText.get(_S2),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        generateRandomDeck();
                    }
                }, true);
    }

    private int getDefaultDeckSize() {
        return controller.getDeck().size() < MagicDeck.DEFAULT_SIZE
                ? ScreenController.isDuelActive() == false
                        ? 60
                        : MagicDeck.DEFAULT_SIZE
                : controller.getDeck().size();
    }

    private boolean generateRandomDeck() {
        final RandomDeckGeneratorDialog dialog = new RandomDeckGeneratorDialog(
                getCardPoolSize(),
                getDefaultDeckSize()
        );
        dialog.setVisible(true);
        if (!dialog.isCancelled()) {
            controller.setDeck(dialog.getDeckGenerator().getRandomDeck(getCardPool()));
            return true;
        } else {
            return false;
        }
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 0, gap 0, wrap 2, flowy");
        add(deckPanel, "w 220!, h 100%, spany 2");
        add(cardPoolPanel, "w 100%, h 100%");
        revalidate();
    }

    private int getCardPoolSize() {
        return cardPoolPanel.getCardPool().size();
    }


    @Override
    public MagicCardDefinition getSelectedCard() {
        return selectedCard;
    }

    @Override
    public boolean requestFocusInWindow() {
        return cardPoolPanel.requestFocusInWindow();
    }

    Collection<MagicCardDefinition> getCardPool() {
        return cardPoolPanel.getCardPool();
    }

    void setDeckTable(final JTable aDeckTable) {
        deckPanel.setDeckTable(aDeckTable);
        deckPanel.getTable().removeFocusListener(this);
        deckPanel.getTable().addFocusListener(this);
    }

    private void doFocusLostAction(FocusEvent e) {
        if (e.getOppositeComponent() == deckPanel.getTable()) {
            cardPoolPanel.clearSelection();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        // not interested.
    }

    @Override
    public void focusLost(FocusEvent e) {
        doFocusLostAction(e);
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
        return actionButtons;
    }

    @Override
    public void notifyShowing() {
        // not applicable.
    }

}
