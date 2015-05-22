package magic.ui.deck.editor;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.ui.cardtable.CardTablePanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class HistoryTabPanel extends JPanel {

    // fired when card selection changes
    public static final String CP_CARD_SELECTED = CardTablePanel.CP_CARD_SELECTED;
    // fired on add card to deck action.
    public static final String CP_ADD_TO_DECK = "addCardToDeck";
    // fired on remove card from deck action.
    public static final String CP_REMOVE_FROM_DECK = "removeCardFromDeck";

    // UI components
    private final DeckCardPoolActionBar actionBar;
    private final CardTablePanel historyTable;
    //
    private final MigLayout miglayout = new MigLayout();    
    private final List<MagicCardDefinition> history;

    public HistoryTabPanel() {
        history = new ArrayList<>();
        //
        actionBar = new DeckCardPoolActionBar(getPlusButtonAction(), getMinusButtonAction());
        actionBar.setOptionBarVisible(false);
        //
        historyTable = new CardTablePanel(this.history, "");
        historyTable.setDeckEditorSelectionMode();
        historyTable.setHeaderVisible(false);
        setDeckTablePropChangeListeners();
        //
        setLookAndFeel();
        refreshLayout();
        //
        if (this.history.size() > 0) {
            historyTable.selectFirstRow();
        }
    }

    private void setDeckTablePropChangeListeners() {
        historyTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        historyTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_LCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        addSelectedCardToDeck();
                    }
                });
        historyTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_RCLICKED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        removeSelectedCardFromDeck(true);
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
        add(actionBar, "w 40!, h 100%");
        add(historyTable, "w 100%, h 100%");
        revalidate();
    }

    public MagicCardDefinition getSelectedCard() {
        if (historyTable.getSelectedCards().size() > 0) {
            return historyTable.getSelectedCards().get(0);
        } else {
            return MagicCardDefinition.UNKNOWN;
        }
    }

    private AbstractAction getPlusButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedCardToDeck();
            }
        };
    }

    private AbstractAction getMinusButtonAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedCardFromDeck(false);
            }
        };
    }

    private void addSelectedCardToDeck() {
        if (!historyTable.getSelectedCards().isEmpty()) {
            for (int i = 0; i < actionBar.getQuantity(); i++) {
                firePropertyChange(CP_ADD_TO_DECK, false, true);
            }
        }
    }

    private void removeSelectedCardFromDeck(final boolean isMouseClick) {
        for (int i = 0; i < actionBar.getQuantity(); i++) {
            firePropertyChange(CP_REMOVE_FROM_DECK, false, true);
        }
    }

    @Override
    public boolean requestFocusInWindow() {
        if (historyTable.getSelectedCards().isEmpty()) {
            historyTable.selectFirstRow();
        }
        return super.requestFocusInWindow();
    }

    void addCardToHistory(final MagicCardDefinition card) {
        if (!history.contains(card)) {
            history.add(card);
            historyTable.setCards(history);
            historyTable.setSelectedCard(card);
        }
    }

}
