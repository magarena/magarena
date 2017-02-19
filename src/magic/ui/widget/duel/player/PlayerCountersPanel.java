package magic.ui.widget.duel.player;

import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerCountersPanel extends JPanel {

    // translatable UI text (prefix with _S).
    private static final String _S1 = "Shield counters";
    private static final String _S2 = "Poison counters";
    private static final String _S3 = "Energy counters";
    private static final String _S4 = "Experience counters";

    private final PlayerCounterPanel shieldCounter;
    private final PlayerCounterPanel poisonCounter;
    private final PlayerCounterPanel energyCounter;
    private final PlayerCounterPanel xpCounter;

    public PlayerCountersPanel() {

        shieldCounter = new PlayerCounterPanel(MagicIcon.SHIELD, MText.get(_S1));
        poisonCounter = new PlayerCounterPanel(MagicIcon.POISON, MText.get(_S2));
        energyCounter = new PlayerCounterPanel(MagicIcon.ENERGY, MText.get(_S3));
        xpCounter = new PlayerCounterPanel(MagicIcon.EXPERIENCE, MText.get(_S4));

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
