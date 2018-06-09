package magic.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import magic.data.DeckGenerators;
import magic.data.DuelConfig;
import magic.data.stats.MagicStats;
import magic.model.phase.MagicDefaultGameplay;
import magic.model.player.PlayerProfile;
import magic.utility.DeckUtils;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.SortedProperties;

public class MagicDuel {

    private static final String GAME = "duel.game";
    private static final String PLAYED = "duel.played";
    private static final String WON = "duel.won";
    private static final String START = "duel.start";

    public static MagicDuel instance;

    private final DuelConfig duelConfig;
    private final int playerIndex = 0;
    private final int opponentIndex = 1;
    private int gameNr;
    private int gamesPlayed;
    private int gamesWon;
    private int startPlayer;

    public MagicDuel(final DuelConfig configuration) {
        this.duelConfig=configuration;
        restart();
    }

    public MagicDuel() {
        this(new DuelConfig());
    }

    public DuelConfig getConfiguration() {
        return duelConfig;
    }

    public int getGameNr() {
        return gameNr;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesTotal() {
        return (duelConfig.getPlayerConfigs().length-1)*duelConfig.getNrOfGames();
    }

    public int getGamesWon() {
        return gamesWon;
    }

    private void determineStartPlayer() {
        startPlayer=MagicRandom.nextRNGInt(2);
    }

    public void setStartPlayer(final int startPlayer) {
        this.startPlayer=startPlayer;
    }

    public boolean isEditable() {
        return gameNr==1;
    }

    public boolean isFinished() {
        // if a duel consists of a total of X games, then in an interactive
        // game it should be "best of" X games, not first to X.
        final int player1GamesWon = getGamesWon();
        final int player2GamesWon = getGamesPlayed() - player1GamesWon;
        final int gamesRequiredToWin = (int)Math.ceil(getGamesTotal()/2.0);
        return (player1GamesWon >= gamesRequiredToWin) || (player2GamesWon >= gamesRequiredToWin);
    }

    void advance(final boolean won, final MagicGame game) {
        gameNr++;
        gamesPlayed++;

        if (won) {
            gamesWon++;
            startPlayer = opponentIndex;
        } else {
            startPlayer = playerIndex;
        }

        MagicStats.logStats(this, game);
    }

    public MagicGame nextGame() {
       return nextGame(MagicRandom.nextRNGInt(), MagicRandom.nextRNGInt(), false);
    }

    public MagicGame nextGame(int seedP1, int seedP2, boolean flipDecks) {
        //create players
        final MagicPlayer player   = new MagicPlayer(duelConfig.getStartingLife(playerIndex),   duelConfig.getPlayerConfig(playerIndex),   playerIndex);
        final MagicPlayer opponent = new MagicPlayer(duelConfig.getStartingLife(opponentIndex), duelConfig.getPlayerConfig(opponentIndex), opponentIndex);
        if(flipDecks){
            MagicDeck tmp = new MagicDeck(player.getConfig().getDeck());
            player.getConfig().getDeck().setContent(opponent.getConfig().getDeck());
            opponent.getConfig().getDeck().setContent(new MagicDeck(tmp));
        }

        //determine who starts first
        final MagicPlayer[] players = new MagicPlayer[]{player,opponent};
        final MagicPlayer start = players[startPlayer];

        //create game
        final MagicGame game = MagicGame.create(
            this,
            MagicDefaultGameplay.getInstance(),
            players,
            start
        );

        //create hand and library
        MagicRandom.setRNGState(seedP1);
        player.createHandAndLibrary(duelConfig.getHandSize());
        MagicRandom.setRNGState(seedP2);
        opponent.createHandAndLibrary(duelConfig.getHandSize());
        return game;
    }

    public int getNrOfPlayers() {
        return duelConfig.getPlayerConfigs().length;
    }

    public DuelPlayerConfig getPlayer(final int index) {
        return duelConfig.getPlayerConfig(index);
    }

    public DuelPlayerConfig[] getPlayers() {
        return duelConfig.getPlayerConfigs();
    }

    // only used by magic.test classes.
    public void setPlayers(final DuelPlayerConfig[] aPlayerDefinitions) {
        duelConfig.setPlayerConfigs(aPlayerDefinitions);
    }

    // Used by the "Generate Deck" button in DuelPanel.
    public void buildDeck(final DuelPlayerConfig player) {
        DeckGenerators.setRandomDeck(player);
    }

    public void buildDecks() {
        for (final DuelPlayerConfig player : duelConfig.getPlayerConfigs()) {
            switch (player.getDeckProfile().getDeckType()) {
            case Random:
                DeckGenerators.setRandomDeck(player);
                break;
            case Preconstructed:
                setDeckFromFile(player, DeckUtils.getPrebuiltDecksFolder());
                break;
            case Custom:
                setDeckFromFile(player, DeckUtils.getDecksFolder());
                break;
            case Firemind:
                setDeckFromFile(player, DeckUtils.getFiremindDecksFolder());
                break;
            default:
                break;
            }
        }
    }

    private void setDeckFromFile(DuelPlayerConfig player, Path deckFolder) {
        String deckFilename = DeckUtils.getNormalizedFilename(player.getDeckProfile().getDeckValue());
        player.setDeck(loadDeck(deckFolder.resolve(deckFilename)));
    }

    private MagicDeck loadDeck(final Path deckFilePath) {
        return DeckUtils.loadDeckFromFile(deckFilePath);
    }

    public void initialize() {
        buildDecks();
    }

    public static final File getLatestDuelFile() {
        return MagicFileSystem.getDataPath(DataPath.DUELS).resolve("latest.duel").toFile();
    }

    private void save(final Properties properties) {
        duelConfig.save(properties);
        properties.setProperty(GAME,Integer.toString(gameNr));
        properties.setProperty(PLAYED,Integer.toString(gamesPlayed));
        properties.setProperty(WON,Integer.toString(gamesWon));
        properties.setProperty(START,Integer.toString(startPlayer));
    }

    public void save(final File file) {
        final Properties properties = new SortedProperties();
        save(properties);
        try {
            FileIO.toFile(file, properties, "Duel");
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable save duel to " + file);
        }
    }

    private void load(final Properties properties) {
        duelConfig.load(properties, true);
        gameNr=Integer.parseInt(properties.getProperty(GAME,"1"));
        gamesPlayed=Integer.parseInt(properties.getProperty(PLAYED,"0"));
        gamesWon=Integer.parseInt(properties.getProperty(WON,"0"));
        startPlayer=Integer.parseInt(properties.getProperty(START,"0"));
    }

    public void load(final File file) {
        load(FileIO.toProp(file));
    }

    public void restart() {
        gameNr=1;
        gamesPlayed=0;
        gamesWon=0;
        determineStartPlayer();
    }

    public PlayerProfile getWinningPlayerProfile() {
        if (!isFinished()) {
            return null;
        } else {
            final int playerOneGamesWon = getGamesWon();
            final int playerTwoGamesWon = getGamesPlayed() - playerOneGamesWon;
            if (playerOneGamesWon > playerTwoGamesWon) {
                return getConfiguration().getPlayerProfile(0);
            } else {
                return getConfiguration().getPlayerProfile(1);
            }
        }
    }

    public static void restartDuel() {
        if (instance != null) {
            instance.restart();
        }
    }

    public static void newDuel() {
        instance = new MagicDuel(DuelConfig.getInstance());
        instance.initialize();
    }

    public static boolean isDuelReady() {
        return instance != null;
    }

    public boolean isNotFinished() {
        return !isFinished();
    }

    public static void resumeDuel() {
        final File duelFile = getLatestDuelFile();
        if (duelFile.exists()) {
            instance = new MagicDuel(DuelConfig.getInstance());
            instance.load(duelFile);
        }
    }

    public boolean hasInvalidDecks() {
        return !getPlayer(0).getDeck().isValid()
            || !getPlayer(1).getDeck().isValid();
    }
}
