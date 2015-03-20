package magic.ui;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import magic.data.CardDefinitions;
import magic.data.DownloadableFile;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;

public final class MagicDownload {
    private MagicDownload() {}

    public static boolean isRemoteFileDownloadable(final DownloadableFile downloadableFile) throws IOException {
        final long remoteFileSize = getRemoteFileSize(downloadableFile.getDownloadUrl());
        final long localFileSize = downloadableFile.getFile().length();
        return (remoteFileSize > 0) && (remoteFileSize != localFileSize);
    }

    public static int doDownloadImageFile(final DownloadableFile downloadableFile, final Proxy proxy) throws IOException {
        final File localImageFile = downloadableFile.getFile();
        final Dimension oldImageSize = getImageDimensions(localImageFile);
        downloadableFile.download(proxy);
        final Dimension newImageSize = getImageDimensions(localImageFile);
        if (!newImageSize.equals(oldImageSize)) {
                // only interested in counting where image size changes because
            // you can also get downloads where the file size has changed
            // but the image size is still the same and in this context\
            // that means the image is still LQ so don't decrement the LQ
            // count displayed in the progress bar.
            return 1;
        } else {
            return 0;
        }
    }

    public static List<MagicCardDefinition> getLowQualityImageCards() {
        final List<MagicCardDefinition> cards = new ArrayList<>();
        for (final MagicCardDefinition cardDefinition : CardDefinitions.getDefaultPlayableCardDefs()) {
            if (cardDefinition.getImageURL() != null) {
                final File imageFile = MagicFileSystem.getCardImageFile(cardDefinition);
                if (imageFile.exists() && isLowQualityImage(imageFile)) {
                    cards.add(cardDefinition);
                }
            }
        }
        return cards;
    }

    private static boolean isLowQualityImage(final File imageFile) {
        Dimension imageSize = null;
        try {
            imageSize = getImageDimensions(imageFile);
            return (imageSize.width < CardImagesProvider.HIGH_QUALITY_IMAGE_SIZE.width);
        } catch (IOException | NullPointerException ex) {
            System.err.println(imageFile.getName() + " (" + imageSize + ") : " + ex);
            return false;
        }
    }

    public static long getRemoteFileSize(final URL downloadUrl) throws IOException {
        final URLConnection urlConn = downloadUrl.openConnection();
        final HttpURLConnection httpConn = (HttpURLConnection)urlConn;
        httpConn.setRequestMethod("HEAD");
        if (httpConn.getResponseCode() == 200) {
            return urlConn.getContentLengthLong();
        } else {
            throw new IOException("Url not reachable : " + downloadUrl.toString());
        }
    }

    private static Dimension getImageDimensions(final File resourceFile) throws IOException {
        try (final ImageInputStream in = ImageIO.createImageInputStream(resourceFile)) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                final ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        }
        return null;
    }
    
}
