package magic.ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class URLUtils {

    public static final String URL_THEMES = "http://github.com/magarena/magarena/wiki/Themes";
    public static final String URL_AVATARS = "http://www.slightlymagic.net/forum/viewforum.php?f=89";
    public static final String URL_FORUM = "http://www.slightlymagic.net/forum/viewforum.php?f=82";
    public static final String URL_WIKI = "http://github.com/magarena/magarena/wiki/";
    public static final String URL_USERGUIDE = URL_WIKI;
    public static final String URL_FIREMIND_SCRIPTS = "http://www.firemind.ch/card_script_submissions/new";
    public static final String URL_HOMEPAGE = "http://magarena.github.io/";

    public static void openURL(final String url) {
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            if (desktop != null) {
                final URI uri = new URI(url);
                desktop.browse(uri);
            }
        } catch (IOException ioe) {
            System.err.println("ERROR! Unable to launch browser");
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        } catch (URISyntaxException use) {
            System.err.println("ERROR! Invalid URI");
            System.err.println(use.getMessage());
            use.printStackTrace();
        }
    }
}
