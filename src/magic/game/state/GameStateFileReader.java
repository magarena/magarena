package magic.game.state;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import magic.MagicMain;
import magic.data.FileIO;
import magic.utility.MagicFileSystem;

public final class GameStateFileReader {
    private GameStateFileReader() {}

    private static final String PROP_PlayerCount = "players";
    private static final String PROP_Difficulty = "difficulty";
    private static final String PROP_StartPlayerIndex = "startPlayerIndex";
   
    public static GameState loadGameStateFromFile(final String filename) {
        final File propertyFile = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.SAVED_GAMES).resolve(filename).toFile();
        final Properties prop = FileIO.toProp(propertyFile);
        final GameState gameState = new GameState();
        //
        gameState.setDifficulty(Integer.parseInt(prop.getProperty(PROP_Difficulty)));
        gameState.setStartPlayerIndex(Integer.parseInt(prop.getProperty(PROP_StartPlayerIndex)));
        //
        final int players = Integer.parseInt(prop.getProperty(PROP_PlayerCount));
        for (int i = 0; i < players; i++) {
            final GamePlayerState playerState = gameState.getPlayer(i);
            setPlayerState(prop, gameState, i);
            setCardsZoneState(prop, "hand", playerState.getHand(), i);
            setCardsZoneState(prop, "permanent", playerState.getPermanents(), i);
            setCardsZoneState(prop, "library", playerState.getLibrary(), i);
            setCardsZoneState(prop, "graveyard", playerState.getGraveyard(), i);
            setCardsZoneState(prop, "exiled", playerState.getExiled(), i);
        }
        return gameState;
    }

    private static void setPlayerState(final Properties prop, final GameState scenario, final int playerIndex) {
        final GamePlayerState player = scenario.getPlayer(playerIndex);
        final String keyPrefix = "p" + playerIndex;
        player.setName(prop.getProperty(keyPrefix + ".name"));
        player.setFace(Integer.parseInt(prop.getProperty(keyPrefix + ".face")));
        player.setLife(Integer.parseInt(prop.getProperty(keyPrefix + ".life")));
        player.setAiType(prop.getProperty(keyPrefix + ".ai"));
        player.setDeckProfileColors(prop.getProperty(keyPrefix + ".deck.color"));
    }

    private static void setCardsZoneState(
            final Properties prop,
            final String zoneName,
            final List<GameCardState> cards,
            final int playerIndex) {

        final List<String> usedKeys = new ArrayList<>();
        for (int i = 0; i < prop.size(); i++) {
            // card name
            String cardName = "";
            final String cardKey = "p" + playerIndex + "." + zoneName + i + ".card";
            if (prop.containsKey(cardKey)) {
                cardName = prop.getProperty(cardKey);
                usedKeys.add(cardKey);
            }
            // card quantity
            int cardQuantity = 1;
            final String quantityKey = "p" + playerIndex + "." + zoneName + i + ".quantity";
            if (prop.containsKey(quantityKey)) {
                cardQuantity = Integer.parseInt(prop.getProperty(quantityKey));
                usedKeys.add(quantityKey);
            }
            // card tapped state
            boolean isCardTapped = false;
            final String tappedKey = "p" + playerIndex + "." + zoneName + i + ".tapped";
            if (prop.containsKey(tappedKey)) {
                isCardTapped = Boolean.parseBoolean(prop.getProperty(tappedKey));
                usedKeys.add(tappedKey);
            }
            if (!cardName.isEmpty()) {
                cards.add(new GameCardState(cardName, cardQuantity, isCardTapped));
            }
        }
        for (String usedKey : usedKeys) {
            prop.remove(usedKey);
        }
    }
    
    public static final FileFilter TEST_FILE_FILTER = new FileFilter() {
        private static final String TEST_FILE_EXTENSION = ".game";
        @Override
        public boolean accept(final File file) {
            return file.isDirectory() || file.getName().endsWith(TEST_FILE_EXTENSION);
        }
        @Override
        public String getDescription() {
            return "Saved Game File";
        }
    };

    public static String getSaveGameFilename() {
        final JFileChooser fileChooser = new JFileChooser(MagicFileSystem.getDataPath(MagicFileSystem.DataPath.SAVED_GAMES).toFile());
        fileChooser.setDialogTitle("Load & resume saved game");
        fileChooser.setFileFilter(TEST_FILE_FILTER);
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Add the description preview pane
//        fileChooser.setAccessory(new DeckDescriptionPreview(fileChooser));
        final int action = fileChooser.showOpenDialog(MagicMain.rootFrame);
        if (action == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getName();
        } else {
            return "";
        }
    }
}
