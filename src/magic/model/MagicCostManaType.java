package magic.model;

import java.util.Arrays;

import javax.swing.ImageIcon;

import magic.data.TextImages;

public enum MagicCostManaType {

	// Ordered from most restrictive to least restrictive.
	Black("black","{B}",new MagicManaType[]{MagicManaType.Black},0),
	Blue("blue","{U}",new MagicManaType[]{MagicManaType.Blue},1),
	Green("green","{G}",new MagicManaType[]{MagicManaType.Green},2),
	Red("red","{R}",new MagicManaType[]{MagicManaType.Red},3),
	White("white","{W}",new MagicManaType[]{MagicManaType.White},4),	
	BlackGreen("black/green","{B/G}",new MagicManaType[]{MagicManaType.Black,MagicManaType.Green},5),
	BlackRed("black/red","{B/R}",new MagicManaType[]{MagicManaType.Black,MagicManaType.Red},6),
	BlueBlack("blue/black","{U/B}",new MagicManaType[]{MagicManaType.Blue,MagicManaType.Black},7),
	BlueRed("blue/red","{U/R}",new MagicManaType[]{MagicManaType.Blue,MagicManaType.Red},8),
	GreenBlue("green/blue","{G/U}",new MagicManaType[]{MagicManaType.Green,MagicManaType.Blue},9),
	GreenWhite("green/white","{G/W}",new MagicManaType[]{MagicManaType.Green,MagicManaType.White},10),
	RedGreen("red/green","{R/G}",new MagicManaType[]{MagicManaType.Red,MagicManaType.Green},11),
	RedWhite("red/white","{R/W}",new MagicManaType[]{MagicManaType.Red,MagicManaType.White},12),
	WhiteBlack("white/black","{W/B}",new MagicManaType[]{MagicManaType.White,MagicManaType.Black},13),
	WhiteBlue("white/blue","{W/U}",new MagicManaType[]{MagicManaType.White,MagicManaType.Blue},14),
	Colorless("colorless","{1}",MagicManaType.ALL_TYPES,15),
	;
	
	public static final int NR_OF_TYPES=values().length;
	
	private final String name;
	private final String text;
	private final MagicManaType types[];
	private final int index;

	private MagicCostManaType(final String name,final String text,final MagicManaType types[],final int index) {
		
		this.name=name;
		this.text=text;
		this.types=types;
		this.index=index;
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
	
	public int getIndex() {
		
		return index;
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