package magic.ui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;

import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.model.DuelPlayerConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.player.PlayerProfile;
import magic.ui.cardBuilder.renderers.CardBuilder;
import magic.ui.prefs.ImageSizePresets;
import magic.ui.theme.PlayerAvatar;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicResources;

public final class MagicImages {

    private static final Map<MagicIcon, ImageIcon> manaIcons = new HashMap<>();
    private static final Map<MagicIcon, ImageIcon> icons = new HashMap<>();
    private static final Map<String, PlayerAvatar> avatarsMap = new HashMap<>();

    public static final BufferedImage BACK_IMAGE;
    static {
        BufferedImage image = ImageFileIO.toImg(MagicResources.getImageUrl("card-back.jpg"), null);
        Dimension size = getPreferredImageSize(image);
        BACK_IMAGE = GraphicsUtils.scale(image, size.width, size.height);
    }

    // BufferedImages
    public static final BufferedImage MISSING = loadImage("missing.png");
    public static final BufferedImage MISSING2 = loadImage("missing2.png");
    public static final BufferedImage MISSING_CARD = loadImage("missing_card.png");
    
    // "M" logo variations. Based on assumption that logo.png is 512 x 512.
    public static final BufferedImage LOGO = GraphicsUtils.scale(loadImage("logo.png"), 160, 160);
    public static final BufferedImage MENU_LOGO = GraphicsUtils.scale(LOGO, 40, 40);
    public static final BufferedImage APP_LOGO = GraphicsUtils.scale(LOGO, 32, 32);

    // default texture images
    public static final BufferedImage WOOD = loadTextureImage("wood.jpg");
    public static final BufferedImage MARBLE = loadTextureImage("marble.jpg");
    public static final BufferedImage GRANITE = loadTextureImage("granite.jpg");
    public static final BufferedImage GRANITE2 = loadTextureImage("granite2.jpg");
    public static final BufferedImage OPAL = loadTextureImage("opal.jpg");
    public static final BufferedImage OPAL2 = loadTextureImage("opal2.jpg");


    /**
     * Gets preferred viewing size for a card image based on preset setting in preferences.
     * <p>
     * Note the image's aspect ratio is king. If the preferred setting is not the same
     * as the native image's then the image will be resized as close as possible to the
     * preferred size whilst retaining the image's aspect ratio. This should ensure
     * smooth transition between an animated and static image view.
     */
    public static Dimension getPreferredImageSize(Image image) {
        ImageSizePresets preset = GeneralConfig.getInstance().getPreferredImageSize();
        if (preset == ImageSizePresets.SIZE_ORIGINAL) {
            return new Dimension(image.getWidth(null), image.getHeight(null));
        } else {
            Dimension approxPrefSize = preset.getSize();
            // ratio of image width to height.
            double imageRatio = image.getWidth(null) / (double) image.getHeight(null);
            return new Dimension(
                approxPrefSize.width,
                (int)(approxPrefSize.width / imageRatio)
            );
        }
    }

    public static ImageIcon getIcon(MagicIcon icon) {
        if (icon.isManaIcon()) {
            return getSmallManaIcon(icon);
        }
        if (!icons.containsKey(icon)) {
            icons.put(icon, new ImageIcon(MagicResources.getImageUrl(icon.getFilename())));
        }
        return icons.get(icon);
    }

    private static BufferedImage loadImage(String name) {
        return ImageFileIO.toImg(MagicResources.getImageUrl(name), MISSING2);
    }

    private static BufferedImage loadTextureImage(String name) {
        return ImageFileIO.toImg(MagicResources.getTextureImageUrl(name), MISSING2);
    }

    private static ImageIcon getSizedManaIcon(MagicIcon manaIcon, boolean big) {
        return big
                ? new ImageIcon(GraphicsUtils.scale(loadImage(manaIcon.getFilename()), 25, 25))
                : new ImageIcon(GraphicsUtils.scale(loadImage(manaIcon.getFilename()), 15, 15));
    }

    private static ImageIcon getManaIcon(MagicIcon manaIcon, boolean isBigIcon) {
        if (!manaIcons.containsKey(manaIcon)) {
            manaIcons.put(manaIcon, getSizedManaIcon(manaIcon, isBigIcon));
        }
        return getSizedManaIcon(manaIcon, isBigIcon);
    }

    public static ImageIcon getSmallManaIcon(MagicIcon manaIcon) {
        return getManaIcon(manaIcon, false);
    }

    public static ImageIcon getBigManaIcon(MagicIcon manaIcon) {
        return getManaIcon(manaIcon, true);
    }

    public static ImageIcon getIcon(MagicColor c) {
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

    public static ImageIcon getIcon(MagicPermanent perm) {
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

    public static ImageIcon getIcon(MagicCardDefinition cdef) {
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

    public static ImageIcon getIcon(MagicManaType mtype) {
        switch (mtype) {
            case Colorless:
                return getSmallManaIcon(MagicIcon.MANA_COLORLESS);
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

    public static ImageIcon getIcon(String manaSymbol){
        switch (manaSymbol) {
            case "{T}":
                return getIcon(MagicIcon.MANA_TAP);
            case "{Q}":
                return getIcon(MagicIcon.MANA_UNTAP);
            case "{S}":
                return getIcon(MagicIcon.MANA_SNOW);
            case "{C}":
                return getIcon(MagicIcon.MANA_COLORLESS);
            default:
                MagicManaCost mana = MagicManaCost.create(manaSymbol);
                List<MagicIcon> icons = mana.getIcons();
                return getIcon(icons.get(0));
        }
    }


    public static ImageIcon getIconSize1(DuelPlayerConfig playerDef) {
        return getSizedAvatarImageIcon(playerDef, 1);
    }

    public static ImageIcon getIconSize2(DuelPlayerConfig playerDef) {
        return getSizedAvatarImageIcon(playerDef, 2);
    }

    public static ImageIcon getIconSize3(DuelPlayerConfig playerDef) {
        return getSizedAvatarImageIcon(playerDef, 3);
    }

    public static ImageIcon getIconSize4(DuelPlayerConfig playerDef) {
        return getSizedAvatarImageIcon(playerDef, 4);
    }

    private static ImageIcon getSizedAvatarImageIcon(DuelPlayerConfig playerDef, int size) {
        return getPlayerAvatar(playerDef.getProfile()).getIcon(size);
    }

    private static BufferedImage getAvatarImage(PlayerProfile profile) {
        File file = new File(profile.getProfilePath().resolve("player.avatar").toString());
        return file.exists() ? ImageFileIO.toImg(file, MISSING) : MISSING;
    }

    public static PlayerAvatar getPlayerAvatar(PlayerProfile profile) {
        if (GraphicsEnvironment.isHeadless() == false) {
            String key = profile.getId();
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

    public static BufferedImage getMissingCardImage() {
        return MISSING_CARD;
    }

    public static BufferedImage getMissingCardImage(MagicCardDefinition cardDef) {
        return cardDef == MagicCardDefinition.UNKNOWN
            ? MISSING_CARD
            : CardBuilder.getCardBuilderImage(cardDef);
    }

}
