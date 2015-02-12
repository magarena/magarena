package magic.ui;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import magic.data.MagicIcon;
import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.MagicCardDefinition;
import magic.model.MagicPermanent;
import magic.utility.MagicResources;

public final class IconImages {
    
    private static final Map<Integer, ImageIcon> manaSymbols = new HashMap<>();
    private static final Map<MagicIcon, ImageIcon> icons = new HashMap<>();

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

    // Mana icons are extracted from Mana.png sprite sheet.
    private static final BufferedImage MANA = loadImage(MagicIcon.MANA_ICON_SHEET);
    public static final ImageIcon TAPPED = loadSymbolIcon(MagicIcon.TAPPED, false);
    private static final ImageIcon WHITE = loadSymbolIcon(MagicIcon.WHITE, true);
    private static final ImageIcon BLUE = loadSymbolIcon(MagicIcon.BLUE, true);
    private static final ImageIcon BLACK = loadSymbolIcon(MagicIcon.BLACK, true);
    private static final ImageIcon RED = loadSymbolIcon(MagicIcon.RED, true);
    private static final ImageIcon GREEN = loadSymbolIcon(MagicIcon.GREEN, true);
    public static final ImageIcon COST_WHITE = loadSymbolIcon(MagicIcon.WHITE, false);
    public static final ImageIcon COST_BLUE = loadSymbolIcon(MagicIcon.BLUE, false);
    public static final ImageIcon COST_BLACK = loadSymbolIcon(MagicIcon.BLACK, false);
    public static final ImageIcon COST_RED = loadSymbolIcon(MagicIcon.RED, false);
    public static final ImageIcon COST_GREEN = loadSymbolIcon(MagicIcon.GREEN, false);
    public static final ImageIcon COST_HYBRID_WHITE = loadSymbolIcon(MagicIcon.HYBRID_WHITE, false);
    public static final ImageIcon COST_HYBRID_BLUE = loadSymbolIcon(MagicIcon.HYBRID_BLUE, false);
    public static final ImageIcon COST_HYBRID_BLACK = loadSymbolIcon(MagicIcon.HYBRID_BLACK, false);
    public static final ImageIcon COST_HYBRID_RED = loadSymbolIcon(MagicIcon.HYBRID_RED, false);
    public static final ImageIcon COST_HYBRID_GREEN = loadSymbolIcon(MagicIcon.HYBRID_GREEN, false);
    public static final ImageIcon COST_PHYREXIAN_WHITE = loadSymbolIcon(MagicIcon.PHYREXIAN_WHITE, false);
    public static final ImageIcon COST_PHYREXIAN_BLUE = loadSymbolIcon(MagicIcon.PHYREXIAN_BLUE, false);
    public static final ImageIcon COST_PHYREXIAN_BLACK = loadSymbolIcon(MagicIcon.PHYREXIAN_BLACK, false);
    public static final ImageIcon COST_PHYREXIAN_RED = loadSymbolIcon(MagicIcon.PHYREXIAN_RED, false);
    public static final ImageIcon COST_PHYREXIAN_GREEN = loadSymbolIcon(MagicIcon.PHYREXIAN_GREEN, false);
    public static final ImageIcon COST_WHITE_BLUE = loadSymbolIcon(MagicIcon.WHITE_BLUE, false);
    public static final ImageIcon COST_WHITE_BLACK = loadSymbolIcon(MagicIcon.WHITE_BLACK, false);
    public static final ImageIcon COST_BLUE_BLACK = loadSymbolIcon(MagicIcon.BLUE_BLACK, false);
    public static final ImageIcon COST_BLUE_RED = loadSymbolIcon(MagicIcon.BLUE_RED, false);
    public static final ImageIcon COST_BLACK_RED = loadSymbolIcon(MagicIcon.BLACK_RED, false);
    public static final ImageIcon COST_BLACK_GREEN = loadSymbolIcon(MagicIcon.BLACK_GREEN, false);
    public static final ImageIcon COST_RED_WHITE = loadSymbolIcon(MagicIcon.RED_WHITE, false);
    public static final ImageIcon COST_RED_GREEN = loadSymbolIcon(MagicIcon.RED_GREEN, false);
    public static final ImageIcon COST_GREEN_WHITE = loadSymbolIcon(MagicIcon.GREEN_WHITE, false);
    public static final ImageIcon COST_GREEN_BLUE = loadSymbolIcon(MagicIcon.GREEN_BLUE, false);
    public static final ImageIcon COST_ZERO = loadSymbolIcon(MagicIcon.ZERO, false);
    public static final ImageIcon COST_ONE = loadSymbolIcon(MagicIcon.ONE, false);
    public static final ImageIcon COST_TWO = loadSymbolIcon(MagicIcon.TWO, false);
    public static final ImageIcon COST_THREE = loadSymbolIcon(MagicIcon.THREE, false);
    public static final ImageIcon COST_FOUR = loadSymbolIcon(MagicIcon.FOUR, false);
    public static final ImageIcon COST_FIVE = loadSymbolIcon(MagicIcon.FIVE, false);
    public static final ImageIcon COST_SIX = loadSymbolIcon(MagicIcon.SIX, false);
    public static final ImageIcon COST_SEVEN = loadSymbolIcon(MagicIcon.SEVEN, false);
    public static final ImageIcon COST_EIGHT = loadSymbolIcon(MagicIcon.EIGHT, false);
    public static final ImageIcon COST_NINE = loadSymbolIcon(MagicIcon.NINE, false);
    public static final ImageIcon COST_TEN = loadSymbolIcon(MagicIcon.TEN, false);
    public static final ImageIcon COST_ELEVEN = loadSymbolIcon(MagicIcon.ELEVEN, false);
    public static final ImageIcon COST_TWELVE = loadSymbolIcon(MagicIcon.TWELVE, false);
    public static final ImageIcon COST_THIRTEEN = loadSymbolIcon(MagicIcon.THIRTEEN, false);
    public static final ImageIcon COST_FOURTEEN = loadSymbolIcon(MagicIcon.FOURTEEN, false);
    public static final ImageIcon COST_FIFTEEN = loadSymbolIcon(MagicIcon.FIFTEEN, false);
    public static final ImageIcon COST_SIXTEEN = loadSymbolIcon(MagicIcon.SIXTEEN, false);
    public static final ImageIcon COST_X = loadSymbolIcon(MagicIcon.X, false);

    public static ImageIcon getIcon(final MagicIcon icon) {
        if (!icons.containsKey(icon)) {
            icons.put(icon, new ImageIcon(MagicResources.getImageUrl(icon.getFilename())));
        }
        return icons.get(icon);
    }

    private static BufferedImage loadImage(final String name) {
        return ImageFileIO.toImg(MagicResources.getImageUrl(name), MISSING2);
    }

    private static BufferedImage loadTextureImage(final String name) {
        return ImageFileIO.toImg(MagicResources.getTextureImageUrl(name), MISSING2);
    }

    private static ImageIcon loadSymbolIcon(final int pos, final boolean big) {
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

    private static ImageIcon loadSymbolIcon(final MagicIcon manaSymbol, final boolean big) {
        final int key = manaSymbol.hashCode() + (big ? 1 : 0);
        if (!manaSymbols.containsKey(key)) {
            manaSymbols.put(key, loadSymbolIcon(manaSymbol.getIconIndex(), big));
        }
        return manaSymbols.get(key);
    }

    public static ImageIcon loadSymbolIcon(final MagicIcon manaSymbol) {
        return loadSymbolIcon(manaSymbol, false);
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
