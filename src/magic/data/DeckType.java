package magic.data;

import magic.utility.DeckUtils;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Set;
import magic.translate.UiString;

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
    Random(DeckTypeString._S1),
    Preconstructed(DeckTypeString._S2),
    Custom(DeckTypeString._S3),
    Firemind(DeckTypeString._S4)
    ;

    public static final Set<DeckType> PREDEFINED_DECKS = EnumSet.range(Preconstructed, Firemind);

    private final String deckTypeCaption;

    private DeckType(final String caption) {
        this.deckTypeCaption = UiString.get(caption);
    }

    @Override
    public String toString() {
        return deckTypeCaption;
    }

    public static Path getDeckFolder(final DeckType deckType) {
        switch (deckType) {
            case Preconstructed: return DeckUtils.getPrebuiltDecksFolder();
            case Firemind: return DeckUtils.getFiremindDecksFolder();
            default: return Paths.get(DeckUtils.getDeckFolder());
        }
    }

}
