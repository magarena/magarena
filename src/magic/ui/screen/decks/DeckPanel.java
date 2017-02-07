package magic.ui.screen.decks;

import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.widget.cards.table.DeckViewTablePanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckPanel extends JPanel implements IDeckView {

    private final DeckViewTablePanel tablePanel;
    private MagicDeck deck = new MagicDeck();
    private boolean isNewDeck = true;
    private MagicCardDefinition selectCard;

    DeckPanel() {
        this.tablePanel = new DeckViewTablePanel(deck);
        setLayout(new MigLayout("insets 0"));
        refreshLayout();
        setOpaque(false);
    }

    private void refreshLayout() {
        removeAll();
        add(tablePanel, "w 100%, h 100%");
        revalidate();
    }

    void setDeck(MagicDeck deck) {
        if (this.deck != deck) {
            this.deck = deck;
            isNewDeck = true;
        }
    }

    void setDeck(MagicDeck deck, MagicCardDefinition selectCard) {
        if (this.deck != deck) {
            this.deck = deck;
            isNewDeck = true;
        }
        this.selectCard = selectCard;
    }

    @Override
    public void notifyShowing() {
        if (isNewDeck) {
            tablePanel.setCards(deck, selectCard);
            isNewDeck = false;
        } else {
            tablePanel.setSelectedCard(selectCard);
        }
    }

    void setCardsTableListeners(ICardsTableListener... listeners) {
        tablePanel.setListeners(listeners);
    }

    void setSelectedCard(MagicCardDefinition card) {
        tablePanel.setSelectedCard(card);
    }

    MagicCardDefinition getSelectedCard() {
        return tablePanel.getSelectedCard();
    }
}
