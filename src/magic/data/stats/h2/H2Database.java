package magic.data.stats.h2;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import magic.data.stats.DeckStatsInfo;
import magic.data.stats.GameStatsInfo;
import magic.model.MagicDeck;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.player.AiProfile;
import magic.model.player.PlayerProfile;
import magic.utility.DeckUtils;
import magic.utility.MagicFileSystem;
import magic.utility.MagicSystem;
import org.h2.jdbcx.JdbcConnectionPool;

public class H2Database {

    private static final Logger LOGGER = Logger.getLogger(H2Database.class.getName());

    private final JdbcConnectionPool cpool;

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public H2Database() throws SQLException {
        cpool = getConnectionPool();
        applySchemaUpdates();
    }

    public static String getDatabaseFile() {
        Path statsPath = MagicFileSystem.getDataPath(MagicFileSystem.DataPath.STATS);
        return MagicSystem.isDevMode() || MagicSystem.isTestGame()
            ? statsPath.resolve("game-stats-dev").toAbsolutePath().toString()
            : statsPath.resolve("game-stats").toAbsolutePath().toString();
    }

    private JdbcConnectionPool getConnectionPool() {
        // http://www.h2database.com/html/features.html#trace_options
        String traceLevel = "TRACE_LEVEL_FILE=0"; // 0=OFF, 1=ERROR
        return JdbcConnectionPool.create(
            "jdbc:h2:file:" + getDatabaseFile() + ";" + traceLevel,
            "sa", ""
        );
    }

    private void applySchemaUpdates() throws SQLException {
        try (Connection conn = getConnection()) {
            H2Schema.applySchemaUpdates(conn);
        }
    }

    public Connection getConnection() throws SQLException {
        final Connection conn = cpool.getConnection();
        conn.setAutoCommit(true);
        return conn;
    }

    public Connection getReadOnlyConnection() throws SQLException {
        final Connection conn = getConnection();
        conn.setReadOnly(true);
        return conn;
    }

    private String getRepeated(String s, int count, String delim) {
        // http://stackoverflow.com/questions/1900477/can-one-initialise-a-java-string-with-a-single-repeated-character-to-a-specific
        return IntStream.range(0, count)
            .mapToObj(x -> s)
            .collect(Collectors.joining(delim));
    }

    private int getDeckTypeId(Connection conn, MagicDeck deck) throws SQLException {
        String SQL = "SELECT ID FROM DECK_TYPE WHERE NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, deck.getDeckType().name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        SQL = "INSERT INTO DECK_TYPE (NAME) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(SQL, new String[]{"ID"})) {
            ps.setString(1, deck.getDeckType().name());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Unable to get DECK_TYPE ID value.");
    }

