package magic.ui.screen.deck.editor;

import magic.model.MagicDeck;

final class DeckEditorController {

    static final DeckEditorController instance = new DeckEditorController();

    private DeckEditorScreen mainScreen;

    void setMainScreen(DeckEditorScreen screen) {
        this.mainScreen = screen;
    }

    void setDeck(MagicDeck aDeck) {
        mainScreen.setDeck(aDeck);
    }

    private DeckEditorController() { }
}
