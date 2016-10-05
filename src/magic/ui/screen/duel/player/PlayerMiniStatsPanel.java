package magic.ui.screen.duel.player;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import magic.model.player.PlayerStatistics;
import magic.translate.StringContext;
import magic.translate.UiString;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
class PlayerMiniStatsPanel extends JPanel {

    // translatable strings
    @StringContext(eg = "player mini-stats heading representing games and duels [P]layed")
    private static final String _S1 = "P";
    @StringContext(eg = "player mini-stats heading representing games and duels [W]on")
    private static final String _S2 = "W";
    @StringContext(eg = "player mini-stats heading representing games and duels [L]ost")
    private static final String _S3 = "L";
    private static final String _S4 = "Duels";
    private static final String _S5 = "Games";

    private final static Border CELL_BORDER = BorderFactory.createDashedBorder(null);

    private final MigLayout migLayout = new MigLayout();
    private PlayerStatistics stats;
    private final Color foreColor;

    PlayerMiniStatsPanel(final PlayerStatistics stats, final Color foreColor) {
        this.foreColor = foreColor;
        setLookAndFeel();
        setStats(stats);
    }

    private void setStats(final PlayerStatistics stats) {
        this.stats = stats;
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setForeground(foreColor);
        setLayout(migLayout);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0, wrap 4");
        migLayout.setColumnConstraints("[][40!]");
        // stats table
        add(new JLabel());
        add(getStatsLabel(UiString.get(_S1)), "w 100%");
        add(getStatsLabel(UiString.get(_S2)), "w 100%");
        add(getStatsLabel(UiString.get(_S3)), "w 100%");
        add(getStatsLabel(UiString.get(_S4)), "w 60!");
        add(getStatsLabel(stats.getDuelsPlayed().toString()), "w 100%");
        add(getStatsLabel(stats.getDuelsWon().toString()), "w 100%");
        add(getStatsLabel(stats.getDuelsLost().toString()), "w 100%");
        add(getStatsLabel(UiString.get(_S5)), "w 60!");
        add(getStatsLabel(stats.getGamesPlayed().toString()), "w 100%");
        add(getStatsLabel(stats.getGamesWon().toString()), "w 100%");
        add(getStatsLabel(stats.getGamesLost().toString()), "w 100%");
    }

    private JLabel getStatsLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setBorder(CELL_BORDER);
        lbl.setFont(FontsAndBorders.FONT0);
        lbl.setForeground(foreColor);
        return lbl;
    }

}
