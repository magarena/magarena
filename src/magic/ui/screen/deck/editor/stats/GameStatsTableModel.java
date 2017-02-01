package magic.ui.screen.deck.editor.stats;

import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import magic.data.stats.GameStatsInfo;
import magic.data.stats.MagicStats;
import magic.model.MagicDeck;

/**
 * Game statistics for a deck.
 */
@SuppressWarnings("serial")
class GameStatsTableModel extends AbstractTableModel {

    private static final int PAGE_SIZE = 8;

    private final List<DeckGameStats> gameStats;
    private final int totalGames;
    private final int totalPages;
    private int currentPage = 1;

    GameStatsTableModel(MagicDeck deck, int page) {
        this.totalGames = MagicStats.getTotalGamesPlayed(deck);
        this.totalPages = ((totalGames - 1) / PAGE_SIZE) + 1;
        currentPage = page;
        gameStats = getDeckGameStats(deck, MagicStats.getGameStats(deck, PAGE_SIZE, getGamesToSkip()));
    }

    GameStatsTableModel(MagicDeck deck) {
        this(deck, 1);
    }

    private int getGamesToSkip() {
        return currentPage == 1 ? 0 : (currentPage - 1) * PAGE_SIZE;
    }

    private List<DeckGameStats> getDeckGameStats(MagicDeck deck, List<GameStatsInfo> gameStats) {
        return gameStats.stream()
            .map(s -> new DeckGameStats(deck, s))
            .collect(Collectors.toList());
    }


    @Override
    public int getRowCount() {
        return gameStats.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
        case 0: return "Game";
        case 1: return "Config";
        case 2: return "Player";
        case 3: return "Opponent";
        case 4: return "Opponent deck";
        case 5: return "Result";
        default: return "???";
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
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
        DeckGameStats stats = gameStats.get(row);
        switch (col) {
        case 0: return "<html>" + stats.getGameInfo() + "</html>";
        case 1: return "<html>" + stats.getConfigInfo() + "</html>";
        case 2: return "<html>" + stats.getPlayerInfo() + "</html>";
        case 3: return "<html>" + stats.getOpponentInfo() + "</html>";
        case 4: return stats.getOpponentDeckInfo();
        case 5: return stats.getResultInfo();
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