    private int getDeckId(Connection conn, MagicDeck deck) throws SQLException {
        final int deckTypeId = getDeckTypeId(conn, deck);
        String SQL = "SELECT ID FROM DECK "
            + "WHERE NAME = ? AND FILE_CHECKSUM = ? AND DECK_TYPE_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, deck.getName());
            ps.setLong(  2, deck.getDeckFileChecksum());
            ps.setInt(   3, deckTypeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        SQL = "INSERT INTO DECK (NAME, FILE_CHECKSUM, DECK_TYPE_ID, DECK_SIZE, DECK_COLOR) "
            + "VALUES (" + getRepeated("?", 5, ",") + ")";
        try (PreparedStatement ps = conn.prepareStatement(SQL, new String[]{"ID"})) {
            ps.setString(   1, deck.getName());
            ps.setLong(     2, deck.getDeckFileChecksum());
            ps.setInt(      3, deckTypeId);
            ps.setInt(      4, deck.size());
            ps.setString(   5, DeckUtils.getDeckColor(deck));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Unable to get DECK ID value.");
    }

    private int getAiPlayerId(Connection conn, AiProfile aiProfile) throws SQLException {
        String SQL = "SELECT ID FROM PLAYER WHERE AI_TYPE = ? AND AI_LEVEL = ? AND AI_XLIFE = ?";
        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, aiProfile.getAiType().name());
            ps.setInt(2, aiProfile.getAiLevel());
            ps.setInt(3, aiProfile.getExtraLife());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        SQL = "INSERT INTO PLAYER (AI_TYPE, AI_LEVEL, AI_XLIFE) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(SQL, new String[]{"ID"})) {
            ps.setString(1, aiProfile.getAiType().name());
            ps.setInt(2, aiProfile.getAiLevel());
            ps.setInt(3, aiProfile.getExtraLife());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Unable to get PLAYER ID value.");
    }

    private int getHumanPlayerId(Connection conn, PlayerProfile profile) throws SQLException {
        String SQL = "SELECT ID FROM PLAYER WHERE PLAYER_PROFILE = ?";
        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, profile.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        SQL = "INSERT INTO PLAYER (PLAYER_PROFILE) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(SQL, new String[]{"ID"})) {
            ps.setString(1, profile.getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Unable to get PLAYER ID value.");
    }

    private int getPlayerId(Connection conn, MagicPlayer player) throws SQLException {
        return player.isArtificial()
            ? getAiPlayerId(conn, player.getAiProfile())
            : getHumanPlayerId(conn, player.getPlayerDefinition().getProfile());
    }


    public void logGameStats(MagicGame game) throws SQLException {

        try (Connection conn = getConnection()) {

            // updates should all succeed or none at all.
            conn.setAutoCommit(false);

            String SQL = "INSERT INTO GAME ("
                    + "TIME_START, MAG_VERSION, WINNING_PLAYER_NUMBER, CONCEDED, TURNS, START_HAND, START_LIFE"
                    + ") VALUES (" + getRepeated("?", 7, ",") + ")";

            try (PreparedStatement ps = conn.prepareStatement(SQL)) {
                ps.setLong(     1, game.getStartTimeMilli());
                ps.setString(   2, MagicSystem.VERSION);
                ps.setInt(      3, game.getWinner().equals(game.getPlayer(0)) ? 1 : 2);
                ps.setBoolean(  4, game.isConceded());
                ps.setInt(      5, game.getTurn());
                ps.setInt(      6, game.getDuel().getConfiguration().getHandSize());
                ps.setInt(      7, game.getDuel().getConfiguration().getStartLife());
                ps.executeUpdate();
            }

            SQL = "INSERT INTO GAME_PLAYER ("
                    + "GAME_TIME_START, PLAYER_NUMBER, PLAYER_ID, DECK_ID"
                    + ") VALUES (" + getRepeated("?", 4, ",") + ")";

            for (int i = 0; i < game.getPlayers().length; i++) {
                MagicPlayer player = game.getPlayer(i);
                MagicDeck deck = player.getPlayerDefinition().getDeck();
                try (PreparedStatement ps = conn.prepareStatement(SQL)) {
                    ps.setLong(1, game.getStartTimeMilli());
                    ps.setInt(2, i + 1);
                    ps.setInt(3, getPlayerId(conn, player));
                    ps.setInt(4, getDeckId(conn, deck));
                    ps.executeUpdate();
                }
            }

            conn.commit();
        }

    }

    public void close() {
        cpool.dispose();
    }

    private int[] getPlayedWonLost(Connection conn, MagicDeck deck) throws SQLException {
        String sql = "SELECT P, W, L "
            + "FROM DECK_GAME_PWL "
            + "WHERE DECK = ? AND DECK_CRC = ? AND DECK_TYPE = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, deck.getName());
        ps.setLong(2, deck.getDeckFileChecksum());
        ps.setString(3, deck.getDeckType().name());
        int[] pwl = new int[3];
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            pwl[0] = rs.getInt(1);
            pwl[1] = rs.getInt(2);
            pwl[2] = rs.getInt(3);
        }
        return pwl;
    }

    public String getPlayedWonLost(MagicDeck deck) throws SQLException {
        try (Connection conn = getReadOnlyConnection()) {
            int[] stats = getPlayedWonLost(conn, deck);
            return stats[0] + " / " + stats[1] + " / " + stats[2];
        }
    }

    public int getTotalGamesPlayed() throws SQLException {
        try (Connection conn = getReadOnlyConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(1) FROM GAME",
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
            );
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    public int getTotalGamesPlayed(MagicDeck deck) throws SQLException {
        try (Connection conn = getReadOnlyConnection()) {
            return getPlayedWonLost(conn, deck)[0];
        }
    }

    private GameStatsInfo getGameStatsDTO(ResultSet rs) throws SQLException {
        final GameStatsInfo stats = new GameStatsInfo();
        stats.timeStart = rs.getLong("TIME_START");
        stats.magarenaVersion = rs.getString("MAG_VERSION");
        stats.isConceded = rs.getBoolean("CONCEDED");
        stats.player1AiLevel = rs.getInt("P1_AI_LEVEL");
        stats.player1AiType = rs.getString("P1_AI_TYPE");
        stats.player1AiXtraLife = rs.getInt("P1_AI_XLIFE");
        stats.player1DeckColor = rs.getString("P1_DECK_COLOR");
        stats.player1DeckFileChecksum = rs.getLong("P1_DECK_CRC");
        stats.player1DeckName = rs.getString("P1_DECK");
        stats.player1DeckSize = rs.getInt("P1_DECK_SIZE");
        stats.player1DeckType = rs.getString("P1_DECK_TYPE");
        stats.player1ProfileId = rs.getString("P1_PROFILE");
        stats.player2AiLevel = rs.getInt("P2_AI_LEVEL");
        stats.player2AiType = rs.getString("P2_AI_TYPE");
        stats.player2AiXtraLife = rs.getInt("P2_AI_XLIFE");
        stats.player2DeckColor = rs.getString("P2_DECK_COLOR");
        stats.player2DeckFileChecksum = rs.getLong("P2_DECK_CRC");
        stats.player2DeckName = rs.getString("P2_DECK");
        stats.player2DeckSize = rs.getInt("P2_DECK_SIZE");
        stats.player2DeckType = rs.getString("P2_DECK_TYPE");
        stats.player2ProfileId = rs.getString("P2_PROFILE");
        stats.startHandSize = rs.getInt("START_HAND");
        stats.startLife = rs.getInt("START_LIFE");
        stats.timeStart = rs.getLong("TIME_START");
        stats.turns = rs.getInt("TURNS");
        stats.winningPlayerProfile = rs.getString("WINNER");
        return stats;
    }

    public List<GameStatsInfo> getGameStats(int limit, int rowsToSkip) throws SQLException {
        try (Connection conn = getReadOnlyConnection()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT * FROM ALL_GAME_STATS "
                    + "ORDER BY TIME_START DESC "
                    + "LIMIT ? OFFSET ?",
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
            );
            ps1.setInt(1, limit);
            ps1.setInt(2, rowsToSkip);
            ResultSet rs = ps1.executeQuery();
            List<GameStatsInfo> games = new ArrayList<>();
            while (rs.next()) {
                games.add(getGameStatsDTO(rs));
            }
            return games;
        }
    }

    public List<GameStatsInfo> getGameStats(MagicDeck deck, int limit, int page) throws SQLException {
        try (Connection conn = getReadOnlyConnection()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT * "
                + "FROM ALL_GAME_STATS "
                + "WHERE (P1_DECK = ? AND P1_DECK_CRC = ?) "
                + "OR (P2_DECK = ? AND P2_DECK_CRC = ?) "
                + "ORDER BY TIME_START DESC "
                + "LIMIT ? OFFSET ?",
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
            );
            ps1.setString(1, deck.getName());
            ps1.setLong(2, deck.getDeckFileChecksum());
            ps1.setString(3, deck.getName());
            ps1.setLong(4, deck.getDeckFileChecksum());
            ps1.setInt(5, limit);
            ps1.setInt(6, page);
            ResultSet rs = ps1.executeQuery();
            List<GameStatsInfo> games = new ArrayList<>();
            while (rs.next()) {
                games.add(getGameStatsDTO(rs));
            }
            return games;
        }
    }

