package magic.model.player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeck;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.utility.FileIO;
import magic.utility.SortedProperties;

public class PlayerStatistics {

    // properties
    private static final String TIMESTAMP = "timestamp";
    private static final String GAMES_PLAYED = "gamesPlayed";
    private static final String GAMES_WON = "gamesWon";
    private static final String GAMES_CONCEDED = "gamesConceded";
    private static final String DUELS_PLAYED = "duelsPlayed";
    private static final String DUELS_WON = "duelsWon";
    private static final String TURNS_PLAYED = "turnsPlayed";
    private static final String COLOR_BLACK = "colorBlack";
    private static final String COLOR_BLUE = "colorBlue";
    private static final String COLOR_GREEN = "colorGreen";
    private static final String COLOR_RED = "colorRed";
    private static final String COLOR_WHITE = "colorWhite";

    private static final String NO_VALUE = "---";

    private long millisecTimestamp;
    private final Path statsFilePath;
    private final boolean isHuman;

    public int gamesPlayed;
    public int gamesWon;
    public int gamesConceded;
    public int turnsPlayed;
    public int duelsPlayed;
    public int duelsWon;
    public int colorBlack;
    public int colorBlue;
    public int colorGreen;
    public int colorRed;
    public int colorWhite;
    private MagicColor mostUsedColor = null;

    public PlayerStatistics(final PlayerProfile aPlayerProfile) {
        isHuman = aPlayerProfile.isHuman();
        statsFilePath = aPlayerProfile.getProfilePath().resolve("player.stats");
        loadStats();
    }

    public Integer getDuelsPlayed() {
        return duelsPlayed;
    }

    public Integer getDuelsWon() {
        return duelsWon;
    }

    public Integer getDuelsLost() {
        return duelsPlayed - duelsWon;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public Integer getGamesLost() {
        return gamesPlayed - gamesWon;
    }

    public Integer getTurnsPlayed() {
        return turnsPlayed;
    }

    public MagicColor getMostUsedColor() {
        return mostUsedColor;
    }

    private void loadStats() {
        final File statsFile = new File(statsFilePath.toString());
        final Properties properties = statsFile.exists() ? FileIO.toProp(statsFile) : new SortedProperties();
        gamesPlayed = Integer.parseInt(properties.getProperty(GAMES_PLAYED,"0"));
        gamesWon = Integer.parseInt(properties.getProperty(GAMES_WON,"0"));
        gamesConceded = Integer.parseInt(properties.getProperty(GAMES_CONCEDED,"0"));
        turnsPlayed = Integer.parseInt(properties.getProperty(TURNS_PLAYED,"0"));
        duelsPlayed = Integer.parseInt(properties.getProperty(DUELS_PLAYED,"0"));
        duelsWon = Integer.parseInt(properties.getProperty(DUELS_WON,"0"));
        colorBlack = Integer.parseInt(properties.getProperty(COLOR_BLACK,"0"));
        colorBlue = Integer.parseInt(properties.getProperty(COLOR_BLUE,"0"));
        colorGreen = Integer.parseInt(properties.getProperty(COLOR_GREEN,"0"));
        colorRed = Integer.parseInt(properties.getProperty(COLOR_RED,"0"));
        colorWhite = Integer.parseInt(properties.getProperty(COLOR_WHITE,"0"));
        millisecTimestamp = Long.parseLong(properties.getProperty(TIMESTAMP, "0"));
        setMostUsedColor();
    }

    public void save() {

        final Properties properties = new SortedProperties();
        properties.setProperty(GAMES_PLAYED, String.valueOf(gamesPlayed));
        properties.setProperty(GAMES_WON, String.valueOf(gamesWon));
        properties.setProperty(GAMES_CONCEDED, String.valueOf(gamesConceded));
        properties.setProperty(TURNS_PLAYED, String.valueOf(turnsPlayed));
        properties.setProperty(DUELS_PLAYED, String.valueOf(duelsPlayed));
        properties.setProperty(DUELS_WON, String.valueOf(duelsWon));
        properties.setProperty(COLOR_BLACK, String.valueOf(colorBlack));
        properties.setProperty(COLOR_BLUE, String.valueOf(colorBlue));
        properties.setProperty(COLOR_GREEN, String.valueOf(colorGreen));
        properties.setProperty(COLOR_RED, String.valueOf(colorRed));
        properties.setProperty(COLOR_WHITE, String.valueOf(colorWhite));
        properties.setProperty(TIMESTAMP, String.valueOf(System.currentTimeMillis()));

        final File file = new File(statsFilePath.toString());
        try {
            FileIO.toFile(file, properties, "Player Statistics");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setMostUsedColor() {
        final int[] colorCount = new int[MagicColor.NR_COLORS];
        colorCount[0] = colorWhite;
        colorCount[1] = colorBlue;
        colorCount[2] = colorBlack;
        colorCount[3] = colorGreen;
        colorCount[4] = colorRed;
        int mostCount = Integer.MIN_VALUE;
        for (final MagicColor color : MagicColor.values()) {
            final int count = colorCount[color.ordinal()];
            if (count > mostCount) {
                mostCount = count;
                mostUsedColor = color;
            }
        }
    }

    public void update(final boolean isPlayerWinner, final MagicPlayer player, final MagicGame game) {

        gamesPlayed++;

        if (isPlayerWinner) {
            gamesWon++;
        }

        if (player.isHuman() && game.isConceded()) {
            gamesConceded++;
        }

        turnsPlayed += game.getTurn();
        if (game.getDuel().isFinished()) {
            duelsPlayed++;
            if (isPlayerWinner) {
                    duelsWon++;
            }
        }

        final int[] colorCount = new int[MagicColor.NR_COLORS];
        final MagicDeck deck = player.getPlayerDefinition().getDeck();
        for (final MagicCardDefinition card : deck) {
            if (!card.isLand()) {
                for (final MagicColor color : MagicColor.values()) {
                    if (color.hasColor(card.getColorFlags())) {
                        colorCount[color.ordinal()]++;
                        switch (color) {
                        case Black:
                            colorBlack++;
                            break;
                        case Blue:
                            colorBlue++;
                            break;
                        case Green:
                            colorGreen++;
                            break;
                        case Red:
                            colorRed++;
                            break;
                        case White:
                            colorWhite++;
                            break;
                        }
                    }
                }
            }
        }
        setMostUsedColor();

        save();
    }

    public int getDuelsWinPercentage() {
        return getPercentage(duelsWon, duelsPlayed);
    }

    public int getGamesWinPercentage() {
        return getPercentage(gamesWon, gamesPlayed);
    }

    public boolean isHumanPlayer() {
        return isHuman;
    }

    public int getGamesConceded() {
        return  gamesConceded;
    }

    public int getAverageTurnsPerGame() {
        return (gamesPlayed > 0) ? turnsPlayed / gamesPlayed : 0;
    }

    private static int getPercentage(final int value, final int total) {
        return total>0 ? (value*100)/total : 0;
    }

    public String getLastPlayed() {
        return gamesPlayed > 0 ? getTimestampString(millisecTimestamp) : NO_VALUE;
    }

    private String getTimestampString(final long millisecs) {
        if (millisecs > 0) {
            final Date timestampDate = new Date(millisecTimestamp);
            String timestampString = new SimpleDateFormat("yyyy-MM-dd").format(timestampDate);
            final String currentString = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
            if (timestampString.equals(currentString)) {
                timestampString = new SimpleDateFormat("HH:mm").format(timestampDate);
            }
            return timestampString;
        } else {
            return NO_VALUE;
        }
    }

}
