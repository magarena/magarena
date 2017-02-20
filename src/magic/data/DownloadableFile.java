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

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private static String proxySettings = "";
    private static Proxy proxy = Proxy.NO_PROXY;

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

    public void doDownload() throws IOException {
        final File tempFile = new File(localFile.getParent(), "~" + localFile.getName());
        downloadToFile(remoteFile, tempFile);
        Files.move(tempFile.toPath(), localFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    private static Proxy getProxy() {
        if (!proxySettings.equals(CONFIG.getProxySettings())) {
            proxySettings = CONFIG.getProxySettings();
            proxy = CONFIG.getProxy();
        }
        return proxy;
    }

    private static void downloadUsingProxy(URL url, File file) throws IOException {
        try (
            final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            final InputStream inputStream = url.openConnection(getProxy()).getInputStream()) {
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

    public static void downloadToFile(URL url, File file) throws IOException {
        if (getProxy() != Proxy.NO_PROXY && getProxy().type() != Proxy.Type.DIRECT) {
            downloadUsingProxy(url, file);
        } else {
            FileUtils.copyURLToFile(url, file, 10000, 5000);
        }
    }

}
