package magic.data;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicRarity;
import magic.translate.MText;

public class CardStatistics {

    // translatable strings
    private static final String _S1 = "Land";
    private static final String _S2 = "Creature";
    private static final String _S3 = "Artifact";
    private static final String _S4 = "Enchantment";
    private static final String _S5 = "Instant";
    private static final String _S6 = "Sorcery";
    private static final String _S7 = "Planeswalker";


    private static final List<String> MANA_CURVE_TEXT = Collections.unmodifiableList(
        Arrays.asList(
            "X", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9+"
        )
    );

    public static final List<MagicIcon> MANA_CURVE_ICONS = Collections.unmodifiableList(
        Arrays.asList(
            MagicIcon.MANA_X,
            MagicIcon.MANA_0,
            MagicIcon.MANA_1,
            MagicIcon.MANA_2,
            MagicIcon.MANA_3,
            MagicIcon.MANA_4,
            MagicIcon.MANA_5,
            MagicIcon.MANA_6,
            MagicIcon.MANA_7,
            MagicIcon.MANA_8,
            MagicIcon.MANA_9
        )
    );
    public static final int MANA_CURVE_SIZE = MANA_CURVE_TEXT.size();

    public static final List<String> TYPE_NAMES = Collections.unmodifiableList(Arrays.asList(MText.get(_S1),
            MText.get(_S2),
            MText.get(_S3),
            MText.get(_S4),
            MText.get(_S5),
            MText.get(_S6),
            MText.get(_S7)
        )
    );
    public static final int NR_OF_TYPES = TYPE_NAMES.size();

    public static final List<MagicIcon> TYPE_ICONS = Collections.unmodifiableList(
        Arrays.asList(
            MagicIcon.LAND,
            MagicIcon.CREATURE,
            MagicIcon.ARTIFACT,
            MagicIcon.ENCHANTMENT,
            MagicIcon.INSTANT,
            MagicIcon.SORCERY,
            MagicIcon.PLANESWALKER
        )
    );

    private final Collection<MagicCardDefinition> cards;

    public int totalCards;
    public final int[] totalTypes=new int[NR_OF_TYPES];

    private final int[] totalRarity=new int[MagicRarity.length];

    private double averageCost;
    private double averageValue;

    public final int[] colorCount=new int[MagicColor.NR_COLORS];
    public final int[] colorMono=new int[MagicColor.NR_COLORS];

    public final int[] colorLands=new int[MagicColor.NR_COLORS];
    public final int[] colorArtifacts=new int[MagicColor.NR_COLORS];
    public final int[] colorCreatures=new int[MagicColor.NR_COLORS];
    public final int[] colorEnchantments=new int[MagicColor.NR_COLORS];
    public final int[] colorInstants=new int[MagicColor.NR_COLORS];
    public final int[] colorSorcery=new int[MagicColor.NR_COLORS];
    public final int[] colorPlaneswalkers=new int[MagicColor.NR_COLORS];

    public final int[] manaCurve=new int[MANA_CURVE_SIZE];
    public int monoColor;
    public int multiColor;
    public int colorless;

    public CardStatistics(final Collection<MagicCardDefinition> cards) {
        this.cards=cards;
        createStatistics();
    }

    private void createStatistics() {

        for (final MagicCardDefinition card : cards) {

            //ignore tokens
            if (card.isToken() || card.isInvalid()) {
                continue;
            }

            totalCards++;
            totalRarity[card.getRarity()]++;

            int count = 0;
            int index = -1;

            for (final MagicColor color : MagicColor.values()) {

                if (color.hasColor(card.getColorFlags())) {
                    index = color.ordinal();
                    colorCount[index]++;
                    count++;
                }

                if (card.isLand() && card.getManaSource(color) > 0) {
                    colorLands[color.ordinal()]++;
                }

                if (card.hasColor(color)) {
                    if (card.isCreature()) {
                        colorCreatures[color.ordinal()]++;
                    }
                    if (card.isArtifact()) {
                        colorArtifacts[color.ordinal()]++;
                    }
                    if (card.isEnchantment()) {
                        colorEnchantments[color.ordinal()]++;
                    }
                    if (card.isInstant()) {
                        colorInstants[color.ordinal()]++;
                    }
                    if (card.isSorcery()) {
                        colorSorcery[color.ordinal()]++;
                    }
                    if (card.isPlaneswalker()) {
                        colorPlaneswalkers[color.ordinal()]++;
                    }
                }
            }

            if (count == 0) {
                colorless++;
            } else if (count == 1) {
                colorMono[index]++;
                monoColor++;
            } else {
                multiColor++;
            }

            if (card.hasX()) {
                manaCurve[0]++;
            } else if (card.getCost() != MagicManaCost.NONE) {
                final int convertedCost = card.getConvertedCost();
                manaCurve[convertedCost + 1 >= MANA_CURVE_SIZE ? MANA_CURVE_SIZE - 1 : convertedCost + 1]++;
            }

            averageCost += card.getConvertedCost();
            averageValue += card.getValue();

            if (card.isLand()) {
                totalTypes[0]++;
            }
            if (card.isCreature()) {
                totalTypes[1]++;
            }
            if (card.isArtifact()) {
                totalTypes[2]++;
            }
            if (card.isEnchantment()) {
                totalTypes[3]++;
            }
            if (card.isInstant()) {
                totalTypes[4]++;
            }
            if (card.isSorcery()) {
                totalTypes[5]++;
            }
            if (card.isPlaneswalker()) {
                totalTypes[6]++;
            }

        }

        final int total=totalCards-totalTypes[0];
        if (total>0) {
            averageValue /= total;
            averageCost /= total;
        }
    }

    void printStatistics(final PrintStream stream) {

        stream.print("Cards : "+totalCards);
        for (int index=0;index<NR_OF_TYPES;index++) {
            stream.print("  "+TYPE_NAMES.get(index)+" : "+totalTypes[index]);
        }
        stream.println();

        for (int index=0;index<MagicRarity.length;index++) {

            stream.print(MagicRarity.values()[index].getName() + " : " + totalRarity[index] + "  ");
        }
        stream.println();
        stream.printf("Average Cost : %.2f  Value : %.2f\n", averageCost, averageValue);
        stream.println("Monocolor : "+monoColor+"  Multicolor : "+multiColor+"  Colorless : "+colorless);

        for (final MagicColor color : MagicColor.values()) {

            final int index=color.ordinal();
            stream.print("Color "+color.getName()+" : "+colorCount[index]);
            stream.print("  Mono : "+colorMono[index]);
            stream.print("  Lands : "+colorLands[index]);
            stream.println();
        }

        for (int index=0;index<MANA_CURVE_SIZE;index++) {

            stream.print(MANA_CURVE_TEXT.get(index)+" = "+manaCurve[index]+"  ");
        }
        stream.println();
    }

    public int getMaxManaCurve() {
        return Arrays.stream(manaCurve).max().orElse(0);
    }
}
