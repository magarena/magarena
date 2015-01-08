package magic.utility;

import java.io.IOException;
import java.io.InputStream;
import magic.data.FileIO;
import magic.data.MagicFormats;
import magic.data.MagicSets;

public final class MagicResources {
    private MagicResources() { }

    public static String getSetFileContent(final MagicSets magicSet) {
        final String filename = "/magic/data/sets/" + magicSet.toString().replace("_", "") + ".txt";
        try (final InputStream inputStream = MagicFileSystem.getJarResourceStream(filename)) {
            return inputStream != null ? FileIO.toStr(inputStream) : "";
        } catch (final IOException ex) {
            System.err.println(filename + " : " + ex.getMessage());
            return "";
        }
    }

    public static String getFormatFileContent(final MagicFormats magicFormat) {
        final String filename = "/magic/data/formats/" + magicFormat.getFilename() + ".fmt";
        try (final InputStream inputStream = MagicFileSystem.getJarResourceStream(filename)) {
            return inputStream != null ? FileIO.toStr(inputStream) : "";
        } catch (final IOException ex) {
            System.err.println(magicFormat.getFilename() + " : " + ex.getMessage());
            return "";
        }
    }

}
