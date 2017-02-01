package magic.data.stats;

public class GameStatsInfo {

    private static final String[] COL_NAMES = new String[]{
        "Start", "Version",
        "P1 Profile", "P1 AI", "Pl Level", "P1 +Life",
        "P1 Deck", "P1 Deck CRC", "P1 Deck Type", "P1 Deck Size", "P1 Deck Color",
        "P2 Profile", "P2 AI", "P2 Level", "P2 +Life",
        "P2 Deck", "P2 Deck CRC", "P2 Deck Type", "P2 Deck Size", "P2 Deck Color",
        "Winner Id", "Conceded", "Turns",
        "Start Hand", "Start Life"

    };

    public static int fieldsCount() {
        return COL_NAMES.length;
    }

    public static String getFieldName(int col) {
        return COL_NAMES[col];
    }

    public static Object getValueAt(GameStatsInfo stats, int columnIndex) {
        switch (columnIndex) {
        case 0: return String.valueOf(stats.timeStart);
        case 1: return String.valueOf(stats.magarenaVersion);
        case 2: return stats.player1ProfileId;
        case 3: return stats.player1AiType;
        case 4: return String.valueOf(stats.player1AiLevel);
        case 5: return String.valueOf(stats.player1AiXtraLife);
        case 6: return stats.player1DeckName;
        case 7: return String.valueOf(stats.player1DeckFileChecksum);
        case 8: return stats.player1DeckType;
        case 9: return String.valueOf(stats.player1DeckSize);
        case 10: return stats.player1DeckColor;
        case 11: return stats.player2ProfileId;
        case 12: return stats.player2AiType;
        case 13: return String.valueOf(stats.player2AiLevel);
        case 14: return String.valueOf(stats.player2AiXtraLife);
        case 15: return stats.player2DeckName;
        case 16: return String.valueOf(stats.player2DeckFileChecksum);
        case 17: return stats.player2DeckType;
        case 18: return String.valueOf(stats.player2DeckSize);
        case 19: return stats.player2DeckColor;
        case 20: return stats.winningPlayerProfile;
        case 21: return String.valueOf(stats.isConceded);
        case 22: return String.valueOf(stats.turns);
        case 23: return String.valueOf(stats.startHandSize);
        case 24: return String.valueOf(stats.startLife);
        default: return "???";
        }
    }

    public long timeStart;
    public String magarenaVersion;
    public String player1ProfileId;
    public String player1AiType;
    public int player1AiLevel;
    public int player1AiXtraLife;
    public String player1DeckName;
    public long player1DeckFileChecksum;
    public String player1DeckType;
    public int player1DeckSize;
    public String player1DeckColor;
    public String player2ProfileId;
    public String player2AiType;
    public int player2AiLevel;
    public int player2AiXtraLife;
    public String player2DeckName;
    public long player2DeckFileChecksum;
    public String player2DeckType;
    public int player2DeckSize;
    public String player2DeckColor;
    public String winningPlayerProfile;
    public boolean isConceded;
    public int turns;
    public int startHandSize;
    public int startLife;
}
