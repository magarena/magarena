package magic.ui.duel.player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Set;
import javax.swing.JLabel;
import magic.model.MagicPlayerZone;
import magic.model.player.AiPlayer;
import magic.ui.SwingGameController;
import magic.ui.duel.viewer.ChoiceViewer;
import magic.ui.duel.viewer.PlayerViewerInfo;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.PanelButton;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class GamePlayerPanel extends TexturedPanel implements ChoiceViewer {
    
    private PlayerViewerInfo playerInfo;
    private PlayerZoneButtonsPanel zoneButtonsPanel;
    private PlayerImagePanel avatarPanel;
    private final PanelButton avatarButton;

    public GamePlayerPanel(final SwingGameController controller, final PlayerViewerInfo playerInfo) {

        this.playerInfo = playerInfo;

        setOpaque(false);
        setPreferredSize(new Dimension(0, 80));
        setMinimumSize(getPreferredSize());

        zoneButtonsPanel = new PlayerZoneButtonsPanel(playerInfo, controller);

        avatarPanel = new PlayerImagePanel(playerInfo, controller.getGame());

        avatarButton = new PanelButton() {
            @Override
            public Color getValidColor() {
                return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
            }
            @Override
            public void mouseClicked() {
                controller.processClick(playerInfo.player);
            }
        };
        avatarButton.setComponent(avatarPanel);

        setLayout(new MigLayout("flowy, insets 0, gap 4 1, wrap 2"));
        add(avatarButton, "w 80!, h 80!, spany 2");
        add(getPlayerLabel(), "gaptop 3");
        add(zoneButtonsPanel, "w 100%, h 100%");

        if (controller != null) {
            controller.registerChoiceViewer(this);
        }

    }

    private JLabel getPlayerLabel() {
        final StringBuffer sb = new StringBuffer(playerInfo.name);
        if (playerInfo.player.isAiPlayerProfile()) {
            final AiPlayer aiPlayer = (AiPlayer)playerInfo.player.getPlayerDefinition().getPlayerProfile();
            sb.append(", level ").append(aiPlayer.getAiLevel()).append(" AI (").append(aiPlayer.getAiType().name()).append(")");
        }
        final JLabel lbl = new JLabel(sb.toString());
        lbl.setFont(new Font("dialog", Font.PLAIN, 9));
        return lbl;
    }

    @Override
    public void showValidChoices(Set<?> validChoices) {
        avatarButton.setValid(!validChoices.isEmpty() ? validChoices.contains(playerInfo.player) : false);
    }

    public void updateDisplay(final PlayerViewerInfo playerInfo) {
        this.playerInfo = playerInfo;
        avatarPanel.updateDisplay(playerInfo);
        zoneButtonsPanel.updateDisplay(playerInfo);
    }

    public void setActiveZone(MagicPlayerZone zone) {
        zoneButtonsPanel.setActiveZone(zone);
    }

    public PlayerViewerInfo getPlayerInfo() {
        return playerInfo;
    }

    public Rectangle getZoneButtonRectangle(MagicPlayerZone zone, Component canvas) {
        return zoneButtonsPanel.getZoneButtonRectangle(zone, canvas);
    }

    public void doFlashPlayerHandZoneButton() {
        zoneButtonsPanel.doFlashPlayerHandZoneButton();
    }

}
