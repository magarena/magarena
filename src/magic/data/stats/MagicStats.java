package magic.data.stats;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import magic.data.DeckType;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.data.stats.h2.H2Database;
import magic.model.MagicDeck;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.ui.ScreenController;
import magic.utility.DeckUtils;
import magic.utility.MagicSystem;
import org.h2.api.ErrorCode;

public final class MagicStats {
    private MagicStats() {}

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    // do not access directly, use getDB().
    private static H2Database db;

    private static H2Database getDB() throws SQLException {
        // primarily to prevent multiple threads trying
        // to update the schema at the same time.
        synchronized(MagicStats.class) {
            if (db == null) {
                db = new H2Database();
            }
            return db;
        }
    }

    public static void init() {
        try {
            getDB();
        } catch (Exception ex) {
            HandleErrorDisableStats(ex);
        }
    }

    private static boolean isDatabaseAlreadyOpenError(Exception ex) {
        return ex instanceof SQLException
            && ((SQLException) ex).getErrorCode() == ErrorCode.DATABASE_ALREADY_OPEN_1;
    }

    private static void HandleErrorDisableStats(Exception ex) {
        CONFIG.setGameStatsEnabled(false);
        if (isDatabaseAlreadyOpenError(ex)) {
            ScreenController.showWarningMessage(H2Database.getDatabaseFile() + "\n\nCannot connect to stats database as it is already open.\nStats have been switched off (see setting in preferences).");
        } else {
            CONFIG.save();
            throw new RuntimeException(ex);
        }
    }

    /**
     *  Only log game stats for a normal human vs AI game.
     */
    private static boolean isNormalGame(MagicGame game) {
        return !game.isArtificial()
            && !MagicSystem.isTestGame()
            && !MagicSystem.isDevMode()
            && !MagicSystem.isAiVersusAi();
    }

    /**
     * Currently stats are only logged when, at the end of game, you click on
     * the resume button to take you back to the duel decks screen. If after the
     * game ends you open the menu and click back to the main menu no stats are logged.
     */
    public static void logStats(MagicDuel duel, MagicGame game) {
        if (isNormalGame(game)) {
            saveGameData(game);
            logFileBasedStats(duel, game);
        }
    }

    /**
     * Logs player stats using the old, file-based way and which are currently
     * displayed on the new duel and player selection screens.
     *
     * TODO: phase out / replace with database system.
     */
    private static void logFileBasedStats(MagicDuel duel, MagicGame game) {
        if (!MagicSystem.isAiVersusAi()) {
            // log player stats using the old way.
            final DuelConfig duelConfig = duel.getConfiguration();
            final boolean won = game.getLosingPlayer() != game.getPlayers()[0];
            duelConfig.getPlayerProfile(0).getStats().update(won, game.getPlayer(0), game);
            duelConfig.getPlayerProfile(1).getStats().update(!won, game.getPlayer(1), game);
        }
    }

    /**
     * Saves detailed game data to database.
     */
    public static void saveGameData(MagicGame game) {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                getDB().logGameStats(game);
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
    }

    public static String getPlayedWonLost(MagicDeck deck) {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getPlayedWonLost(deck);
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return "";
    }

    public static int getTotalGamesPlayed() {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getTotalGamesPlayed();
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return 0;
    }

    public static int getTotalGamesPlayed(MagicDeck deck) {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getTotalGamesPlayed(deck);
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return 0;
    }

    public static List<GameStatsInfo> getGameStats(int limit, int rowsToSkip) {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getGameStats(limit, rowsToSkip);
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return new ArrayList<>();
    }

    public static List<GameStatsInfo> getGameStats(MagicDeck deck, int limit, int page) {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getGameStats(deck, limit, page);
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return new ArrayList<>();
    }

    public static String getSchemaVersion() {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getSchemaVersion();
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return "";
    }

    private static List<MagicDeck> getTopDecks(List<DeckStatsInfo> decksInfo) {
        List<MagicDeck> decks = new ArrayList<>();
        for (DeckStatsInfo info : decksInfo) {
            MagicDeck deck = DeckUtils.loadDeckFromFile(
                info.deckName, DeckType.valueOf(info.deckType)
            );
            if (DeckUtils.getDeckFileChecksum(deck) == info.deckCheckSum) {
                decks.add(deck);
            }
        }
        return decks;
    }

    private static List<DeckStatsInfo> getMostPlayedDecks(int limit) {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getMostPlayedDecks(limit);
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return new ArrayList<>();
    }

    public static List<MagicDeck> getMostPlayedDecks() {
        return getTopDecks(getMostPlayedDecks(20));
    }

    private static List<DeckStatsInfo> getTopWinningDecks(int limit) {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getTopWinningDecks(limit);
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return new ArrayList<>();
    }

    public static List<MagicDeck> getTopWinningDecks() {
        return getTopDecks(getTopWinningDecks(20));
    }

    private static List<DeckStatsInfo> getRecentlyPlayedDecks(int limit) {
        if (CONFIG.isGameStatsEnabled()) {
            try {
                return getDB().getRecentlyPlayedDecks(limit);
            } catch (Exception ex) {
                HandleErrorDisableStats(ex);
            }
        }
        return new ArrayList<>();
    }

    public static List<MagicDeck> getRecentlyPlayedDecks() {
        return getTopDecks(getRecentlyPlayedDecks(20));
    }


}
