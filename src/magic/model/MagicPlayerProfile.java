package magic.model;

public class MagicPlayerProfile {
	
	private String colorText;
	private MagicColor[] colors;
	
	public MagicPlayerProfile(final String colorText) {
		setColors(colorText);
	}
	
	public void setColors(final String colorText) {

		this.colorText=colorText;
		colors=new MagicColor[colorText.length()];
		for (int i=0;i<colorText.length();i++) {
			
			colors[i]=MagicColor.getColor(colorText.charAt(i));
		}
	}
	
	String getColorText() {
		
		return colorText;
	}
	
	public MagicColor[] getColors() {
		
		return colors;
	}
	
	int getNrOfColors() {
		
		return colors.length;
	}
	
	public int getNrOfNonBasicLands(final int amount) {
		
		switch (colors.length) {
			case 3: return amount/2;
			case 2: return amount/4;
			default: return 0;
		}
	}
	
	boolean allowsManaType(final MagicManaType manaType) {
		
		for (final MagicColor color : colors) {
			
			if (color.getManaType()==manaType) {
				return true;
			}
		}
		return false;
	}			
}