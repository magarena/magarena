package magic.ui;

import magic.ui.utility.GraphicsUtils;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;

/**
 * For a given MagicCardDefinition object returns the corresponding image from
 * the cards directory
 */
public class CachedImagesProvider implements CardImagesProvider {

    private static final CardImagesProvider INSTANCE=new CachedImagesProvider();

    private static final int MAX_IMAGES=100;
    private final Map<String,BufferedImage> scaledImages = new magic.data.LRUCache<>(MAX_IMAGES);
    private final Map<String,BufferedImage> origImages = new magic.data.LRUCache<>(MAX_IMAGES);

    private CachedImagesProvider() {}

    @Override
    public BufferedImage getImage(final MagicCardDefinition cardDef, final int index, final boolean orig) {
        if (cardDef == MagicCardDefinition.UNKNOWN) {
            return IconImages.getMissingCardImage();
        }
        if (cardDef.isInvalid()) {
            if (!MagicFileSystem.getCardImageFile(cardDef, index).exists()) {
                return IconImages.getMissingCardImage();
            }
        } else if (!cardDef.isValid()) {
            return IconImages.getMissingCardImage();
        }        
        final File imageFile = MagicFileSystem.getCardImageFile(cardDef, index);
        return orig ? getOriginalImage(imageFile) : getScaledImage(imageFile);
    }

    private BufferedImage getOriginalImage(final File imageFile) {
        final String cacheKey = imageFile.getName();
        if (!origImages.containsKey(cacheKey)) {
            final BufferedImage image = ImageFileIO.toImg(imageFile, IconImages.getMissingCardImage());
            origImages.put(cacheKey, image);
            return image;
        } else {
            return origImages.get(cacheKey);
        }
    }

    private BufferedImage getScaledImage(final String cacheKey, final BufferedImage sourceImage) {
        if (!scaledImages.containsKey(cacheKey)) {
            final Dimension imageSize = GraphicsUtils.getMaxCardImageSize();
            final BufferedImage image = GraphicsUtils.scale(sourceImage, imageSize.width, imageSize.height);
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

}
