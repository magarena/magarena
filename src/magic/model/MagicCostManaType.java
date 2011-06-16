package magic.model;

import java.util.Arrays;

import javax.swing.ImageIcon;

import magic.data.TextImages;

public enum MagicCostManaType {

	// Ordered from most restrictive to least restrictive.
	Black("black","{B}",new MagicManaType[]{MagicManaType.Black}),
	Blue("blue","{U}",new MagicManaType[]{MagicManaType.Blue}),
	Green("green","{G}",new MagicManaType[]{MagicManaType.Green}),
	Red("red","{R}",new MagicManaType[]{MagicManaType.Red}),
	White("white","{W}",new MagicManaType[]{MagicManaType.White}),	
	BlackGreen("black/green","{B/G}",new MagicManaType[]{MagicManaType.Black,MagicManaType.Green}),
	BlackRed("black/red","{B/R}",new MagicManaType[]{MagicManaType.Black,MagicManaType.Red}),
	BlueBlack("blue/black","{U/B}",new MagicManaType[]{MagicManaType.Blue,MagicManaType.Black}),
	BlueRed("blue/red","{U/R}",new MagicManaType[]{MagicManaType.Blue,MagicManaType.Red}),
	GreenBlue("green/blue","{G/U}",new MagicManaType[]{MagicManaType.Green,MagicManaType.Blue}),
	GreenWhite("green/white","{G/W}",new MagicManaType[]{MagicManaType.Green,MagicManaType.White}),
	RedGreen("red/green","{R/G}",new MagicManaType[]{MagicManaType.Red,MagicManaType.Green}),
	RedWhite("red/white","{R/W}",new MagicManaType[]{MagicManaType.Red,MagicManaType.White}),
	WhiteBlack("white/black","{W/B}",new MagicManaType[]{MagicManaType.White,MagicManaType.Black}),
	WhiteBlue("white/blue","{W/U}",new MagicManaType[]{MagicManaType.White,MagicManaType.Blue}),
	Colorless("colorless","{1}",MagicManaType.ALL_TYPES),
	;
	
	public static final int NR_OF_TYPES=values().length;
	
	private final String name;
	private final String text;
	private final MagicManaType types[];

	private MagicCostManaType(final String name,final String text,final MagicManaType types[]) {
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
	
	public MagicManaType[] getTypes() {
		return types;
	}
	
	public MagicManaType[] getTypes(final MagicPlayerProfile profile) {
		int count=0;
		final MagicManaType profileTypes[]=new MagicManaType[types.length];
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
