package magic.model;

import magic.data.IconImages;

import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum MagicColor {

	Black("black",'b'),
	Blue("blue",'u'),
	Green("green",'g'),
	Red("red",'r'),
	White("white",'w')
	;
	
	public static final int NR_COLORS=values().length;
	
	private final String name;
	private final char symbol;
	private final int mask;
	
	private MagicColor(final String name,final char symbol) {
		this.name=name;	
		this.symbol=symbol;
		this.mask=1<<ordinal();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public char getSymbol() {
		return symbol;
	}
	
	public int getMask() {
		return mask;
	}
	
	public boolean hasColor(final int flags) {
		return (flags&mask)!=0;
	}
	
	public MagicAbility getProtectionAbility() {
		switch (this) {
			case Black: return MagicAbility.ProtectionFromBlack;
			case Blue: return MagicAbility.ProtectionFromBlue;
			case Green: return MagicAbility.ProtectionFromGreen;
			case Red: return MagicAbility.ProtectionFromRed;
			case White: return MagicAbility.ProtectionFromWhite;
		}
        throw new RuntimeException("No protection ability for MagicColor " + this);
	}
	
	public MagicAbility getLandwalkAbility() {
		switch (this) {
			case Black: return MagicAbility.Swampwalk;
			case Blue: return MagicAbility.Islandwalk;
			case Green: return MagicAbility.Forestwalk;
			case Red: return MagicAbility.Mountainwalk;
			case White: return MagicAbility.PlainsWalk;
		}	
        throw new RuntimeException("No landwalk ability for MagicColor " + this);
	}
	
	public MagicSubType getLandSubType() {
		switch (this) {
			case Black: return MagicSubType.Swamp;
			case Blue: return MagicSubType.Island;
			case Green: return MagicSubType.Forest;
			case Red: return MagicSubType.Mountain;
			case White: return MagicSubType.Plains;		
		}
        throw new RuntimeException("No land subtype for MagicColor " + this);
	}
	
	public MagicManaType getManaType() {
		switch (this) {
			case Black: return MagicManaType.Black;
			case Blue: return MagicManaType.Blue;
			case Green: return MagicManaType.Green;
			case Red: return MagicManaType.Red;
			case White: return MagicManaType.White;
		}
		return MagicManaType.Colorless;
	}
	
	public ImageIcon getIcon() {
		switch (this) {
			case Black: return IconImages.BLACK;
			case Blue: return IconImages.BLUE;
			case Green: return IconImages.GREEN;
			case Red: return IconImages.RED;
			case White: return IconImages.WHITE;
		}
        throw new RuntimeException("No icon for MagicColor " + this);
	}

	public static int getFlags(final String colors) {
		int flags=0;
		for (int index=0;index<colors.length();index++) {
			final char symbol=colors.charAt(index);
			final MagicColor color=getColor(symbol);
			flags|=color.getMask();
		}
		return flags;
	}
	
	public static MagicColor getColor(final char symbol) {
		final char usymbol=Character.toLowerCase(symbol);
		for (final MagicColor color : values()) {
			if (color.symbol==usymbol) {
				return color;
			}
		}
        throw new RuntimeException("No corresponding MagicColor for " + symbol);
	}	
	
	public static String getRandomColors(final int count) {
		final List<Character> colors=new ArrayList<Character>(Arrays.<Character>asList('b','u','g','r','w'));
		final StringBuilder colorText=new StringBuilder();
		for (int c=count;c>0;c--) {
			final int index=MagicRandom.nextInt(colors.size());
			colorText.append((char)colors.remove(index));			
		}
		return colorText.toString();
	}

    public static boolean isMono(final int flag) {
        return flag == Black.getMask() || 
            flag == Blue.getMask() || 
            flag == Green.getMask() || 
            flag == Red.getMask() || 
            flag == White.getMask();
    }

    public static boolean isColorless(final int flag) {
        return flag == 0;
    }

    public static boolean isMulti(final int flag) {
        return !isMono(flag) && !isColorless(flag);
    }
}
