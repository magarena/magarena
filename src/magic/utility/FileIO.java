package magic.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Properties;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileIO {

    public static final Charset UTF8 = Charset.forName("UTF-8");

    private static String toStr(final BufferedReader input) throws IOException {
        final StringBuilder contents = new StringBuilder();
        try (final BufferedReader br = input) {
            String line = null; //not declared within while loop
            /*
             * readLine is a bit quirky :
             * it returns the content of a line MINUS the newline.
             * it returns null only for the END of the stream.
             * it returns an empty String if two newlines appear in a row.
             */
            while (br != null && (line = br.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        }
        return contents.toString();
    }

    public static String toStr(final File aFile) throws IOException {
        return toStr(new FileInputStream(aFile));
    }

    public static List<String> toStrList(final File aFile) throws IOException {
        return Files.readAllLines(aFile.toPath(), UTF8);
    }

    public static String toStr(final InputStream ins) throws IOException {
        return toStr(new BufferedReader(new InputStreamReader(ins, UTF8)));
    }

    public static Properties toProp(final File aFile) {
        Properties properties = new SortedProperties();
        try {
            properties = toProp(new FileInputStream(aFile));
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load from " + aFile + ", " + ex.getMessage());
        }
        return properties;
    }

    public static Properties toProp(final InputStream ins) {
        final Properties properties = new SortedProperties();
        try (final InputStream is = ins) {
            properties.load(new BufferedReader(new InputStreamReader(is, UTF8)));
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load from input stream, " + ex.getMessage());
        }
        return properties;
    }

    public static void toFile(final File aFile, final String aContents, final boolean append) throws IOException {
        try (final Writer output = Files.newBufferedWriter(aFile.toPath(), UTF_8, append ? new StandardOpenOption[] {CREATE, APPEND} : new StandardOpenOption[] {CREATE})) {
            output.write(aContents);
        }
    }

    /**
     * Saves a Properties object to file.
     */
    public static void toFile(File aFile, Properties properties, String comments) throws IOException {
        try (PrintWriter writer = new PrintWriter(aFile, "UTF-8")) {
            properties.store(writer, comments);
        }
    }

    public static void copyFile(final File sourceFile, final File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        try (final FileInputStream fIn = new FileInputStream(sourceFile);
             final FileOutputStream fOut = new FileOutputStream(destFile)) {
            final FileChannel source = fIn.getChannel();
            final FileChannel destination = fOut.getChannel();
            long transfered = 0;
            final long bytes = source.size();
            while (transfered < bytes) {
                transfered += destination.transferFrom(source, 0, source.size());
                destination.position(transfered);
            }
        }
    }
}
