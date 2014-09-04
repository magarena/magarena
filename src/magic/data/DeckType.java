package magic.data;

import java.util.EnumSet;
import java.util.Set;

/**
 * Ways to group decks.
 * <p>
 * Be careful about renaming the enum value since this is used
 * in settings files such as those used to store new duel configuration.
 */
public enum DeckType {

    // TODO: Favourite("Favourite"),           // most used decks
    // TODO: Bookmarked("Bookmarked"),         // decks tagged by player
    // TODO: Recent("Recently Played"),        // last 20 most recently played decks
    Random("Random"),
    Preconstructed("Prebuilt"),
    Custom("Player")
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
