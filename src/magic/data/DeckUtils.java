package magic.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicRandom;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class DeckUtils {

    public static final String DECK_EXTENSION=".dec";
    private static final int DECK_FILE_MAX_LINES = GeneralConfig.getInstance().getDeckFileMaxLines();

    public static final FileFilter DECK_FILEFILTER=new FileFilter() {
        @Override
        public boolean accept(final File file) {
            return file.isDirectory()||file.getName().endsWith(DECK_EXTENSION);
        }
        @Override
        public String getDescription() {
            return "Magarena deck";
        }
    };

    private static final String[] CARD_TYPES={"creatures","spells","lands"};

    public static String getDeckFolder() {
        return MagicFileSystem.getDataPath(DataPath.DECKS).toString();
    }

    public static Path getPrebuiltDecksFolder() {
        final Path decksPath = Paths.get(getDeckFolder());
        return decksPath.resolve("prebuilt");
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
            final String name = CardDefinitions.getASCII(cardDefinition.getFullName());
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
                magic.data.FileIO.close(writer);
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
    private static List<String> getDeckFileContent(final String filename) throws IOException {
        return FileIO.toStrList(new File(filename));
    }

    private static MagicDeck parseDeckFileContent(final List<String> content) {

        final MagicDeck deck = new MagicDeck();

        if (content.isEmpty()) {
            deck.setInvalidDeck("Deck file is empty.");
            return deck;
        }
        
        if (content.size() > DECK_FILE_MAX_LINES) {
            deck.setInvalidDeck("Lines in file exceeds " + DECK_FILE_MAX_LINES + ".");
            return deck;
        }

        final int MAX_LINE_ERRORS = 3;
        final int MAX_LINE_LENGTH = 50; // characters.
        int lineNumber = 0;
        final List<String> lineErrors = new ArrayList<>();
        
        for (final String nextLine: content) {

            if (lineErrors.size() > MAX_LINE_ERRORS) {
                lineErrors.remove(lineErrors.size()-1);
                lineErrors.add("...more...");
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
                        lineErrors.add("line " + lineNumber +": line length exceeds " + MAX_LINE_LENGTH + " characters.");
                        continue;
                    }

                    // check for space delimiter
                    final int index = line.indexOf(' ');
                    if (index == -1) {
                        lineErrors.add("line " + lineNumber +": invalid line format.\nExpected: <quantity><space><card name>");
                        continue;
                    }

                    // is expected card quantity a valid int?
                    int cardQuantity;
                    try {
                        cardQuantity = Integer.parseInt(line.substring(0,index));
                    } catch (NumberFormatException e) {
                        lineErrors.add("line " + lineNumber +": invalid line format.\nExpected: <quantity><space><card name>");
                        continue;
                    }

                    // validate card name
                    final String cardName = line.substring(index+1).trim();
                    MagicCardDefinition cardDefinition = getCard(cardName);

                    for (int count=cardQuantity; count > 0; count--) {
                        deck.add(cardDefinition);
                    }

                    if (!cardDefinition.isValid() || cardDefinition.isHidden()) {
                        lineErrors.add("line " + lineNumber +": invalid card (" + cardDefinition.getName() +").");
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

    /**
     * Loads a deck file into a new MagicDeck instance.
     * <p>
     * @param deckFilePath full path of deck file to load.
     * @return
     */
    public static MagicDeck loadDeckFromFile(final Path deckFilePath) throws IOException {
        final MagicDeck deck = getDeck(getDeckFileContent(deckFilePath.toString()));
        deck.setFilename(deckFilePath.getFileName().toString());
        return deck;
    }

    private static MagicDeck getDeck(final List<String> content) {
        return parseDeckFileContent(content);
    }

    public static void loadDeck(final String filename,final MagicPlayerDefinition player) {

        final List<String> content;
        try {
            content = getDeckFileContent(filename);
        } catch (IOException ex) {
            throw new RuntimeException("Invalid deck file: " + filename, ex);
        }

        if (content.isEmpty()) { return; }

        final int[] colorCount = new int[MagicColor.NR_COLORS];
        final MagicDeck deck = player.getDeck();
        final MagicDeck unsupported = new MagicDeck();

        deck.setFilename(new File(filename).getName());
        deck.clear(); // remove previous cards

        for (final String nextLine: content) {
            final String line = nextLine.trim();
            if (!line.isEmpty()&&!line.startsWith("#")) {
                if (line.startsWith(">")) {
                    deck.setDescription(line.substring(1));
                } else {
                    final int index = line.indexOf(' ');
                    final int amount = Integer.parseInt(line.substring(0,index));
                    final String name=line.substring(index+1).trim();
                    final MagicCardDefinition cardDefinition = getCard(name);
                    for (int count=amount;count>0;count--) {
                        final int colorFlags=cardDefinition.getColorFlags();
                        for (final MagicColor color : MagicColor.values()) {
                            if (color.hasColor(colorFlags)) {
                                colorCount[color.ordinal()]++;
                            }
                        }
                        if (cardDefinition.isValid()) {
                            deck.add(cardDefinition);
                        } else {
                            unsupported.add(cardDefinition);
                            break; // multiple copies of unsupported card -> ignore other copies
                        }
                    }
                }
            }
        }

        showUnsupportedCards(unsupported);

        // Find up to 3 of the most common colors in the deck.
        final StringBuilder colorText=new StringBuilder();
        while (colorText.length()<3) {
            int maximum=0;
            int index=0;
            for (int i=0;i<colorCount.length;i++) {
                if (colorCount[i]>maximum) {
                    maximum=colorCount[i];
                    index=i;
                }
            }
            if (maximum==0) {
                break;
            }
            colorText.append(MagicColor.values()[index].getSymbol());
            colorCount[index]=0;
        }
        final MagicDeckProfile profile = new MagicDeckProfile(colorText.toString());
        profile.setPreConstructed();
        player.setDeckProfile(profile);
    }

    public static void showUnsupportedCards(final MagicDeck unsupported) {
        if (unsupported.isEmpty()) {
            return;
        }

        // show error message for unsupported cards
        final StringBuilder sb = new StringBuilder();
        sb.append("The loaded deck contained unsupported card(s): ");

        // generate list of unsupported cards
        for (int i = 0; i < unsupported.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(unsupported.get(i).getName());
        }

        // options panel doesn't have automatic text wrapping
        // because the method that provides max char limit isn't
        // coded, so override that method
        final JOptionPane cleanupPane = new JOptionPane(sb.toString(), JOptionPane.ERROR_MESSAGE) {
            private static final long serialVersionUID = 232L;

            @Override
            public int getMaxCharactersPerLineCount() {
                return 70;
            }
        };
        cleanupPane.createDialog(null, "Unsupported Cards").setVisible(true);

        unsupported.clear();
    }

    private static void retrieveDeckFiles(final File folder,final List<File> deckFiles) {
        final File[] files=folder.listFiles();
        for (final File file : files) {

            if (file.isDirectory()) {
                retrieveDeckFiles(file,deckFiles);
            } else if (file.getName().endsWith(DECK_EXTENSION)) {
                deckFiles.add(file);
            }
        }
    }

    /**
     *  Load a deck randomly chosen from the "decks" directory.
     *  (includes both custom & prebuilt decks).
     */
    public static void loadRandomDeckFile(final MagicPlayerDefinition player) {
        final File deckFile=new File(getDeckFolder());
        final List<File> deckFiles=new ArrayList<>();
        retrieveDeckFiles(deckFile,deckFiles);
        final int size=deckFiles.size();
        if (size==0) {
            // Creates a simple default deck.
            final MagicDeck deck = player.getDeck();
            deck.setFilename("Default.dec");
            final MagicCardDefinition creature=CardDefinitions.getCard("Elite Vanguard");
            final MagicCardDefinition land=CardDefinitions.getCard("Plains");
            for (int count=24;count>0;count--) {
                deck.add(creature);
            }
            for (int count=16;count>0;count--) {
                deck.add(land);
            }
            player.setDeckProfile(new MagicDeckProfile("w"));
        } else {
            loadDeck(deckFiles.get(MagicRandom.nextRNGInt(size)).toString(),player);
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
            cardDefinition.setIsValid(false);
            return cardDefinition;
        }
    }
}
