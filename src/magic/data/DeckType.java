package magic.data;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Set;
import magic.translate.MText;
import magic.utility.DeckUtils;

/**
 * Ways to group decks.
 * <p>
 * Be careful about renaming the enum value since this is used
 * in settings files such as those used to store new duel configuration.
 */
public enum DeckType {

    // TODO: Bookmarked("Bookmarked"),         // decks tagged by player
    Random(DeckTypeString._S1),
    Preconstructed(DeckTypeString._S2),
    Custom(DeckTypeString._S3),
    Firemind(DeckTypeString._S4),
    PopularDecks("Popular decks"),
    WinningDecks("Winning decks"),
    RecentDecks("Recently played decks")
    ;

    private final String deckTypeCaption;

    private DeckType(final String caption) {
        this.deckTypeCaption = MText.get(caption);
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

    public static final Set<DeckType> getPredefinedDecks() {
        return GeneralConfig.isGameStatsOn()
            ? EnumSet.range(Preconstructed, RecentDecks)
            : EnumSet.range(Preconstructed, Firemind);
    }

}
