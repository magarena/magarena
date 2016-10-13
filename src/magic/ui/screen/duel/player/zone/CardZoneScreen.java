package magic.ui.screen.duel.player.zone;

import java.util.Collections;
import magic.model.MagicCardList;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.cards.canvas.CardsCanvas;

@SuppressWarnings("serial")
public class CardZoneScreen extends HeaderFooterScreen {

    private final CardsCanvas content;
    private final MagicCardList cards;

    public CardZoneScreen(final MagicCardList cardsIn, final String zoneName, final boolean animateCards) {

        super(zoneName);

        this.cards = new MagicCardList(cardsIn);
        this.content = new CardsCanvas();
        setDefaultProperties(animateCards);

        Collections.sort(this.cards);
        this.content.refresh(this.cards);

        setMainContent(this.content);
    }

    private void setDefaultProperties(boolean animateCards) {
        content.setAnimationEnabled(animateCards);
        content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
    }
}
