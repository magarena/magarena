package magic.ui.duel.player;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import magic.model.MagicPlayerZone;
import magic.ui.duel.viewer.PlayerViewerInfo;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerZoneButtonsPanel extends JPanel {

    private static ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<MagicPlayerZone, ZoneToggleButton> zoneButtons;

    public PlayerZoneButtonsPanel(final PlayerViewerInfo playerInfo) {

        // LinkedHashMap so insertion order is retained.
        zoneButtons = new LinkedHashMap<>();
        zoneButtons.put(MagicPlayerZone.LIBRARY, getZoneToggleButton(
                MagicPlayerZone.LIBRARY, playerInfo.library.size(), false)
        );
        zoneButtons.put(MagicPlayerZone.HAND, getZoneToggleButton(
                MagicPlayerZone.HAND, playerInfo.hand.size(), !playerInfo.isAi)
        );
        zoneButtons.put(MagicPlayerZone.GRAVEYARD, getZoneToggleButton(
                MagicPlayerZone.GRAVEYARD, playerInfo.graveyard.size(), true)
        );
        zoneButtons.put(MagicPlayerZone.EXILE, getZoneToggleButton(
                MagicPlayerZone.EXILE, playerInfo.exile.size(), true)
        );

        setLayout(new MigLayout("insets 0 2 0 0"));
        for (ZoneToggleButton button : zoneButtons.values()) {
            add(button);
        }

        setOpaque(false);

    }

    private ZoneToggleButton getZoneToggleButton(
            final MagicPlayerZone zone,
            final int cardCount,
            final boolean isActive) {

        final ZoneToggleButton btn = new ZoneToggleButton(zone, cardCount, isActive);
        buttonGroup.add(btn);
        return btn;
    }

    void updateDisplay(final PlayerViewerInfo playerInfo) {
        if (playerInfo != null) {
            zoneButtons.get(MagicPlayerZone.HAND).setNumberOfCardsInZone(playerInfo.hand.size());
            zoneButtons.get(MagicPlayerZone.LIBRARY).setNumberOfCardsInZone(playerInfo.library.size());
            zoneButtons.get(MagicPlayerZone.GRAVEYARD).setNumberOfCardsInZone(playerInfo.graveyard.size());
            zoneButtons.get(MagicPlayerZone.EXILE).setNumberOfCardsInZone(playerInfo.exile.size());

        }
    }

    public static void clearButtonGroup() {
        buttonGroup = new ButtonGroup();
    }

    void setActiveZone(MagicPlayerZone zone) {
        final ZoneToggleButton btn = zoneButtons.get(zone);
        btn.setSelected(true);
        btn.doAlertAnimation();
    }

}
