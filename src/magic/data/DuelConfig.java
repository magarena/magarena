package magic.data;

import magic.utility.FileIO;
import java.io.File;
import java.util.Properties;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.DuelPlayerConfig;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;
import magic.utility.SortedProperties;

public class DuelConfig {

    private static final DuelConfig INSTANCE=new DuelConfig();

    // Properties file keys.
    private static final String START_LIFE          = "config.life";
    private static final String HAND_SIZE           = "config.hand";
    private static final String GAMES               = "config.games";
    private static final String CUBE                = "config.cube";
    private static final String PLAYER_ONE          = "p1.profile";
    private static final String PLAYER_TWO          = "p2.profile";

    public static final int MAX_PLAYERS = 2;

    // default values.
    private int startLife = 20;
    private int handSize = 7;
    private int games = 7;
    private MagicFormat cube = MagicFormat.ALL;
    private DuelPlayerConfig[] players = new DuelPlayerConfig[MAX_PLAYERS];

    // CTR
    public DuelConfig() {

        // Ensure DuelConfig has valid PlayerProfile references.
        // If missing then creates default profiles.
        PlayerProfiles.refreshMap();

        players[0] = new DuelPlayerConfig(
                PlayerProfiles.getDefaultHumanPlayer(),
                MagicDeckProfile.getDeckProfile(MagicDeckProfile.ANY_THREE)
        );
        players[1] = new DuelPlayerConfig(
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

    public MagicFormat getCube() {
        return cube;
    }
    public void setCube(final MagicFormat aCube) {
        this.cube = aCube;
    }

    public PlayerProfile getPlayerProfile(final int playerIndex) {
        return players[playerIndex].getProfile();
    }
    public void setPlayerProfile(final int playerIndex, final PlayerProfile playerProfile) {
        players[playerIndex].setProfile(playerProfile);
    }

    public MagicDeckProfile getPlayerDeckProfile(final int playerIndex) {
        return players[playerIndex].getDeckProfile();
    }

    public void setPlayerDeckProfile(final int playerIndex, final DeckType deckType, final String deckValue) {
        players[playerIndex].setDeckProfile(MagicDeckProfile.getDeckProfile(deckType, deckValue));
    }

    public void load(final Properties properties, final boolean loadPlayerDecks) {
        startLife = Integer.parseInt(properties.getProperty(START_LIFE, Integer.toString(startLife)));
        handSize = Integer.parseInt(properties.getProperty(HAND_SIZE, Integer.toString(handSize)));
        games = Integer.parseInt(properties.getProperty(GAMES, Integer.toString(games)));
        cube = MagicCustomFormat.get(properties.getProperty(CUBE, cube.getName()));
        loadPlayerConfigs(properties, loadPlayerDecks);
    }

    private void loadPlayerConfigs(final Properties properties, final boolean loadPlayerDecks) {
        setPlayerProfile(0, PlayerProfile.getHumanPlayer(properties.getProperty(PLAYER_ONE)));
        setPlayerProfile(1, PlayerProfile.getAiPlayer(properties.getProperty(PLAYER_TWO)));
        for (int i = 0; i < getPlayerConfigs().length; i++) {
            getPlayerConfig(i).setDeckProfile(
                    properties.getProperty(
                            getPlayerPrefix(i) + "deckProfile",
                            DeckType.Random + ";" + MagicDeckProfile.ANY_THREE)
            );
            if (loadPlayerDecks) {
                getPlayerConfig(i).loadDeck(properties, getPlayerPrefix(i));
            }
        }
    }

    public void load() {
        final File configFile = MagicDuel.getLatestDuelFile();
        final Properties properties = configFile.exists() ? FileIO.toProp(configFile) : new SortedProperties();
        load(properties, false);
    }

    public void save(final Properties properties) {
        properties.setProperty(START_LIFE, Integer.toString(startLife));
        properties.setProperty(HAND_SIZE, Integer.toString(handSize));
        properties.setProperty(GAMES, Integer.toString(games));
        properties.setProperty(CUBE, cube.getName());
        properties.setProperty(PLAYER_ONE, players[0].getProfile().getId());
        properties.setProperty(PLAYER_TWO, players[1].getProfile().getId());

        for (int i = 0; i < getPlayerConfigs().length; i++) {
            getPlayerConfig(i).save(properties, getPlayerPrefix(i));
        }
    }

    private static String getPlayerPrefix(final int index) {
        return "p" + (index + 1) + ".";
    }

    public static DuelConfig getInstance() {
        return INSTANCE;
    }

    public int getGamesRequiredToWinDuel() {
        return (int)Math.ceil(getNrOfGames()/2.0);
    }

    public DuelPlayerConfig getPlayerConfig(final int index) {
        return players[index];
    }

    public DuelPlayerConfig[] getPlayerConfigs() {
        return players;
    }

    public void setPlayerConfigs(DuelPlayerConfig[] aConfigs) {
        players = aConfigs;
    }

}
