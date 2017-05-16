package magic.ui.screen.duel.player.zone;

import java.util.Collections;
import magic.model.MagicCardList;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.widget.cards.canvas.CardsCanvas;

public class LibraryZoneScreen extends HeaderFooterScreen {

    private final CardsCanvas content;
    private final MagicCardList cards;

    public LibraryZoneScreen(final MagicCardList cardsIn) {

        super("Library");

        this.cards = new MagicCardList(cardsIn);
        this.content = new CardsCanvas();
        setDefaultProperties(false);

        // displays top card at top left.
        Collections.reverse(cards);

        this.content.refresh(this.cards);
        setMainContent(this.content);
    }

    private void setDefaultProperties(boolean animateCards) {
        content.setAnimationEnabled(animateCards);
        content.setLayoutMode(CardsCanvas.LayoutMode.SCALE_TO_FIT);
        content.setStackDuplicateCards(false);
    }
}
