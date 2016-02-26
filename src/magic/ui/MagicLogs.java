package magic.ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;

public class MagicLogs {

    private static List<String> newCards = null;

    public static boolean isCardInDownloadsLog(MagicCardDefinition card) {
        if (newCards == null) {
            newCards = getCardNamesFromDownloadLog();
        }
        return newCards.contains(card.getName());
    }

    private static List<String> getCardNamesFromDownloadLog() {
        final List<String> cardNames = new ArrayList<>();
        // disable because slow implement
//        final Path logPath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.LOGS).resolve("newcards.log");
//        if (logPath.toFile().exists()) {
//            try {
//            	// bug of jdk? "java.nio.charset.MalformedInputException: Input length = 1" here
//                for (final String cardName : FileUtil.readString(new FileInputStream(logPath.toFile()),null).split("\n")) {
//                    cardNames.add(cardName.trim());
//                }
//            } catch (final IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
        return cardNames;
    }

    public static void clearLoadedLogs() {
        if (newCards != null) {
            newCards.clear();
            newCards = null;
        }
    }

}
