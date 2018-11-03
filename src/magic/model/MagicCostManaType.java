package magic.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public enum MagicCostManaType {

    // Ordered from least restrictive to most restrictive.
    // Same order as in mana cost
    Generic("generic", "{1}", Arrays.asList(MagicManaType.Colorless, MagicManaType.White, MagicManaType.Blue, MagicManaType.Black, MagicManaType.Red, MagicManaType.Green)),
    WhiteBlue("white/blue", "{W/U}", Arrays.asList(MagicManaType.White, MagicManaType.Blue)),
    WhiteBlack("white/black", "{W/B}", Arrays.asList(MagicManaType.White, MagicManaType.Black)),
    BlueBlack("blue/black", "{U/B}", Arrays.asList(MagicManaType.Blue, MagicManaType.Black)),
    BlueRed("blue/red", "{U/R}", Arrays.asList(MagicManaType.Blue, MagicManaType.Red)),
    BlackRed("black/red", "{B/R}", Arrays.asList(MagicManaType.Black, MagicManaType.Red)),
    BlackGreen("black/green", "{B/G}", Arrays.asList(MagicManaType.Black, MagicManaType.Green)),
    RedGreen("red/green", "{R/G}", Arrays.asList(MagicManaType.Red, MagicManaType.Green)),
    RedWhite("red/white", "{R/W}", Arrays.asList(MagicManaType.Red, MagicManaType.White)),
    GreenWhite("green/white", "{G/W}", Arrays.asList(MagicManaType.Green, MagicManaType.White)),
    GreenBlue("green/blue", "{G/U}", Arrays.asList(MagicManaType.Green, MagicManaType.Blue)),
    PhyrexianWhite("phyrexian white", "{W/P}", Collections.singletonList(MagicManaType.White)),
    PhyrexianBlue("phyrexian blue", "{U/P}", Collections.singletonList(MagicManaType.Blue)),
    PhyrexianBlack("phyrexian black", "{B/P}", Collections.singletonList(MagicManaType.Black)),
    PhyrexianRed("phyrexian red", "{R/P}", Collections.singletonList(MagicManaType.Red)),
    PhyrexianGreen("phyrexian green", "{G/P}", Collections.singletonList(MagicManaType.Green)),
    HybridWhite("hybrid white", "{2/W}", Collections.singletonList(MagicManaType.White)),
    HybridBlue("hybrid blue", "{2/U}", Collections.singletonList(MagicManaType.Blue)),
    HybridBlack("hybrid black", "{2/B}", Collections.singletonList(MagicManaType.Black)),
    HybridRed("hybrid red", "{2/R}", Collections.singletonList(MagicManaType.Red)),
    HybridGreen("hybrid green", "{2/G}", Collections.singletonList(MagicManaType.Green)),
    White("white", "{W}", Collections.singletonList(MagicManaType.White)),
    Blue("blue", "{U}", Collections.singletonList(MagicManaType.Blue)),
    Black("black", "{B}", Collections.singletonList(MagicManaType.Black)),
    Red("red", "{R}", Collections.singletonList(MagicManaType.Red)),
    Green("green", "{G}", Collections.singletonList(MagicManaType.Green)),
    Snow("snow", "{S}", Collections.singletonList(MagicManaType.Snow)),
    Colorless("colorless", "{C}", Collections.singletonList(MagicManaType.Colorless))
    ;

    public static final int NR_OF_TYPES=values().length;
    public static final EnumSet<MagicCostManaType> MONO = EnumSet.range(White, Green);
    public static final EnumSet<MagicCostManaType> HYBRID = EnumSet.range(WhiteBlue, GreenBlue);
    public static final EnumSet<MagicCostManaType> CHOICE = EnumSet.range(WhiteBlue, HybridGreen);
    public static final EnumSet<MagicCostManaType> NON_MONO = EnumSet.complementOf(MONO);

    private final String name;
    private final String text;
    private final List<MagicManaType> types;

    private MagicCostManaType(final String name,final String text,final List<MagicManaType> types) {
        this.name=name;
        this.text=text;
        this.types=types;
    }

    public MagicCostManaType next() {
        switch (this) {
            case White: return Blue;
            case Blue:  return Black;
            case Black: return Red;
            case Red:   return Green;
            case Green: return White;
            default: throw new RuntimeException("No next mana cost type for " + this);
        }
    }

    public MagicCostManaType prev() {
        switch (this) {
            case White: return Green;
            case Blue:  return White;
            case Black: return Blue;
            case Red:   return Black;
            case Green: return Red;
            default: throw new RuntimeException("No next mana cost type for " + this);
        }
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public List<MagicManaType> getTypes() {
        return types;
    }

    public MagicManaType[] getTypes(final MagicDeckProfile profile) {
        int count=0;
        final MagicManaType[] profileTypes=new MagicManaType[types.size()];
        for (final MagicManaType manaType : types) {
            if (profile.allowsManaType(manaType)) {
                profileTypes[count++]=manaType;
            }
        }
        return Arrays.copyOf(profileTypes,count);
    }
}
