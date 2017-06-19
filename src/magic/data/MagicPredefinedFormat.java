package magic.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.utility.DeckUtils;
import magic.utility.MagicResources;

public class MagicPredefinedFormat extends MagicFormat {

    // add new formats here...
    // @name: display name in UI.
    // @filename: case-sensitive name of file (without extension) in magic/data/formats.

    public static final MagicFormat STANDARD = new MagicPredefinedFormat("Standard", "standard");
    public static final MagicFormat MODERN = new MagicPredefinedFormat("Modern", "modern");
    public static final MagicFormat LEGACY = new MagicPredefinedFormat("Legacy", "legacy");
    public static final MagicFormat VINTAGE = new MagicPredefinedFormat("Vintage", "vintage");
    public static final MagicFormat ICE_AGE_BLOCK = new MagicPredefinedFormat("Ice Age Block", "ice_age_block");
    public static final MagicFormat MIRAGE_BLOCK = new MagicPredefinedFormat("Mirage block", "mirage_block");
    public static final MagicFormat TEMPEST_BLOCK = new MagicPredefinedFormat("Tempest block", "tempest_block");
    public static final MagicFormat URZA_BLOCK = new MagicPredefinedFormat("Urza block", "urza_block");
    public static final MagicFormat MASQUES_BLOCK = new MagicPredefinedFormat("Masques block", "masques_block");
    public static final MagicFormat INVASION_BLOCK = new MagicPredefinedFormat("Invasion block", "invasion_block");
    public static final MagicFormat ODYSSEY_BLOCK = new MagicPredefinedFormat("Odyssey block", "odyssey_block");
    public static final MagicFormat ONSLAUGHT_BLOCK = new MagicPredefinedFormat("Onslaught block", "onslaught_block");
    public static final MagicFormat MIRRODIN_BLOCK = new MagicPredefinedFormat("Mirrodin block", "mirrodin_block");
    public static final MagicFormat KAMIGAWA_BLOCK = new MagicPredefinedFormat("Kamigawa block", "kamigawa_block");
    public static final MagicFormat RAVNICA_BLOCK = new MagicPredefinedFormat("Ravnica block", "ravnica_block");
    public static final MagicFormat TIME_SPIRAL_BLOCK = new MagicPredefinedFormat("Time Spiral block", "time_spiral_block");
    public static final MagicFormat LORWYN_SHADOWMOOR_BLOCK = new MagicPredefinedFormat("Lorwyn-Shadowmoor block", "lorwyn_shadowmoor_block");
    public static final MagicFormat SHARDS_OF_ALARA_BLOCK = new MagicPredefinedFormat("Shards of Alara block", "shards_of_alara_block");
    public static final MagicFormat ZENDIKAR_RISE_OF_THE_ELDRAZI_BLOCK = new MagicPredefinedFormat("Zendikar-Rise of the Eldrazi block", "zendikar_rise_of_the_eldrazi_block");
    public static final MagicFormat SCARS_OF_MIRRODIN_BLOCK = new MagicPredefinedFormat("Scars of Mirrodin block", "scars_of_mirrodin_block");
    public static final MagicFormat INNISTRAD_AVACYN_RESTORED_BLOCK = new MagicPredefinedFormat("Innistrad-Avacyn Restored block", "innistrad_avacyn_restored_block");
    public static final MagicFormat RETURN_TO_RAVNICA_BLOCK = new MagicPredefinedFormat("Return to Ravnica block", "return_to_ravnica_block");
    public static final MagicFormat THEROS_BLOCK = new MagicPredefinedFormat("Theros block", "theros_block");
    public static final MagicFormat KHANS_OF_TARKIR_BLOCK = new MagicPredefinedFormat("Khans of Tarkir block", "khans_of_tarkir_block");
    public static final MagicFormat BATTLE_FOR_ZENDIKAR_BLOCK = new MagicPredefinedFormat("Battle for Zendikar block", "battle_for_zendikar_block");
    public static final MagicFormat SHADOWS_OVER_INNISTRAD_BLOCK = new MagicPredefinedFormat("Shadows over Innistrad block", "shadows_over_innistrad_block");
    public static final MagicFormat KALADESH_BLOCK = new MagicPredefinedFormat("Kaladesh block", "kaladesh_block");
    public static final MagicFormat AMONKHET_BLOCK = new MagicPredefinedFormat("Amonkhet block", "amonkhet_block");

    private static final List<MagicFormat> values = Collections.unmodifiableList(Arrays.asList(
        STANDARD,
        MODERN,
        LEGACY,
        VINTAGE,
        AMONKHET_BLOCK,
        KALADESH_BLOCK,
        SHADOWS_OVER_INNISTRAD_BLOCK,
        BATTLE_FOR_ZENDIKAR_BLOCK,
        KHANS_OF_TARKIR_BLOCK,
        THEROS_BLOCK,
        RETURN_TO_RAVNICA_BLOCK,
        INNISTRAD_AVACYN_RESTORED_BLOCK,
        SCARS_OF_MIRRODIN_BLOCK,
        ZENDIKAR_RISE_OF_THE_ELDRAZI_BLOCK,
        SHARDS_OF_ALARA_BLOCK,
        LORWYN_SHADOWMOOR_BLOCK,
        TIME_SPIRAL_BLOCK,
        RAVNICA_BLOCK,
        KAMIGAWA_BLOCK,
        MIRRODIN_BLOCK,
        ONSLAUGHT_BLOCK,
        ODYSSEY_BLOCK,
        INVASION_BLOCK,
        MASQUES_BLOCK,
        URZA_BLOCK,
        TEMPEST_BLOCK,
        MIRAGE_BLOCK,
        ICE_AGE_BLOCK
    ));

    public static List<MagicFormat> values() {
        return values;
    }

    private final String name;
    private final String filename;
    private final int minimumDeckSize;
    private final int maximumCardCopies;

    private final List<String> bannedCardNames = new ArrayList<>();
    private final List<String> restrictedCardNames = new ArrayList<>();
    private final List<MagicSets> magicSets = new ArrayList<>();

    private MagicPredefinedFormat(final String name, final String filename, int minDeckSize, int maxCardCopies) {
        this.name = name;
        this.filename = filename;
        this.minimumDeckSize = minDeckSize;
        this.maximumCardCopies = maxCardCopies;
    }

    private MagicPredefinedFormat(final String name, final String filename) {
        this(name, filename, 60, 4);
    }

    @Override
    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    private void loadMagicFormatFile() {
        try (final Scanner sc = new Scanner(MagicResources.getFileContent(this))) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                if (!line.startsWith("#") && !line.isEmpty()) {
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

    @Override
    public CardLegality getCardLegality(MagicCardDefinition card, int cardCount) {
        if (cardCount > maximumCardCopies && !card.canHaveAnyNumberInDeck()) {
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

    @Override
    public int getMinimumDeckSize() {
        return minimumDeckSize;
    }

    @Override
    public boolean isDeckLegal(final MagicDeck aDeck) {
        if (aDeck.size() < minimumDeckSize) {
            return false;
        }
        for (final MagicCardDefinition card : DeckUtils.getDistinctCards(aDeck)) {
            final int cardCountCheck = aDeck.getCardCount(card);
            if (!isCardLegal(card, cardCountCheck)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCardBanned(MagicCardDefinition aCard) {
        return !bannedCardNames.isEmpty() && bannedCardNames.contains(aCard.getName());
    }

    private boolean isCardRestricted(MagicCardDefinition card) {
        return !restrictedCardNames.isEmpty() && restrictedCardNames.contains(card.getName());
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
