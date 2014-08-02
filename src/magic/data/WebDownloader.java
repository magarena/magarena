package magic.data;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.URL;
import org.apache.commons.io.FileUtils;

public abstract class WebDownloader {
    public abstract void download(final Proxy proxy) throws IOException;

    public abstract String getFilename();

    public abstract File getFile();

    public abstract boolean exists();

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

    public static String getHTML(final Proxy proxy, final URL url) {
        InputStream inputStream = null;
        BufferedReader dataStream = null;
        final StringBuilder sb = new StringBuilder();
        String line;

        try {
            inputStream = url.openConnection(proxy).getInputStream();
            dataStream = new BufferedReader(new InputStreamReader(inputStream));

            while( (line = dataStream.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to download webpage");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            magic.data.FileIO.close(inputStream);
            magic.data.FileIO.close(dataStream);
        }

        return sb.toString();
    }
}
