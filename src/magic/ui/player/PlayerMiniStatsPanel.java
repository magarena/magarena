package magic.ui.player;

import magic.model.player.PlayerStatistics;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import java.awt.Color;

@SuppressWarnings("serial")
public class PlayerMiniStatsPanel extends JPanel {

    private final static Border CELL_BORDER = BorderFactory.createDashedBorder(null);

    private final MigLayout migLayout = new MigLayout();
    private PlayerStatistics stats;
    private final Color foreColor;

    public PlayerMiniStatsPanel(final PlayerStatistics stats, final Color foreColor) {
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
        migLayout.setColumnConstraints("[][30!]");
        // stats table
        add(new JLabel());
        add(getStatsLabel("P"), "w 100%");
        add(getStatsLabel("W"), "w 100%");
        add(getStatsLabel("L"), "w 100%");
        add(getStatsLabel("Duels"), "w 60!");
        add(getStatsLabel(stats.getDuelsPlayed().toString()), "w 100%");
        add(getStatsLabel(stats.getDuelsWon().toString()), "w 100%");
        add(getStatsLabel(stats.getDuelsLost().toString()), "w 100%");
        add(getStatsLabel("Games"), "w 60!");
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
