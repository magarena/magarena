package magic.ui;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import magic.data.MagicIcon;
import magic.data.ManaSymbol;
import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.MagicCardDefinition;
import magic.model.MagicPermanent;
import magic.utility.MagicResources;

public final class IconImages {
    
    private static final Map<Integer, ImageIcon> manaSymbols = new HashMap<>();

    // BufferedImages
    public static final BufferedImage MISSING = loadImage("missing.png");
    public static final BufferedImage MISSING2 = loadImage("missing2.png");
    public static final BufferedImage MISSING_CARD = loadImage("missing_card.png");
    public static final BufferedImage CARD_BACK = loadImage("card_back.jpg");
    public static final BufferedImage WIZARD = loadImage("wizard.png");

    // default texture images
    public static final BufferedImage WOOD = loadTextureImage("wood.jpg");
    public static final BufferedImage MARBLE = loadTextureImage("marble.jpg");
    public static final BufferedImage GRANITE = loadTextureImage("granite.jpg");
    public static final BufferedImage GRANITE2 = loadTextureImage("granite2.jpg");
    public static final BufferedImage OPAL = loadTextureImage("opal.jpg");
    public static final BufferedImage OPAL2 = loadTextureImage("opal2.jpg");

    public static final ImageIcon FLYING=getIcon(MagicIcon.FLYING);
    public static final ImageIcon TRAMPLE=getIcon(MagicIcon.TRAMPLE);
    public static final ImageIcon STRIKE=getIcon(MagicIcon.STRIKE);
    public static final ImageIcon DEATHTOUCH=getIcon(MagicIcon.DEATHTOUCH);
    public static final ImageIcon PROTBLACK=getIcon(MagicIcon.PROTBLACK);
    public static final ImageIcon PROTBLUE=getIcon(MagicIcon.PROTBLUE);
    public static final ImageIcon PROTGREEN=getIcon(MagicIcon.PROTGREEN);
    public static final ImageIcon PROTRED=getIcon(MagicIcon.PROTRED);
    public static final ImageIcon PROTWHITE=getIcon(MagicIcon.PROTWHITE);
    public static final ImageIcon PROTALLCOLORS=getIcon(MagicIcon.PROTALLCOLORS);
    public static final ImageIcon DEFENDER=getIcon(MagicIcon.DEFENDER);
    public static final ImageIcon VIGILANCE=getIcon(MagicIcon.VIGILANCE);
    public static final ImageIcon DOUBLESTRIKE=getIcon(MagicIcon.DOUBLESTRIKE);
    public static final ImageIcon INFECT=getIcon(MagicIcon.INFECT);
    public static final ImageIcon WITHER=getIcon(MagicIcon.WITHER);
    public static final ImageIcon LIFELINK=getIcon(MagicIcon.LIFELINK);
    public static final ImageIcon REACH=getIcon(MagicIcon.REACH);
    public static final ImageIcon SHROUD=getIcon(MagicIcon.SHROUD);
    public static final ImageIcon HEXPROOF=getIcon(MagicIcon.HEXPROOF);
    public static final ImageIcon FEAR=getIcon(MagicIcon.FEAR);
    public static final ImageIcon INTIMIDATE=getIcon(MagicIcon.INTIMIDATE);
    public static final ImageIcon INDESTRUCTIBLE=getIcon(MagicIcon.INDESTRUCTIBLE);

