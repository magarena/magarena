package magic.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFiles;

public class DownloadImageFile extends WebDownloader {

    private File file;
    private final URL url;
    private final String cardName;

    public DownloadImageFile(final MagicCardDefinition cdef) throws MalformedURLException {
        file = MagicFiles.getCardImageFile(cdef);
        url = new URL(cdef.getImageURL());
        cardName = cdef.getName();
    }

    @Override
    public String getFilename() {
        return file.getName();
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void download(final Proxy proxy) throws IOException {
        if (!filenamePrefix.isEmpty()) {
            file = new File(file.getParent(), filenamePrefix + file.getName());
        }
        WebDownloader.downloadToFile(proxy, url, file);
    }

    public String getCardName() {
        return cardName;
    }

    @Override
    public URL getDownloadUrl() {
        return url;
    }

}
