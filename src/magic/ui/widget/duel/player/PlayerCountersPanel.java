package magic.ui.widget.duel.player;

import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerCountersPanel extends JPanel {

    private static final ImageIcon shieldIcon = MagicImages.getIcon(MagicIcon.SHIELD);
    private static final ImageIcon poisonIcon = MagicImages.getIcon(MagicIcon.POISON);
    private static final ImageIcon energyIcon = MagicImages.getIcon(MagicIcon.ENERGY);
    private static final ImageIcon xpIcon = MagicImages.getIcon(MagicIcon.EXPERIENCE);

    private static final ImageIcon shieldIconOff = getSwitchedOffImage(shieldIcon);
    private static final ImageIcon poisonIconOff = getSwitchedOffImage(poisonIcon);
    private static final ImageIcon energyIconOff = getSwitchedOffImage(energyIcon);
    private static final ImageIcon xpIconOff = getSwitchedOffImage(xpIcon);

    private static final Color OFF_COLOR = MagicStyle.getTranslucentColor(Color.BLACK, 100);
    private static final Font COUNTER_FONT = new Font("Dialog", Font.PLAIN, 11);

    private final JLabel shieldLabel;
    private final JLabel poisonLabel;
    private final JLabel energyLabel;
    private final JLabel xpLabel;

    private static ImageIcon getSwitchedOffImage(ImageIcon icon) {
        return new ImageIcon(ImageHelper.getTranslucentImage(icon.getImage(), 0.4f));
    }
    
    public PlayerCountersPanel() {

        shieldLabel = getCounterLabel("Shield counters");
        poisonLabel = getCounterLabel("Poison counters");
        energyLabel = getCounterLabel("Energy counters");
        xpLabel = getCounterLabel("Experience counters");

        setLayout(new MigLayout("flowy, insets 2 0 0 2, gapy 4"));
        add(shieldLabel, "w 100%");
        add(poisonLabel, "w 100%");
        add(energyLabel, "w 100%");
        add(xpLabel, "w 100%");

        setOpaque(false);
    }

    private JLabel getCounterLabel(String tooltip) {
        JLabel lbl = new JLabel();
        lbl.setFont(COUNTER_FONT);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl.setHorizontalTextPosition(SwingConstants.LEFT);
        lbl.setToolTipText(tooltip);
        return lbl;
    }

    private void setCounterLabel(JLabel lbl, int value) {
        lbl.setText(String.valueOf(value));
        lbl.setForeground(value > 0 ? Color.BLACK : OFF_COLOR);
    }

    private void setShieldLabel(JLabel lbl, int value) {
        lbl.setIcon(value > 0 ? shieldIcon : shieldIconOff);
        setCounterLabel(lbl, value);
    }

    private void setPoisonLabel(JLabel lbl, int value) {
        lbl.setIcon(value > 0 ? poisonIcon : poisonIconOff);
        setCounterLabel(lbl, value);
    }

    private void setEnergyLabel(JLabel lbl, int value) {
        lbl.setIcon(value > 0 ? energyIcon : energyIconOff);
        setCounterLabel(lbl, value);
    }

    private void setXpLabel(JLabel lbl, int value) {
        lbl.setIcon(value > 0 ? xpIcon : xpIconOff);
        setCounterLabel(lbl, value);
    }

    void updateDisplay(PlayerViewerInfo playerInfo) {
        setShieldLabel(shieldLabel, playerInfo.preventDamage);
        setPoisonLabel(poisonLabel, playerInfo.poison);
        setEnergyLabel(energyLabel, playerInfo.energy);
        setXpLabel(xpLabel, playerInfo.experience);
    }

}
