package magic.ui.widget.player;

import magic.model.player.PlayerProfile;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Dimension;

/**
 * Composite JPanel displays player avatar and player details.
 *
 */
@SuppressWarnings("serial")
public class PlayerProfilePanel extends JPanel {

    private final MigLayout migLayout = new MigLayout();
    private final JLabel avatarLabel = new JLabel();
    private final PlayerDetailsPanel detailsPanel = new PlayerDetailsPanel();

    public PlayerProfilePanel(final PlayerProfile player) {
        setLookAndFeel();
        setPlayer(player);
    }
    public PlayerProfilePanel() {
        setLookAndFeel();
    }

    private void setLookAndFeel() {
        setPreferredSize(new Dimension(0, 70));
        setOpaque(false);
        setLayout(migLayout);
    }

    public void setPlayer(final PlayerProfile player) {
        avatarLabel.setIcon(player.getAvatar().getIcon(2));
        detailsPanel.setPlayer(player);
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0 0 0 6, gap 0");
        add(avatarLabel, "w 70!, h 70!");
        add(detailsPanel, "w 100%");
    }

}
