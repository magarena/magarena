package magic.ui.screen.deck;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.widget.cards.table.CardTablePanelA;
import magic.ui.screen.deck.editor.DeckSideBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckViewPanel extends JPanel {

    private MagicDeck deck;
    private final MigLayout migLayout = new MigLayout();
    private final DeckSideBar sideBarPanel;
    private final CardTablePanelA deckTable;

    DeckViewPanel(final MagicDeck aDeck, final MagicCardDefinition selectedCard) {

        this.deck = aDeck;

        sideBarPanel = new DeckSideBar();
        sideBarPanel.setDeck(deck);

        deckTable = new CardTablePanelA(this.deck, "  " + this.deck.getName());
        deckTable.setDeckEditorSelectionMode();
        deckTable.setHeaderVisible(false);
        deckTable.showCardCount(true);
        setDeckTablePropChangeListeners();

        setLookAndFeel();
        refreshLayout();

        if (selectedCard != null) {
            deckTable.setSelectedCard(selectedCard);
        } else {
            deckTable.setSelectedCard(null);
        }

    }

    DeckViewPanel(final MagicDeck aDeck) {
        this(aDeck, null);
    }

    private void setDeckTablePropChangeListeners() {
        deckTable.addPropertyChangeListener(CardTablePanelA.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        setCard(getSelectedCard());
                    }
                });
    }

    private MagicCardDefinition getSelectedCard() {
        if (deckTable.getSelectedCards().size() > 0) {
            return deckTable.getSelectedCards().get(0);
        } else {
            return MagicCardDefinition.UNKNOWN;
        }
    }

    private void setCard(final MagicCardDefinition card) {
        final int cardCount = deck.getCardCount(card);
        sideBarPanel.setCard(card);
        sideBarPanel.setCardCount(cardCount);
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(migLayout);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0");
        add(sideBarPanel, "h 100%");
        add(deckTable, "w 100%, h 100%");
    }

    MagicDeck getDeck() {
        return this.deck;
    }

    void setDeck(MagicDeck aDeck) {
        this.deck = aDeck;
    }

}
