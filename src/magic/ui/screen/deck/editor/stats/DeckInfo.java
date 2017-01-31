package magic.ui.screen.deck.editor.stats;

public class DeckInfo {

    final public String deckName;
    final public String deckColor;

    DeckInfo(String deckName, String deckColor) {
        this.deckName = deckName;
        this.deckColor = getDeckColor(deckColor);
    }

    private String getDeckColor(String deckColor) {
        return deckColor;
    }

}
