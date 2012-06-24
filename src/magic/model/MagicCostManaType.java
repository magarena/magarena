package magic.model;

import magic.data.TextImages;

import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.List;

public enum MagicCostManaType {

    // Ordered from least restrictive to most restrictive.
    // Same order as in mana cost
    Colorless("colorless","{1}",MagicManaType.ALL_TYPES),
    WhiteBlue("white/blue","{W/U}",Arrays.asList(MagicManaType.White,MagicManaType.Blue)),
    WhiteBlack("white/black","{W/B}",Arrays.asList(MagicManaType.White,MagicManaType.Black)),
    BlueBlack("blue/black","{U/B}",Arrays.asList(MagicManaType.Blue,MagicManaType.Black)),
    BlueRed("blue/red","{U/R}",Arrays.asList(MagicManaType.Blue,MagicManaType.Red)),
    BlackRed("black/red","{B/R}",Arrays.asList(MagicManaType.Black,MagicManaType.Red)),
    BlackGreen("black/green","{B/G}",Arrays.asList(MagicManaType.Black,MagicManaType.Green)),
    RedGreen("red/green","{R/G}",Arrays.asList(MagicManaType.Red,MagicManaType.Green)),
    RedWhite("red/white","{R/W}",Arrays.asList(MagicManaType.Red,MagicManaType.White)),
    GreenWhite("green/white","{G/W}",Arrays.asList(MagicManaType.Green,MagicManaType.White)),
    GreenBlue("green/blue","{G/U}",Arrays.asList(MagicManaType.Green,MagicManaType.Blue)),
    White("white","{W}",Arrays.asList(MagicManaType.White)),    
    Blue("blue","{U}",Arrays.asList(MagicManaType.Blue)),
    Black("black","{B}",Arrays.asList(MagicManaType.Black)),
    Red("red","{R}",Arrays.asList(MagicManaType.Red)),
    Green("green","{G}",Arrays.asList(MagicManaType.Green)),
    ;
    
    public static final int NR_OF_TYPES=values().length;
    
    private final String name;
    private final String text;
    private final List<MagicManaType> types;

    private MagicCostManaType(final String name,final String text,final List<MagicManaType> types) {
        this.name=name;
        this.text=text;
        this.types=types;
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
    
    public MagicManaType[] getTypes(final MagicPlayerProfile profile) {
        int count=0;
        final MagicManaType profileTypes[]=new MagicManaType[types.size()];
        for (final MagicManaType manaType : types) {
            if (profile.allowsManaType(manaType)) {
                profileTypes[count++]=manaType;
            }
        }
        return Arrays.copyOf(profileTypes,count);
    }
    
    public ImageIcon getIcon() {
        return TextImages.getIcon(text);
    }
}
