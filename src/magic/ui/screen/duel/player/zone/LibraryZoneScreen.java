package magic.ui.screen.duel.player.zone;

import java.awt.image.BufferedImage;
import java.util.Collections;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.ui.MagicImages;
import magic.ui.ScreenController;
import magic.ui.screen.HeaderFooterScreen;
import magic.ui.screen.cardflow.ICardFlowProvider;
import magic.ui.widget.cards.canvas.CardsCanvas;
import magic.ui.widget.cards.canvas.ICardsCanvasListener;

public class LibraryZoneScreen extends HeaderFooterScreen
    implements ICardsCanvasListener, ICardFlowProvider {

    private final CardsCanvas content;
    private final MagicCardList cards;
    private int clickedCardIndex = 0;

    public LibraryZoneScreen(final MagicCardList cardsIn) {

        super("Library");

        cards = new MagicCardList(cardsIn);
        content = new CardsCanvas();
        content.setListener(this);
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

    @Override
    public void cardSelected(MagicCardDefinition aCard) {
        // not applicable
    }

    @Override
    public void cardClicked(int index, MagicCardDefinition card) {
        clickedCardIndex = index;
        ScreenController.showCardFlowScreen(this, "Library");
    }

    @Override
    public BufferedImage getImage(int index) {
        return MagicImages.getCardImage(cards.get(index));
    }

    @Override
    public int getImagesCount() {
        return cards.size();
    }

    @Override
    public int getStartImageIndex() {
        return clickedCardIndex;
    }

    @Override
    public long getAnimationDuration() {
        return 300L;
    }

}
