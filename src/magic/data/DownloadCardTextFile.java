package magic.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

public class DownloadCardTextFile extends WebDownloader {
	private static final String startPattern = "ctext\">";
	private static final String endPattern = "</p>";
	
	private final File file;
	private URL url;
	
	DownloadCardTextFile(final File file, final URL url) {
		this.file = file;
		this.url = url;
	}
	
	public String getFilename() {
		return file.getName();
	}
	
	public void download(final Proxy proxy) {
        String html = WebDownloader.getHTML(proxy, url);
		
        // find text in html
		int iStart =  html.indexOf(startPattern);
		String foundText = "";
		if(iStart > -1) {
			iStart += startPattern.length();
			int iEnd = html.indexOf(endPattern, iStart);
			foundText = html.substring(iStart, iEnd);
			
			foundText = foundText.replaceAll("\\<br\\>", " "); // replace newlines
			foundText = foundText.replaceAll("\\<[^\\>]*\\>", ""); // remove other html tags
		}
		
		// write text out to file
		if(foundText.length() > 0) {
			FileWriter outputStream = null;
			try {
	            outputStream = new FileWriter(file);
				outputStream.write(foundText);
			} catch (final IOException ex) {
	            System.err.println("ERROR! Unable to write to card text file");
	            System.err.println(ex.getMessage());
	            ex.printStackTrace();
				final boolean isDeleted = file.delete();
	            if (!isDeleted) {
	                System.err.println("ERROR! Unable to delete " + file);
	            }
			} finally {
	            magic.data.FileIO.close(outputStream);
	        }
		}
	}
}
