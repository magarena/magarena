package magic.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

public class FileIO {
    
    private static String toStr(final BufferedReader input) throws IOException {
        final StringBuilder contents = new StringBuilder();
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

    public static String toStr(final File aFile) throws IOException {
        return toStr(new BufferedReader(new FileReader(aFile)));
    }
    
    static String toStr(final InputStream ins) throws IOException {
        return toStr(new BufferedReader(new InputStreamReader(ins)));
    }

    public static Properties toProp(final File aFile) {
        Properties properties = new Properties();
        try {
            properties = toProp(new FileInputStream(aFile));
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load from " + aFile + ", " + ex.getMessage());
        } 
        return properties;
    }
    
    public static Properties toProp(final InputStream ins) {
        final Properties properties = new Properties();
        try {
            properties.load(ins);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load from input stream, " + ex.getMessage());
        } finally {
            close(ins);
        }
        return properties;
    }

    public static BufferedImage toImg(final File aFile, final BufferedImage def) {
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
    
    static BufferedImage toImg(final URL loc, final BufferedImage def) {
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
    
    public static BufferedImage toImg(final InputStream ins, final BufferedImage def) {
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
    
    public static void toFile(final File aFile, final String aContents, final boolean append) throws IOException {
        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(aFile, append));
            output.write(aContents);
        } finally {
            close(output);
        }
    }
    
    public static void toFile(
            final File aFile, 
            final Properties properties, 
            final String name) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(aFile);
            properties.store(fos, name);
        } finally {
            close(fos);
        }
    }

    static void close(final Closeable resource) {
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
