package magic.data;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

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

    /**
    * Change the contents of text file in its entirety, overwriting any
    * existing text.
    *
    * This style of implementation throws all exceptions to the caller.
    *
    * @param aFile is an existing file which can be written to.
    * @throws IllegalArgumentException if param does not comply.
    * @throws FileNotFoundException if the file does not exist.
    * @throws IOException if problem encountered during write.
    */
    static public void toFile(File aFile, String aContents)
             throws FileNotFoundException, IOException {
        if (aFile == null) {
            throw new IllegalArgumentException("File should not be null.");
        }
        if (!aFile.exists()) {
            throw new FileNotFoundException ("File does not exist: " + aFile);
        }
        if (!aFile.isFile()) {
            throw new IllegalArgumentException("Should not be a directory: " + aFile);
        }
        if (!aFile.canWrite()) {
            throw new IllegalArgumentException("File cannot be written: " + aFile);
        }

        //use buffering
        Writer output = new BufferedWriter(new FileWriter(aFile));
        try {
            //FileWriter always assumes default encoding is OK!
            output.write(aContents);
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
} 
