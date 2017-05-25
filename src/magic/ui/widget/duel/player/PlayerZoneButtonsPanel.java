package magic.ui.widget.duel.player;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.model.MagicCardList;
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

    public static int[] getKeyBindings(MagicPlayerZone aZone) {
        switch (aZone) {
            case HAND: return new int[]{KeyEvent.VK_1, KeyEvent.VK_Z, KeyEvent.VK_NUMPAD1};
            case GRAVEYARD: return new int[]{KeyEvent.VK_2, KeyEvent.VK_X, KeyEvent.VK_NUMPAD2};
            case EXILE: return new int[]{KeyEvent.VK_3, KeyEvent.VK_C, KeyEvent.VK_NUMPAD3};
            default: return new int[]{};
        }
    }

    public PlayerZoneButtonsPanel(final PlayerViewerInfo playerInfo, final SwingGameController controller) {

        this.playerInfo = playerInfo;
        this.controller = controller;

        // LinkedHashMap so insertion order is retained.
        zoneButtons = new LinkedHashMap<>();
        zoneButtons.put(MagicPlayerZone.LIBRARY, getLibraryZoneButton(playerInfo));
        zoneButtons.put(MagicPlayerZone.HAND, getZoneToggleButton(
                MagicPlayerZone.HAND, playerInfo.hand, true)
        );
        zoneButtons.put(MagicPlayerZone.GRAVEYARD, getZoneToggleButton(
                MagicPlayerZone.GRAVEYARD, playerInfo.graveyard, true)
        );
        zoneButtons.put(MagicPlayerZone.EXILE, getZoneToggleButton(
                MagicPlayerZone.EXILE, playerInfo.exile, true)
        );

        // hidden zone button that is activated whenever player is
        // required to choose one or more cards.
        zoneButtons.put(MagicPlayerZone.CHOICE, getZoneToggleButton(
                MagicPlayerZone.CHOICE, MagicCardList.NONE, true)
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
            player.library,
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

    private String getTooltipText(MagicPlayerZone aZone) {
        String keyText = Arrays.stream(getKeyBindings(aZone))
            .mapToObj(k -> KeyEvent.getKeyText(k))
            .collect(Collectors.joining(","));
        return keyText.isEmpty()
            ? String.format("<html><b>%s</b></html>", aZone.getName())
            : String.format("<html><b>%s</b> [%s]</html>", aZone.getName(), keyText);
    }

    private ZoneToggleButton getZoneToggleButton(final MagicPlayerZone zone, final MagicCardList cards, final boolean isActive) {

        final ZoneToggleButton btn = new ZoneToggleButton(zone, cards, isActive);
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
        btn.setToolTipText(getTooltipText(zone));
        buttonGroup.add(btn);
        return btn;
    }

    void updateDisplay(final PlayerViewerInfo playerInfo) {
        this.playerInfo = playerInfo;
        if (playerInfo != null) {
            zoneButtons.get(MagicPlayerZone.HAND).setCards(playerInfo.hand);
            zoneButtons.get(MagicPlayerZone.LIBRARY).setCards(playerInfo.library);
            zoneButtons.get(MagicPlayerZone.GRAVEYARD).setCards(playerInfo.graveyard);
            zoneButtons.get(MagicPlayerZone.EXILE).setCards(playerInfo.exile);
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

    private boolean isValidChoiceButton(ZoneToggleButton btn) {
        return btn.isNot(MagicPlayerZone.LIBRARY)
            && btn.isNot(MagicPlayerZone.CHOICE);
    }

    void showValidChoices(Set<?> validChoices) {
        zoneButtons.values().stream()
            .filter(btn -> isValidChoiceButton(btn))
            .forEach(btn -> btn.setValidChoices(validChoices));
    }

}
