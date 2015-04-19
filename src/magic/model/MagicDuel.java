package magic.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;
import magic.utility.MagicSystem;
import magic.data.DeckGenerators;
import magic.data.DeckUtils;
import magic.data.DuelConfig;
import magic.exception.InvalidDeckException;
import magic.model.phase.MagicDefaultGameplay;
import magic.model.player.PlayerProfile;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class MagicDuel {

    private static final String OPPONENT="opponent";
    private static final String GAME="game";
    private static final String PLAYED="played";
    private static final String WON="won";
    private static final String START="start";

    private final DuelConfig duelConfig;
    private int opponentIndex;
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

    public MagicDuel(final DuelConfig configuration,final MagicDuel duel) {
        this(configuration);
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
        gamesPlayed++;
        if (won) {
            gamesWon++;
            startPlayer=1;
        } else {
            startPlayer=0;
        }
        gameNr++;
        if (gameNr>duelConfig.getNrOfGames()) {
            gameNr=1;
            opponentIndex++;
            determineStartPlayer();
        }

        if (game.isReal() && !MagicSystem.isTestGame() && !MagicSystem.isAiVersusAi()) {
            duelConfig.getPlayerProfile(0).getStats().update(won, game.getPlayer(0), game);
            duelConfig.getPlayerProfile(1).getStats().update(!won, game.getPlayer(1), game);
        }
    }

    private DuelPlayerConfig[] createPlayers() {
        final DuelPlayerConfig[] players = new DuelPlayerConfig[DuelConfig.MAX_PLAYERS];
        players[0] =
                new DuelPlayerConfig(
                        duelConfig.getPlayerProfile(0),
                        duelConfig.getPlayerDeckProfile(0));
        players[1] =
                new DuelPlayerConfig(
                        duelConfig.getPlayerProfile(1),
                        duelConfig.getPlayerDeckProfile(1));
        return players;
    }

    public MagicGame nextGame() {
        //create players
        final MagicPlayer player   = new MagicPlayer(duelConfig.getStartLife(), duelConfig.getPlayerConfig(0), 0);
        final MagicPlayer opponent = new MagicPlayer(duelConfig.getStartLife(), duelConfig.getPlayerConfig(opponentIndex), 1);

        //give the AI player extra life
        opponent.setLife(opponent.getLife() + opponent.getAiProfile().getExtraLife());

        //determine who starts first
        final MagicPlayer start    = startPlayer == 0 ? player : opponent;

        //create game
        final MagicGame game = MagicGame.create(
            this,
            MagicDefaultGameplay.getInstance(),
            new MagicPlayer[]{player,opponent},
            start
        );

        //create hand and library
        player.createHandAndLibrary(duelConfig.getHandSize());
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
    public void buildDeck(final DuelPlayerConfig player) throws InvalidDeckException {
        DeckGenerators.setRandomDeck(player);
    }

    public void buildDecks() throws InvalidDeckException {
        for (final DuelPlayerConfig player : duelConfig.getPlayerConfigs()) {
            switch (player.getDeckProfile().getDeckType()) {
            case Random:
                DeckGenerators.setRandomDeck(player);
                break;
            case Preconstructed:
                setDeckFromFile(player, DeckUtils.getPrebuiltDecksFolder());
                break;
            case Custom:
                setDeckFromFile(player, Paths.get(DeckUtils.getDeckFolder()));
                break;
            case Firemind:
                setDeckFromFile(player, DeckUtils.getFiremindDecksFolder());
                break;            
            default:
                break;
            }
        }
    }

    private void setDeckFromFile(final DuelPlayerConfig player, final Path deckFolder) throws InvalidDeckException {
        final String deckFilename = player.getDeckProfile().getDeckValue() + DeckUtils.DECK_EXTENSION;
        player.setDeck(loadDeck(deckFolder.resolve(deckFilename)));
    }

    private MagicDeck loadDeck(final Path deckFilePath) throws InvalidDeckException {
        return DeckUtils.loadDeckFromFile(deckFilePath);
    }

    public void initialize() throws InvalidDeckException {
        duelConfig.setPlayerConfigs(createPlayers());
        buildDecks();
    }

    public static final File getDuelFile() {
        return MagicFileSystem.getDataPath(DataPath.DUELS).resolve("saved.duel").toFile();
    }

    private void save(final Properties properties) {
        duelConfig.save(properties);
        properties.setProperty(OPPONENT,Integer.toString(opponentIndex));
        properties.setProperty(GAME,Integer.toString(gameNr));
        properties.setProperty(PLAYED,Integer.toString(gamesPlayed));
        properties.setProperty(WON,Integer.toString(gamesWon));
        properties.setProperty(START,Integer.toString(startPlayer));
    }

    public void save(final File file) {
        final Properties properties = getNewSortedProperties();
        save(properties);
        try {
            magic.data.FileIO.toFile(file, properties, "Duel");
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable save duel to " + file);
        }
    }

    @SuppressWarnings("serial")
    private Properties getNewSortedProperties() {
       return new Properties() {
           @Override
           public synchronized Enumeration<Object> keys() {
               return Collections.enumeration(new TreeSet<Object>(super.keySet()));
           }
       };
    }

    private void load(final Properties properties) {
        duelConfig.load(properties);
        opponentIndex=Integer.parseInt(properties.getProperty(OPPONENT,"1"));
        gameNr=Integer.parseInt(properties.getProperty(GAME,"1"));
        gamesPlayed=Integer.parseInt(properties.getProperty(PLAYED,"0"));
        gamesWon=Integer.parseInt(properties.getProperty(WON,"0"));
        startPlayer=Integer.parseInt(properties.getProperty(START,"0"));
    }

    public void load(final File file) {
        load(magic.data.FileIO.toProp(file));
    }

    public void restart() {
        opponentIndex=1;
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
}
