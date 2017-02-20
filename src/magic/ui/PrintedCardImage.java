package magic.ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import magic.cardBuilder.renderers.CardBuilder;
import magic.data.CardImageFile;
import magic.data.DownloadableFile;
import magic.data.GeneralConfig;
import magic.exception.DownloadException;
import magic.model.IRenderableCard;
import magic.ui.helpers.UrlHelper;
import magic.utility.MagicFileSystem;

public abstract class PrintedCardImage {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    public static boolean imageExists(IRenderableCard face) {
        return MagicFileSystem.getPrintedCardImage(face).exists();
    }

    private static void downloadEnglishImage(CardImageFile aFile) throws DownloadException {
        try {
            aFile.doDownload();
        } catch (IOException ex) {
            throw new DownloadException(String.format("%s [%s]", ex.toString(), aFile.getFilename()), ex, aFile);
        }
    }

    private static boolean downloadNonEnglishImage(CardImageFile aFile, CardTextLanguage aLang) throws DownloadException, MalformedURLException {
        URL url = UrlHelper.getAltMagicCardsInfoUrl(aFile, aLang);
        if (UrlHelper.isUrlValid(url)) {
            DownloadableFile df = new DownloadableFile(aFile.getLocalFile(), url);
            try {
                df.doDownload();
                return true;
            } catch (IOException ex) {
                throw new DownloadException(String.format("%s [%s (%s)]", ex.toString(), aFile.getFilename(), aLang), ex, aFile);
            }
        }
        return false;
    }

    public static void tryDownloadingPrintedImage(CardImageFile imageFile) throws DownloadException {
        try {
            CardTextLanguage textLang = GeneralConfig.getInstance().getCardTextLanguage();
            if (textLang.isEnglish() || !downloadNonEnglishImage(imageFile, textLang)) {
                downloadEnglishImage(imageFile);
            }
        } catch (MalformedURLException ex) {
            throw new DownloadException(String.format("%s [%s]", ex.toString(), imageFile.getUrl()), ex, imageFile);
        }
    }

    private static void tryDownloadingPrintedImage(IRenderableCard face) throws DownloadException {
        if (CONFIG.getImagesOnDemand() && !imageExists(face)) {
            try {
                tryDownloadingPrintedImage(new CardImageFile(face));
            } catch (MalformedURLException ex) {
                throw new DownloadException(String.format("%s [%s]", ex.toString(), face.getImageName()), ex);
            }
        }
    }

    static BufferedImage get(IRenderableCard face) throws DownloadException {
        tryDownloadingPrintedImage(face);
        return imageExists(face)
            ? ImageFileIO.getOptimizedImage(MagicFileSystem.getPrintedCardImage(face))
            : CardBuilder.getCardBuilderImage(face);
    }
}
