package magic.data;

import java.io.File;
import java.net.Proxy;
import java.net.URL;

public class DownloadImageFile extends WebDownloader {

	private final File file;
	private final URL url;
	
	DownloadImageFile(final File file, final URL url) {
		this.file=file;
		this.url=url;
	}
	
	public String getFilename() {
		return file.getName();
	}
	
	public void download(final Proxy proxy) {
        WebDownloader.downloadToFile(proxy, url, file);
	}
}
