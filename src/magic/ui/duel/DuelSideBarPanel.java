package magic.ui.duel;

import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.model.MagicPlayerZone;
import magic.ui.IPlayerZoneListener;
import magic.ui.SwingGameController;
import magic.ui.duel.player.GamePlayerPanel;
import magic.ui.duel.player.PlayerZoneButtonsPanel;
import magic.ui.duel.resolution.DefaultResolutionProfile;
import magic.ui.duel.viewer.GameStatusPanel;
import magic.ui.duel.viewer.LogBookViewer;
import magic.ui.duel.viewer.LogStackViewer;
import magic.ui.duel.viewer.PlayerViewerInfo;
import magic.ui.duel.viewer.StackViewer;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelSideBarPanel extends JPanel implements IPlayerZoneListener {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final GamePlayerPanel opponentViewer;
    private final GamePlayerPanel playerViewer;
    private final LogStackViewer logStackViewer;
    private final LogBookViewer logBookViewer;
    private final GameStatusPanel gameStatusPanel;
    private final SwingGameController controller;

    DuelSideBarPanel(final SwingGameController controller, final StackViewer imageStackViewer) {
        this.controller = controller;

        PlayerZoneButtonsPanel.clearButtonGroup();
        opponentViewer = new GamePlayerPanel(controller, controller.getViewerInfo().getPlayerInfo(true));
        playerViewer = new GamePlayerPanel(controller, controller.getViewerInfo().getPlayerInfo(false));

        logBookViewer = new LogBookViewer(controller.getGame().getLogBook());
        logBookViewer.setVisible(!CONFIG.isLogViewerDisabled());

        logStackViewer = new LogStackViewer(logBookViewer, imageStackViewer);
        logStackViewer.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        gameStatusPanel= new GameStatusPanel(controller);
        gameStatusPanel.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        controller.addPlayerZoneListener(this);

        setOpaque(false);
    }

    GameStatusPanel getGameStatusPanel() {
        return gameStatusPanel;
    }

    LogBookViewer getLogBookViewer() {
        return logBookViewer;
    }

    void doSetLayout() {

        final int insets = 6;
        final int maxWidth = DefaultResolutionProfile.getPanelWidthLHS() - (insets * 2);

        removeAll();
        setLayout(new MigLayout("insets " + insets + ", gap 0 10, flowy"));

        add(opponentViewer, "w " + maxWidth + "!, h " + DefaultResolutionProfile.PLAYER_VIEWER_HEIGHT_SMALL + "!");
        add(logStackViewer, "w " + maxWidth + "!, h 100%");
        add(gameStatusPanel, "w " + maxWidth + "!, h " + DefaultResolutionProfile.GAME_VIEWER_HEIGHT + "!");
        add(playerViewer,   "w " + maxWidth + "!, h " + DefaultResolutionProfile.PLAYER_VIEWER_HEIGHT_SMALL + "!");

        logStackViewer.setLogStackLayout();

    }

    void doUpdate() {
        opponentViewer.updateDisplay(controller.getViewerInfo().getPlayerInfo(true));
        playerViewer.updateDisplay(controller.getViewerInfo().getPlayerInfo(false));
        gameStatusPanel.update();
    }

    @Override
    public void setActivePlayerZone(PlayerViewerInfo playerInfo, MagicPlayerZone zone) {
        if (playerInfo == controller.getViewerInfo().getPlayerInfo(true)) {
            opponentViewer.setActiveZone(zone);
        } else {
            playerViewer.setActiveZone(zone);
        }
    }

}
