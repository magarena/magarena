package magic.ui.duel;

import java.awt.Color;
import javax.swing.BorderFactory;
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
import magic.ui.duel.viewer.UserActionPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelSideBarPanel extends JPanel implements IPlayerZoneListener {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final int TOTAL_PLAYERS = 2;

    private final PlayerCompositePanel[] playerCompositePanels = new PlayerCompositePanel[TOTAL_PLAYERS];
    private final LogStackViewer logStackViewer;
    private final LogBookViewer logBookViewer;
    private final GameStatusPanel gameStatusPanel;
    private final SwingGameController controller;

    DuelSideBarPanel(final SwingGameController controller, final StackViewer imageStackViewer) {

        this.controller = controller;

        logBookViewer = new LogBookViewer(controller.getGame().getLogBook());
        logBookViewer.setVisible(!CONFIG.isLogViewerDisabled());

        logStackViewer = new LogStackViewer(logBookViewer, imageStackViewer);
        logStackViewer.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        gameStatusPanel= new GameStatusPanel(controller);
        gameStatusPanel.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        controller.addPlayerZoneListener(this);

        createPlayerPanels();

        setOpaque(false);
    }

    private void createPlayerPanels() {

        PlayerZoneButtonsPanel.clearButtonGroup();

        // playerViewer
        playerCompositePanels[0] = new PlayerCompositePanel(
                new GamePlayerPanel(controller, controller.getViewerInfo().getPlayerInfo(false)),
                gameStatusPanel.getUserActionPanel()
        );
        // opponentViewer
        playerCompositePanels[1] = new PlayerCompositePanel(
            new GamePlayerPanel(controller, controller.getViewerInfo().getPlayerInfo(true))
        );

//        for (int i = 0; i < gameInfo.getPlayerInfo().length; i++) {
//            final PlayerViewerInfo playerInfo = gameInfo.getPlayerInfo(i);
//            if (playerInfo.isAi || MagicSystem.isAiVersusAi()) {
//                playerCompositePanels[i] = new PlayerCompositePanel(
//                        new GamePlayerPanel(controller, playerInfo)
//                );
//            } else {
//                playerCompositePanels[i] = new PlayerCompositePanel(
//                        new GamePlayerPanel(controller, playerInfo),
//                        new UserActionPanel(controller)
//                );
//            }
//        }

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
        setLayout(new MigLayout("insets " + insets + ", gap 0 6, flowy"));

        add(playerCompositePanels[1], "w " + maxWidth + "!");
        add(logStackViewer, "w " + maxWidth + "!, h 100%");
        add(gameStatusPanel, "w " + maxWidth + "!");
        add(playerCompositePanels[0],   "w " + maxWidth + "!");

        logStackViewer.setLogStackLayout();

    }

    void doUpdate() {
        playerCompositePanels[0].getPlayerPanel().updateDisplay(controller.getViewerInfo().getPlayerInfo(false));
        playerCompositePanels[1].getPlayerPanel().updateDisplay(controller.getViewerInfo().getPlayerInfo(true));
        gameStatusPanel.update();
    }

    @Override
    public void setActivePlayerZone(PlayerViewerInfo playerInfo, MagicPlayerZone zone) {
        if (playerInfo == controller.getViewerInfo().getPlayerInfo(true)) {
            playerCompositePanels[1].getPlayerPanel().setActiveZone(zone);
        } else {
            playerCompositePanels[0].getPlayerPanel().setActiveZone(zone);
        }
    }

    private class PlayerCompositePanel extends TexturedPanel {

        private final GamePlayerPanel playerPanel;
        private final UserActionPanel promptPanel;

        PlayerCompositePanel(final GamePlayerPanel playerPanel, final UserActionPanel userActionPanel) {

            this.playerPanel = playerPanel;
            this.promptPanel = userActionPanel;

            setOpaque(true);
            setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

            final MigLayout layout = new MigLayout("insets 0, gap 0, flowy");
            layout.setColumnConstraints(
                    "[100%, fill]"
            );
            setLayout(layout);
            add(playerPanel);
            if (userActionPanel != null) {
                add(userActionPanel);
            }
        }

        PlayerCompositePanel(final GamePlayerPanel playerPanel) {
            this(playerPanel, null);
        }

        GamePlayerPanel getPlayerPanel() {
            return playerPanel;
        }

        UserActionPanel getUserActionPanel() {
            return promptPanel;
        }

    }

}