    public static final ImageIcon PLUS=getIcon(MagicIcon.PLUS);
    public static final ImageIcon MINUS=getIcon(MagicIcon.MINUS);
    public static final ImageIcon CHARGE=getIcon(MagicIcon.CHARGE);
    public static final ImageIcon FEATHER=getIcon(MagicIcon.FEATHER);
    public static final ImageIcon GOLDCOUNTER=getIcon(MagicIcon.GOLDCOUNTER);
    public static final ImageIcon BRIBECOUNTER=getIcon(MagicIcon.BRIBECOUNTER);
    public static final ImageIcon TIMECOUNTER=getIcon(MagicIcon.TIMECOUNTER);
    public static final ImageIcon FADECOUNTER=getIcon(MagicIcon.FADECOUNTER);
    public static final ImageIcon QUESTCOUNTER=getIcon(MagicIcon.QUESTCOUNTER);
    public static final ImageIcon LEVELCOUNTER=getIcon(MagicIcon.LEVELCOUNTER);
    public static final ImageIcon HOOFPRINTCOUNTER=getIcon(MagicIcon.HOOFPRINTCOUNTER);
    public static final ImageIcon AGECOUNTER=getIcon(MagicIcon.AGECOUNTER);
    public static final ImageIcon ICECOUNTER=getIcon(MagicIcon.ICECOUNTER);
    public static final ImageIcon SPORECOUNTER=getIcon(MagicIcon.SPORECOUNTER);
    public static final ImageIcon ARROWHEADCOUNTER=getIcon(MagicIcon.ARROWHEADCOUNTER);
    public static final ImageIcon LOYALTYCOUNTER=getIcon(MagicIcon.LOYALTYCOUNTER);
    public static final ImageIcon KICOUNTER=getIcon(MagicIcon.KICOUNTER);
    public static final ImageIcon DEPLETIONCOUNTER=getIcon(MagicIcon.DEPLETIONCOUNTER);
    public static final ImageIcon MININGCOUNTER=getIcon(MagicIcon.MININGCOUNTER);
    public static final ImageIcon MUSTERCOUNTER=getIcon(MagicIcon.MUSTERCOUNTER);
    public static final ImageIcon TREASURECOUNTER=getIcon(MagicIcon.TREASURECOUNTER);
    public static final ImageIcon STRIFECOUNTER=getIcon(MagicIcon.STRIFECOUNTER);
    public static final ImageIcon STUDYCOUNTER=getIcon(MagicIcon.STUDYCOUNTER);
    public static final ImageIcon TRAPCOUNTER=getIcon(MagicIcon.TRAPCOUNTER);
    public static final ImageIcon SHIELDCOUNTER=getIcon(MagicIcon.SHIELDCOUNTER);
    public static final ImageIcon WISHCOUNTER=getIcon(MagicIcon.WISHCOUNTER);
    public static final ImageIcon SHELLCOUNTER=getIcon(MagicIcon.SHELLCOUNTER);
    public static final ImageIcon BLAZECOUNTER=getIcon(MagicIcon.BLAZECOUNTER);
    public static final ImageIcon TIDECOUNTER=getIcon(MagicIcon.TIDECOUNTER);
    public static final ImageIcon GEMCOUNTER=getIcon(MagicIcon.GEMCOUNTER);
    public static final ImageIcon PRESSURECOUNTER=getIcon(MagicIcon.PRESSURECOUNTER);
    public static final ImageIcon VERSECOUNTER=getIcon(MagicIcon.VERSECOUNTER);
    public static final ImageIcon MUSICCOUNTER=getIcon(MagicIcon.MUSICCOUNTER);

    public static final ImageIcon ANY_MANA=getIcon(MagicIcon.ANY_MANA);

