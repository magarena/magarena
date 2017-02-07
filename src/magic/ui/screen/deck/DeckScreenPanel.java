package magic.ui.screen.deck;

import javax.swing.JPanel;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.screen.deck.editor.DeckSideBar;
import magic.ui.screen.decks.DeckViewsPanel;
import magic.ui.screen.decks.ICardsTableListener;
import magic.ui.widget.cards.canvas.CardImageOverlay;
import magic.ui.widget.deck.stats.IPwlWorkerListener;
import magic.ui.widget.deck.stats.PwlWorker;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class DeckScreenPanel extends JPanel implements ICardsTableListener  {

    private final MagicDeck deck;
    private final DeckSideBar sideBarPanel;
    private final DeckViewsPanel viewsPanel;
    private PwlWorker pwlWorker;

    DeckScreenPanel(final MagicDeck aDeck, MagicCardDefinition selectCard) {

        this.deck = aDeck;

        sideBarPanel = new DeckSideBar();
        sideBarPanel.setDeck(deck);

        viewsPanel = new DeckViewsPanel();
        viewsPanel.setCardsTableListeners(this);
        viewsPanel.setDeck(deck, selectCard);

        doPWLStatsQuery(deck, sideBarPanel, viewsPanel);

        setLookAndFeel();
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
    }

    private void refreshLayout() {
        setLayout(new MigLayout("insets 0", "[]0[fill, grow]", "fill, grow"));
        add(sideBarPanel, "h 100%");
        add(viewsPanel, "w 100%, h 100%");
    }

    MagicDeck getDeck() {
        return this.deck;
    }

    private void doPWLStatsQuery(MagicDeck deck, IPwlWorkerListener... listeners) {
        pwlWorker = new PwlWorker(deck);
        pwlWorker.setListeners(listeners);
        pwlWorker.execute();
    }

    @Override
    public void onCardSelected(MagicCardDefinition card) {
        sideBarPanel.setCard(card);
    }

    @Override
    public void onLeftClick(MagicCardDefinition card) {
        // not applicable
    }

    @Override
    public void onRightClick(MagicCardDefinition card) {
        new CardImageOverlay(card);
    }

}
