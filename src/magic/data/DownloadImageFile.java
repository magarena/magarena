package magic.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class DownloadImageFile {

	private final File file;
	private final URL url;
	
	public DownloadImageFile(final File file, final URL url) {
		
		this.file=file;
		this.url=url;
	}
	
	public String getFilename() {
		
		return file.getName();
	}
	
	public void download() {

		try {
			final OutputStream outputStream=new BufferedOutputStream(new FileOutputStream(file));
			final InputStream inputStream=url.openStream();
			final byte buffer[]=new byte[65536];
			while (true) {
				
				final int len=inputStream.read(buffer);
				if (len<0) {
					break;
				}
				outputStream.write(buffer,0,len);
			}
			inputStream.close();
			outputStream.close();
		} catch (final Exception ex) {
			file.delete();
		}
	}
}