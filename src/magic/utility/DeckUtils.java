package magic.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
import magic.exception.InvalidDeckException;
import magic.model.DuelPlayerConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicRandom;
import magic.utility.MagicFileSystem.DataPath;
import org.apache.commons.io.FilenameUtils;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

public class DeckUtils {

    public static final String DECK_EXTENSION=".dec";

    private static final String[] CARD_TYPES={"creatures","spells","lands"};

    public static Path getDecksFolder() {
        return MagicFileSystem.getDataPath(DataPath.DECKS);
    }

    public static Path getPrebuiltDecksFolder() {
        Path decksPath = getDecksFolder().resolve("prebuilt");
        MagicFileSystem.verifyDirectoryPath(decksPath);
        return decksPath;
    }

    public static Path getFiremindDecksFolder() {
        Path decksPath = getDecksFolder().resolve("firemind");
        MagicFileSystem.verifyDirectoryPath(decksPath);
        return decksPath;
    }

    public static void createDeckFolder() {
        final File deckFolderFile = getDecksFolder().toFile();
        if (!deckFolderFile.exists() && !deckFolderFile.mkdir()) {
            System.err.println("WARNING. Unable to create " + getDecksFolder());
        }
    }

    public static boolean saveDeck(final String filename, final MagicDeck deck) {

        final List<SortedMap<String,Integer>> cardMaps=new ArrayList<>();
        boolean isSuccessful = true;

        for (int count=3;count>0;count--) {
            cardMaps.add(new TreeMap<>());
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

        //save deck
        try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), UTF_8)) {
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
     */
    private static List<String> getDeckFileContent(final String filename) {
        try {
            return FileIO.toStrList(new File(filename));
        } catch (IOException ex) {
            throw new InvalidDeckException("Invalid deck (\".dec\") file: " + filename, ex);
        }
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
        return deckFile.toFile().exists() ? getDeckFileChecksum(deckFile) : -1;
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
        if (deckFilePath == null || !deckFilePath.toFile().exists()) {
            throw new InvalidDeckException("File " + deckFilePath + " does not exist");
        }
        final List<String> lines = getDeckFileContent(deckFilePath.toString());
        final MagicDeck deck = DeckParser.parseLines(lines);
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

    public static List<File> getDeckFiles() {
        try {
            DeckFileVisitor dfv = new DeckFileVisitor();
            Files.walkFileTree(MagicFileSystem.getDataPath(DataPath.DECKS), Collections.singleton(FOLLOW_LINKS),
                    Integer.MAX_VALUE, dfv);
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
    }

    public static Set<MagicCardDefinition> getDistinctCards(final MagicDeck aDeck) {
        final Set<MagicCardDefinition> distinctCards = new HashSet<>();
        distinctCards.addAll(aDeck);
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

    /**
     * Returns string as {@code filename}.dec
     */
    public static String getNormalizedFilename(String filename) {
        return filename.endsWith(".dec") ? filename : filename + ".dec";
    }

    /**
     * Searches for a deck file and returns the first matching file
     * in the \decks\* folder structure.
     */
    public static File findDeckFile(String filename) {
        String target = getNormalizedFilename(filename);
        for (File deckFile : DeckUtils.getDeckFiles()) {
            if (target.equals(deckFile.getName())) {
                return deckFile;
            }
        }
        return null;
    }
}
