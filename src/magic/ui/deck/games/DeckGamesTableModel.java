package magic.ui.deck.games;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import magic.data.stats.GameStatsInfo;
import magic.data.stats.MagicStats;
import magic.model.MagicDeck;
import magic.translate.MText;

/**
 * Game statistics for a deck.
 */
@SuppressWarnings("serial")
class DeckGamesTableModel extends AbstractTableModel {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Game";
    private static final String _S3 = "Player";
    private static final String _S4 = "Opponent";
    private static final String _S5 = "Opponent deck";

    private static final int PAGE_SIZE = 8;

    private final List<DeckGame> gameStats;
    private final int totalGames;
    private final int totalPages;
    private int currentPage = 1;

    DeckGamesTableModel(MagicDeck deck, int page) {
        this.totalGames = MagicStats.getTotalGamesPlayed(deck);
        this.totalPages = ((totalGames - 1) / PAGE_SIZE) + 1;
        currentPage = page;
        gameStats = getDeckGameStats(deck, MagicStats.getGameStats(deck, PAGE_SIZE, getGamesToSkip()));
    }

    DeckGamesTableModel(MagicDeck deck) {
        this(deck, 1);
    }

    private int getGamesToSkip() {
        return currentPage == 1 ? 0 : (currentPage - 1) * PAGE_SIZE;
    }

    private List<DeckGame> getDeckGameStats(MagicDeck deck, List<GameStatsInfo> gameStats) {
        return gameStats.stream()
            .map(s -> new DeckGame(deck, s))
            .collect(Collectors.toList());
    }


    @Override
    public int getRowCount() {
        return gameStats.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
        case 0: return "";
        case 1: return MText.get(_S1);
        case 2: return MText.get(_S3);
        case 3: return MText.get(_S4);
        case 4: return MText.get(_S5);
        default: return "???";
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
        case 0: return DeckGame.class;
        case 1: return DeckGame.class;
        case 2: return PlayerInfo.class;
        case 3: return PlayerInfo.class;
        case 4: return DeckInfo.class;
        default: return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int col) {
        DeckGame stats = gameStats.get(row);
        switch (col) {
        case 0: return stats;
        case 1: return stats;
        case 2: return stats.getDeckPlayerInfo();
        case 3: return stats.getOpponentInfo();
        case 4: return stats.getOpponentDeckInfo();
        default: return "???";
        }
    }

    int getPages() {
        return totalPages;
    }

    boolean hasNextPage() {
        return currentPage < totalPages;
    }

    int getNextPageNum() {
        return currentPage + 1;
    }

    boolean hasPrevPage() {
        return currentPage > 1;
    }

    int getPrevPageNum() {
        return currentPage - 1;
    }

    int getPageNum() {
        return currentPage;
    }

    int getTotalPages() {
        return totalPages;
    }

}
