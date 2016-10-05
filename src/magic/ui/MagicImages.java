package magic.ui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

import magic.cardBuilder.renderers.CardBuilder;
import magic.data.CardImageFile;
import magic.data.GeneralConfig;
import magic.data.LRUCache;
import magic.data.MagicIcon;
import magic.data.TextImages;
import magic.model.DuelPlayerConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.player.PlayerProfile;
import magic.ui.dialog.prefs.ImageSizePresets;
import magic.ui.theme.PlayerAvatar;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicFileSystem;
import magic.utility.MagicResources;

public final class MagicImages {

    public static final BufferedImage BACK_IMAGE;
    private static Proxy proxy;

    static {
        BufferedImage image = ImageFileIO.toImg(MagicResources.getImageUrl("card-back.jpg"), null);
        Dimension size = getPreferredImageSize(image);
        BACK_IMAGE = GraphicsUtils.scale(image, size.width, size.height);
    }

    // when the preferred image or icon is missing.
    public static final BufferedImage MISSING_BIG = loadImage("missing.png");
    private static final BufferedImage MISSING_SMALL = loadImage("missing2.png");
    public static final BufferedImage MISSING_CARD = BACK_IMAGE;

    // "M" logo variations.
    public static final BufferedImage LOGO = loadImage("logo.png");
    public static final BufferedImage MENU_LOGO = GraphicsUtils.scale(LOGO, 40, 40);
    public static final BufferedImage APP_LOGO = GraphicsUtils.scale(LOGO, 32, 32);

    // About
    public static final BufferedImage ABOUT_LOGO = loadImage("magarena-logo.png");

    // default texture images
    public static final BufferedImage WOOD = loadTextureImage("wood.jpg");
    public static final BufferedImage MARBLE = loadTextureImage("marble.jpg");
    public static final BufferedImage GRANITE = loadTextureImage("granite.jpg");
    public static final BufferedImage GRANITE2 = loadTextureImage("granite2.jpg");
    public static final BufferedImage OPAL = loadTextureImage("opal.jpg");
    public static final BufferedImage OPAL2 = loadTextureImage("opal2.jpg");

    private static final Map<MagicIcon, ImageIcon> smallManaIcons = new HashMap<>();
    private static final Map<MagicIcon, ImageIcon> bigManaIcons = new HashMap<>();
    private static final Map<MagicIcon, ImageIcon> icons = new HashMap<>();
    private static final Map<String, PlayerAvatar> avatarsMap = new HashMap<>();

    private static final int MAX_IMAGES = 100;
    private static final Map<String, BufferedImage> cache = new LRUCache<>(MAX_IMAGES);
    private static final Map<Long, BufferedImage> permanentCache = new LRUCache<>(MAX_IMAGES);

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

    private static BufferedImage loadManaImage(MagicIcon manaIcon) {
        return ImageFileIO.toImg(MagicResources.getManaImageUrl(manaIcon), MISSING_SMALL);
    }

    public static BufferedImage loadImage(String name) {
        return ImageFileIO.toImg(MagicResources.getImageUrl(name), MISSING_SMALL);
    }

    private static BufferedImage loadTextureImage(String name) {
        return ImageFileIO.toImg(MagicResources.getTextureImageUrl(name), MISSING_SMALL);
    }

    public static ImageIcon getSmallManaIcon(MagicIcon manaIcon) {
        if (!smallManaIcons.containsKey(manaIcon)) {
            Image image = GraphicsUtils.scale(loadManaImage(manaIcon), 15, 15);
            smallManaIcons.put(manaIcon, new ImageIcon(image));
        }
        return smallManaIcons.get(manaIcon);
    }

