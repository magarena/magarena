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
    Random(UIText._S1),
    Preconstructed(UIText._S2),
    Custom(UIText._S3),
    Firemind(UIText._S4),
    PopularDecks(UIText._S5),
    WinningDecks(UIText._S6),
    RecentDecks(UIText._S7)
    ;

    private final String caption;

    private DeckType(final String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return MText.get(caption);
    }

    public static Path getDeckFolder(final DeckType deckType) {
        switch (deckType) {
            case Preconstructed: return DeckUtils.getPrebuiltDecksFolder();
            case Firemind: return DeckUtils.getFiremindDecksFolder();
            default: return Paths.get(DeckUtils.getDecksFolder());
        }
    }

    public static final Set<DeckType> getPredefinedDecks() {
        return GeneralConfig.isGameStatsOn()
            ? EnumSet.range(Preconstructed, RecentDecks)
            : EnumSet.range(Preconstructed, Firemind);
    }

    public static final DeckType[] getDuelDeckTypes() {
        return EnumSet.range(Random, Firemind).toArray(new DeckType[0]);
    }

}

/**
 * translatable UI text (prefix with _S).
 */
final class UIText {
    static final String _S1 = "Random";
    static final String _S2 = "Prebuilt decks";
    static final String _S3 = "Player decks";
    static final String _S4 = "Firemind top decks";
    static final String _S5 = "Popular decks";
    static final String _S6 = "Winning decks";
    static final String _S7 = "Recently played decks";
    private UIText() {}
}


