package magic.ui.screen.duel.setup;

import java.awt.Color;
import javax.swing.BorderFactory;
import magic.model.player.PlayerProfile;
import magic.ui.screen.interfaces.IThemeStyle;
import magic.ui.theme.Theme;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Displays player avatar, details and statistics.
 *
 */
@SuppressWarnings("serial")
public class DuelPlayerPanel extends TexturedPanel implements IThemeStyle {

    private final MigLayout migLayout = new MigLayout();
    private final PlayerProfilePanel playerProfilePanel = new PlayerProfilePanel();
    private final PlayerStatsPanel statsPanel;
    private PlayerProfile player;

    // CTR
    public DuelPlayerPanel(final PlayerProfile player) {
        this.player = player;
        statsPanel = new PlayerStatsPanel(player.getStats());
        setLookAndFeel();
        setPlayer(player);
    }

    private void setLookAndFeel() {
        refreshStyle();
        setLayout(migLayout);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, gapy 10");
        add(playerProfilePanel, "w 100%, h 70!");
        add(statsPanel, "w 100%, h 100%");
    }

    public void setPlayer(final PlayerProfile player) {
        this.player = player;
        playerProfilePanel.setPlayer(player);
        statsPanel.setPlayerStats(player.getStats());
        refreshLayout();
    }
    public PlayerProfile getPlayer() {
        return player;
    }

    @Override
    public void refreshStyle() {
        final Color refBG = MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND);
        final Color thisBG = MagicStyle.getTranslucentColor(refBG, 220);
        setBackground(thisBG);
        setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
    }

}
