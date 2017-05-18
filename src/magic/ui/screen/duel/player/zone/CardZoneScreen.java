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
import magic.ui.widget.cards.canvas.CardsCanvas.LayoutMode;
import magic.ui.widget.cards.canvas.ICardsCanvasListener;

@SuppressWarnings("serial")
public class CardZoneScreen extends HeaderFooterScreen
    implements ICardsCanvasListener, ICardFlowProvider {

    private final CardsCanvas content;
    private final MagicCardList cards;
    private int startImageIndex = 0;
    private final String zoneName;

    public CardZoneScreen(final MagicCardList cardsIn, final String zoneName, final boolean animateCards) {

        super(zoneName);
        this.zoneName = zoneName;

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
        for (int i = index; i < cards.size(); i++) {
            if (cards.get(i).getCardDefinition() == card) {
                startImageIndex = i;
                ScreenController.showCardFlowScreen(this, zoneName);
                break;
            }
        }
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
        return startImageIndex;
    }
}
