package magic.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Proxy;
import magic.cardBuilder.renderers.CardBuilder;
import magic.data.CardImageFile;
import magic.data.GeneralConfig;
import magic.model.IRenderableCard;
import magic.utility.MagicFileSystem;

/**
 * Handles card images only via IRenderableCard.
 */
public final class MagicCardImages {

    private enum ImageType { 
        UNKNOWN,
        CUSTOM,
        PROXY,
        FULL
    }

    private static Proxy proxy;

    private static ImageType getImageType(IRenderableCard face) {

        if (face.isUnknown()) {
            return ImageType.UNKNOWN;
        }

        if (MagicFileSystem.getCustomCardImageFile(face).exists()) {
            return ImageType.CUSTOM;
        }

        if (MagicFileSystem.getCroppedCardImageFile(face).exists()) {
            return ImageType.PROXY;
        }

        if (GeneralConfig.getInstance().getImagesOnDemand()
                && !MagicFileSystem.getCardImageFile(face).exists()) {
            return ImageType.FULL;
        }

        if (MagicFileSystem.getCardImageFile(face).exists()) {
            return ImageType.FULL;
        }

        // else missing image proxy...
        return ImageType.PROXY;
    }

    private static void tryDownloadingImage(IRenderableCard face) {
        if (proxy == null) {
            proxy = GeneralConfig.getInstance().getProxy();
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
                return ImageFileIO.getOptimizedImage(MagicFileSystem.getCustomCardImageFile(face));
            case PROXY:
                return CardBuilder.getCardBuilderImage(face);
            case FULL:
                if (!MagicFileSystem.getCardImageFile(face).exists()) {
                    tryDownloadingImage(face);
                }
                if (MagicFileSystem.getCardImageFile(face).exists()) {
                    return ImageFileIO.getOptimizedImage(MagicFileSystem.getCardImageFile(face));
                }
        }
        return CardBuilder.getCardBuilderImage(face);
    }

    public static boolean isProxyImage(IRenderableCard face) {
        return getImageType(face) == ImageType.PROXY;
    }

    private MagicCardImages() {}
}
