package magic.ui;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import magic.data.MagicIcon;
import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.MagicCardDefinition;
import magic.model.MagicPermanent;
import magic.model.DuelPlayerConfig;
import magic.model.player.PlayerProfile;
import magic.ui.theme.PlayerAvatar;
import magic.utility.MagicResources;

public final class IconImages {

    private static final Map<Integer, ImageIcon> manaIcons = new HashMap<>();
    private static final Map<MagicIcon, ImageIcon> icons = new HashMap<>();
    private static final Map<String, PlayerAvatar> avatarsMap = new HashMap<>();

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

    private static final BufferedImage MANA_ICON_SHEET = loadImage(MagicIcon.MANA_ICON_SHEET);

    public static ImageIcon getIcon(final MagicIcon icon) {
        if (icon.isManaIcon()) {
            return getSmallManaIcon(icon);
        } else if (!icons.containsKey(icon)) {
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
        final BufferedImage subimage = MANA_ICON_SHEET.getSubimage(col * imgW, row * imgH, imgW, imgH);
        if (big) {
            return new ImageIcon(magic.ui.utility.GraphicsUtils.scale(subimage,bigW,bigH));
        } else {
            return new ImageIcon(magic.ui.utility.GraphicsUtils.scale(subimage,icoW,icoH));
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
            case White:
                return getBigManaIcon(MagicIcon.MANA_WHITE);
            case Blue:
                return getBigManaIcon(MagicIcon.MANA_BLUE);
            case Black:
                return getBigManaIcon(MagicIcon.MANA_BLACK);
            case Green:
                return getBigManaIcon(MagicIcon.MANA_GREEN);
            case Red:
                return getBigManaIcon(MagicIcon.MANA_RED);
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
                return getSmallManaIcon(MagicIcon.MANA_1);
            case Snow:
                return getSmallManaIcon(MagicIcon.MANA_SNOW);
            case Black:
                return getSmallManaIcon(MagicIcon.MANA_BLACK);
            case Blue:
                return getSmallManaIcon(MagicIcon.MANA_BLUE);
            case Green:
                return getSmallManaIcon(MagicIcon.MANA_GREEN);
            case Red:
                return getSmallManaIcon(MagicIcon.MANA_RED);
            case White:
                return getSmallManaIcon(MagicIcon.MANA_WHITE);
        }
        throw new RuntimeException("No icon available for MagicManaType " + mtype);
    }

    public static ImageIcon getIconSize1(final DuelPlayerConfig playerDef) {
        return getSizedAvatarImageIcon(playerDef, 1);
    }

    public static ImageIcon getIconSize2(final DuelPlayerConfig playerDef) {
        return getSizedAvatarImageIcon(playerDef, 2);
    }

    public static ImageIcon getIconSize3(final DuelPlayerConfig playerDef) {
        return getSizedAvatarImageIcon(playerDef, 3);
    }

    public static ImageIcon getIconSize4(final DuelPlayerConfig playerDef) {
        return getSizedAvatarImageIcon(playerDef, 4);
    }

    private static ImageIcon getSizedAvatarImageIcon(final DuelPlayerConfig playerDef, final int size) {
        return getPlayerAvatar(playerDef.getProfile()).getIcon(size);
    }

    private static BufferedImage getAvatarImage(final PlayerProfile profile) {
        final File file = new File(profile.getProfilePath().resolve("player.avatar").toString());
        if (file.exists()) {
            return ImageFileIO.toImg(file, IconImages.MISSING);
        } else {
            return IconImages.MISSING;
        }
    }

    public static PlayerAvatar getPlayerAvatar(final PlayerProfile profile) {
        if (java.awt.GraphicsEnvironment.isHeadless() == false) {
            final String key = profile.getId();
            if (!avatarsMap.containsKey(key)) {
                avatarsMap.put(key, new PlayerAvatar(getAvatarImage(profile)));
            }
            return avatarsMap.get(key);
        } else {
            return null;
        }
    }

    public static void getClearAvatarsCache() {
        avatarsMap.clear();
    }
}
