package magic.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import magic.cardBuilder.renderers.CardBuilder;
import magic.data.GeneralConfig;
import magic.exception.DownloadException;
import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.ui.screen.images.download.CardImageDisplayMode;
import magic.utility.MagicFileSystem;

/**
 * Handles card images only via IRenderableCard.
 */
public final class MagicCardImages {

    private enum ImageType {
        UNKNOWN,
        CUSTOM,
        PROXY,
        PRINTED
    }

    private static final String[] CUSTOM_IMAGE_ENDINGS = new String[]{".jpg", ".full.jpg"};

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private static File getCustomCardImageFile(IRenderableCard face, String ending) {
        Path imagesFolder = MagicFileSystem.getImagesPath(MagicFileSystem.ImagesPath.CUSTOM);
        return new File(imagesFolder.toFile(), face.getImageName() + ending);
    }

    public static boolean isCustomCardImageFound(IRenderableCard face) {
        return Arrays.stream(CUSTOM_IMAGE_ENDINGS)
            .anyMatch(ending -> getCustomCardImageFile(face, ending).exists());
    }

    private static BufferedImage getCustomCardImage(IRenderableCard face) {
        return Arrays.stream(CUSTOM_IMAGE_ENDINGS)
            .map(ending -> getCustomCardImageFile(face, ending))
            .filter(f -> f.exists())
            .map(f -> ImageFileIO.getOptimizedImage(f))
            .findFirst()
            .orElse(MagicImages.MISSING_CARD);
    }

    private static boolean printedImageExists(IRenderableCard face) {
        return MagicFileSystem.getPrintedCardImage(face).exists();
    }

    private static ImageType getImageType(IRenderableCard face) {

        if (face.isUnknown()) {
            return ImageType.UNKNOWN;
        }

        if (isCustomCardImageFound(face)) {
            return ImageType.CUSTOM;
        }

        if (CONFIG.getCardImageDisplayMode() == CardImageDisplayMode.PROXY
            && !face.isPlaneswalker() && !face.isFlipCard() && !face.isToken()) {
            return ImageType.PROXY;
        }

        if (printedImageExists(face) || CONFIG.getImagesOnDemand()) {
            return ImageType.PRINTED;
        }

        // else missing image proxy...
        return ImageType.PROXY;
    }

    private static BufferedImage getPrintedCardImage(IRenderableCard face) {
        try {
            return PrintedCardImage.get(face);
        } catch (DownloadException ex) {
            System.err.println(ex.toString());
            return CardBuilder.getCardBuilderImage(face);
        }
    }

    private static BufferedImage getProxyCardImage(IRenderableCard face) {
        try {
            return ProxyCardImage.get(face);
        } catch (DownloadException ex) {
            System.err.println(ex.toString());
            return CardBuilder.getCardBuilderImage(face);
        }
    }

    public static BufferedImage createImage(IRenderableCard face) {
        final ImageType type = getImageType(face);
        switch (type) {
            case UNKNOWN: return MagicImages.MISSING_CARD;
            case CUSTOM:  return getCustomCardImage(face);
            case PROXY:   return getProxyCardImage(face);
            case PRINTED: return getPrintedCardImage(face);
        }
        throw new RuntimeException("Unsupported image type: " + type);
    }

    public static boolean isProxyImage(IRenderableCard face) {
        return getImageType(face) == ImageType.PROXY;
    }

    public static boolean isPrintedCardImageMissing(MagicCardDefinition card) {
        return !MagicFileSystem.getPrintedCardImage(card).exists();
    }

    private static boolean customCardImageMissing(MagicCardDefinition card) {
        return !isCustomCardImageFound(card);
    }

    public static boolean isCroppedCardImageMissing(MagicCardDefinition card) {
        return !MagicFileSystem.getCroppedCardImage(card).exists();
    }

    public static boolean isCardImageMissing(MagicCardDefinition aCard) {
        if (CONFIG.getCardImageDisplayMode() == CardImageDisplayMode.PRINTED) {
            return customCardImageMissing(aCard)
                && isPrintedCardImageMissing(aCard);
        } else { // PROXY
            return customCardImageMissing(aCard)
                && isCroppedCardImageMissing(aCard)
                && isPrintedCardImageMissing(aCard);
        }
    }

    private MagicCardImages() {}
}
