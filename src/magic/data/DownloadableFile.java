package magic.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.URL;
import org.apache.commons.io.FileUtils;

public abstract class DownloadableFile {
    public abstract void download(final Proxy proxy) throws IOException;

    public abstract String getFilename();

    public abstract File getLocalFile();

    public abstract URL getDownloadUrl();

    protected String filenamePrefix = "";

    public static void downloadToFile(final Proxy proxy, final URL url, final File file) throws IOException {
        if (proxy != Proxy.NO_PROXY && proxy.type() != Proxy.Type.DIRECT) {
            downloadUsingProxy(proxy, url, file);
        } else {
            FileUtils.copyURLToFile(url, file, 10000, 5000);
        }
    }

    private static void downloadUsingProxy(final Proxy proxy, final URL url, final File file) throws IOException {
        try (
            final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            final InputStream inputStream = url.openConnection(proxy).getInputStream()) {
                final byte[] buffer=new byte[65536];
                while (true) {
                    final int len=inputStream.read(buffer);
                    if (len<0) {
                        break;
                    }
                    outputStream.write(buffer,0,len);
                }
        } catch (final Exception ex) {
            file.delete();
            throw ex;
        }
    }

    public void setFilenamePrefix(final String prefix) {
        filenamePrefix = prefix;
    }

}
