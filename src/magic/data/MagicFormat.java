package magic.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.utility.DeckUtils;
import magic.utility.MagicResources;

public enum MagicFormat {

    // add new formats here...
    // @name: display name in UI.
    // @filename: case-sensitive name of file (without extension) in magic/data/formats.

    STANDARD ("Standard", "standard"),
    MODERN ("Modern", "modern"),
    LEGACY("Legacy", "legacy"),
    VINTAGE("Vintage", "vintage"),
    ICE_AGE_BLOCK("Ice Age Block", "ice_age_block"),
    MIRAGE_BLOCK("Mirage block", "mirage_block"),
    TEMPEST_BLOCK("Tempest block", "tempest_block"),
    URZA_BLOCK("Urza block", "urza_block"),
    MASQUES_BLOCK("Masques block", "masques_block"),
    INVASION_BLOCK("Invasion block", "invasion_block"),
    ODYSSEY_BLOCK("Odyssey block", "odyssey_block"),
    ONSLAUGHT_BLOCK("Onslaught block", "onslaught_block"),
    MIRRODIN_BLOCK("Mirrodin block", "mirrodin_block"),
    KAMIGAWA_BLOCK("Kamigawa block", "kamigawa_block"),
    RAVNICA_BLOCK("Ravnica block", "ravnica_block"),
    TIME_SPIRAL_BLOCK("Time Spiral block", "time_spiral_block"),
    LORWYN_SHADOWMOOR_BLOCK("Lorwyn-Shadowmoor block", "lorwyn_shadowmoor_block"),
    SHARDS_OF_ALARA_BLOCK("Shards of Alara block", "shards_of_alara_block"),
    ZENDIKAR_RISE_OF_THE_ELDRAZI_BLOCK("Zendikar-Rise of the Eldrazi block", "zendikar_rise_of_the_eldrazi_block"),
    SCARS_OF_MIRRODIN_BLOCK("Scars of Mirrodin block", "scars_of_mirrodin_block"),
    INNISTRAD_AVACYN_RESTORED_BLOCK("Innistrad-Avacyn Restored block", "innistrad_avacyn_restored_block"),
    RETURN_TO_RAVNICA_BLOCK("Return to Ravnica block", "return_to_ravnica_block"),
    THEROS_BLOCK("Theros block", "theros_block"),
    KHANS_OF_TARKIR_BLOCK("Khans of Tarkir block", "khans_of_tarkir_block")
    ;

    private final String name;
    private final String filename;
    private final int minimumDeckSize;
    private final int maximumCardCopies;
    
    private final List<String> bannedCardNames = new ArrayList<>();
    private final List<String> restrictedCardNames = new ArrayList<>();
    private final List<MagicSets> magicSets = new ArrayList<>();

    // CTR
    private MagicFormat(String name, String filename, int minDeckSize, int maxCardCopies) {
        this.name = name;
        this.filename = filename;
        this.minimumDeckSize = minDeckSize;
        this.maximumCardCopies = maxCardCopies;
    }
    // CTR
    private MagicFormat(String name, String filename) {
        this(name, filename, 60, 4);
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
        for (MagicFormat f : MagicFormat.values()) {
            values.add(f.getName());
        }
        return values.toArray(new String[values.size()]);
    }

    private boolean isCardLegal(MagicCardDefinition card, int cardCount) {
        return getCardLegality(card, cardCount) == CardLegality.Legal;
    }

    public boolean isCardLegal(MagicCardDefinition card) {
        return isCardLegal(card, 1);
    }

    private void loadMagicFormatFile() {
        try (final Scanner sc = new Scanner(MagicResources.getFileContent(this))) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                final boolean skipLine = (line.startsWith("#") || line.isEmpty());
                if (!skipLine) {
                    switch (line.substring(0, 1)) {
                        case "!":
                            bannedCardNames.add(line.substring(1));
                            break;
                        case "*":
                            restrictedCardNames.add(line.substring(1));
                            break;
                        default:
                            magicSets.add(MagicSets.valueOf(line));
                    }
                }
            }
        }
    }

    public CardLegality getCardLegality(MagicCardDefinition card, int cardCount) {
        if (cardCount > getMaximumCardCopies() && card.isLand() == false) {
            return CardLegality.TooManyCopies;
        }
        if (magicSets.isEmpty()) {
            loadMagicFormatFile();
        }
        if (isCardBanned(card)) {
            return CardLegality.Banned;
        } else if (cardCount > 1 && isCardRestricted(card)) {
            return CardLegality.Restricted;
        } else if (isCardInFormat(card)) { // this takes the longest so is done last.
            return CardLegality.Legal;
        } else {
            return CardLegality.Illegal;
        }
    }

    public int getMinimumDeckSize() {
        return minimumDeckSize;
    }

    private int getMaximumCardCopies() {
        return maximumCardCopies;
    }

    public boolean isDeckLegal(final MagicDeck aDeck) {
        if (aDeck.size() < getMinimumDeckSize()) {
            return false;
        }
        for (final MagicCardDefinition card : DeckUtils.getDistinctCards(aDeck)) {
            final int cardCountCheck = card.isLand() ? 1 : aDeck.getCardCount(card);
            if (isCardLegal(card, cardCountCheck) == false) {
                return false;
            }
        }
        return true;
    }

    private boolean isCardBanned(MagicCardDefinition aCard) {
        return bannedCardNames.isEmpty() == false && bannedCardNames.contains(aCard.getName());
    }

    private boolean isCardRestricted(MagicCardDefinition card) {
        return restrictedCardNames.isEmpty() == false && restrictedCardNames.contains(card.getName());
    }

    private boolean isCardInFormat(MagicCardDefinition card) {
        for (MagicSets magicSet : magicSets) {
            if (MagicSetDefinitions.isCardInSet(card, magicSet)) {
                return true;
            }
        }
        return false;
    }

}
