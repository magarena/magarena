package magic.ui.deck.editor;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.ScreenController;
import magic.ui.cardtable.CardTablePanel;
import magic.ui.cardtable.DeckTablePanel;
import magic.ui.screen.widget.ActionBarButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckPanel extends JPanel implements IDeckEditorView {

    // fired when card selection changes
    public static final String CP_CARD_SELECTED = CardTablePanel.CP_CARD_SELECTED;
    // fired on add card to deck action.
    public static final String CP_ADD_TO_DECK = "addCardToDeck";
    // fired on remove card from deck action.
    public static final String CP_REMOVE_FROM_DECK = "removeCardFromDeck";
   
    // UI components
    private final CardQuantityActionPanel quantityPanel;
    private final DeckTablePanel deckTablePanel;
    private final MigLayout miglayout = new MigLayout();
    private MagicDeck deck;
    private final MagicDeck originalDeck;
    private final IDeckEditorListener listener;
    private final List<ActionBarButton> actionButtons = new ArrayList<>();

    DeckPanel(final MagicDeck originalDeck, final IDeckEditorListener aListener, final CardQuantityActionPanel aPanel) {
        
        this.quantityPanel = aPanel;
        this.listener = aListener;
        this.originalDeck = originalDeck;
        this.deck = getDeckCopy(originalDeck);

        actionButtons.add(getClearDeckActionButton());

        deckTablePanel = new DeckTablePanel(this.deck, getDeckTitle(this.deck));
        deckTablePanel.setDeckEditorSelectionMode();
        deckTablePanel.setHeaderVisible(false);
        deckTablePanel.showCardCount(true);
        setDeckTablePropChangeListeners();

        setLookAndFeel();
        refreshLayout();

        if (this.deck.size() > 0) {
            deckTablePanel.selectFirstRow();
        }
    }

    /**
     * work with a copy of the original deck so it is easy to cancel updates.
     */
    private MagicDeck getDeckCopy(final MagicDeck deck) {
        final MagicDeck deckCopy = new MagicDeck();
        if (deck != null) {
            deckCopy.setContent(deck);
        }
        return deckCopy;
    }
   
    private ActionBarButton getClearDeckActionButton() {
        return new ActionBarButton(
                IconImages.getIcon(MagicIcon.CLEAR_ICON),
                "Clear deck",
                "Remove all cards from deck. Confirmation required.",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        doClearDeck();
                    }
                });
    }

    private void setDeckTablePropChangeListeners() {
        deckTablePanel.addPropertyChangeListener(
                CardTablePanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        deckTablePanel.addPropertyChangeListener(
                CardTablePanel.CP_CARD_LCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        addSelectedCardToDeck();
                    }
                });
        deckTablePanel.addPropertyChangeListener(
                CardTablePanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        removeSelectedCardFromDeck(true);
                    }
                });
    }

    private String getDeckTitle(final MagicDeck deck) {
        return "  " + deck.getName();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 0, gap 0, wrap 2, flowy");
        add(deckTablePanel, "w 100%, h 100%");
        revalidate();
    }

    private void doClearDeck() {
        if (deck.size() > 0) {
            final int userResponse = JOptionPane.showOptionDialog(
                    ScreenController.getMainFrame(),
                    "<html>Remove all cards from deck?<br><br><b>This action cannot be undone</b>.</html>",
                    "Clear Deck?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {"Yes", "No"}, "No");
            if (userResponse == JOptionPane.YES_OPTION) {
                setDeck(null);
                listener.deckUpdated(getDeck());
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    void setDeck(final MagicDeck newDeck) {
        if (newDeck == null) {
            deck = new MagicDeck();
        } else {
            deck = getDeckCopy(newDeck);
        }
        deckTablePanel.setTitle(getDeckTitle(deck));
        deckTablePanel.setCards(deck);
        deckTablePanel.selectFirstRow();
        listener.deckUpdated(getDeck());
    }

    MagicDeck getDeck() {
        return deck;
    }

    @Override
    public MagicCardDefinition getSelectedCard() {
        if (deckTablePanel.getSelectedCards().size() > 0) {
            return deckTablePanel.getSelectedCards().get(0);
        } else {
            return MagicCardDefinition.UNKNOWN;
        }
    }

    private void addSelectedCardToDeck() {
        for (int i = 0; i < quantityPanel.getQuantity(); i++) {
            firePropertyChange(CP_ADD_TO_DECK, false, true);
        }
    }

    private void removeSelectedCardFromDeck(final boolean isMouseClick) {
        final int cardCount = deck.getCardCount(getSelectedCard());
        int quantity = Math.min(cardCount, quantityPanel.getQuantity());
        if (cardCount - quantity < 1 && isMouseClick) {
            quantity = cardCount - 1;
            Toolkit.getDefaultToolkit().beep();
        }
        for (int i = 0; i < quantity; i++) {
            firePropertyChange(CP_REMOVE_FROM_DECK, false, true);
        }
    }

    void addCardToDeck(final MagicCardDefinition card) {
        deck.add(card);
        deckTablePanel.setCards(deck);
        deckTablePanel.setSelectedCard(card);
        listener.deckUpdated(getDeck());
    }

    void removeCardFromDeck(MagicCardDefinition card) {
        deck.remove(card);
        deckTablePanel.setCards(deck);
        deckTablePanel.setSelectedCard(card);
        listener.deckUpdated(getDeck());
    }

    @Override
    public boolean requestFocusInWindow() {
        if (deckTablePanel.getSelectedCards().isEmpty()) {
            deckTablePanel.selectFirstRow();
        }
        return super.requestFocusInWindow();
    }

    void updateOriginalDeck() {
        originalDeck.setContent(deck);        
    }

    boolean isUpdatingExistingDeck() {
        return originalDeck != null;
    }

    void setSelectedCard(MagicCardDefinition selectedCard) {
        deckTablePanel.setSelectedCard(selectedCard);
    }

    JTable getDeckTable() {
        return deckTablePanel.getDeckTable();
    }

    void setDeckTable(final JTable aDeckTable) {
        deckTablePanel.setDeckTable(aDeckTable);
    }

    @Override
    public void doPlusButtonAction() {
        addSelectedCardToDeck();
    }

    @Override
    public void doMinusButtonAction() {
        removeSelectedCardFromDeck(false);        
    }

    @Override
    public List<ActionBarButton> getActionButtons() {
        return actionButtons;
    }

}
