package magic.ui.deck.games;

class DeckInfo {

    final String deckName;
    final String deckColor;

    DeckInfo(String deckName, String deckColor) {
        this.deckName = deckName;
        this.deckColor = getDeckColor(deckColor);
    }

    private String getDeckColor(String deckColor) {
        return deckColor;
    }

}
