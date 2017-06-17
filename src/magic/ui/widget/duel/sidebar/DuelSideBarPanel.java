package magic.ui.widget.duel.sidebar;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerZone;
import magic.ui.IPlayerZoneListener;
import magic.ui.duel.resolution.DefaultResolutionProfile;
import magic.ui.duel.viewerinfo.CardViewerInfo;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.widget.duel.player.GamePlayerPanel;
import magic.ui.widget.duel.player.PlayerZoneButtonsPanel;
import magic.ui.widget.duel.viewer.GameStatusPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DuelSideBarPanel extends JPanel implements IPlayerZoneListener {

    private enum ComponentSlot {
        PLAYER1,
        PLAYER2,
        LOGSTACK,
        TURNINFO;
    }

    private static final int TOTAL_PLAYERS = 2;

    private final GamePlayerPanel[] playerPanels = new GamePlayerPanel[TOTAL_PLAYERS];
    private final LogStackViewer logStackViewer;
    private final GameStatusPanel gameStatusPanel;
    private final SwingGameController controller;
    private final List<LayoutSlot> layoutSlots = new ArrayList<>();

    public DuelSideBarPanel(final SwingGameController controller) {

        this.controller = controller;

        logStackViewer = new LogStackViewer(controller);
        controller.setLogStackViewer(logStackViewer);

        gameStatusPanel= new GameStatusPanel(controller);

        controller.addPlayerZoneListener(this);

        createPlayerPanels();
        setLayoutSlots();

        setOpaque(false);
    }

    private void createPlayerPanels() {
        PlayerZoneButtonsPanel.clearButtonGroup();
        PlayerViewerInfo playerInfo = controller.getGameViewerInfo().getMainPlayer();
        PlayerViewerInfo opponentInfo = controller.getGameViewerInfo().getOpponent();
        playerPanels[0] = new GamePlayerPanel(controller, playerInfo);
        playerPanels[1] = new GamePlayerPanel(controller, opponentInfo);
        controller.registerChoiceViewer(playerPanels[0]);
        controller.registerChoiceViewer(playerPanels[1]);
    }

    public GameStatusPanel getGameStatusPanel() {
        return gameStatusPanel;
    }

    public void doSetLayout() {

        final int insets = 6;
        final int maxWidth = DefaultResolutionProfile.getPanelWidthLHS() - (insets * 2);

        final MigLayout layout = new MigLayout("insets " + insets + ", gap 0 6, flowy");
        layout.setColumnConstraints(
                "[" + maxWidth + "!, fill]"
        );
        setLayout(layout);

        removeAll();
        for (LayoutSlot slot : layoutSlots) {
            add(slot.getComponent(), slot.getLayoutConstraints());
        }

        // IMPORTANT! Ensures you do not "see" it laying out components (when running duel directly).
        revalidate();

    }

    public void doUpdate(final GameViewerInfo gameInfo) {
        playerPanels[0].updateDisplay(gameInfo.getMainPlayer());
        playerPanels[1].updateDisplay(gameInfo.getOpponent());
        gameStatusPanel.update();
        logStackViewer.update();
    }

    @Override
    public void setActivePlayerZone(PlayerViewerInfo playerInfo, MagicPlayerZone zone) {
        if (playerInfo == controller.getGameViewerInfo().getOpponent()) {
            playerPanels[1].setActiveZone(zone);
        } else {
            playerPanels[0].setActiveZone(zone);
        }
    }

    private void setLayoutSlots() {
        layoutSlots.clear();
        for (String s : GeneralConfig.getInstance().getDuelSidebarLayout().split(",")) {
            final ComponentSlot slot = ComponentSlot.valueOf(s.trim());
            layoutSlots.add(getComponentSlot(slot));
        }
    }

    private LayoutSlot getComponentSlot(final ComponentSlot slot) {
        switch (slot) {
            case PLAYER1: return new LayoutSlot(playerPanels[0]);
            case PLAYER2: return new LayoutSlot(playerPanels[1]);
            case LOGSTACK: return new LayoutSlot(logStackViewer, "h 100%");
            case TURNINFO: return new LayoutSlot(gameStatusPanel);
            default:
                throw new AssertionError(slot.name());
        }
    }

    public void refreshLayout() {
        setLayoutSlots();
        doSetLayout();
    }

    public void doFlashLibraryZoneButton(PlayerViewerInfo playerInfo) {
        getPlayerPanel(playerInfo.player).doFlashLibraryZoneButton();
    }

    public void doFlashPlayerHandZoneButton(PlayerViewerInfo playerInfo) {
        getPlayerPanel(playerInfo.player).doFlashPlayerHandZoneButton();
    }

    public Rectangle getStackViewerRectangle(Component canvas) {
        return logStackViewer.getStackViewerRectangle(canvas);
    }

    private GamePlayerPanel getPlayerPanel(final MagicPlayer player) {
        for (GamePlayerPanel panel : playerPanels) {
            if (panel.getPlayerInfo().player == player) {
                return panel;
            }
        }
        throw new RuntimeException("Missing GamePlayerPanel for " + player.getName());
    }

    public void doHighlightPlayerZone(CardViewerInfo cardInfo, MagicPlayerZone zone, boolean b) {
        playerPanels[cardInfo.getControllerIndex()].doHighlightPlayerZone(zone, b);
    }

    public Rectangle getLibraryButtonLayout(PlayerViewerInfo aPlayer, Component canvas) {
        return playerPanels[aPlayer.player.getIndex()].getLibraryButtonLayout(canvas);
    }

    public Rectangle getHandButtonLayout(PlayerViewerInfo aPlayer, Component canvas) {
        return playerPanels[aPlayer.player.getIndex()].getHandButtonLayout(canvas);
    }

    public Rectangle getTurnPanelLayout(Component container) {
        return gameStatusPanel.getTurnPanelLayout(container);
    }

    private static class LayoutSlot {

        private JComponent component;
        private String constraints;

        public LayoutSlot(final JComponent component, final String constraints) {
            this.component = component;
            this.constraints = constraints;
        }

        public LayoutSlot(final JComponent component) {
            this(component, "");
        }

        public JComponent getComponent() {
            return component;
        }

        public String getLayoutConstraints() {
            return constraints;
        }

    }
}
