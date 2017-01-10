package magic.ui.screen.deck.editor;

import magic.model.MagicDeck;

final class DeckEditorController {

    private DeckEditorController() { }

    static final DeckEditorController instance = new DeckEditorController();

    private DeckEditorScreen mainScreen;
    private MagicDeck refDeck;
    private MagicDeck editDeck;

    private void setDecks(MagicDeck aDeck) {
        refDeck = aDeck == null ? new MagicDeck() : aDeck;
        editDeck = new MagicDeck(refDeck);
    }

    void init(DeckEditorScreen screen, MagicDeck aDeck) {
        this.mainScreen = screen;
        setDecks(aDeck);
    }

    void setDeck(MagicDeck aDeck) {
        setDecks(aDeck);
        mainScreen.doRefreshViews();
    }

    MagicDeck getDeck() {
        return editDeck;
    }

    boolean hasDeckChanged() {
        return !editDeck.equals(refDeck);
    }

    void setDeckStatusToUnsaved() {
        editDeck.setUnsavedStatus();
    }
}
