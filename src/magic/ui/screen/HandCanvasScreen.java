package magic.ui.screen;

import magic.ui.screen.cardflow.FlashTextOverlay;
import magic.ui.widget.cards.canvas.CardsCanvas;

public abstract class HandCanvasScreen extends HeaderFooterScreen {

    protected CardsCanvas cardsCanvas;
    protected final FlashTextOverlay flashOverlay;
    protected HandCanvasOptionsPanel optionsPanel;
    protected HandCanvasLayeredPane layeredPane;

    public HandCanvasScreen(String title) {
        super(title);
        flashOverlay = new FlashTextOverlay(600, 60);
    }

    protected void setCardsLayout() {
        switch (HandZoneLayout.getLayout()) {
        case STACKED_DUPLICATES:
            cardsCanvas.setStackDuplicateCards(true);
            break;
        case NO_STACKING:
            cardsCanvas.setStackDuplicateCards(false);
            break;
        default:
            throw new IndexOutOfBoundsException();
        }
    }

    public void flashLayoutSetting() {
        flashOverlay.flashText(HandZoneLayout.getLayout().getDisplayName());
    }

    public void setCardsLayout(int ordinal) {
        HandZoneLayout.setLayout(ordinal);
        setCardsLayout();
        flashLayoutSetting();
    }

}
