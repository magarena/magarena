package magic.ui.screen.duel.setup;

class NullPlayerStatsViewerInfo implements IPlayerStatsViewerInfo {

    @Override
    public String getLastPlayedDate() {
        return NO_VALUE;
    }

    @Override
    public String getDuelsPlayed() {
        return NO_VALUE;
    }

    @Override
    public String getDuelsWonLost() {
        return NO_VALUE;
    }

    @Override
    public String getGamesPlayed() {
        return NO_VALUE;
    }

    @Override
    public String getGamesWonLost() {
        return NO_VALUE;
    }

    @Override
    public String getGamesConceded() {
        return NO_VALUE;
    }

    @Override
    public String getTurnsPlayed() {
        return NO_VALUE;
    }

    @Override
    public String getAverageTurnsPerGame() {
        return NO_VALUE;
    }

    @Override
    public String getMostUsedColor() {
        return NO_VALUE;
    }

}
