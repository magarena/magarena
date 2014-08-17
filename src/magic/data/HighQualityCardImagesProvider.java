package magic.data;

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
            return IconImages.CARD_BACK;
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

    private BufferedImage getScaledImage(final File imageFile) {
        final String cacheKey = imageFile.getName();        
        if (!scaledImages.containsKey(cacheKey)) {
            final BufferedImage image = GraphicsUtilities.scale(getOriginalImage(imageFile), CARD_WIDTH, CARD_HEIGHT);
            scaledImages.put(cacheKey, image);
            return image;
        } else {
            return scaledImages.get(cacheKey);
        }
    }

    public static CardImagesProvider getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void clearCache() {
        origImages.clear();
        scaledImages.clear();
    }
}
