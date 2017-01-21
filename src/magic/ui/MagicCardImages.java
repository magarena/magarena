package magic.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Path;
import java.util.Arrays;
import magic.cardBuilder.renderers.CardBuilder;
import magic.data.CardImageFile;
import magic.data.GeneralConfig;
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
    private static Proxy proxy;

    private static File getCustomCardImageFile(IRenderableCard face, String ending) {
        Path imagesFolder = MagicFileSystem.getImagesPath(MagicFileSystem.ImagesPath.CUSTOM);
        return new File(imagesFolder.toFile(), face.getImageName() + ending);
    }

    private static boolean customCardImageExists(IRenderableCard face) {
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

    private static ImageType getImageType(IRenderableCard face) {

        if (face.isUnknown()) {
            return ImageType.UNKNOWN;
        }

        if (customCardImageExists(face)) {
            return ImageType.CUSTOM;
        }

        if (CONFIG.getCardImageDisplayMode() == CardImageDisplayMode.PROXY) {
            return ImageType.PROXY;
        }

        if (MagicFileSystem.getPrintedCardImage(face).exists() || CONFIG.getImagesOnDemand()) {
            return ImageType.PRINTED;
        }

        // else missing image proxy...
        return ImageType.PROXY;
    }

    private static void tryDownloadingPrintedImage(IRenderableCard face) {
        if (proxy == null) {
            proxy = CONFIG.getProxy();
        }
        try {
            CardImageFile cif = new CardImageFile(face);
            cif.doDownload(proxy);
        } catch (IOException ex) {
            System.err.println(face.getCardDefinition().getDistinctName() + " : " + ex);
        }
    }

    public static BufferedImage createImage(IRenderableCard face) {
        final ImageType type = getImageType(face);
        switch (type) {
            case UNKNOWN:
                return MagicImages.MISSING_CARD;
            case CUSTOM:
                return getCustomCardImage(face);
            case PROXY:
                return CardBuilder.getCardBuilderImage(face);
            case PRINTED:
                if (!MagicFileSystem.getPrintedCardImage(face).exists()) {
                    // on-demand image download.
                    tryDownloadingPrintedImage(face);
                }
                if (MagicFileSystem.getPrintedCardImage(face).exists()) {
                    if (CONFIG.getCardImageDisplayMode() == CardImageDisplayMode.PRINTED || CONFIG.getCardTextLanguage() != CardTextLanguage.ENGLISH) {
                        return ImageFileIO.getOptimizedImage(MagicFileSystem.getPrintedCardImage(face));
                    } else {
                        return face.isPlaneswalker() || face.isFlipCard() || face.isToken()
                            ? ImageFileIO.getOptimizedImage(MagicFileSystem.getPrintedCardImage(face))
                            : CardBuilder.getCardBuilderImage(face);
                    }
                }
        }
        return CardBuilder.getCardBuilderImage(face);
    }

    public static boolean isProxyImage(IRenderableCard face) {
        return getImageType(face) == ImageType.PROXY;
    }

    public static boolean isCardImageMissing(MagicCardDefinition aCard) {
        return !customCardImageExists(aCard)
            && !MagicFileSystem.getCroppedCardImage(aCard).exists()
            && !MagicFileSystem.getPrintedCardImage(aCard).exists();
    }

    private MagicCardImages() {}
}
