package magic.ui.screen.duel.setup;

interface IPlayerStatsViewerInfo {

    static final String NO_VALUE = "---";

    String getLastPlayedDate();
    String getDuelsPlayed();
    String getDuelsWonLost();
    String getGamesPlayed();
    String getGamesWonLost();
    String getGamesConceded();
    String getTurnsPlayed();
    String getAverageTurnsPerGame();
    String getMostUsedColor();

}
