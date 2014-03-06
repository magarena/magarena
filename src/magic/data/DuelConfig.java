package magic.data;

import magic.MagicMain;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.model.MagicColor;
import magic.model.MagicPlayerProfile;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class DuelConfig {

    private static final DuelConfig INSTANCE=new DuelConfig();

    private static final String ANY_DECK="@";
    private static final String ANY_THREE="***";
    private static final String ANY_TWO="**";
    private static final String ANY_ONE="*";

    private static final String CONFIG_FILENAME="newduel.cfg";
    private static final String START_LIFE="life";
    private static final String HAND_SIZE="hand";
    private static final String GAMES="games";
    private static final String PLAYER="player";
    private static final String OPPONENT="opponent";
    private static final String CUBE="cube";
    private static final String AI="ai";

    // default values.
    private int startLife = 20;
    private int handSize = 7;
    private int games = 7;
    private String playerOneDeckGenerator = ANY_THREE;
    private String playerTwoDeckGenerator = ANY_THREE;
    private String cube = CubeDefinitions.getCubeNames()[0];
    private String ai = "minimax";
    private PlayerProfile playerOne = null;
    private PlayerProfile playerTwo = null;

    // CTR
    public DuelConfig() {
        // Ensure DuelConfig has valid PlayerProfile references.
        // If missing then creates default profiles.
        PlayerProfiles.refreshMap();
    }

    // CTR: copy constructor - a common way of creating a copy of an existing object.
    public DuelConfig(final DuelConfig duelConfig) {
        startLife=duelConfig.startLife;
        handSize=duelConfig.handSize;
        games=duelConfig.games;
        playerOneDeckGenerator=duelConfig.playerOneDeckGenerator;
        playerTwoDeckGenerator=duelConfig.playerTwoDeckGenerator;
        ai=duelConfig.ai;
    }

    public PlayerProfile getPlayerOneProfile() {
        return playerOne;
    }
    public void setPlayerOneProfile(PlayerProfile playerProfile) {
        this.playerOne = playerProfile;
    }

    public PlayerProfile getPlayerTwoProfile() {
        return playerTwo;
    }
    public void setPlayerTwoProfile(PlayerProfile playerProfile) {
        this.playerTwo = playerProfile;
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

    private static MagicPlayerProfile getMagicPlayerProfile(final String colorText) {
        if (ANY_DECK.equals(colorText)) {
            return new MagicPlayerProfile("");
        } else if (ANY_THREE.equals(colorText)) {
            return new MagicPlayerProfile(MagicColor.getRandomColors(3));
        } else if (ANY_TWO.equals(colorText)) {
            return new MagicPlayerProfile(MagicColor.getRandomColors(2));
        } else if (ANY_ONE.equals(colorText)) {
            return new MagicPlayerProfile(MagicColor.getRandomColors(1));
        } else if (DeckGenerators.getInstance().getGeneratorNames().contains(colorText)) {
            // custom deck generator
            return new MagicPlayerProfile("", colorText);
        }
        return new MagicPlayerProfile(colorText);
    }

    public MagicPlayerProfile getMagicPlayerProfile() {
        return getMagicPlayerProfile(playerOneDeckGenerator);
    }

    public String getPlayerOneDeckGenerator() {
        return playerOneDeckGenerator;
    }

    public void setPlayerOneDeckGenerator(final String deckGenerator) {
        playerOneDeckGenerator = deckGenerator;
    }

    public String getPlayerTwoDeckGenerator() {
        return playerTwoDeckGenerator;
    }

    public void setPlayerTwoDeckGenerator(final String deckGenerator) {
        playerTwoDeckGenerator = deckGenerator;
    }

    public MagicPlayerProfile getMagicOpponentProfile() {
        return getMagicPlayerProfile(playerTwoDeckGenerator);
    }

    public String getCube() {
        return cube;
    }

    public void setCube(final String cube) {
        this.cube=cube;
    }

    public MagicAI[] getPlayerAIs() {
        final MagicAI playerAI = MagicAIImpl.getAI(ai).getAI();
        return new MagicAI[]{playerAI, playerAI};
    }

    public String getAI() {
        return ai;
    }

    public void setAI(final String ai) {
        this.ai=ai;
    }

    public void load(final Properties properties) {
        setPlayerOneProfile(PlayerProfile.getHumanPlayer(properties.getProperty("HumanPlayer")));
        setPlayerTwoProfile(PlayerProfile.getAiPlayer(properties.getProperty("AiPlayer")));
        startLife=Integer.parseInt(properties.getProperty(START_LIFE,Integer.toString(startLife)));
        handSize=Integer.parseInt(properties.getProperty(HAND_SIZE,Integer.toString(handSize)));
        games=Integer.parseInt(properties.getProperty(GAMES,Integer.toString(games)));
        playerOneDeckGenerator=properties.getProperty(PLAYER,playerOneDeckGenerator);
        playerTwoDeckGenerator=properties.getProperty(OPPONENT,playerTwoDeckGenerator);
        cube=properties.getProperty(CUBE,cube);
        ai=properties.getProperty(AI,ai);
    }

    public void load() {
        final File configFile = getConfigFile();
        final Properties properties = configFile.exists() ? FileIO.toProp(configFile) : new Properties();
        load(properties);
    }

    public void save(final Properties properties) {
        properties.setProperty("HumanPlayer", getPlayerOneProfile().getId());
        properties.setProperty("AiPlayer", getPlayerTwoProfile().getId());
        properties.setProperty(START_LIFE, Integer.toString(startLife));
        properties.setProperty(HAND_SIZE, Integer.toString(handSize));
        properties.setProperty(GAMES, Integer.toString(games));
        properties.setProperty(PLAYER, playerOneDeckGenerator);
        properties.setProperty(OPPONENT, playerTwoDeckGenerator);
        properties.setProperty(CUBE, cube);
        properties.setProperty(AI, ai);
    }

    public void save() {
        final Properties properties=new Properties();
        save(properties);
        try { //save config
            FileIO.toFile(getConfigFile(), properties, "Duel configuration");
            System.err.println("Saved duel config");
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to save duel config");
        }
    }

    private static File getConfigFile() {
        return new File(MagicMain.getSavedDuelsPath(), CONFIG_FILENAME);
    }

    public static DuelConfig getInstance() {
        return INSTANCE;
    }
}
