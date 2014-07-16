package magic.data;

import magic.MagicMain;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicRandom;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

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
import java.util.Collections;

public class DeckUtils {

    public static final String DECK_EXTENSION=".dec";

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
        return MagicMain.getGamePath()+File.separator+"decks";
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

        final List<SortedMap<String,Integer>> cardMaps=new ArrayList<SortedMap<String,Integer>>();
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
                writer.write(">" + description);
            }
        } catch (final IOException ex) {
            isSuccessful = false;
            System.err.println("ERROR! Unable to save deck");
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            magic.data.FileIO.close(writer);
        }

        return isSuccessful;

    }

    private static List<String> getDeckFileContent(final String filename) {
        List<String> content = Collections.emptyList();
        try {
            content = FileIO.toStrList(new File(filename));
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + filename);
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return content;
    }

    private static MagicDeck parseDeckFileContent(final List<String> content) {

        final MagicDeck unsupported = new MagicDeck();
        final MagicDeck deck = new MagicDeck();

        final int[] colorCount = new int[MagicColor.NR_COLORS];
        for (final String nextLine: content) {
            final String line = nextLine.trim();
            if (!line.isEmpty()&&!line.startsWith("#")) {
                if (line.startsWith(">")) {
                    deck.setDescription(line.substring(1));
                } else {
                    final int index = line.indexOf(' ');
                    final int amount = Integer.parseInt(line.substring(0,index));
                    final String name=line.substring(index+1).trim();
                    final MagicCardDefinition cardDefinition = CardDefinitions.getCard(name);
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

        return deck;
    }

    public static MagicDeck loadDeckFromFile(final String filename) {
        MagicDeck deck = null;
        final List<String> content = getDeckFileContent(filename);
        if (content.isEmpty() == false) {
            final File deckFile = new File(filename);
            try {
                deck = parseDeckFileContent(content);
                deck.setFilename(deckFile.getName());
            } catch (Exception e) {
                System.err.println("Invalid deck file (" + filename + ") - " + e.toString());
                JOptionPane.showMessageDialog(
                        MagicMain.rootFrame,
                        "Failed to parse deck file -\n" + deckFile.getName() +"\n\n" + e.toString(),
                        "Invalid Deck File",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        return deck;
    }

    public static void loadDeck(final String filename,final MagicPlayerDefinition player) {

        final List<String> content = getDeckFileContent(filename);
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
                    final MagicCardDefinition cardDefinition = CardDefinitions.getCard(name);
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
        final List<File> deckFiles=new ArrayList<File>();
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

}
