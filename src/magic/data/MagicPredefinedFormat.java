package magic.data;

import java.util.Arrays;
import java.util.ArrayList;
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

    public static final MagicPredefinedFormat STANDARD = new MagicPredefinedFormat("Standard", "standard");
    public static final MagicPredefinedFormat MODERN = new MagicPredefinedFormat("Modern", "modern");
    public static final MagicPredefinedFormat LEGACY = new MagicPredefinedFormat("Legacy", "legacy");
    public static final MagicPredefinedFormat VINTAGE = new MagicPredefinedFormat("Vintage", "vintage");
    public static final MagicPredefinedFormat ICE_AGE_BLOCK = new MagicPredefinedFormat("Ice Age Block", "ice_age_block");
    public static final MagicPredefinedFormat MIRAGE_BLOCK = new MagicPredefinedFormat("Mirage block", "mirage_block");
    public static final MagicPredefinedFormat TEMPEST_BLOCK = new MagicPredefinedFormat("Tempest block", "tempest_block");
    public static final MagicPredefinedFormat URZA_BLOCK = new MagicPredefinedFormat("Urza block", "urza_block");
    public static final MagicPredefinedFormat MASQUES_BLOCK = new MagicPredefinedFormat("Masques block", "masques_block");
    public static final MagicPredefinedFormat INVASION_BLOCK = new MagicPredefinedFormat("Invasion block", "invasion_block");
    public static final MagicPredefinedFormat ODYSSEY_BLOCK = new MagicPredefinedFormat("Odyssey block", "odyssey_block");
    public static final MagicPredefinedFormat ONSLAUGHT_BLOCK = new MagicPredefinedFormat("Onslaught block", "onslaught_block");
    public static final MagicPredefinedFormat MIRRODIN_BLOCK = new MagicPredefinedFormat("Mirrodin block", "mirrodin_block");
    public static final MagicPredefinedFormat KAMIGAWA_BLOCK = new MagicPredefinedFormat("Kamigawa block", "kamigawa_block");
    public static final MagicPredefinedFormat RAVNICA_BLOCK = new MagicPredefinedFormat("Ravnica block", "ravnica_block");
    public static final MagicPredefinedFormat TIME_SPIRAL_BLOCK = new MagicPredefinedFormat("Time Spiral block", "time_spiral_block");
    public static final MagicPredefinedFormat LORWYN_SHADOWMOOR_BLOCK = new MagicPredefinedFormat("Lorwyn-Shadowmoor block", "lorwyn_shadowmoor_block");
    public static final MagicPredefinedFormat SHARDS_OF_ALARA_BLOCK = new MagicPredefinedFormat("Shards of Alara block", "shards_of_alara_block");
    public static final MagicPredefinedFormat ZENDIKAR_RISE_OF_THE_ELDRAZI_BLOCK = new MagicPredefinedFormat("Zendikar-Rise of the Eldrazi block", "zendikar_rise_of_the_eldrazi_block");
    public static final MagicPredefinedFormat SCARS_OF_MIRRODIN_BLOCK = new MagicPredefinedFormat("Scars of Mirrodin block", "scars_of_mirrodin_block");
    public static final MagicPredefinedFormat INNISTRAD_AVACYN_RESTORED_BLOCK = new MagicPredefinedFormat("Innistrad-Avacyn Restored block", "innistrad_avacyn_restored_block");
    public static final MagicPredefinedFormat RETURN_TO_RAVNICA_BLOCK = new MagicPredefinedFormat("Return to Ravnica block", "return_to_ravnica_block");
    public static final MagicPredefinedFormat THEROS_BLOCK = new MagicPredefinedFormat("Theros block", "theros_block");
    public static final MagicPredefinedFormat KHANS_OF_TARKIR_BLOCK = new MagicPredefinedFormat("Khans of Tarkir block", "khans_of_tarkir_block");

    private static final List<MagicPredefinedFormat> values = Arrays.asList(
        STANDARD,
        MODERN,
        LEGACY,
        VINTAGE,
        ICE_AGE_BLOCK,
        MIRAGE_BLOCK,
        TEMPEST_BLOCK,
        URZA_BLOCK,
        MASQUES_BLOCK,
        INVASION_BLOCK,
        ODYSSEY_BLOCK,
        ONSLAUGHT_BLOCK,
        MIRRODIN_BLOCK,
        KAMIGAWA_BLOCK,
        RAVNICA_BLOCK,
        TIME_SPIRAL_BLOCK,
        LORWYN_SHADOWMOOR_BLOCK,
        SHARDS_OF_ALARA_BLOCK,
        ZENDIKAR_RISE_OF_THE_ELDRAZI_BLOCK,
        SCARS_OF_MIRRODIN_BLOCK,
        INNISTRAD_AVACYN_RESTORED_BLOCK,
        RETURN_TO_RAVNICA_BLOCK,
        THEROS_BLOCK,
        KHANS_OF_TARKIR_BLOCK
    );

    public static List<MagicPredefinedFormat> values() {
        return values;
    }

    private final String name;
    private final String filename;
    private final int minimumDeckSize;
    private final int maximumCardCopies;
    
    private final List<String> bannedCardNames = new ArrayList<>();
    private final List<String> restrictedCardNames = new ArrayList<>();
    private final List<MagicSets> magicSets = new ArrayList<>();

    private MagicPredefinedFormat(String name, String filename, int minDeckSize, int maxCardCopies) {
        this.name = name;
        this.filename = filename;
        this.minimumDeckSize = minDeckSize;
        this.maximumCardCopies = maxCardCopies;
    }
    
    private MagicPredefinedFormat(String name, String filename) {
        this(name, filename, 60, 4);
    }

    @Override
    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
        for (MagicPredefinedFormat f : MagicPredefinedFormat.values()) {
            values.add(f.getName());
        }
        return values.toArray(new String[values.size()]);
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

    private boolean isCardExemptFromMaxCopiesRestriction(MagicCardDefinition card) {
        return card.isLand() == true
                || card.getName().equals("Relentless Rats")
                || card.getName().equals("Shadowborn Apostle");
    }

    @Override
    public CardLegality getCardLegality(MagicCardDefinition card, int cardCount) {
        if (cardCount > getMaximumCardCopies() && isCardExemptFromMaxCopiesRestriction(card) == false) {
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
