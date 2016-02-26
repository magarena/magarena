package magic.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

public class DownloadableFile {

    private final File localFile;
    private final URL remoteFile;

    public DownloadableFile(File aFile, URL aUrl) {
        this.localFile = aFile;
        this.remoteFile = aUrl;
    }

    public File getLocalFile() {
        return localFile;
    }

    public URL getUrl() {
        return remoteFile;
    }

    public String getFilename() {
        return localFile.getName();
    }

    public void doDownload(final Proxy proxy) throws IOException {
        final File tempFile = new File(localFile.getParent(), "~" + localFile.getName());
        downloadToFile(proxy, remoteFile, tempFile);
        Files.move(tempFile.toPath(), localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void downloadToFile(final Proxy proxy, final URL url, final File file) throws IOException {
        if (proxy != Proxy.NO_PROXY && proxy.type() != Proxy.Type.DIRECT) {
            downloadUsingProxy(proxy, url, file);
        } else {
            FileUtils.copyURLToFile(url, file, 10000, 15000);
        }
    }

    private static void downloadUsingProxy(final Proxy proxy, final URL url, final File file) throws IOException {
        try (
            final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            final InputStream inputStream = url.openConnection(proxy).getInputStream()) {
            final byte[] buffer = new byte[65536];
            while (true) {
                final int len = inputStream.read(buffer);
                if (len < 0) {
                    break;
                }
                outputStream.write(buffer, 0, len);
            }
        } catch (final Exception ex) {
            file.delete();
            throw ex;
        }
    }

}
