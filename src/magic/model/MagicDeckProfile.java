package magic.model;

import magic.data.DeckGenerators;
import magic.data.DeckType;

public class MagicDeckProfile {

    public static final String ANY_DECK="@";
    public static final String ANY_THREE="***";
    public static final String ANY_TWO="**";
    public static final String ANY_ONE="*";

    private String deckGeneratorName;
    private String colorText;
    private MagicColor[] colors;
    private boolean isPreConstructed;

    private DeckType deckType = DeckType.Random;
    private String deckValue = ANY_THREE;

    public MagicDeckProfile(final String colorText) {
        this(colorText, ANY_DECK);
    }

    public MagicDeckProfile(final String colorText, final String deckGeneratorName) {
        this.deckGeneratorName = deckGeneratorName;
        this.deckValue = deckGeneratorName;
        setColors(colorText);
    }

    public MagicDeckProfile(DeckType deckType2, String deckValue2) {
        this.deckType = deckType2;
        this.deckValue = deckValue2;
    }

    public void setColors(final String colorText) {
        this.colorText=colorText;
        colors=new MagicColor[colorText.length()];
        for (int i=0;i<colorText.length();i++) {
            colors[i]=MagicColor.getColor(colorText.charAt(i));
        }
    }

    public String getDeckGeneratorName() {
        return deckGeneratorName;
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

    public static MagicDeckProfile getDeckProfile(final String colorText) {
        if (ANY_DECK.equals(colorText)) {
            return new MagicDeckProfile("", colorText);
        } else if (ANY_THREE.equals(colorText)) {
            return new MagicDeckProfile(MagicColor.getRandomColors(3), colorText);
        } else if (ANY_TWO.equals(colorText)) {
            return new MagicDeckProfile(MagicColor.getRandomColors(2), colorText);
        } else if (ANY_ONE.equals(colorText)) {
            return new MagicDeckProfile(MagicColor.getRandomColors(1), colorText);
        } else if (DeckGenerators.getInstance().getGeneratorNames().contains(colorText)) {
            // custom deck generator
            return new MagicDeckProfile("", colorText);
        }
        return new MagicDeckProfile(colorText, colorText);
    }
    public static MagicDeckProfile getDeckProfile(final DeckType deckType, final String deckValue) {
        switch (deckType) {
        case Random:
            return getDeckProfile(deckValue);
        default:
            return new MagicDeckProfile(deckType, deckValue);
        }
    }

    public DeckType getDeckType() {
        return deckType;
    }

    public String getDeckValue() {
        return deckValue;
    }

}
