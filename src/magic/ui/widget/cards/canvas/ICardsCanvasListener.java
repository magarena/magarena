package magic.ui.widget.cards.canvas;

import magic.model.MagicCardDefinition;

public interface ICardsCanvasListener {
    void cardSelected(MagicCardDefinition aCard);
    void cardClicked(int index, MagicCardDefinition card);
}
