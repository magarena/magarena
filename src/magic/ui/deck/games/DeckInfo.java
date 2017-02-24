package magic.ui.deck.games;

import magic.data.DeckType;

class DeckInfo {

    final String deckName;
    final String deckColor;
    long checksum;
    DeckType deckType;

    DeckInfo(String deckName, DeckType deckType, long checksum, String deckColor) {
        this.deckName = deckName;
        this.deckType = deckType;
        this.checksum = checksum;
        this.deckColor = getDeckColor(deckColor);
    }

    private String getDeckColor(String deckColor) {
        return deckColor;
    }

}