    public static ImageIcon getBigManaIcon(MagicIcon manaIcon) {
        if (!bigManaIcons.containsKey(manaIcon)) {
            Image image = GraphicsUtils.scale(loadManaImage(manaIcon), 25, 25);
            bigManaIcons.put(manaIcon, new ImageIcon(image));
        }
        return bigManaIcons.get(manaIcon);
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

    public static ImageIcon getIcon(MagicCardDefinition cdef) {
        if (cdef.getTypes().size()>1) {
            return getIcon(MagicIcon.MULTIPLE);
        } else if (cdef.isLand()) {
            return getIcon(MagicIcon.LAND);
        } else if (cdef.isCreature()) {
            return getIcon(MagicIcon.CREATURE);
        } else if (cdef.isArtifact()) {
            return getIcon(MagicIcon.ARTIFACT);
        } else if (cdef.isEnchantment()) {
            return getIcon(MagicIcon.ENCHANTMENT);
        } else if (cdef.isInstant()) {
            return getIcon(MagicIcon.INSTANT);
        } else if (cdef.isSorcery()) {
            return getIcon(MagicIcon.SORCERY);
        } else {
            return getIcon(MagicIcon.PLANESWALKER);
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

    public static ImageIcon getIcon(String manaSymbol) {
        return getIcon(TextImages.getIcon(manaSymbol));
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
        return file.exists() ? ImageFileIO.toImg(file, MISSING_BIG) : MISSING_BIG;
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

    private static void tryDownloadingImage(MagicCardDefinition aCard) {
        if (proxy == null) {
            proxy = GeneralConfig.getInstance().getProxy();
        }
        try {
            CardImageFile cif = new CardImageFile(aCard);
            cif.doDownload(proxy);
        } catch (IOException ex) {
            System.err.println(aCard.getDistinctName() + " : " + ex);
        }
    }

    private static BufferedImage createCardImage(MagicCardDefinition aCard) {

        if (aCard == null || aCard == MagicCardDefinition.UNKNOWN) {
            return getMissingCardImage();
        }

        if (MagicFileSystem.getCustomCardImageFile(aCard).exists()) {
            return ImageFileIO.getOptimizedImage(MagicFileSystem.getCustomCardImageFile(aCard));
        }

        if (MagicFileSystem.getCroppedCardImageFile(aCard).exists()) {
            return CardBuilder.getCardBuilderImage(aCard);
        }

        if (GeneralConfig.getInstance().getImagesOnDemand() && !MagicFileSystem.getCardImageFile(aCard).exists()) {
            tryDownloadingImage(aCard);
        }

        if (MagicFileSystem.getCardImageFile(aCard).exists()) {
            return ImageFileIO.getOptimizedImage(MagicFileSystem.getCardImageFile(aCard));
        }

        // else get missing image proxy...
        return getMissingCardImage(aCard);
    }

    private static BufferedImage createPermanentImage(MagicPermanent permanent) {

        final MagicCardDefinition aCard = permanent.getCardDefinition();

        if (permanent == null || permanent.getCardDefinition() == MagicCardDefinition.UNKNOWN) {
            return getMissingCardImage();
        }

        if (MagicFileSystem.getCustomCardImageFile(aCard).exists()) {
            return ImageFileIO.getOptimizedImage(MagicFileSystem.getCustomCardImageFile(aCard));
        }

        if (MagicFileSystem.getCroppedCardImageFile(aCard).exists()) {
            return CardBuilder.getCardBuilderImage(permanent);
        }

        if (GeneralConfig.getInstance().getImagesOnDemand() && !MagicFileSystem.getCardImageFile(aCard).exists()) {
            tryDownloadingImage(aCard);
        }

        if (MagicFileSystem.getCardImageFile(aCard).exists()) {
            return ImageFileIO.getOptimizedImage(MagicFileSystem.getCardImageFile(aCard));
        }

        // else get missing image proxy...
        return CardBuilder.getCardBuilderImage(permanent);
    }


    public static boolean isProxyImage(MagicCardDefinition aCard) {

        if (aCard == null || aCard == MagicCardDefinition.UNKNOWN) {
            return false;
        }

        if (MagicFileSystem.getCustomCardImageFile(aCard).exists()) {
            return false;
        }

        if (MagicFileSystem.getCroppedCardImageFile(aCard).exists()) {
            return true;
        }

        if (MagicFileSystem.getCardImageFile(aCard).exists()) {
            return false;
        }

        // else missing image proxy...
        return true;
    }

    public static BufferedImage getCardImage(MagicCardDefinition aCard) {
        final String key = aCard.getDistinctName();
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        final BufferedImage image = createCardImage(aCard);
        if (image != MISSING_CARD) {
            cache.put(key, image);
        }
        return image;
    }

    public static BufferedImage getPermanentImage(MagicPermanent permanent) {
        final Long key = permanent.getStateId();
        if (permanentCache.containsKey(key)) {
            return permanentCache.get(key);
        }
        final BufferedImage image = createPermanentImage(permanent);
        if (image != MISSING_CARD) {
            permanentCache.put(key, image);
        }
        return image;
    }

    public static boolean hasProxyImage(MagicDeck aDeck) {
        return aDeck.stream().anyMatch(card -> MagicImages.isProxyImage(card));
    }

    public static void clearCache() {
        cache.clear();
    }

}
