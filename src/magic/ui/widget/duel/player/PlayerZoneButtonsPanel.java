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
import magic.ui.ScreenController;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.helpers.MouseHelper;
import magic.ui.screen.duel.game.SwingGameController;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class PlayerZoneButtonsPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "Library";

    private static ButtonGroup buttonGroup = new ButtonGroup();
    private final Map<MagicPlayerZone, ZoneToggleButton> zoneButtons;
    private final SwingGameController controller;
    private PlayerViewerInfo playerInfo;

    public PlayerZoneButtonsPanel(final PlayerViewerInfo playerInfo, final SwingGameController controller) {

        this.playerInfo = playerInfo;
        this.controller = controller;

        // LinkedHashMap so insertion order is retained.
        zoneButtons = new LinkedHashMap<>();
        zoneButtons.put(MagicPlayerZone.LIBRARY, getLibraryZoneButton(playerInfo));
        zoneButtons.put(MagicPlayerZone.HAND, getZoneToggleButton(
                MagicPlayerZone.HAND, playerInfo.hand.size(), true)
        );
        zoneButtons.put(MagicPlayerZone.GRAVEYARD, getZoneToggleButton(
                MagicPlayerZone.GRAVEYARD, playerInfo.graveyard.size(), true)
        );
        zoneButtons.put(MagicPlayerZone.EXILE, getZoneToggleButton(
                MagicPlayerZone.EXILE, playerInfo.exile.size(), true)
        );

        // hidden zone button that is activated whenever player is
        // required to choose one or more cards.
        zoneButtons.put(MagicPlayerZone.CHOICE, getZoneToggleButton(
                MagicPlayerZone.CHOICE, 0, true)
        );
        zoneButtons.get(MagicPlayerZone.CHOICE).setVisible(false);

        setLayout(new MigLayout("insets 0 2 0 0"));
        for (ZoneToggleButton button : zoneButtons.values()) {
            add(button, "hidemode 3");
        }

        setOpaque(false);

    }

    private ZoneToggleButton getLibraryZoneButton(PlayerViewerInfo player) {
        ZoneToggleButton btn = getZoneToggleButton(
            MagicPlayerZone.LIBRARY,
            player.library.size(),
            false
        );
        btn.setToolTipText(
            String.format("<html><b>%s</b><br>%s</html>",
                MText.get(_S1),
                player.getQualifiedDeckName())
        );
        if (MagicSystem.isDevMode() || MagicSystem.isTestGame()) {
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    ScreenController.showLibraryZoneScreen(playerInfo.library);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    MouseHelper.showHandCursor(PlayerZoneButtonsPanel.this);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    MouseHelper.showDefaultCursor();
                }
            });
        }
        return btn;
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
        this.playerInfo = playerInfo;
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
