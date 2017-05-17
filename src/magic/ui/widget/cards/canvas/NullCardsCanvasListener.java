package magic.ui.widget.cards.canvas;

import magic.model.MagicCardDefinition;

public class NullCardsCanvasListener implements ICardsCanvasListener {
    @Override
    public void cardSelected(MagicCardDefinition aCard) {}

    @Override
    public void cardClicked(int index, MagicCardDefinition card) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
