package magic.ui.deck.editor;

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
import magic.ui.MagicImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.cardtable.DeckTablePanel;
import magic.ui.screen.widget.ActionBarButton;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckPanel extends JPanel implements IDeckEditorView {

    // translatable string
    private static final String _S1 = "Clear deck";
    private static final String _S2 = "Remove all cards from deck. Confirmation required.";
    private static final String _S3 = "Remove all cards from deck?";
    private static final String _S4 = "This action cannot be undone.";
    private static final String _S5 = "Clear Deck?";
    private static final String _S6 = "Yes";
    private static final String _S7 = "No";

    // fired when card selection changes
    public static final String CP_CARD_SELECTED = "99aa7f10-b4ca-4917-9f1f-fa8de1f7ee80";
    // fired on add card to deck action.
    public static final String CP_ADD_TO_DECK = "8f5d8209-b274-4384-b7da-0c239bf7d92b";
    // fired on remove card from deck action.
    public static final String CP_REMOVE_FROM_DECK = "33e31608-cf6b-45a2-9fde-bafaaf66f2d2";

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
                MagicImages.getIcon(MagicIcon.CLEAR_ICON),
                UiString.get(_S1),
                UiString.get(_S2),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        doClearDeck();
                    }
                });
    }

    private void setDeckTablePropChangeListeners() {
        deckTablePanel.addPropertyChangeListener(
                DeckTablePanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        deckTablePanel.addPropertyChangeListener(
                DeckTablePanel.CP_CARD_LCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        addSelectedCardToDeck();
                    }
                });
        deckTablePanel.addPropertyChangeListener(
                DeckTablePanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        //removeSelectedCardFromDeck(true);
                    }
                });
    }

    private String getDeckTitle(final MagicDeck deck) {
        return String.format("   %s", deck.getName());
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
                    String.format("<html>%s<br><br><b>%s</b></html>", UiString.get(_S3), UiString.get(_S4)),
                    UiString.get(_S5),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {UiString.get(_S6), UiString.get(_S7)}, UiString.get(_S7));
            if (userResponse == JOptionPane.YES_OPTION) {
                setDeck(null);
            }
        } else {
            MagicSound.BEEP.play();
        }
    }

    void setDeck(final MagicDeck newDeck) {
        if (newDeck == null) {
            deck = new MagicDeck();
        } else {
            deck = getDeckCopy(newDeck);
        }
        deckTablePanel.setTitle(getDeckTitle(deck));
        deckTablePanel.setDeck(deck);
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
            MagicSound.BEEP.play();
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