    // Mana icons are extracted from Mana.png sprite sheet.
    private static final BufferedImage MANA = loadImage(ManaSymbol.ICON_SHEET_FILENAME);
    public static final ImageIcon TAPPED = loadSymbolIcon(ManaSymbol.TAPPED, false);
    private static final ImageIcon WHITE = loadSymbolIcon(ManaSymbol.WHITE, true);
    private static final ImageIcon BLUE = loadSymbolIcon(ManaSymbol.BLUE, true);
    private static final ImageIcon BLACK = loadSymbolIcon(ManaSymbol.BLACK, true);
    private static final ImageIcon RED = loadSymbolIcon(ManaSymbol.RED, true);
    private static final ImageIcon GREEN = loadSymbolIcon(ManaSymbol.GREEN, true);
    public static final ImageIcon COST_WHITE = loadSymbolIcon(ManaSymbol.WHITE, false);
    public static final ImageIcon COST_BLUE = loadSymbolIcon(ManaSymbol.BLUE, false);
    public static final ImageIcon COST_BLACK = loadSymbolIcon(ManaSymbol.BLACK, false);
    public static final ImageIcon COST_RED = loadSymbolIcon(ManaSymbol.RED, false);
    public static final ImageIcon COST_GREEN = loadSymbolIcon(ManaSymbol.GREEN, false);
    public static final ImageIcon COST_HYBRID_WHITE = loadSymbolIcon(ManaSymbol.HYBRID_WHITE, false);
    public static final ImageIcon COST_HYBRID_BLUE = loadSymbolIcon(ManaSymbol.HYBRID_BLUE, false);
    public static final ImageIcon COST_HYBRID_BLACK = loadSymbolIcon(ManaSymbol.HYBRID_BLACK, false);
    public static final ImageIcon COST_HYBRID_RED = loadSymbolIcon(ManaSymbol.HYBRID_RED, false);
    public static final ImageIcon COST_HYBRID_GREEN = loadSymbolIcon(ManaSymbol.HYBRID_GREEN, false);
    public static final ImageIcon COST_PHYREXIAN_WHITE = loadSymbolIcon(ManaSymbol.PHYREXIAN_WHITE, false);
    public static final ImageIcon COST_PHYREXIAN_BLUE = loadSymbolIcon(ManaSymbol.PHYREXIAN_BLUE, false);
    public static final ImageIcon COST_PHYREXIAN_BLACK = loadSymbolIcon(ManaSymbol.PHYREXIAN_BLACK, false);
    public static final ImageIcon COST_PHYREXIAN_RED = loadSymbolIcon(ManaSymbol.PHYREXIAN_RED, false);
    public static final ImageIcon COST_PHYREXIAN_GREEN = loadSymbolIcon(ManaSymbol.PHYREXIAN_GREEN, false);
    public static final ImageIcon COST_WHITE_BLUE = loadSymbolIcon(ManaSymbol.WHITE_BLUE, false);
    public static final ImageIcon COST_WHITE_BLACK = loadSymbolIcon(ManaSymbol.WHITE_BLACK, false);
    public static final ImageIcon COST_BLUE_BLACK = loadSymbolIcon(ManaSymbol.BLUE_BLACK, false);
    public static final ImageIcon COST_BLUE_RED = loadSymbolIcon(ManaSymbol.BLUE_RED, false);
    public static final ImageIcon COST_BLACK_RED = loadSymbolIcon(ManaSymbol.BLACK_RED, false);
    public static final ImageIcon COST_BLACK_GREEN = loadSymbolIcon(ManaSymbol.BLACK_GREEN, false);
    public static final ImageIcon COST_RED_WHITE = loadSymbolIcon(ManaSymbol.RED_WHITE, false);
    public static final ImageIcon COST_RED_GREEN = loadSymbolIcon(ManaSymbol.RED_GREEN, false);
    public static final ImageIcon COST_GREEN_WHITE = loadSymbolIcon(ManaSymbol.GREEN_WHITE, false);
    public static final ImageIcon COST_GREEN_BLUE = loadSymbolIcon(ManaSymbol.GREEN_BLUE, false);
    public static final ImageIcon COST_ZERO = loadSymbolIcon(ManaSymbol.ZERO, false);
    public static final ImageIcon COST_ONE = loadSymbolIcon(ManaSymbol.ONE, false);
    public static final ImageIcon COST_TWO = loadSymbolIcon(ManaSymbol.TWO, false);
    public static final ImageIcon COST_THREE = loadSymbolIcon(ManaSymbol.THREE, false);
    public static final ImageIcon COST_FOUR = loadSymbolIcon(ManaSymbol.FOUR, false);
    public static final ImageIcon COST_FIVE = loadSymbolIcon(ManaSymbol.FIVE, false);
    public static final ImageIcon COST_SIX = loadSymbolIcon(ManaSymbol.SIX, false);
    public static final ImageIcon COST_SEVEN = loadSymbolIcon(ManaSymbol.SEVEN, false);
    public static final ImageIcon COST_EIGHT = loadSymbolIcon(ManaSymbol.EIGHT, false);
    public static final ImageIcon COST_NINE = loadSymbolIcon(ManaSymbol.NINE, false);
    public static final ImageIcon COST_TEN = loadSymbolIcon(ManaSymbol.TEN, false);
    public static final ImageIcon COST_ELEVEN = loadSymbolIcon(ManaSymbol.ELEVEN, false);
    public static final ImageIcon COST_TWELVE = loadSymbolIcon(ManaSymbol.TWELVE, false);
    public static final ImageIcon COST_THIRTEEN = loadSymbolIcon(ManaSymbol.THIRTEEN, false);
    public static final ImageIcon COST_FOURTEEN = loadSymbolIcon(ManaSymbol.FOURTEEN, false);
    public static final ImageIcon COST_FIFTEEN = loadSymbolIcon(ManaSymbol.FIFTEEN, false);
    public static final ImageIcon COST_SIXTEEN = loadSymbolIcon(ManaSymbol.SIXTEEN, false);
    public static final ImageIcon COST_X = loadSymbolIcon(ManaSymbol.X, false);

