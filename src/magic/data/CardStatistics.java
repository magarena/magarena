package magic.data;

import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicRarity;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CardStatistics {

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

    public static final List<String> TYPE_NAMES = Collections.unmodifiableList(
        Arrays.asList(
            "Land",
            "Spell",
            "Creature",
            "Equipment",
            "Aura",
            "Enchantment",
            "Artifact"
        )
    );
    public static final int NR_OF_TYPES = TYPE_NAMES.size();

    public static final List<MagicIcon> TYPE_ICONS = Collections.unmodifiableList(
        Arrays.asList(
            MagicIcon.LAND,
            MagicIcon.SPELL,
            MagicIcon.CREATURE,
            MagicIcon.EQUIPMENT,
            MagicIcon.AURA,
            MagicIcon.ENCHANTMENT,
            MagicIcon.ARTIFACT
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
            if (card.isToken()) {
                continue;
            }

            totalCards++;

            totalRarity[card.getRarity()]++;

            if (card.isLand()) {
                totalTypes[0]++;
                for (final MagicColor color : MagicColor.values()) {
                    if (card.getManaSource(color) > 0) {
                        colorLands[color.ordinal()]++;
                    }
                }
            } else {
                if (card.hasX()) {
                    manaCurve[0]++;
                } else {
                    final int convertedCost=card.getConvertedCost();
                    manaCurve[convertedCost+1>=MANA_CURVE_SIZE?MANA_CURVE_SIZE-1:convertedCost+1]++;
                }

                averageCost+=card.getConvertedCost();
                averageValue+=card.getValue();

                if (card.isCreature()) {
                    totalTypes[2]++;
                } else if (card.isEquipment()) {
                    totalTypes[3]++;
                } else if (card.isArtifact()) {
                    totalTypes[6]++;
                } else if (card.isAura()) {
                    totalTypes[4]++;
                } else if (card.isEnchantment()) {
                    totalTypes[5]++;
                } else {
                    totalTypes[1]++;
                }

                int count=0;
                int index=-1;
                for (final MagicColor color : MagicColor.values()) {

                    if (color.hasColor(card.getColorFlags())) {
                        index=color.ordinal();
                        colorCount[index]++;
                        count++;
                    }
                }
                if (count==0) {
                    colorless++;
                } else if (count==1) {
                    colorMono[index]++;
                    monoColor++;
                } else {
                    multiColor++;
                }
            }
        }

        final int total=totalCards-totalTypes[0];
        if (total>0) {
            averageValue /= total;
            averageCost /= total;
        }
    }

    void printStatictics(final PrintStream stream) {

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
}
