package magic.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import magic.cardBuilder.renderers.CardBuilder;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.data.GeneralConfig;
import magic.exception.DownloadException;
import magic.model.IRenderableCard;
import magic.model.MagicCardDefinition;
import magic.ui.helpers.UrlHelper;
import magic.utility.MagicFileSystem;

public abstract class ProxyCardImage {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private static boolean croppedImageExists(IRenderableCard face) {
        return MagicFileSystem.getCroppedCardImage(face).exists();
    }

    private static boolean tryDownloadingFile(DownloadableFile aFile) {
        try {
            aFile.doDownload();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean tryDownloadingCroppedImage(CardImageFile imageFile) throws MalformedURLException, DownloadException {
        MagicCardDefinition card = imageFile.getCard();
        if (card.getImageURL().contains("magiccards.info/scans/")) {
            URL remote = new URL(card.getImageURL().replace("/scans/", "/crop/"));
            if (UrlHelper.isUrlValid(remote)) {
                File local = MagicFileSystem.getCroppedCardImage(imageFile.getCard());
                return tryDownloadingFile(new DownloadableFile(local, remote));
            }
        }
        return false;
    }

    private static void tryDownloadingCroppedImage(IRenderableCard face) throws DownloadException {
        try {
            tryDownloadingCroppedImage(new CardImageFile(face));
        } catch (MalformedURLException ex) {
            throw new DownloadException(String.format("%s [%s]", ex.toString(), face.getImageName()), ex);
        }
    }

    static BufferedImage get(IRenderableCard face) throws DownloadException {
        if (CONFIG.getImagesOnDemand()) {
            if (!croppedImageExists(face) && !PrintedCardImage.imageExists(face)) {
                tryDownloadingCroppedImage(face);
            }
            if (!croppedImageExists(face) && !PrintedCardImage.imageExists(face)) {
                // CB can extract crop from printed image as a last resort.
                PrintedCardImage.get(face);
            }
        }
        return CardBuilder.getCardBuilderImage(face);
    }

}
