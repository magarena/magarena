package magic.ui.screen.interfaces;

import java.nio.file.Path;
import magic.data.DeckType;
import magic.model.MagicDeck;

public interface IDeckConsumer {
    void setDeck(MagicDeck deck);
    void setDeck(String deckName, DeckType deckType);
    boolean setDeck(MagicDeck deck, Path deckPath);
}
