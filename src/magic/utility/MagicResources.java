package magic.utility;

import java.io.IOException;
import java.io.InputStream;
import magic.data.FileIO;
import magic.data.MagicFormats;
import magic.data.MagicSets;

public final class MagicResources {
    private MagicResources() { }

    public static String getFileContent(final MagicSets magicSet) {
        return getResourceFileContent("/magic/data/sets/" + magicSet.toString().replace("_", "") + ".txt");
    }

    public static String getFileContent(final MagicFormats magicFormat) {
        return getResourceFileContent("/magic/data/formats/" + magicFormat.getFilename() + ".fmt");
    }

    private static String getResourceFileContent(final String filename) {
        try (final InputStream inputStream = MagicFileSystem.getJarResourceStream(filename)) {
            return inputStream != null ? FileIO.toStr(inputStream) : "";
        } catch (final IOException ex) {
            System.err.println(filename + " : " + ex.getMessage());
            return "";
        }
    }

}
