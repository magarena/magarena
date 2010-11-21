package magic.model;

import javax.swing.ImageIcon;

import magic.data.IconImages;

public enum MagicManaType {

	Colorless("colorless","{1}",0),
	Black("black","{B}",1),
	Blue("blue","{U}",2),
	Green("green","{G}",3),
	Red("red","{R}",4),
	White("white","{W}",5),
	;
	
	public static final MagicManaType[] ALL_COLORS={Black,Blue,Green,Red,White};
	public static final MagicManaType[] ALL_TYPES={Colorless,Black,Blue,Green,Red,White}; // Colorless must be in front.
	
	public static final int NR_OF_TYPES=values().length;
	
	private final String name;
	private final String text;
	private final int index;
	
	private MagicManaType(final String name,final String text,final int index) {
		
		this.name=name;
		this.text=text;
		this.index=index;
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
	
	public int getIndex() {
		
		return index;
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