package magic.data;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Properties;

public class FileIO {

    static public String toStr(File aFile) throws IOException {
        //...checks on aFile are elided
        StringBuilder contents = new StringBuilder();

        //use buffering, reading one line at a time
        //FileReader always assumes default encoding is OK!
        BufferedReader input = new BufferedReader(new FileReader(aFile));
        try {
            String line = null; //not declared within while loop
            /*
            * readLine is a bit quirky :
            * it returns the content of a line MINUS the newline.
            * it returns null only for the END of the stream.
            * it returns an empty String if two newlines appear in a row.
            */
            while (( line = input.readLine()) != null){
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } finally {
            close(input);
        }
        
        return contents.toString();
    }
    
    static public String toStr(InputStream ins) throws IOException {
        //...checks on aFile are elided
        StringBuilder contents = new StringBuilder();

        //use buffering, reading one line at a time
        //FileReader always assumes default encoding is OK!
        BufferedReader input = new BufferedReader(new InputStreamReader(ins));
        try {
            String line = null; //not declared within while loop
            /*
            * readLine is a bit quirky :
            * it returns the content of a line MINUS the newline.
            * it returns null only for the END of the stream.
            * it returns an empty String if two newlines appear in a row.
            */
            while (( line = input.readLine()) != null){
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } finally {
            close(input);
        }
        
        return contents.toString();
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

    static public void toFile(File aFile, String aContents) {
        if (aFile == null) {
            throw new IllegalArgumentException("File should not be null.");
        }

        Writer output = null;
        try {
            output = new BufferedWriter(new FileWriter(aFile));
            output.write(aContents);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to write to " + aFile);
            System.err.println(ex.getMessage());
        } finally {
            close(output);
        }
    }
    
    static public BufferedImage toImg(final File aFile, final BufferedImage def) {
        BufferedImage img = def;
        if (aFile == null || !(aFile.exists() && aFile.isFile())) {
            img = def;
        } else {
            try {
                img = ImageIO.read(aFile);
            } catch (final IOException ex) {
                System.err.println("ERROR! Unable to read from " + aFile.getName());
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
    
    static public void toFile(File aFile, Properties properties, String name) throws IOException {
        if (aFile == null) {
            throw new IllegalArgumentException("File should not be null.");
        }
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(aFile);
            properties.store(fos, name);
        } finally {
            close(fos);
        }
    }

    static public Properties toProp(File aFile) {
        if (aFile == null) {
            throw new IllegalArgumentException("File should not be null.");
        }
        
        final Properties properties=new Properties();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(aFile);
            properties.load(fis);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load from " + aFile);
            System.err.println(ex.getMessage());
        } finally {
            close(fis);
        }

        return properties;
    }
} 
