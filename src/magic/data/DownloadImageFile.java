package magic.data;

import magic.model.MagicCardDefinition;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

public class DownloadImageFile extends WebDownloader {

    private final File file;
    private final URL url;
    private final MagicCardDefinition cdef;

    DownloadImageFile(final File file, final URL url) {
        this(file, url, MagicCardDefinition.UNKNOWN);
    }

    DownloadImageFile(final File file, final URL url, final MagicCardDefinition cdef) {
        this.file=file;
        this.url=url;
        this.cdef=cdef;
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
        WebDownloader.downloadToFile(proxy, url, file);
    }

    @Override
    public boolean exists() {
        return file.exists() && file.length() != 0L && !cdef.isIgnored(file.length());
    }

    public String getCardName() {
        if (cdef != null) {
            return cdef.getName();
        } else {
            return "";
        }
    }

}
