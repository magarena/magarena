package magic.data;

import java.io.File;
import java.util.Properties;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicPlayerDefinition;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

/**
 * Represents the default settings when starting a new duel.
 * <p>
 * The settings are saved to CONFIG_FILENAME and are loaded
 * whenever the user starts a new duel.
 * <p>
 * Note that references to decks are specifically to deck
 * profiles and not actual decks (which are generated later).
 */
public class DuelConfig {

    private static final DuelConfig INSTANCE=new DuelConfig();

    // Properties file.
    private static final String CONFIG_FILENAME     = MagicDuel.getDuelFile().getName();
    // Properties file keys.
    private static final String START_LIFE          = "life";
    private static final String HAND_SIZE           = "hand";
    private static final String GAMES               = "games";
    private static final String CUBE                = "cube";
    private static final String PLAYER_ONE          = "playerOne";
    private static final String PLAYER_TWO          = "playerTwo";
    private static final String PLAYER_ONE_DECK     = "playerOneDeck";
    private static final String PLAYER_TWO_DECK     = "playerTwoDeck";

    public static final int MAX_PLAYERS = 2;

    // default values.
    private int startLife = 20;
    private int handSize = 7;
    private int games = 7;
    private String cube = CubeDefinitions.getCubeNames()[0];
    private MagicPlayerDefinition[] playerDefs = new MagicPlayerDefinition[MAX_PLAYERS];

    // CTR
    public DuelConfig() {
        
        // Ensure DuelConfig has valid PlayerProfile references.
        // If missing then creates default profiles.
        PlayerProfiles.refreshMap();

        playerDefs[0] = new MagicPlayerDefinition(
                PlayerProfiles.getDefaultHumanPlayer(),
                MagicDeckProfile.getDeckProfile(MagicDeckProfile.ANY_THREE)
        );
        playerDefs[1] = new MagicPlayerDefinition(
                PlayerProfiles.getDefaultAiPlayer(),
                MagicDeckProfile.getDeckProfile(MagicDeckProfile.ANY_THREE)
        );
    }

    public int getStartLife() {
        return startLife;
    }
    public void setStartLife(final int startLife) {
        this.startLife = startLife;
    }

    public int getHandSize() {
        return handSize;
    }
    public void setHandSize(final int handSize) {
        this.handSize = handSize;
    }

    public void setNrOfGames(final int aGames) {
        this.games = aGames;
    }
    public int getNrOfGames() {
        return games;
    }

    public String getCube() {
        return cube;
    }
    public void setCube(final String cube) {
        this.cube=cube;
    }

    public PlayerProfile getPlayerProfile(final int playerIndex) {
        return playerDefs[playerIndex].getProfile();
    }
    public void setPlayerProfile(final int playerIndex, final PlayerProfile playerProfile) {
        playerDefs[playerIndex].setProfile(playerProfile);
    }

    public MagicDeckProfile getPlayerDeckProfile(final int playerIndex) {
        return playerDefs[playerIndex].getDeckProfile();
    }

    public void setPlayerDeckProfile(final int playerIndex, final DeckType deckType, final String deckValue) {
        playerDefs[playerIndex].setDeckProfile(MagicDeckProfile.getDeckProfile(deckType, deckValue));
    }

    public void load(final Properties properties) {
        startLife=Integer.parseInt(properties.getProperty(START_LIFE,Integer.toString(startLife)));
        handSize=Integer.parseInt(properties.getProperty(HAND_SIZE,Integer.toString(handSize)));
        games=Integer.parseInt(properties.getProperty(GAMES,Integer.toString(games)));
        cube=properties.getProperty(CUBE,cube);
        setPlayerProfile(0, PlayerProfile.getHumanPlayer(properties.getProperty(PLAYER_ONE)));
        setPlayerProfile(1, PlayerProfile.getAiPlayer(properties.getProperty(PLAYER_TWO)));
        setPlayerDeckProfile(0, properties.getProperty(PLAYER_ONE_DECK, DeckType.Random + ";" + MagicDeckProfile.ANY_THREE));
        setPlayerDeckProfile(1, properties.getProperty(PLAYER_TWO_DECK, DeckType.Random + ";" + MagicDeckProfile.ANY_THREE));
        for (int index = 0; index < getPlayerDefinitions().length; index++) {
            getPlayerDefinition(index).load(properties, getPlayerPrefix(index));
        }
    }

    private void setPlayerDeckProfile(final int playerIndex, final String deckPropertyValue) {
        final DeckType deckType = DeckType.valueOf(deckPropertyValue.split(";", 0)[0]);
        final String deckValue = deckPropertyValue.split(";", 0)[1];
        setPlayerDeckProfile(playerIndex, deckType, deckValue);
    }

    public void load() {
        final File configFile = getConfigFile();
        final Properties properties = configFile.exists() ? FileIO.toProp(configFile) : new Properties();
        load(properties);
    }

    public void save(final Properties properties) {
        properties.setProperty(PLAYER_ONE, playerDefs[0].getProfile().getId());
        properties.setProperty(PLAYER_TWO, playerDefs[1].getProfile().getId());
        properties.setProperty(START_LIFE, Integer.toString(startLife));
        properties.setProperty(HAND_SIZE, Integer.toString(handSize));
        properties.setProperty(GAMES, Integer.toString(games));
        properties.setProperty(PLAYER_ONE_DECK, playerDefs[0].getDeckProfile().getDeckType().name() + ";" + playerDefs[0].getDeckProfile().getDeckValue());
        properties.setProperty(PLAYER_TWO_DECK, playerDefs[1].getDeckProfile().getDeckType().name() + ";" + playerDefs[1].getDeckProfile().getDeckValue());
        properties.setProperty(CUBE, cube);
        for (int index = 0; index < getPlayerDefinitions().length; index++) {
            getPlayerDefinition(index).save(properties, getPlayerPrefix(index));
        }
    }

    private static String getPlayerPrefix(final int index) {
        return "p"+(index+1)+".";
    }

    private static File getConfigFile() {
        return MagicFileSystem.getDataPath(DataPath.DUELS).resolve(CONFIG_FILENAME).toFile();
    }

    public static DuelConfig getInstance() {
        return INSTANCE;
    }

    public int getGamesRequiredToWinDuel() {
        return (int)Math.ceil(getNrOfGames()/2.0);
    }

    public MagicPlayerDefinition getPlayerDefinition(final int index) {
        return playerDefs[index];
    }

    public MagicPlayerDefinition[] getPlayerDefinitions() {
        return playerDefs;
    }

    public void setPlayerDefinitions(MagicPlayerDefinition[] aPlayerDefinitions) {
        playerDefs = aPlayerDefinitions;
    }
}
