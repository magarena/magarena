package magic.ui.player;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
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
    private final JTextArea statsTextArea;
    private PlayerProfile player;

    // CTR
    public DuelPlayerPanel(final PlayerProfile player) {
        this.player = player;
        this.statsTextArea = getNewStatsTextArea();
        setLookAndFeel();
        setPlayer(player);
    }

    private JTextArea getNewStatsTextArea() {
        final JTextArea textArea = new JTextArea();
        // propagate mouse events to DuelPlayerPanel container
        // so that highlighting works properly.
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { dispatchEvent(e); }
            @Override
            public void mouseEntered(MouseEvent e) { dispatchEvent(e); }
            @Override
            public void mouseExited(MouseEvent e) { dispatchEvent(e); }
        });
        return textArea;
    }

    private void setLookAndFeel() {
        refreshStyle();
        setLayout(migLayout);
        // stats text area
        statsTextArea.setEditable(false);
        statsTextArea.setFocusable(false);
        statsTextArea.setTabSize(16);
        statsTextArea.setBackground(new Color(0,0,0,1));
        statsTextArea.setBorder(null);
        statsTextArea.setForeground(Color.LIGHT_GRAY);
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, gapy 10");
        add(playerProfilePanel, "w 100%, h 70!");
        add(statsTextArea, "w 100%, h 100%");
    }

    public void setPlayer(final PlayerProfile player) {
        this.player = player;
        playerProfilePanel.setPlayer(player);
        statsTextArea.setText(player.getStats().toString());
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
