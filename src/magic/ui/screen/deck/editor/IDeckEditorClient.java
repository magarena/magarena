package magic.ui.screen.deck.editor;

import magic.model.MagicDeck;

public interface IDeckEditorClient {
    public MagicDeck getDeck();
    public boolean setDeck(MagicDeck deck);
}
