package magic.data;

import java.util.EnumSet;
import java.util.Set;

public enum DeckType {

//    Favourite("Favourite"),           // most used decks
//    Bookmarked("Bookmarked"),         // decks tagged by player
//    Recent("Recently Played"),        // last 20 most recently played decks
    Random("Random"),
    Preconstructed("Preconstructed"),
    Custom("Custom")
    ;

    public static final Set<DeckType> PREDEFINED_DECKS = EnumSet.range(Preconstructed, Custom);

    private final String deckTypeCaption;

    private DeckType(final String caption) {
        this.deckTypeCaption = caption;
    }

    @Override
    public String toString() {
        return deckTypeCaption;
    }

}
