package magic.model;

import javax.swing.ImageIcon;

import magic.data.IconImages;

public enum MagicManaType {

	Colorless("colorless","{1}"),
	Black("black","{B}"),
	Blue("blue","{U}"),
	Green("green","{G}"),
	Red("red","{R}"),
	White("white","{W}"),
	;
	
	public static final MagicManaType[] ALL_COLORS={Black,Blue,Green,Red,White};
	public static final MagicManaType[] ALL_TYPES={Colorless,Black,Blue,Green,Red,White}; // Colorless must be in front.
	
	public static final int NR_OF_TYPES=values().length;
	
	private final String name;
	private final String text;
	
	private MagicManaType(final String name, final String text) {
		this.name=name;
		this.text=text;
	}
	
	public String getName() {
		return name;
	}
		
	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}
	
	public ImageIcon getIcon(final boolean small) {
		switch (this) {
			case Colorless: return small?IconImages.COST_ONE:IconImages.ONE;
			case Black: return small?IconImages.COST_BLACK:IconImages.BLACK;
			case Blue: return small?IconImages.COST_BLUE:IconImages.BLUE;
			case Green: return small?IconImages.COST_GREEN:IconImages.GREEN;
			case Red: return small?IconImages.COST_RED:IconImages.RED;
			case White: return small?IconImages.COST_WHITE:IconImages.WHITE;
		}
		return null;
	}
}
