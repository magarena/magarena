package magic.ui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import magic.model.MagicCardDefinition;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicFileSystem;

public class CachedImagesProvider implements CardImagesProvider {

    private static final CardImagesProvider INSTANCE=new CachedImagesProvider();

    private static final int MAX_IMAGES=100;
    private final Map<String,BufferedImage> scaledImages = new magic.data.LRUCache<>(MAX_IMAGES);
    private final Map<String,BufferedImage> origImages = new magic.data.LRUCache<>(MAX_IMAGES);

    private CachedImagesProvider() {}

    @Override
    public BufferedImage getImage(final MagicCardDefinition cardDef, final int index, final boolean orig) {
        if (cardDef == MagicCardDefinition.UNKNOWN) {
            return MagicImages.getMissingCardImage();
        }
        final File imageFile = getImageFile(cardDef, index);
        return orig ? getOriginalImage(imageFile, cardDef) : getScaledImage(imageFile, cardDef);
    }

    private File getImageFile(final MagicCardDefinition cardDef, final int index) {
        final File defaultImage = MagicFileSystem.getCardImageFile(cardDef, index);
        final File customImage = MagicFileSystem.getCustomImagesPath().resolve(defaultImage.getName()).toFile();
        return customImage.exists() ? customImage : defaultImage;
    }

    private BufferedImage getOriginalImage(final File imageFile, final MagicCardDefinition cardDef) {
        final String cacheKey = imageFile.getName();
        if (!origImages.containsKey(cacheKey)) {
            final BufferedImage image = ImageFileIO.toImg(imageFile, () -> MagicImages.getMissingCardImage(cardDef));
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

    private BufferedImage getScaledImage(final File imageFile, final MagicCardDefinition cardDef) {
        return getScaledImage(imageFile.getName(), getOriginalImage(imageFile, cardDef));
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
