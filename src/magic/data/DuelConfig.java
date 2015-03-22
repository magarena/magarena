package magic.data;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.model.MagicDeckProfile;
import magic.model.player.AiPlayer;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

/**
 * Represents the default settings when starting a new duel.
 * <p>
 * The settings are saved to "newduel.cfg" and are loaded
 * whenever the user starts a new duel.
 * <p>
 * Note that references to decks are specifically to deck
 * profiles and not actual decks (which are generated later).
 */
public class DuelConfig {

    private static final DuelConfig INSTANCE=new DuelConfig();

    // Properties file.
    private static final String CONFIG_FILENAME     = "newduel.cfg";
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
    private final PlayerProfile[] players = new PlayerProfile[MAX_PLAYERS];
    private final MagicDeckProfile[] playerDeckProfiles = new MagicDeckProfile[MAX_PLAYERS];

    // CTR
    public DuelConfig() {
        // Ensure DuelConfig has valid PlayerProfile references.
        // If missing then creates default profiles.
        PlayerProfiles.refreshMap();
        players[0] = PlayerProfiles.getDefaultHumanPlayer();
        players[1] = PlayerProfiles.getDefaultAiPlayer();
        playerDeckProfiles[0] = MagicDeckProfile.getDeckProfile(MagicDeckProfile.ANY_THREE);
        playerDeckProfiles[1] = MagicDeckProfile.getDeckProfile(MagicDeckProfile.ANY_THREE);
    }

    // CTR: copy constructor - a common way of creating a copy of an existing object.
    public DuelConfig(final DuelConfig duelConfig) {
        startLife=duelConfig.startLife;
        handSize=duelConfig.handSize;
        games=duelConfig.games;
        playerDeckProfiles[0] = duelConfig.getPlayerDeckProfile(0);
        playerDeckProfiles[1] = duelConfig.getPlayerDeckProfile(1);
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
        return players[playerIndex];
    }
    public void setPlayerProfile(final int playerIndex, final PlayerProfile playerProfile) {
        players[playerIndex] = playerProfile;
    }

    public MagicDeckProfile getPlayerDeckProfile(final int playerIndex) {
        return playerDeckProfiles[playerIndex];
    }
    public void setPlayerDeckProfile(final int playerIndex, final DeckType deckType, final String deckValue) {
        playerDeckProfiles[playerIndex] = MagicDeckProfile.getDeckProfile(deckType, deckValue);
    }

    public MagicAI[] getPlayerAIs() {
        final MagicAI playerAI;
        if (players[1] instanceof AiPlayer) {
            final AiPlayer aiPlayer = (AiPlayer)players[1];
            playerAI = aiPlayer.getAiType().getAI();
        } else {
            playerAI = MagicAIImpl.MMAB.getAI();
        }
        return new MagicAI[]{playerAI, playerAI};
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
        properties.setProperty(PLAYER_ONE, players[0].getId());
        properties.setProperty(PLAYER_TWO, players[1].getId());
        properties.setProperty(START_LIFE, Integer.toString(startLife));
        properties.setProperty(HAND_SIZE, Integer.toString(handSize));
        properties.setProperty(GAMES, Integer.toString(games));
        properties.setProperty(PLAYER_ONE_DECK, playerDeckProfiles[0].getDeckType().name() + ";" + playerDeckProfiles[0].getDeckValue());
        properties.setProperty(PLAYER_TWO_DECK, playerDeckProfiles[1].getDeckType().name() + ";" + playerDeckProfiles[1].getDeckValue());
        properties.setProperty(CUBE, cube);
    }

    public void save() {
        final Properties properties=new Properties();
        save(properties);
        try {
            FileIO.toFile(getConfigFile(), properties, "Duel configuration");
            System.err.println("Saved duel config");
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to save duel config");
        }
    }

    private static File getConfigFile() {
        return MagicFileSystem.getDataPath(DataPath.DUELS).resolve(CONFIG_FILENAME).toFile();
    }

    public static DuelConfig getInstance() {
        return INSTANCE;
    }


    /**
     * Dependent on AI type, but approximately the maximum number of
     * seconds the AI has to to make a decision.
     * <p>
     * Currently, this a user-adjustable setting from 1 to 8 seconds.
     */
    public int getAiDifficulty() {
        if (players[1] instanceof AiPlayer) {
            final AiPlayer player = (AiPlayer)players[1];
            return player.getAiLevel();
        } else {
            return 6;
        }
    }

    public int getAiExtraLife() {
        if (players[1] instanceof AiPlayer) {
            final AiPlayer aiPlayer = (AiPlayer)players[1];
            return aiPlayer.getExtraLife();
        } else {
            return 0;
        }
    }

    public int getGamesRequiredToWinDuel() {
        return (int)Math.ceil(getNrOfGames()/2.0);
    }
    
}
