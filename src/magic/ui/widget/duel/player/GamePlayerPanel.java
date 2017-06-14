package magic.ui.widget.duel.player;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Set;
import javax.swing.JLabel;
import magic.model.MagicPlayerZone;
import magic.ui.FontsAndBorders;
import magic.ui.IChoiceViewer;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class GamePlayerPanel extends TexturedPanel implements IChoiceViewer {

    private static final int PANEL_HEIGHT = 82;

    private PlayerViewerInfo playerInfo;
    private final PlayerZoneButtonsPanel zoneButtonsPanel;
    private final PlayerCountersPanel countersPanel;
    private final PlayerAvatarButton avatarButton;

    public GamePlayerPanel(SwingGameController controller, final PlayerViewerInfo playerInfo) {

        this.playerInfo = playerInfo;

        zoneButtonsPanel = new PlayerZoneButtonsPanel(playerInfo, controller);
        avatarButton = new PlayerAvatarButton(playerInfo, controller);
        countersPanel = new PlayerCountersPanel();

        setLayout(new MigLayout("flowy, insets 0, gap 4 1, wrap 2"));
        add(avatarButton, "w 80!, h 80!, spany 2");
        add(getPlayerLabel(), "gaptop 3");
        add(zoneButtonsPanel, "w 100%, h 100%");
        add(countersPanel, "w 100%, h 100%, spany 2");

        setBorder(FontsAndBorders.BLACK_BORDER);
        setPreferredSize(new Dimension(0, PANEL_HEIGHT));
        setMinimumSize(getPreferredSize());
    }

    private JLabel getPlayerLabel() {
        final JLabel lbl = new JLabel(playerInfo.playerLabel);
        lbl.setFont(new Font("dialog", Font.PLAIN, 9));
        return lbl;
    }

    private boolean isThisPlayerValidChoice(Set<?> validChoices) {
        return !validChoices.isEmpty() && validChoices.contains(playerInfo.player);
    }

    @Override
    public void showValidChoices(Set<?> validChoices) {
        avatarButton.showAsValidChoice(isThisPlayerValidChoice(validChoices));
        if (playerInfo.isHuman()) {
            zoneButtonsPanel.showValidChoices(validChoices);
        }
    }

    public void updateDisplay(final PlayerViewerInfo playerInfo) {
        this.playerInfo = playerInfo;
        avatarButton.updateDisplay(playerInfo);
        zoneButtonsPanel.updateDisplay(playerInfo);
        countersPanel.updateDisplay(playerInfo);
    }

    public void setActiveZone(MagicPlayerZone zone) {
        zoneButtonsPanel.setActiveZone(zone);
    }

    public PlayerViewerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void doFlashPlayerHandZoneButton() {
        zoneButtonsPanel.doFlashPlayerHandZoneButton();
    }

    public void doFlashLibraryZoneButton() {
        zoneButtonsPanel.doFlashLibraryZoneButton();
    }

    public void doHighlightPlayerZone(MagicPlayerZone zone, boolean b) {
        zoneButtonsPanel.doHighlightPlayerZone(zone, b);
    }

    public Rectangle getLibraryButtonLayout(Component canvas) {
        return zoneButtonsPanel.getZoneButtonRectangle(MagicPlayerZone.LIBRARY, canvas);
    }

    public Rectangle getHandButtonLayout(Component canvas) {
        return zoneButtonsPanel.getZoneButtonRectangle(MagicPlayerZone.HAND, canvas);
    }

}
