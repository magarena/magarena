package magic.ui.screen.deck.editor;

import magic.model.MagicDeck;
import magic.ui.screen.widget.ScreenSideBar;
import magic.ui.widget.deck.DeckInfoPanel;
import magic.ui.widget.deck.stats.IPwlWorkerListener;
import magic.ui.widget.duel.viewer.CardViewer;

@SuppressWarnings("serial")
public class DeckSideBar extends ScreenSideBar
    implements IPwlWorkerListener {

    private final DeckInfoPanel deckInfo = new DeckInfoPanel();

    public DeckSideBar() {
        refreshLayout();
        deckInfo.addPropertyChangeListener(
            DeckInfoPanel.CP_LAYOUT_CHANGED,
            (e) -> { refreshLayout(); }
        );
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        migLayout.setColumnConstraints("[fill, grow]");
        migLayout.setRowConstraints("[][fill, grow]");
        add(cardScrollPane.component());
        add(deckInfo);
        revalidate();
    }

    public void setDeck(MagicDeck aDeck) {
        deckInfo.setDeck(aDeck);
    }

    public void setCardCount(final int count) {
        cardViewer.setCardCount(count);
    }

    public CardViewer getCardViewer() {
        return cardViewer;
    }

    @Override
    public void setPlayedWonLost(String pwl) {
        deckInfo.setPlayedWonLost(pwl);
    }

}
