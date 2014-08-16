package magic.utility;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Utility class for useful or common file-system related tasks.
 *
 */
public final class MagicDownload {
    private MagicDownload() {}

    public static long getDownloadableFileSize1(final URL downloadUrl) {
        long cLength = -1;
        try {
            URLConnection conn = downloadUrl.openConnection();
            cLength = conn.getContentLengthLong();
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return cLength;
    }

    public static long getDownloadableFileSize1(final String urlString) {
        try {
            return getDownloadableFileSize1(new URL(urlString));
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static long getDownloadableFileSize2(String fileUrl) {

        URL oracle;
        try {
            oracle = new URL(fileUrl);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }

        HttpURLConnection yc;
        try {
            yc = (HttpURLConnection) oracle.openConnection();
            populateDesktopHttpHeaders(yc);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        long fileSize = 0;
        try {
            // retrieve file size from Content-Length header field
            fileSize = Long.parseLong(yc.getHeaderField("Content-Length"));
        } catch (NumberFormatException nfe) {
        }

        return fileSize;
    }

    private static void populateDesktopHttpHeaders(URLConnection urlCon) {
        // add custom header in order to be easily detected
        urlCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        urlCon.setRequestProperty("Accept-Language",
                "el-gr,el;q=0.8,en-us;q=0.5,en;q=0.3");
        urlCon.setRequestProperty("Accept-Charset",
                "ISO-8859-7,utf-8;q=0.7,*;q=0.7");
    }

    public static Dimension getImageDimensions(final File resourceFile) throws IOException {
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