    private DeckStatsInfo getNewDeckStatsInfo(ResultSet rs) throws SQLException {
        final DeckStatsInfo info = new DeckStatsInfo();
        info.deckName = rs.getString("DECK");
        info.deckCheckSum = rs.getLong("DECK_CRC");
        info.deckType = rs.getString("DECK_TYPE");
        return info;
    }

    public List<DeckStatsInfo> getMostPlayedDecks(int limit) throws SQLException {
        final List<DeckStatsInfo> decks = new ArrayList<>();
        try (Connection conn = getReadOnlyConnection()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT DECK, DECK_CRC, DECK_TYPE, P "
                + "FROM POPULAR_DECKS LIMIT ?",
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
            );
            ps1.setInt(1, limit);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                decks.add(getNewDeckStatsInfo(rs));
            }
        }
        return decks;
    }

    public String getSchemaVersion() throws SQLException {
        try (Connection conn = getReadOnlyConnection()) {
            String sql = "SELECT SCHEMA_VERSION FROM GAMESTATS_SETTINGS";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString(1) : "";
        }
    }

    public List<DeckStatsInfo> getTopWinningDecks(int limit) throws SQLException {
        final List<DeckStatsInfo> decks = new ArrayList<>();
        try (Connection conn = getReadOnlyConnection()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT DECK, DECK_CRC, DECK_TYPE "
                + "FROM WINNING_DECKS LIMIT ?",
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
            );
            ps1.setInt(1, limit);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                decks.add(getNewDeckStatsInfo(rs));
            }
        }
        return decks;
    }

    public List<DeckStatsInfo> getRecentlyPlayedDecks(int limit) throws SQLException {
        final List<DeckStatsInfo> decks = new ArrayList<>();
        try (Connection conn = getReadOnlyConnection()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT DECK, DECK_CRC, DECK_TYPE "
                + "FROM RECENT_DECKS LIMIT ?",
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY
            );
            ps1.setInt(1, limit);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                decks.add(getNewDeckStatsInfo(rs));
            }
        }
        return decks;
    }

}
