package magic.model;

public class MagicPlayerProfile {
    
    private String deckGeneratorName;    
    private String colorText;
    private MagicColor[] colors;
    private boolean isPreConstructed = false;
    
    public MagicPlayerProfile(final String colorText) {
        this(colorText, null);
    }
    
    public MagicPlayerProfile(final String colorText, final String deckGeneratorName) {
        setColors(colorText);
        setDeckGeneratorName(deckGeneratorName);
    }
    
    public void setColors(final String colorText) {

        this.colorText=colorText;
        colors=new MagicColor[colorText.length()];
        for (int i=0;i<colorText.length();i++) {
            
            colors[i]=MagicColor.getColor(colorText.charAt(i));
        }
    }
    
    public void setDeckGeneratorName(final String name) {
        this.deckGeneratorName = name;
    }
    
    public String getDeckGeneratorName() {
        return deckGeneratorName;
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
    
    public void setPreConstructed() {
        isPreConstructed = true;
    }
    
    public boolean isPreConstructed() {
        return isPreConstructed;
    }
}
