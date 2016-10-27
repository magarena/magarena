package magic.data;

import magic.utility.FileIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Locale;
import magic.utility.ProgressReporter;
import magic.model.MagicCardDefinition;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class UnimplementedParser {

    private static final File SCRIPTS_MISSING_DIRECTORY = MagicFileSystem.getDataPath(DataPath.SCRIPTS_MISSING).toFile();
    private static final List<String> errorList = new ArrayList<>();
    private static final List<MagicCardDefinition> allUnimplementedCardDefs = new ArrayList<>();
    private static final List<MagicCardDefinition> parsedCards = new ArrayList<>();
    private static final List<MagicCardDefinition> errorCards = new ArrayList<>();

    public static void parseScriptsMissing(final ProgressReporter reporter) {
        reporter.setMessage("Initializing scripts missing folder...");
        final File[] scriptFiles = MagicFileSystem.getSortedScriptFiles(SCRIPTS_MISSING_DIRECTORY);
        reporter.setMessage("Parsing missing scripts...");
        for (final File file : scriptFiles) {
            parseCardDefinition(file);
        }
    }

    private static void parseCardDefinition(final File file) {
        try {
            final MagicCardDefinition cdef = prop2carddef(file);
            cdef.validate();
            addParsedDefinition(cdef);
            parsedCards.add(cdef);
        } catch (final Throwable cause) {
            errorList.add("ERROR file: " + file + " cause: " + cause.getMessage());
        }
    }

    private static MagicCardDefinition prop2carddef(final File scriptFile) {
        final Properties content = FileIO.toProp(scriptFile);
        final MagicCardDefinition cardDefinition = new MagicCardDefinition();
        for (final String key : content.stringPropertyNames()) {
            try {
                setProperty(cardDefinition, key, content.getProperty(key));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return cardDefinition;
    }

    private static void setProperty(final MagicCardDefinition card, final String property, final String value) {
        try {
            CardProperty.valueOf(property.toUpperCase(Locale.ENGLISH)).setProperty(card, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("unknown card property value \"" + property + "\" = \"" + value + "\"");
        }
    }

    private static void addParsedDefinition(final MagicCardDefinition cardDefinition) {
        allUnimplementedCardDefs.add(cardDefinition);
    }

    public static void parseCardAbilities() {
        for (final MagicCardDefinition cdef : getUnimplementedCardDefs()) {
            try {
                cdef.loadAbilities();
                if (!parsedCards.contains(cdef)){
                    parsedCards.add(cdef);
                }
            } catch (Throwable cause) {
                errorList.add("ERROR: " + cause.getMessage() + " " + cdef);
                errorCards.add(cdef);
            }
        }
        exportParseResults();
    }

    public static List<MagicCardDefinition> getUnimplementedCardDefs() {
        return allUnimplementedCardDefs;
    }

    private static void saveParsedCardsList(List<MagicCardDefinition> cardList, String filename) {
        final Path LOGS_PATH = MagicFileSystem.getDataPath(DataPath.LOGS);
        final File LOG_FILE = LOGS_PATH.resolve(filename + ".log").toFile();
        try (final PrintWriter writer = new PrintWriter(LOG_FILE)) {
            for (MagicCardDefinition card : cardList) {
                final String cardName = card.getName();
                writer.println(cardName);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to save " + LOG_FILE + " - " + ex);
        }
    }

    private static void saveParseErrorLog(final List<String> error) {
        final Path LOGS_PATH = MagicFileSystem.getDataPath(DataPath.LOGS);
        final File LOG_FILE = LOGS_PATH.resolve("parse_errors.log").toFile();
        try (final PrintWriter writer = new PrintWriter(LOG_FILE)) {
            for (String errorMessage : error) {
                writer.println(errorMessage);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to save " + LOG_FILE + " - " + ex);
        }
    }

    private static void exportParseResults() {
        parsedCards.removeAll(errorCards);
        Collections.sort(errorList);
        saveParsedCardsList(errorCards, "errorCards");
        saveParsedCardsList(parsedCards, "parsedCards");
        saveParseErrorLog(errorList);
    }

}
