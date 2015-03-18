package magic.ui.duel.player;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.model.MagicPlayerZone;
import magic.ui.duel.viewer.PlayerViewerInfo;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerZoneButtonsPanel extends JPanel {

    private static ButtonGroup buttonGroup = new ButtonGroup();

    private final ZoneToggleButton libraryZoneButton;
    private final ZoneToggleButton handZoneButton;
    private final ZoneToggleButton graveyardZoneButton;
    private final ZoneToggleButton exileZoneButton;

    public PlayerZoneButtonsPanel(final PlayerViewerInfo playerInfo) {

        setLayout(new MigLayout("insets 0 2 0 0"));

        libraryZoneButton = getZoneToggleButton(
                MagicPlayerZone.LIBRARY, MagicIcon.LIBRARY_ZONE, playerInfo.library.size(), "Library", false, true);
        handZoneButton = getZoneToggleButton(
                MagicPlayerZone.HAND, MagicIcon.HAND_ZONE, playerInfo.hand.size(), "Hand", !playerInfo.isAi, true);
        graveyardZoneButton = getZoneToggleButton(
                MagicPlayerZone.GRAVEYARD, MagicIcon.GRAVEYARD_ZONE, playerInfo.graveyard.size(), "Graveyard", true, true);
        exileZoneButton = getZoneToggleButton(
                MagicPlayerZone.EXILE, MagicIcon.EXILE_ZONE, playerInfo.exile.size(), "Exile", true, true);

        add(libraryZoneButton);
        add(handZoneButton);
        add(graveyardZoneButton);
        add(exileZoneButton);

        if (buttonGroup.getButtonCount() > 0) {
            buttonGroup.getElements().nextElement().setSelected(true);
        }

        setOpaque(false);

    }

    private ZoneToggleButton getZoneToggleButton(
            final MagicPlayerZone zone,
            final MagicIcon icon,
            final int cardCount,
            final String tooltip,
            final boolean isActive,
            final boolean isAnimated) {

        final ZoneToggleButton btn = new ZoneToggleButton(zone, icon, cardCount, isActive, isAnimated);
        btn.setToolTipText(tooltip);
        buttonGroup.add(btn);
        return btn;
    }

    void updateDisplay(final PlayerViewerInfo playerInfo) {
        if (playerInfo != null) {
            handZoneButton.setNumberOfCardsInZone(playerInfo.hand.size());
            libraryZoneButton.setNumberOfCardsInZone(playerInfo.library.size());
            graveyardZoneButton.setNumberOfCardsInZone(playerInfo.graveyard.size());
            exileZoneButton.setNumberOfCardsInZone(playerInfo.exile.size());
        }
    }

    public static void clearButtonGroup() {
        buttonGroup = new ButtonGroup();
    }

    void setActiveZone(MagicPlayerZone zone) {
        System.out.println("PlayerZoneButtonsPanel.setActiveZone(" + zone + ")");
        final ZoneToggleButton btn = getZoneToggleButton(zone);
        btn.setSelected(true);
        btn.doAlertAnimation();
    }

    private ZoneToggleButton getZoneToggleButton(MagicPlayerZone zone) {
        switch (zone) {
            case HAND: return handZoneButton;
            case LIBRARY: return libraryZoneButton;
            case GRAVEYARD: return graveyardZoneButton;
            case EXILE: return exileZoneButton;
            default:
                throw new RuntimeException("Invalid MagicPlayerZone");
        }
    }

}
