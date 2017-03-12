package magic.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import magic.data.CardDefinitions;
import magic.data.DeckType;
import magic.data.GeneralConfig;
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicRandom;
import magic.translate.MText;
import magic.utility.MagicFileSystem.DataPath;
import org.apache.commons.io.FilenameUtils;

public class DeckUtils {

    // translatable strings
    private static final String _S1 = "Deck file is empty.";
    private static final String _S2 = "Lines in file exceeds %d.";
    private static final String _S3 = "...more...";
    private static final String _S4 = "line %d: line length exceeds %d characters.";
    private static final String _S5 = "line %d: invalid line format.";
    private static final String _S6 = "Expected: <quantity><space><card name>";
    private static final String _S7 = "line %d: invalid card (%s).";

    public static final String DECK_EXTENSION=".dec";
    private static final int DECK_FILE_MAX_LINES = GeneralConfig.getInstance().getDeckFileMaxLines();

    private static final String[] CARD_TYPES={"creatures","spells","lands"};

    public static String getDeckFolder() {
        return MagicFileSystem.getDataPath(DataPath.DECKS).toString();
    }

    public static Path getPrebuiltDecksFolder() {
        Path decksPath = Paths.get(getDeckFolder()).resolve("prebuilt");
        MagicFileSystem.verifyDirectoryPath(decksPath);
        return decksPath;
    }

    public static Path getFiremindDecksFolder() {
        Path decksPath = Paths.get(getDeckFolder()).resolve("firemind");
        MagicFileSystem.verifyDirectoryPath(decksPath);
        return decksPath;
    }

