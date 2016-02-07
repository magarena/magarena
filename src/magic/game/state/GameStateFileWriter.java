package magic.game.state;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;

public final class GameStateFileWriter {
    private GameStateFileWriter() {}

    private static final String PROP_Version = "magarenaVersion";
    private static final String PROP_PlayerCount = "players";
    private static final String PROP_Difficulty = "difficulty";
    private static final String PROP_StartPlayerIndex = "startPlayerIndex";

    public static void createSaveGameFile(final GameState gameState, final File aFile) {
        final Properties properties = getNewSortedProperties();
        setGameProperties(properties, gameState);
        setAllPlayerProperties(properties, gameState);
        savePropertyFile(properties, aFile);
    }

    public static void createSaveGameFile(final GameState gameState, final String filename) {
        final File propertyFile = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.SAVED_GAMES).resolve(filename).toFile();
        createSaveGameFile(gameState, propertyFile);
    }

    private static void savePropertyFile(final Properties properties, final File aFile) {
        try (final FileOutputStream fos = new FileOutputStream(aFile)) {
            properties.store(fos, "Magarena Saved Game");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void setGameProperties(final Properties properties, final GameState gameState) {
        properties.setProperty(PROP_Version, MagicSystem.VERSION);
        properties.setProperty(PROP_PlayerCount, Integer.toString(gameState.getPlayers().size()));
        properties.setProperty(PROP_Difficulty, Integer.toString(gameState.getDifficulty()));
        properties.setProperty(PROP_StartPlayerIndex, Integer.toString(gameState.getStartPlayerIndex()));
    }

    private static void setAllPlayerProperties(final Properties properties, final GameState gameState) {
        for (int i = 0; i < gameState.getPlayers().size(); i++) {
            final GamePlayerState player = gameState.getPlayers().get(i);
            final String playerKeyPrefix = "p" + i;
            setPlayerProperties(properties, player, playerKeyPrefix);
            setCardZoneProperties(player.getLibrary(), playerKeyPrefix + ".library", properties);
            setCardZoneProperties(player.getPermanents(), playerKeyPrefix + ".permanent", properties);
            setCardZoneProperties(player.getHand(), playerKeyPrefix + ".hand", properties);
            setCardZoneProperties(player.getGraveyard(), playerKeyPrefix + ".graveyard", properties);
            setCardZoneProperties(player.getExiled(), playerKeyPrefix + ".exiled", properties);
        }
    }

    private static void setPlayerProperties(final Properties properties, final GamePlayerState player, final String keyPrefix) {
        properties.setProperty(keyPrefix + ".name", player.getName());
        properties.setProperty(keyPrefix + ".face", Integer.toString(player.getFace()));
        properties.setProperty(keyPrefix + ".life", Integer.toString(player.getLife()));
        properties.setProperty(keyPrefix + ".ai", player.getAiType());
        properties.setProperty(keyPrefix + ".deck.color", player.getDeckProfileColors());
    }

    private static void setCardZoneProperties(final List<GameCardState> cards, final String keyPrefix, final Properties properties) {
        for (int j = 0; j < cards.size(); j++) {
            final GameCardState card = cards.get(j);
            final String key = keyPrefix + j;
            properties.setProperty(key + ".card", card.getCardName());
            properties.setProperty(key + ".quantity", Integer.toString(card.getQuantity()));
            if (card.isTapped()) {
                properties.setProperty(key + ".tapped", Boolean.toString(card.isTapped()));
            }
        }
    }

    @SuppressWarnings("serial")
    private static Properties getNewSortedProperties() {
        return new Properties() {
            @Override
            public synchronized Enumeration<Object> keys() {
                return Collections.enumeration(new TreeSet<>(super.keySet()));
            }
        };
    }

}
