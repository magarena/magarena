package magic.ui.screen.stats;

import magic.data.stats.GameStatsInfo;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import magic.data.stats.MagicStats;

@SuppressWarnings("serial")
class GameStatsTableModel extends AbstractTableModel {

    private static final int PAGE_SIZE = 17;

    private final List<GameStatsInfo> games;
    private final int totalGames;
    private final int totalPages;
    private int currentPage = 1;

    GameStatsTableModel(int page) {
        this.totalGames = MagicStats.getTotalGamesPlayed();
        this.totalPages = ((totalGames - 1) / PAGE_SIZE) + 1;
        this.currentPage = page;
        this.games = MagicStats.getGameStats(PAGE_SIZE, getGamesToSkip());
    }

    GameStatsTableModel() {
        this(1);
    }

    private int getGamesToSkip() {
        return currentPage == 1 ? 0 : (currentPage - 1) * PAGE_SIZE;
    }

    @Override
    public int getRowCount() {
        return games.size();
    }

    @Override
    public int getColumnCount() {
        return GameStatsInfo.fieldsCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return GameStatsInfo.getFieldName(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return GameStatsInfo.getValueAt(games.get(row), col);
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

    int getTotalGames() {
        return totalGames;
    }

}
