package magic.ui.screen.deck.editor;

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
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.ui.MagicImages;
import magic.ui.MagicSound;
import magic.ui.ScreenController;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.widget.cards.table.DeckTablePanel;
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

    private final DeckEditorController controller = DeckEditorController.instance;

    private final CardQuantityActionPanel quantityPanel;
    private final DeckTablePanel deckTablePanel;
    private final MigLayout miglayout = new MigLayout();
    private final IDeckEditorListener listener;
    private final List<ActionBarButton> actionButtons = new ArrayList<>();

    DeckPanel(IDeckEditorListener aListener, final CardQuantityActionPanel aPanel) {

        this.quantityPanel = aPanel;
        this.listener = aListener;

        actionButtons.add(getClearDeckActionButton());

        deckTablePanel = new DeckTablePanel(controller.getDeck(), getDeckTitle());
        deckTablePanel.setDeckEditorSelectionMode();
        deckTablePanel.setHeaderVisible(false);
        deckTablePanel.showCardCount(true);
        setDeckTablePropChangeListeners();

        setLookAndFeel();
        refreshLayout();

        if (!controller.getDeck().isEmpty()) {
            deckTablePanel.selectFirstRow();
        }
    }

    private ActionBarButton getClearDeckActionButton() {
        return new ActionBarButton(
                MagicImages.getIcon(MagicIcon.CLEAR),
                MText.get(_S1),
                MText.get(_S2),
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
                        removeSelectedCardFromDeck(true);
                    }
                });
    }

    private String getDeckTitle() {
        return String.format("   %s", controller.getDeck().getName());
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
        if (!controller.getDeck().isEmpty()) {
            final int userResponse = JOptionPane.showOptionDialog(ScreenController.getFrame(),
                    String.format("<html>%s<br><br><b>%s</b></html>", MText.get(_S3), MText.get(_S4)),
                    MText.get(_S5),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] {MText.get(_S6), MText.get(_S7)}, MText.get(_S7));
            if (userResponse == JOptionPane.YES_OPTION) {
                controller.setDeck(new MagicDeck());
            }
        } else {
            MagicSound.BEEP.play();
        }
    }

    void doRefreshView() {
        deckTablePanel.setTitle(getDeckTitle());
        deckTablePanel.setDeck(controller.getDeck());
        listener.deckUpdated(controller.getDeck());
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
        final int cardCount = controller.getDeck().getCardCount(getSelectedCard());
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
        controller.getDeck().add(card);
        deckTablePanel.setCards(controller.getDeck());
        deckTablePanel.setSelectedCard(card);
        listener.deckUpdated(controller.getDeck());
    }

    void removeCardFromDeck(MagicCardDefinition card) {
        controller.getDeck().remove(card);
        deckTablePanel.setCards(controller.getDeck());
        deckTablePanel.setSelectedCard(card);
        listener.deckUpdated(controller.getDeck());
    }

    @Override
    public boolean requestFocusInWindow() {
        if (deckTablePanel.getSelectedCards().isEmpty()) {
            deckTablePanel.selectFirstRow();
        }
        return super.requestFocusInWindow();
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

    @Override
    public void notifyShowing() {
        // not applicable
    }

}
