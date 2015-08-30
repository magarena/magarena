package magic.game.state;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;

public final class GameStateFileReader {
    private GameStateFileReader() {}

    public static final String TEST_FILE_EXTENSION = ".game";

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

    private static void setCardsZoneState(final Properties prop, final String zoneName, final List<GameCardState> cards, final int playerIndex) {

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
    
}
