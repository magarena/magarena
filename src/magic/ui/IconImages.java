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
    
    private static final Map<Integer, ImageIcon> manaIcons = new HashMap<>();
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
    public static final ImageIcon TAPPED = getSmallManaIcon(MagicIcon.TAPPED);
    private static final ImageIcon WHITE = getBigManaIcon(MagicIcon.WHITE);
    private static final ImageIcon BLUE = getBigManaIcon(MagicIcon.BLUE);
    private static final ImageIcon BLACK = getBigManaIcon(MagicIcon.BLACK);
    private static final ImageIcon RED = getBigManaIcon(MagicIcon.RED);
    private static final ImageIcon GREEN = getBigManaIcon(MagicIcon.GREEN);
    public static final ImageIcon COST_WHITE = getSmallManaIcon(MagicIcon.WHITE);
    public static final ImageIcon COST_BLUE = getSmallManaIcon(MagicIcon.BLUE);
    public static final ImageIcon COST_BLACK = getSmallManaIcon(MagicIcon.BLACK);
    public static final ImageIcon COST_RED = getSmallManaIcon(MagicIcon.RED);
    public static final ImageIcon COST_GREEN = getSmallManaIcon(MagicIcon.GREEN);
    public static final ImageIcon COST_HYBRID_WHITE = getSmallManaIcon(MagicIcon.HYBRID_WHITE);
    public static final ImageIcon COST_HYBRID_BLUE = getSmallManaIcon(MagicIcon.HYBRID_BLUE);
    public static final ImageIcon COST_HYBRID_BLACK = getSmallManaIcon(MagicIcon.HYBRID_BLACK);
    public static final ImageIcon COST_HYBRID_RED = getSmallManaIcon(MagicIcon.HYBRID_RED);
    public static final ImageIcon COST_HYBRID_GREEN = getSmallManaIcon(MagicIcon.HYBRID_GREEN);
    public static final ImageIcon COST_PHYREXIAN_WHITE = getSmallManaIcon(MagicIcon.PHYREXIAN_WHITE);
    public static final ImageIcon COST_PHYREXIAN_BLUE = getSmallManaIcon(MagicIcon.PHYREXIAN_BLUE);
    public static final ImageIcon COST_PHYREXIAN_BLACK = getSmallManaIcon(MagicIcon.PHYREXIAN_BLACK);
    public static final ImageIcon COST_PHYREXIAN_RED = getSmallManaIcon(MagicIcon.PHYREXIAN_RED);
    public static final ImageIcon COST_PHYREXIAN_GREEN = getSmallManaIcon(MagicIcon.PHYREXIAN_GREEN);
    public static final ImageIcon COST_WHITE_BLUE = getSmallManaIcon(MagicIcon.WHITE_BLUE);
    public static final ImageIcon COST_WHITE_BLACK = getSmallManaIcon(MagicIcon.WHITE_BLACK);
    public static final ImageIcon COST_BLUE_BLACK = getSmallManaIcon(MagicIcon.BLUE_BLACK);
    public static final ImageIcon COST_BLUE_RED = getSmallManaIcon(MagicIcon.BLUE_RED);
    public static final ImageIcon COST_BLACK_RED = getSmallManaIcon(MagicIcon.BLACK_RED);
    public static final ImageIcon COST_BLACK_GREEN = getSmallManaIcon(MagicIcon.BLACK_GREEN);
    public static final ImageIcon COST_RED_WHITE = getSmallManaIcon(MagicIcon.RED_WHITE);
    public static final ImageIcon COST_RED_GREEN = getSmallManaIcon(MagicIcon.RED_GREEN);
    public static final ImageIcon COST_GREEN_WHITE = getSmallManaIcon(MagicIcon.GREEN_WHITE);
    public static final ImageIcon COST_GREEN_BLUE = getSmallManaIcon(MagicIcon.GREEN_BLUE);
    public static final ImageIcon COST_ZERO = getSmallManaIcon(MagicIcon.ZERO);
    public static final ImageIcon COST_ONE = getSmallManaIcon(MagicIcon.ONE);
    public static final ImageIcon COST_TWO = getSmallManaIcon(MagicIcon.TWO);
    public static final ImageIcon COST_THREE = getSmallManaIcon(MagicIcon.THREE);
    public static final ImageIcon COST_FOUR = getSmallManaIcon(MagicIcon.FOUR);
    public static final ImageIcon COST_FIVE = getSmallManaIcon(MagicIcon.FIVE);
    public static final ImageIcon COST_SIX = getSmallManaIcon(MagicIcon.SIX);
    public static final ImageIcon COST_SEVEN = getSmallManaIcon(MagicIcon.SEVEN);
    public static final ImageIcon COST_EIGHT = getSmallManaIcon(MagicIcon.EIGHT);
    public static final ImageIcon COST_NINE = getSmallManaIcon(MagicIcon.NINE);
    public static final ImageIcon COST_TEN = getSmallManaIcon(MagicIcon.TEN);
    public static final ImageIcon COST_ELEVEN = getSmallManaIcon(MagicIcon.ELEVEN);
    public static final ImageIcon COST_TWELVE = getSmallManaIcon(MagicIcon.TWELVE);
    public static final ImageIcon COST_THIRTEEN = getSmallManaIcon(MagicIcon.THIRTEEN);
    public static final ImageIcon COST_FOURTEEN = getSmallManaIcon(MagicIcon.FOURTEEN);
    public static final ImageIcon COST_FIFTEEN = getSmallManaIcon(MagicIcon.FIFTEEN);
    public static final ImageIcon COST_SIXTEEN = getSmallManaIcon(MagicIcon.SIXTEEN);
    public static final ImageIcon COST_X = getSmallManaIcon(MagicIcon.X);

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

    private static ImageIcon getSizedManaIcon(final int pos, final boolean big) {
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

    private static ImageIcon getManaIcon(final MagicIcon manaIcon, final boolean isBigIcon) {
        final int key = manaIcon.hashCode() + (isBigIcon ? 1 : 0);
        if (!manaIcons.containsKey(key)) {
            manaIcons.put(key, getSizedManaIcon(manaIcon.getIconIndex(), isBigIcon));
        }
        return manaIcons.get(key);
    }

    public static ImageIcon getSmallManaIcon(final MagicIcon manaIcon) {
        return getManaIcon(manaIcon, false);
    }

    public static ImageIcon getBigManaIcon(final MagicIcon manaIcon) {
        return getManaIcon(manaIcon, true);
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
