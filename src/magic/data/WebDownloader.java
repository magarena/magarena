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

public abstract class WebDownloader {    
    public abstract void download(final Proxy proxy);
    
    public abstract String getFilename();
    
    public abstract File getFile();

    public abstract boolean exists();
    
    public static void downloadToFile(final Proxy proxy, final URL url, final File file) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
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
    
    public static String getHTML(final Proxy proxy, final URL url) {
        InputStream inputStream = null;
        BufferedReader dataStream = null;
        StringBuilder sb = new StringBuilder();
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