    public static ImageIcon getIcon(final MagicIcon icon) {
        return new ImageIcon(MagicResources.getImageUrl(icon.getFilename()));
    }

    private static BufferedImage loadImage(final String name) {
        System.out.printf("loadImage(%s)\n", name);
        return ImageFileIO.toImg(MagicResources.getImageUrl(name), MISSING2);
    }

    private static BufferedImage loadTextureImage(final String name) {
        System.out.printf("loadTexturedImage(%s)\n", name);
        return ImageFileIO.toImg(MagicResources.getTextureImageUrl(name), MISSING2);
    }

    public static ImageIcon loadSymbolIcon(final int pos, final boolean big) {
        final int imgW = 75;
        final int imgH = 75;
        final int icoW = 15;
        final int icoH = 15;
        final int bigW = 25;
        final int bigH = 25;
        final int row = pos / 10;
        final int col = pos % 10;
        final BufferedImage subimage = MANA.getSubimage(col * imgW, row * imgH, imgW, imgH);
        if (big) {
            return new ImageIcon(magic.ui.GraphicsUtilities.scale(subimage,bigW,bigH));
        } else {
            return new ImageIcon(magic.ui.GraphicsUtilities.scale(subimage,icoW,icoH));
        }
    }

    private static ImageIcon loadSymbolIcon(final ManaSymbol manaSymbol, final boolean big) {
        final int key = manaSymbol.hashCode() + (big ? 1 : 0);
        if (!manaSymbols.containsKey(key)) {
            manaSymbols.put(key, loadSymbolIcon(manaSymbol.getIconIndex(), big));
        }
        return manaSymbols.get(key);
    }

    public static ImageIcon getIcon(final MagicColor c) {
        switch (c) {
            case White: return IconImages.WHITE;
            case Blue: return IconImages.BLUE;
            case Black: return IconImages.BLACK;
            case Green: return IconImages.GREEN;
            case Red: return IconImages.RED;
        }
        throw new RuntimeException("No icon for MagicColor " + c);
    }

    public static ImageIcon getIcon(final MagicPermanent perm) {
        if (perm.isAttacking()) {
            return getIcon(MagicIcon.ATTACK);
        } else if (perm.isBlocking()) {
            return getIcon(MagicIcon.BLOCK);
        } else if (perm.isCreature()) {
            return getIcon(MagicIcon.CREATURE);
        } else {
            return getIcon(perm.getCardDefinition());
        }
    }
    
    public static ImageIcon getIcon(final MagicCardDefinition cdef) {
        if (cdef.isLand()) {
            return getIcon(MagicIcon.LAND);
        } else if (cdef.isCreature()) {
            return getIcon(MagicIcon.CREATURE);
        } else if (cdef.isEquipment()) {
            return getIcon(MagicIcon.EQUIPMENT);
        } else if (cdef.isAura()) {
            return getIcon(MagicIcon.AURA);
        } else if (cdef.isEnchantment()) {
            return getIcon(MagicIcon.ENCHANTMENT);
        } else if (cdef.isArtifact()) {
            return getIcon(MagicIcon.ARTIFACT);
        } else {
            return getIcon(MagicIcon.SPELL);
        }
    }

    public static ImageIcon getIcon(final MagicManaType mtype) {
        switch (mtype) {
            case Colorless:
                return IconImages.COST_ONE;
            case Black:
                return IconImages.COST_BLACK;
            case Blue:
                return IconImages.COST_BLUE;
            case Green:
                return IconImages.COST_GREEN;
            case Red:
                return IconImages.COST_RED;
            case White:
                return IconImages.COST_WHITE;
        }
        throw new RuntimeException("No icon available for MagicManaType " + mtype);
    }
}
