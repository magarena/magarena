package magic.data;

import java.io.*;

public class TextFile {

    /**
    * Fetch the entire contents of a text file, and return it in a String.
    * This style of implementation does not throw Exceptions to the caller.
    *
    * @param aFile is a file which already exists and can be read.
    */
    static public String read(File aFile) throws IOException {
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
            input.close();
        }
        
        return contents.toString();
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
    static public void write(File aFile, String aContents)
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
            output.write( aContents );
        } finally {
            output.close();
        }
    }
} 
