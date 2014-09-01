package magic.data;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import magic.model.MagicCardDefinition;
import magic.ui.utility.GraphicsUtilities;
import magic.utility.MagicFileSystem;

/**
 * For a given MagicCardDefinition object returns the corresponding image from
 * the cards directory
 */
public class HighQualityCardImagesProvider implements CardImagesProvider {

    private static final CardImagesProvider INSTANCE=new HighQualityCardImagesProvider();
    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private static final int MAX_IMAGES=100;
    private final Map<String,BufferedImage> scaledImages = new magic.data.LRUCache<>(MAX_IMAGES);
    private final Map<String,BufferedImage> origImages = new magic.data.LRUCache<>(MAX_IMAGES);

    private HighQualityCardImagesProvider() {}

    @Override
    public BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean orig) {

        if (cardDefinition == MagicCardDefinition.MORPH) {
            return getMorphImage(orig);
        }
        if (cardDefinition == MagicCardDefinition.UNKNOWN) {
            return IconImages.MISSING_CARD;
        }
        if (cardDefinition.isMissing()) {
            if (!MagicFileSystem.getCardImageFile(cardDefinition, index).exists()) {
                return IconImages.MISSING_CARD;
            }
        } else if (!cardDefinition.isValid()) {
            return IconImages.MISSING_CARD;
        }        
        final File imageFile = MagicFileSystem.getCardImageFile(cardDefinition, index);
        return orig ? getOriginalImage(imageFile) : getScaledImage(imageFile);
    }

    private BufferedImage getOriginalImage(final File imageFile) {
        final String cacheKey = imageFile.getName();
        if (!origImages.containsKey(cacheKey)) {
            final BufferedImage image = FileIO.toImg(imageFile, IconImages.MISSING_CARD);
            origImages.put(cacheKey, image);
            return image;
        } else {
            return origImages.get(cacheKey);
        }
    }

    private BufferedImage getScaledImage(final String cacheKey, final BufferedImage sourceImage) {
        if (!scaledImages.containsKey(cacheKey)) {
            final Dimension imageSize = CONFIG.getMaxCardImageSize();
            final BufferedImage image = GraphicsUtilities.scale(sourceImage, imageSize.width, imageSize.height);
            scaledImages.put(cacheKey, image);
            return image;
        } else {
            return scaledImages.get(cacheKey);
        }
    }

    private BufferedImage getScaledImage(final File imageFile) {
        return getScaledImage(imageFile.getName(), getOriginalImage(imageFile));
    }

    public static CardImagesProvider getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void clearCache() {
        origImages.clear();
        scaledImages.clear();
    }

    private BufferedImage getMorphImage(final boolean orig) {
        if (orig) {
            return IconImages.CARD_BACK;
        } else {
            return getScaledImage("mtgCardBackFace", IconImages.CARD_BACK);
        }
    }

}