    public static void createDeckFolder() {
        final File deckFolderFile=new File(getDeckFolder());
        if (!deckFolderFile.exists() && !deckFolderFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + getDeckFolder());
        }
    }

    public static boolean saveDeck(final String filename, final MagicDeck deck) {

        final List<SortedMap<String,Integer>> cardMaps=new ArrayList<>();
        boolean isSuccessful = true;

        for (int count=3;count>0;count--) {
            cardMaps.add(new TreeMap<String, Integer>());
        }

        for (final MagicCardDefinition cardDefinition : deck) {
            final String name = cardDefinition.getAsciiName();
            final int index;
            if (cardDefinition.isLand()) {
                index=2;
            } else if (cardDefinition.isCreature()) {
                index=0;
            } else {
                index=1;
            }
            final SortedMap<String,Integer> cardMap=cardMaps.get(index);
            final Integer count=cardMap.get(name);
            cardMap.put(name,count==null?Integer.valueOf(1):Integer.valueOf(count+1));
        }

        BufferedWriter writer = null;
        try { //save deck
            writer = new BufferedWriter(new FileWriter(filename));
            for (int index=0;index<=2;index++) {
                final SortedMap<String,Integer> cardMap=cardMaps.get(index);
                if (!cardMap.isEmpty()) {
                    writer.write("# "+cardMap.size()+" "+CARD_TYPES[index]);
                    writer.newLine();
                    for (final Map.Entry<String,Integer> entry : cardMap.entrySet()) {
                        writer.write(entry.getValue()+" "+entry.getKey());
                        writer.newLine();
                    }
                    writer.newLine();
                }
            }
            final String description = deck.getDescription();
            if (description != null) {
                writer.write(">" + description.replaceAll("(\\r|\\n|\\r\\n)", "\\\\n"));
            }
        } catch (final IOException ex) {
            isSuccessful = false;
            System.err.println("Invalid deck : " + deck.getFilename() + " - " + ex.getMessage());
        } finally {
            if (writer != null) {
                magic.utility.FileIO.close(writer);
            }
        }

        return isSuccessful;

    }

    /**
     * reads deck file into list of strings where each string represents a line
     * in the file. If getDeckFileContent() generates an IOException then it is
     * an invalid or corrupt deck file but this method should not handle the exception
     * as it is called from multiple locations which may want to handle an invalid
     * file in different ways.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    private static List<String> getDeckFileContent(final String filename) {
        try {
            return FileIO.toStrList(new File(filename));
        } catch (IOException ex) {
            throw new InvalidDeckException("Invalid deck (\".dec\") file: " + filename, ex);
        }
    }

    private static MagicDeck parseDeckFileContent(final List<String> content) {

        final MagicDeck deck = new MagicDeck();

        if (content.isEmpty()) {
            deck.setInvalidDeck(MText.get(_S1));
            return deck;
        }

        if (content.size() > DECK_FILE_MAX_LINES) {
            deck.setInvalidDeck(MText.get(_S2, DECK_FILE_MAX_LINES));
            return deck;
        }

        final int MAX_LINE_ERRORS = 3;
        final int MAX_LINE_LENGTH = 50; // characters.
        int lineNumber = 0;
        final List<String> lineErrors = new ArrayList<>();

        for (final String nextLine: content) {

            if (lineErrors.size() > MAX_LINE_ERRORS) {
                lineErrors.remove(lineErrors.size()-1);
                lineErrors.add(MText.get(_S3));
                deck.clear();
                break;
            }

            lineNumber++;
            final String line = nextLine.trim();
            if (!line.isEmpty() && !line.startsWith("#")) {
                if (line.startsWith(">")) {
                    deck.setDescription(line.substring(1));
                } else {

                    // check line length
                    if (line.length() > MAX_LINE_LENGTH) {
                        lineErrors.add(MText.get(_S4, lineNumber, MAX_LINE_LENGTH));
                        continue;
                    }

                    // check for space delimiter
                    final int index = line.indexOf(' ');
                    if (index == -1) {
                        lineErrors.add(String.format("%s\n%s", MText.get(_S5, lineNumber), MText.get(_S6)));
                        continue;
                    }

                    // is expected card quantity a valid int?
                    int cardQuantity;
                    try {
                        cardQuantity = Integer.parseInt(line.substring(0,index));
                    } catch (NumberFormatException e) {
                        lineErrors.add(String.format("%s\n%s", MText.get(_S5, lineNumber), MText.get(_S6)));
                        continue;
                    }

                    // validate card name
                    final String cardName = line.substring(index+1).trim();
                    MagicCardDefinition cardDefinition = getCard(cardName);

                    for (int count=cardQuantity; count > 0; count--) {
                        deck.add(cardDefinition);
                    }

                    if (cardDefinition.isInvalid() || cardDefinition.isNonPlayable()) {
                        lineErrors.add(MText.get(_S7, lineNumber, cardDefinition.getName()));
                    }

                }
            }
        }

        if (lineErrors.size() > 0) {
            final StringBuffer sb = new StringBuffer();
            for (String lineError : lineErrors) {
                sb.append(lineError).append("\n");
            }
            deck.setInvalidDeck(sb.toString());
        }

        return deck;
    }

    private static long getDeckFileChecksum(final Path deckFilePath) {
        try (
            InputStream fis = new FileInputStream(deckFilePath.toFile());
            InputStream bis = new BufferedInputStream(fis);
        ) {
            CRC32 crc = new CRC32();
            int cnt;
            while ((cnt = bis.read()) != -1) {
                crc.update(cnt);
            }
            return crc.getValue();
        } catch (IOException ex) {
            Logger.getLogger(DeckUtils.class.getName()).log(Level.WARNING, null, ex);
            return 0;
        }
    }

    public static long getDeckFileChecksum(String name, DeckType deckType) {
        Path deckPath = DeckType.getDeckFolder(deckType);
        Path deckFile = deckPath.resolve(name + ".dec");
        return getDeckFileChecksum(deckFile);
    }

    public static long getDeckFileChecksum(MagicDeck aDeck) {
        return getDeckFileChecksum(aDeck.getName(), aDeck.getDeckType());
    }

    private static boolean isSamePath(Path p1, Path p2) {
        try {
            return Files.isSameFile(p1, p2);
        } catch (IOException ex) {
            Logger.getLogger(DeckUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private static DeckType getDeckType(Path deckFilePath) {
        Path deckFolder = Paths.get(FilenameUtils.getFullPath(deckFilePath.toString()));
        if (isSamePath(deckFolder, getPrebuiltDecksFolder())) {
            return DeckType.Preconstructed;
        }
        if (isSamePath(deckFolder, getFiremindDecksFolder())) {
            return DeckType.Firemind;
        }
        if (isSamePath(deckFolder, MagicFileSystem.getDataPath(DataPath.DECKS))) {
            return DeckType.Custom;
        }
        throw new RuntimeException("Unable to determine deck type from " + deckFilePath);
    }

    /**
     * Loads a deck file into a new MagicDeck instance.
     * <p>
     * @param deckFilePath full path of deck file to load.
     * @return
     */
    public static MagicDeck loadDeckFromFile(final Path deckFilePath) {
        final List<String> lines = getDeckFileContent(deckFilePath.toString());
        final MagicDeck deck = parseDeckFileContent(lines);
        deck.setFilename(deckFilePath.getFileName().toString());
        deck.setDeckFileChecksum(getDeckFileChecksum(deckFilePath));
        deck.setDeckType(getDeckType(deckFilePath));
        return deck;
    }

    public static MagicDeck loadDeckFromFile(String name, DeckType deckType) {
        Path deckPath = DeckType.getDeckFolder(deckType);
        return loadDeckFromFile(deckPath.resolve(name + ".dec"));
    }

    public static void loadAndSetPlayerDeck(final String filename, final DuelPlayerConfig player) {

        final MagicDeck deck = loadDeckFromFile(Paths.get(filename));

        if (deck.isValid()) {
            player.setDeck(deck);
            player.setDeckProfile(getDeckProfile(deck));
        } else {
            throw new InvalidDeckException(deck);
        }

    }

    private static MagicDeckProfile getDeckProfile(MagicDeck deck) {
        final MagicDeckProfile profile = new MagicDeckProfile(getDeckColor(deck));
        profile.setPreConstructed();
        return profile;
    }

    private static int[] getDeckColorCount(final MagicDeck deck) {
        final int[] colorCount = new int[MagicColor.NR_COLORS];
        for (MagicCardDefinition cardDef : deck) {
            final int colorFlags = cardDef.getColorFlags();
            for (final MagicColor color : MagicColor.values()) {
                if (color.hasColor(colorFlags)) {
                    colorCount[color.ordinal()]++;
                }
            }
        }
        return colorCount;
    }

    /**
     * Find up to 3 of the most common colors in the deck.
     */
    public static String getDeckColor(final MagicDeck deck) {
        final int[] colorCount = getDeckColorCount(deck);
        final StringBuilder colorText = new StringBuilder();
        while (colorText.length() < 3) {
            int maximum=0;
            int index=0;
            for (int i = 0; i < colorCount.length; i++) {
                if (colorCount[i] > maximum) {
                    maximum = colorCount[i];
                    index = i;
                }
            }
            if (maximum == 0) {
                break;
            }
            colorText.append(MagicColor.values()[index].getSymbol());
            colorCount[index]=0;
        }
        return colorText.toString();
    }

    private static List<File> getDeckFiles() {
        try {
            DeckFileVisitor dfv = new DeckFileVisitor();
            Files.walkFileTree(MagicFileSystem.getDataPath(DataPath.DECKS), dfv);
            return dfv.getFiles();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     *  Load a deck randomly chosen from the "decks" directory.
     *  (includes both custom & prebuilt decks).
     */
    public static void loadRandomDeckFile(final DuelPlayerConfig player) {
        List<File> deckFiles = getDeckFiles();
        if (deckFiles.isEmpty()) {
            // Creates a simple default deck.
            final MagicDeck deck = player.getDeck();
            deck.setFilename("Default.dec");
            final MagicCardDefinition creature = CardDefinitions.getCard("Elite Vanguard");
            final MagicCardDefinition land = CardDefinitions.getCard("Plains");
            for (int count = 24; count > 0; count--) {
                deck.add(creature);
            }
            for (int count = 16; count > 0; count--) {
                deck.add(land);
            }
            player.setDeckProfile(new MagicDeckProfile("w"));
        } else {
            loadAndSetPlayerDeck(deckFiles.get(MagicRandom.nextRNGInt(deckFiles.size())).toString(), player);
        }
    }

    /**
     * Extracts the name of a deck from its filename.
     */
    public static String getDeckNameFromFilename(final String deckFilename) {
        if (deckFilename.indexOf(DECK_EXTENSION) > 0) {
            return deckFilename.substring(0, deckFilename.lastIndexOf(DECK_EXTENSION));
        } else {
            return deckFilename;
        }
    }
    /**
     * Gets the name of a deck file without the extension.
     */
    public static String getDeckNameFromFile(final Path deckFile) {
        return getDeckNameFromFilename(deckFile.getFileName().toString());
    }

    public static MagicCardDefinition getCard(final String name) {
        try {
            return CardDefinitions.getCard(name);
        } catch (final RuntimeException e) {
            final MagicCardDefinition cardDefinition = new MagicCardDefinition();
            cardDefinition.setName(name);
            cardDefinition.setDistinctName(name);
            cardDefinition.setInvalid();
            return cardDefinition;
        }
    }

    public static List<File> getDecksContainingCard(final MagicCardDefinition cardDef) {
        final List<File> matchingDeckFiles = new ArrayList<>();
        if (cardDef != null) {
            for (File deckFile : getDeckFiles()) {
                try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(deckFile), "UTF-8"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (!line.startsWith("#")) {
                            if (line.contains(cardDef.getName())) {
                                matchingDeckFiles.add(deckFile);
                                break;
                            }
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return matchingDeckFiles;
    };

    public static Set<MagicCardDefinition> getDistinctCards(final MagicDeck aDeck) {
        final Set<MagicCardDefinition> distinctCards = new HashSet<>();
        for (final MagicCardDefinition card : aDeck) {
            distinctCards.add(card);
        }
        return distinctCards;
    }

    public static Path getDeckPath(MagicDeck deck) {
        Path deckPath = DeckType.getDeckFolder(deck.getDeckType());
        return deckPath.resolve(deck.getName() + ".dec");
    }

    static Path getPlayerDecksFolder() {
        return MagicFileSystem.getDataPath(MagicFileSystem.DataPath.DECKS);
    }

    static boolean isValidDeckFolder(Path dir) throws IOException {
        return Files.isSameFile(dir, getPlayerDecksFolder())
            || Files.isSameFile(dir, getPrebuiltDecksFolder())
            || Files.isSameFile(dir, getFiremindDecksFolder());
    }    
}
