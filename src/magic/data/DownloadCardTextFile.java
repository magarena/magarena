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
    private final URL url;

    DownloadCardTextFile(final File file, final URL url) {
        this.file = file;
        this.url = url;
    }

    public String getFilename() {
        return file.getName();
    }

    public File getFile() {
        return file;
    }

    public boolean exists() {
        return file.exists();
    }

    public void download(final Proxy proxy) {
        final String html = WebDownloader.getHTML(proxy, url);

        // find text in html
        int iStart =  html.indexOf(startPattern);
        String foundText = null;
        if (iStart > -1) {
            iStart += startPattern.length();
            final int iEnd = html.indexOf(endPattern, iStart);
            foundText = html.substring(iStart, iEnd) + " ";

            foundText = foundText.replaceAll("\\<br\\>", " "); // replace newlines
            foundText = foundText.replaceAll("\\<[^\\>]*\\>", ""); // remove other html tags
            foundText = foundText.replaceAll("( |^)\\(.+?\\)", ""); // remove reminder text
            foundText = foundText.trim(); // remove whitespace
        }

        // write text out to file
        // even if there's no text we want to create the file to ensure that we don't redownload it
        if (foundText != null) {
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
        } else {
            System.err.println("ERROR! Unable to download card text for " + file);
        }
    }
}
