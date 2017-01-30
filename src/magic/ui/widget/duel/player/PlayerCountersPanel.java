package magic.ui.widget.duel.player;

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerCountersPanel extends JPanel {

    private static final ImageIcon shieldIcon = MagicImages.getIcon(MagicIcon.SHIELD);
    private static final ImageIcon poisonIcon = MagicImages.getIcon(MagicIcon.POISON);
    private static final ImageIcon energyIcon = MagicImages.getIcon(MagicIcon.ENERGY);
    private static final ImageIcon xpIcon = MagicImages.getIcon(MagicIcon.EXPERIENCE);

    private static final Font ZONE_FONT = new Font("Dialog", Font.BOLD, 12);

    private final JLabel shieldLabel = new JLabel(shieldIcon);
    private final JLabel poisonLabel = new JLabel(poisonIcon);
    private final JLabel energyLabel = new JLabel(energyIcon);
    private final JLabel xpLabel = new JLabel(xpIcon);

    public PlayerCountersPanel() {
        setOpaque(false);
        setLayout(new MigLayout("insets 1 4 0 4, gapx 6", "[40!]"));
        add(shieldLabel);
        add(poisonLabel);
        add(energyLabel);
        add(xpLabel);
    }

    void updateDisplay(PlayerViewerInfo playerInfo) {

        shieldLabel.setIcon(shieldIcon);
        shieldLabel.setText(String.valueOf(playerInfo.preventDamage));
        shieldLabel.setFont(ZONE_FONT);
        shieldLabel.setHorizontalAlignment(SwingConstants.LEFT);
        shieldLabel.setToolTipText("Shield counters");

        poisonLabel.setIcon(poisonIcon);
        poisonLabel.setText(String.valueOf(playerInfo.poison));
        poisonLabel.setFont(ZONE_FONT);
        poisonLabel.setHorizontalAlignment(SwingConstants.LEFT);
        poisonLabel.setToolTipText("Poison counters");

        energyLabel.setIcon(energyIcon);
        energyLabel.setText(String.valueOf(playerInfo.energy));
        energyLabel.setFont(ZONE_FONT);
        energyLabel.setHorizontalAlignment(SwingConstants.LEFT);
        energyLabel.setToolTipText("Energy counters");

        xpLabel.setIcon(xpIcon);
        xpLabel.setText(String.valueOf(playerInfo.experience));
        xpLabel.setFont(ZONE_FONT);
        xpLabel.setHorizontalAlignment(SwingConstants.LEFT);
        xpLabel.setToolTipText("Experience counters");
    }

}
