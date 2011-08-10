package magic.data;

import java.io.*;
import java.net.Proxy;
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
	
	public void download(final Proxy proxy) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
		try { //download image
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            inputStream = url.openConnection(proxy).getInputStream();
			final byte buffer[]=new byte[65536];
			while (true) {
				final int len=inputStream.read(buffer);
				if (len<0) {
					break;
				}
				outputStream.write(buffer,0,len);
			}
		} catch (final IOException ex) {
            System.err.println("ERROR! Unable to download file");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
			final boolean isDeleted = file.delete();
            if (!isDeleted) {
                System.err.println("ERROR! Unable to delete " + file);
            }
		} finally {
            magic.data.FileIO.close(inputStream);
            magic.data.FileIO.close(outputStream);
        }
	}
}
