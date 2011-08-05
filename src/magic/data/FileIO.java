package magic.data;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Properties;

public class FileIO {
    
    static private String toStr(final BufferedReader input) throws IOException {
        StringBuilder contents = new StringBuilder();
        try {
            String line = null; //not declared within while loop
            /*
             * readLine is a bit quirky :
             * it returns the content of a line MINUS the newline.
             * it returns null only for the END of the stream.
             * it returns an empty String if two newlines appear in a row.
             */
            while (input != null && (line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } finally {
            close(input);
        }
        return contents.toString();
    }

    static public String toStr(File aFile) throws IOException {
        return toStr(new BufferedReader(new FileReader(aFile)));
    }
    
    static public String toStr(InputStream ins) throws IOException {
        return toStr(new BufferedReader(new InputStreamReader(ins)));
    }

    static public Properties toProp(File aFile) {
        final Properties properties=new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(aFile);
            properties.load(fis);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load from " + aFile + ", " + ex.getMessage());
        } finally {
            close(fis);
        }
        return properties;
    }

    static public BufferedImage toImg(final File aFile, final BufferedImage def) {
        BufferedImage img = def;
        if (aFile == null || !aFile.isFile()) {
            img = def;
        } else {
            try {
                img = ImageIO.read(aFile);
            } catch (final IOException ex) {
                System.err.println("ERROR! Unable to read from " + aFile);
                img = def;
            }
        }
        return img;
    }
    
    static public BufferedImage toImg(final URL loc, final BufferedImage def) {
        BufferedImage img = def;
        if (loc == null) {
            img = def;
        } else {
            try {
                img = ImageIO.read(loc);
            } catch (final IOException ex) {
                System.err.println("ERROR! Unable to read from " + loc);
                img = def;
            }
        }
        return img;
    }
    
    static public BufferedImage toImg(final InputStream ins, final BufferedImage def) {
        BufferedImage img = def;
        if (ins == null) {
            img = def;
        } else {
            try {
                img = ImageIO.read(ins);
            } catch (final IOException ex) {
                System.err.println("ERROR! Unable to read from input stream");
                img = def;
            } finally {
                close(ins);
            }
        }
        return img;
    }
    
    static public void toFile(File aFile, String aContents) throws IOException {
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(aFile));
            output.write(aContents);
        } finally {
            close(output);
        }
    }
    
    static public void toFile(File aFile, Properties properties, String name) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(aFile);
            properties.store(fos, name);
        } finally {
            close(fos);
        }
    }

    static public void close(final Closeable resource) {
        if (resource == null) {
            return;
        }
        boolean closed = false;
        while (!closed) {
            try {
                resource.close();
                closed = true;
            } catch (final Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
} 
