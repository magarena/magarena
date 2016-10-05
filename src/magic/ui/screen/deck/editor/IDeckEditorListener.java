package magic.ui.screen.deck.editor;

import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;

public interface IDeckEditorListener {
    void deckUpdated(final MagicDeck deck);
    void cardSelected(final MagicCardDefinition card);
    void setDeck(MagicDeck deck);

    public void addCardToRecall(MagicCardDefinition card);
}
