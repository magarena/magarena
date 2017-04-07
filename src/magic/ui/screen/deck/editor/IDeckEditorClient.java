package magic.ui.screen.deck.editor;

import magic.model.MagicDeck;

public interface IDeckEditorClient {
    MagicDeck getDeck();
    boolean setDeck(MagicDeck deck);
}
