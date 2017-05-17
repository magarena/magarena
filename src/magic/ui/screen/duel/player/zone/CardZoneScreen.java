package magic.ui.screen.duel.player.zone;

import java.util.Collections;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.widget.cards.canvas.CardImageOverlay;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.cards.canvas.ICardsCanvasListener;

@SuppressWarnings("serial")
public class CardZoneScreen extends HeaderFooterScreen
    implements ICardsCanvasListener {

    private final CardsCanvas content;
    private final MagicCardList cards;

    public CardZoneScreen(final MagicCardList cardsIn, final String zoneName, final boolean animateCards) {

        super(zoneName);

        this.cards = new MagicCardList(cardsIn);
        content = new CardsCanvas();
        content.setListener(this);
        setDefaultProperties(animateCards);

        Collections.sort(this.cards);
        this.content.refresh(this.cards);

        setMainContent(this.content);
    }

    private void setDefaultProperties(boolean animateCards) {
        content.setAnimationEnabled(animateCards);
        content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
    }

    @Override
    public void cardSelected(MagicCardDefinition aCard) {
        // not applicable.
    }

    @Override
    public void cardClicked(int index, MagicCardDefinition card) {
        new CardImageOverlay(card);
    }
}
