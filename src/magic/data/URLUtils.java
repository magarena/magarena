package magic.data;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class URLUtils {
    
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