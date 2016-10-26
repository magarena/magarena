package magic.ui.screen.duel.setup;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import magic.model.player.PlayerStatistics;
import magic.translate.MText;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class PlayerStatsPanel extends JPanel {

    private static final String _S1 = "Last played:";
    private static final String _S2 = "Duels completed:";
    private static final String _S3 = "Duels won / lost:";
    private static final String _S4 = "Games played:";
    private static final String _S5 = "Games won / lost:";
    private static final String _S6 = "Games conceded:";
    private static final String _S7 = "Turns played:";
    private static final String _S8 = "Average turns per game:";
    private static final String _S9 = "Most used color:";

    private static final IPlayerStatsViewerInfo NO_STATS = new NullPlayerStatsViewerInfo();

    PlayerStatsPanel(PlayerStatistics stats) {

        final MigLayout miglayout = new MigLayout("wrap 2, insets 2");
        miglayout.setColumnConstraints("[50%][50%, right]");
        setLayout(miglayout);

        setOpaque(false);

        refreshStats(getPlayerStatsViewerInfo(stats));
    }

    private void refreshStats(final IPlayerStatsViewerInfo stats) {

        removeAll();

        add(getStatsLabel(MText.get(_S1)));
        add(getStatsLabel(stats.getLastPlayedDate()));

        add(getStatsLabel(MText.get(_S2)));
        add(getStatsLabel(stats.getDuelsPlayed()));

        add(getStatsLabel(MText.get(_S3)));
        add(getStatsLabel(stats.getDuelsWonLost()));

        add(getStatsLabel(MText.get(_S4)));
        add(getStatsLabel(stats.getGamesPlayed()));

        add(getStatsLabel(MText.get(_S5)));
        add(getStatsLabel(stats.getGamesWonLost()));

        add(getStatsLabel(MText.get(_S6)));
        add(getStatsLabel(stats.getGamesConceded()));

        add(getStatsLabel(MText.get(_S7)));
        add(getStatsLabel(stats.getTurnsPlayed()));

        add(getStatsLabel(MText.get(_S8)));
        add(getStatsLabel(stats.getAverageTurnsPerGame()));

        add(getStatsLabel(MText.get(_S9)));
        add(getStatsLabel(stats.getMostUsedColor()));

        revalidate();
    }

    private JLabel getStatsLabel(String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    void setPlayerStats(PlayerStatistics stats) {
        refreshStats(getPlayerStatsViewerInfo(stats));
    }

    private IPlayerStatsViewerInfo getPlayerStatsViewerInfo(PlayerStatistics stats) {
        return stats.getGamesPlayed() > 0
                ? new PlayerStatsViewerInfo(stats)
                : NO_STATS;
    }

}
