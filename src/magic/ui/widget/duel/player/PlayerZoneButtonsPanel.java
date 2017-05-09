package magic.ui.widget.duel.player;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.model.MagicPlayerZone;
import magic.translate.MText;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerZoneButtonsPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "Library";

    private static ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<MagicPlayerZone, ZoneToggleButton> zoneButtons;
    private final SwingGameController controller;
    private final PlayerViewerInfo playerInfo;

    public PlayerZoneButtonsPanel(final PlayerViewerInfo playerInfo, final SwingGameController controller) {

        this.playerInfo = playerInfo;
        this.controller = controller;

        // LinkedHashMap so insertion order is retained.
        zoneButtons = new LinkedHashMap<>();
        zoneButtons.put(MagicPlayerZone.LIBRARY, getZoneToggleButton(
                MagicPlayerZone.LIBRARY, playerInfo.library.size(), false)
        );
        zoneButtons.get(MagicPlayerZone.LIBRARY).setToolTipText(
            String.format("<html><b>%s</b><br>%s</html>",
                MText.get(_S1),
                playerInfo.getQualifiedDeckName())
        );
        zoneButtons.put(MagicPlayerZone.HAND, getZoneToggleButton(
                MagicPlayerZone.HAND, playerInfo.hand.size(), true)
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

    private ZoneToggleButton getZoneToggleButton(final MagicPlayerZone zone, final int cardCount, final boolean isActive) {

        final ZoneToggleButton btn = new ZoneToggleButton(zone, cardCount, isActive);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (btn.isActive() && SwingUtilities.isLeftMouseButton(e)) {
                    controller.getPlayerZoneViewer().setPlayerZone(playerInfo, zone);
                }
            }
            @Override
            public void mouseEntered(MouseEvent ev) {
                if (btn.isActive()) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
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
        if (zone == MagicPlayerZone.LIBRARY) {
            btn.doAlertAnimation();
        }
    }

    Rectangle getZoneButtonRectangle(MagicPlayerZone zone, Component canvas) {
        final ZoneToggleButton btn = zoneButtons.get(zone);
        final Point pointOnCanvas = SwingUtilities.convertPoint(this, btn.getLocation(), canvas);
        return new Rectangle(pointOnCanvas.x, pointOnCanvas.y, btn.getWidth(), btn.getHeight());
    }

    void doFlashPlayerHandZoneButton() {
        zoneButtons.get(MagicPlayerZone.HAND).doAlertAnimation();
    }

    void doFlashLibraryZoneButton() {
        zoneButtons.get(MagicPlayerZone.LIBRARY).doAlertAnimation();
    }

    void doHighlightPlayerZone(MagicPlayerZone zone, boolean b) {
        zoneButtons.get(zone).doHighlight(b);
    }

}
