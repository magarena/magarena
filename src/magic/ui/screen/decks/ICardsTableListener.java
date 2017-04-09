package magic.ui.screen.decks;

import magic.model.MagicCardDefinition;

public interface ICardsTableListener {
    void onCardSelected(MagicCardDefinition card);
    void onLeftClick(MagicCardDefinition card);
    void onRightClick(MagicCardDefinition card);
}
