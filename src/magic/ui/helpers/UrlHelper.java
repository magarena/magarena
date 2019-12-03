package magic.ui.helpers;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JOptionPane;
import magic.data.DownloadableFile;
import magic.ui.CardTextLanguage;

public final class UrlHelper {

    public static final String URL_THEMES = "http://github.com/magarena/magarena/wiki/Themes";
    public static final String URL_AVATARS = "http://www.slightlymagic.net/forum/viewforum.php?f=89";
    public static final String URL_FORUM = "http://www.slightlymagic.net/forum/viewforum.php?f=82";
    public static final String URL_WIKI = "http://github.com/magarena/magarena/wiki/";
    public static final String URL_USERGUIDE = URL_WIKI;
    public static final String URL_FIREMIND_SCRIPTS = "http://www.firemind.ch/card_script_submissions/new";
    public static final String URL_HOMEPAGE = "http://magarena.github.io/";

    /**
     * Try to open URL in browser.
     *
     * @param url URl to open
     * @return true if successful, false if operation failed or is not supported
     */
    public static boolean tryOpenURL(final String url) {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            if (desktop != null) {
                final URI uri = new URI(url);
                desktop.browse(uri);
                return true;
            }
        } catch (IOException | UnsupportedOperationException ioe) {
            System.err.println("ERROR! Unable to launch browser url " + url);
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        } catch (URISyntaxException use) {
            System.err.println("ERROR! Invalid URI");
            System.err.println(use.getMessage());
            use.printStackTrace();
        }
        return false;
    }

    public static void openURL(final String url) {
        if (!tryOpenURL(url)) {
            try {
                JOptionPane.showMessageDialog(null, "Unable to launch browser for url " + url,
                    "Error launching browser", JOptionPane.ERROR_MESSAGE);
            } catch (HeadlessException e) {
                System.err.println("Unable to launch browser for url " + url);
            }
        }
    }

    /**
     * Returns a {@code magiccards.info} image url for a given non-English language code.
     */
    public static URL getAltMagicCardsInfoUrl(DownloadableFile aFile, CardTextLanguage lang) throws MalformedURLException {
        final String BASE = "/magiccards.info/scans/";
        final String TARGET = BASE + "en/";
        final String s = aFile.getUrl().toExternalForm();
        return s.contains(TARGET)
            ? new URL(s.replaceAll(TARGET, BASE + lang.getMagicCardsCode() + "/"))
            : null;
    }

    /**
     * Quickly checks to see whether URL is reachable.
     */
    public static boolean isUrlValid(final URL url) {
        final int timeout = 2000; // time, in milliseconds, used for open a  link  referenced by this URLConnection.

        if (url == null)
            return false;

        try {
            final HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            huc.setConnectTimeout(timeout); // Using constant named
            final int responseCode = huc.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (IOException ex) {
            return false;
        }
    }

    private UrlHelper() {
    }
}
