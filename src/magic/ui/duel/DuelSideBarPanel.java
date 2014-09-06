package magic.ui.duel;

import magic.ui.duel.viewer.PlayerViewer;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.ui.GameController;
import magic.ui.duel.resolution.DefaultResolutionProfile;
import magic.ui.duel.resolution.ResolutionProfileResult;
import magic.ui.duel.resolution.ResolutionProfileType;
import magic.ui.duel.viewer.GameDuelViewer;
import magic.ui.duel.viewer.LogBookViewer;
import magic.ui.duel.viewer.LogStackViewer;
import magic.ui.duel.viewer.StackViewer;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelSideBarPanel extends JPanel {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final PlayerViewer opponentViewer;
    private final PlayerViewer playerViewer;
    private final LogStackViewer logStackViewer;
    private final LogBookViewer logBookViewer;
    private final GameDuelViewer gameDuelViewer;

    DuelSideBarPanel(final GameController controller, final StackViewer imageStackViewer) {
        setOpaque(false);
        //
        opponentViewer = new PlayerViewer(controller, true);
        playerViewer = new PlayerViewer(controller, false);
        logBookViewer = new LogBookViewer(controller.getGame().getLogBook());
        logBookViewer.setVisible(!CONFIG.isLogViewerDisabled());
        logStackViewer = new LogStackViewer(logBookViewer, imageStackViewer);
        logStackViewer.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
        gameDuelViewer= new GameDuelViewer(controller);
        gameDuelViewer.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);
    }

    GameDuelViewer getGameDuelViewer() {
        return gameDuelViewer;
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
        add(gameDuelViewer, "w " + maxWidth + "!, h " + DefaultResolutionProfile.GAME_VIEWER_HEIGHT + "!");
        add(playerViewer,   "w " + maxWidth + "!, h " + DefaultResolutionProfile.PLAYER_VIEWER_HEIGHT_SMALL + "!");

        logStackViewer.setLogStackLayout();

    }

    void doUpdate() {
        opponentViewer.update();
        playerViewer.update();
        gameDuelViewer.update();
    }

    void resizeComponents(final ResolutionProfileResult result) {
        opponentViewer.setSmall(result.getFlag(ResolutionProfileType.GamePlayerViewerSmall));
        playerViewer.setBounds(result.getBoundary(ResolutionProfileType.GamePlayerViewer));
        playerViewer.setSmall(result.getFlag(ResolutionProfileType.GamePlayerViewerSmall));
        gameDuelViewer.setBounds(result.getBoundary(ResolutionProfileType.GameDuelViewer));
    }

}
