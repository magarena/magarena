package magic.model;

public class MagicPlayerProfile {
	
	private final String colorText;
	private final MagicColor[] colors;
	
	public MagicPlayerProfile(final String colorText) {

		this.colorText=colorText;
		colors=new MagicColor[colorText.length()];
		for (int i=0;i<colorText.length();i++) {
			
			colors[i]=MagicColor.getColor(colorText.charAt(i));
		}
	}
	
	public String getColorText() {
		
		return colorText;
	}
	
	public MagicColor[] getColors() {
		
		return colors;
	}
	
	public int getNrOfColors() {
		
		return colors.length;
	}
	
	public int getNrOfNonBasicLands(final int amount) {
		
		switch (colors.length) {
			case 3: return amount/2;
			case 2: return amount/4;
			default: return 0;
		}
	}
	
	public boolean allowsManaType(final MagicManaType manaType) {
		
		for (final MagicColor color : colors) {
			
			if (color.getManaType()==manaType) {
				return true;
			}
		}
		return false;
	}			
}