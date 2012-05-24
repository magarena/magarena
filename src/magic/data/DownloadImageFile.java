package magic.data;

import java.io.File;
import java.net.Proxy;
import java.net.URL;

import magic.model.MagicCardDefinition;

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
	
	public String getFilename() {
		return file.getName();
	}

    public File getFile() {
        return file;
    }
	
	public void download(final Proxy proxy) {
        WebDownloader.downloadToFile(proxy, url, file);
	}

    public boolean exists() {
        return file.exists() && file.length() != 0L && !cdef.isIgnored(file.length());
    }
}
