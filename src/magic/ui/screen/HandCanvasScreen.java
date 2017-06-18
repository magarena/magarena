package magic.ui.screen;

import magic.ui.screen.cardflow.FlashTextOverlay;
import magic.ui.widget.cards.canvas.CardsCanvas;

public abstract class HandCanvasScreen extends HeaderFooterScreen {

    protected CardsCanvas cardsCanvas;
    protected final FlashTextOverlay flashOverlay;

    public HandCanvasScreen(String title) {
        super(title);
        flashOverlay = new FlashTextOverlay(600, 60);
    }

}
