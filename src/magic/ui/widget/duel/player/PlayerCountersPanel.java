package magic.ui.widget.duel.player;

import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerCountersPanel extends JPanel {

    private final PlayerCounterPanel shieldCounter;
    private final PlayerCounterPanel poisonCounter;
    private final PlayerCounterPanel energyCounter;
    private final PlayerCounterPanel xpCounter;

    public PlayerCountersPanel() {

        shieldCounter = new PlayerCounterPanel(MagicIcon.SHIELD, "Shield counters");
        poisonCounter = new PlayerCounterPanel(MagicIcon.POISON, "Poison counters");
        energyCounter = new PlayerCounterPanel(MagicIcon.ENERGY, "Energy counters");
        xpCounter = new PlayerCounterPanel(MagicIcon.EXPERIENCE, "Experience counters");

        setLayout(new MigLayout("flowy, gapy 4, insets 2 0 0 2", "fill, grow"));
        add(shieldCounter);
        add(poisonCounter);
        add(energyCounter);
        add(xpCounter);

        setOpaque(false);
    }

    void updateDisplay(PlayerViewerInfo playerInfo) {
        shieldCounter.update(playerInfo.preventDamage);
        poisonCounter.update(playerInfo.poison);
        energyCounter.update(playerInfo.energy);
        xpCounter.update(playerInfo.experience);
    }

}
