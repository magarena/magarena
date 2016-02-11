package magic.ui.player;

import magic.model.player.PlayerStatistics;

public class PlayerStatsViewerInfo implements IPlayerStatsViewerInfo {

    private final PlayerStatistics stats;

    public PlayerStatsViewerInfo(final PlayerStatistics stats) {
        this.stats = stats;
    }

    @Override
    public String getLastPlayedDate() {
        return stats.getLastPlayed();
    }

    @Override
    public String getDuelsPlayed() {
        return String.format("%d", stats.getDuelsPlayed());
    }

    @Override
    public String getDuelsWonLost() {
        return String.format("%d / %d (%d%%)",
                stats.getDuelsWon(),
                stats.getDuelsLost(),
                stats.getDuelsWinPercentage()
        );
    }

    @Override
    public String getGamesPlayed() {
        return String.format("%d", stats.getGamesPlayed());
    }

    @Override
    public String getGamesWonLost() {
        return String.format("%d / %d (%d%%)",
                stats.getGamesWon(),
                stats.getGamesLost(),
                stats.getGamesWinPercentage()
        );
    }

    @Override
    public String getGamesConceded() {
        return stats.isHumanPlayer()
                ? Integer.toString(stats.getGamesConceded())
                : NO_VALUE;
    }

    @Override
    public String getTurnsPlayed() {
        return String.format("%d", stats.getTurnsPlayed());
    }

    @Override
    public String getAverageTurnsPerGame() {
        return String.format("%d", stats.getAverageTurnsPerGame());
    }

    @Override
    public String getMostUsedColor() {
        return stats.getMostUsedColor().getDisplayName();
    }

}
