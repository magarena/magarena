package magic.ui.screen.keywords;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.utility.MagicFileSystem;

final class KeywordsHelper {

    private static final String _S1 = "Invalid keywords file.";

    private KeywordsHelper() { }

    private static Keyword[] loadKeywordsFileToSortedArray(File keywordsFile) throws FileNotFoundException {

        final List<Keyword> keywords = new ArrayList<>();

        Keyword keyword = new Keyword("");

        try (final Scanner sc = new Scanner(keywordsFile, "UTF-8")) {

            while (sc.hasNextLine()) {

                final String line = sc.nextLine().trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    // ignore a comment or blank line.

                } else if (line.startsWith("*")) {
                    // start of a new keyword definition
                    keyword = new Keyword(line.substring(1));
                    keywords.add(keyword);

                } else if (line.startsWith("@")) {
                    // one or more card names
                    keyword.setExampleCards(line.substring(1).split(";"));

                } else {
                    // one or more lines with keyword description
                    keyword.addDescriptionLine(line);
                }
            }
        }

        return keywords.stream()
            .sorted()
            .toArray(sz -> new Keyword[sz]);
    }

    private static File getKeywordsFile(String lang) {
        return MagicFileSystem
            .getDataPath(MagicFileSystem.DataPath.TRANSLATIONS)
            .resolve("keywords")
            .resolve((lang.isEmpty() ? "English" : lang) + ".txt")
            .toFile();
    }

    /**
     * Returns the default English keywords file.
     */
    private static File getKeywordsFile() throws FileNotFoundException {
        return getKeywordsFile("");
    }

    static Keyword[] getKeywords() throws IOException {

        Keyword[] keywords = null;
        File keywordsFile = null;
        String lang = GeneralConfig.getInstance().getTranslation();

        try {
            keywordsFile = getKeywordsFile(lang);
            keywords = loadKeywordsFileToSortedArray(keywordsFile);
        } catch (FileNotFoundException ex) {
            if (!lang.isEmpty()) {
                // no keywords file exists for current translation so load English file instead.
                keywordsFile = getKeywordsFile();
                keywords = loadKeywordsFileToSortedArray(keywordsFile);
            } else {
                throw ex;
            }
        }

        if (keywords == null || keywords.length == 0) {
            throw new IOException(MText.get(_S1) + "\n" + keywordsFile);
        }

        return keywords;
    }

}
