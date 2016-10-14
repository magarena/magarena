package magic.ui.screen.keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import magic.utility.MagicResources;

final class KeywordsHelper {
    private KeywordsHelper() { }

    static Keyword[] loadKeywordsFileToSortedArray() {
        
        final List<Keyword> keywords = new ArrayList<>();
        final String content = MagicResources.getKeywordsFileContent();
        
        Keyword keyword = null;

        try (final Scanner sc = new Scanner(content)) {
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

}
