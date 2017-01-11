package magic.ui.widget.duel.sidebar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerZone;
import magic.ui.IPlayerZoneListener;
import magic.ui.screen.duel.game.SwingGameController;
import magic.ui.duel.viewerinfo.GameViewerInfo;
import magic.ui.widget.duel.player.GamePlayerPanel;
import magic.ui.widget.duel.player.PlayerZoneButtonsPanel;
import magic.ui.duel.resolution.DefaultResolutionProfile;
import magic.ui.widget.duel.viewer.GameStatusPanel;
import magic.ui.duel.viewerinfo.PlayerViewerInfo;
import magic.ui.widget.duel.viewer.UserActionPanel;
import magic.ui.duel.viewerinfo.CardViewerInfo;
import magic.ui.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.utility.MagicSystem;
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

    private final PlayerCompositePanel[] playerCompositePanels = new PlayerCompositePanel[TOTAL_PLAYERS];
    private final LogStackViewer logStackViewer;
    private final LogViewer logBookViewer;
    private final GameStatusPanel gameStatusPanel;
    private final SwingGameController controller;
    private final List<LayoutSlot> layoutSlots = new ArrayList<>();

    public DuelSideBarPanel(final SwingGameController controller, final StackViewer imageStackViewer) {

        this.controller = controller;

        logBookViewer = new LogViewer(controller);
        logStackViewer = new LogStackViewer(logBookViewer, imageStackViewer, controller);
        controller.setLogStackViewer(logStackViewer);

        gameStatusPanel= new GameStatusPanel(controller);
        gameStatusPanel.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        controller.addPlayerZoneListener(this);

        createPlayerPanels();
        setLayoutSlots();

        setOpaque(false);
    }

    private void createPlayerPanels() {

        PlayerZoneButtonsPanel.clearButtonGroup();

        final PlayerViewerInfo playerInfo = controller.getViewerInfo().getPlayerInfo(false);

        if (playerInfo.isAi() || MagicSystem.isAiVersusAi()) {
            playerCompositePanels[0] = new PlayerCompositePanel(
                    new GamePlayerPanel(controller, playerInfo)
            );
        } else {
            playerCompositePanels[0] = new PlayerCompositePanel(
                    new GamePlayerPanel(controller, playerInfo),
                    gameStatusPanel.getUserActionPanel()
            );
        }

        playerCompositePanels[1] = new PlayerCompositePanel(
            new GamePlayerPanel(controller, controller.getViewerInfo().getPlayerInfo(true))
        );

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
        playerCompositePanels[0].getPlayerPanel().updateDisplay(gameInfo.getPlayerInfo(false));
        playerCompositePanels[1].getPlayerPanel().updateDisplay(gameInfo.getPlayerInfo(true));
        gameStatusPanel.update();
        logBookViewer.update();
    }

    @Override
    public void setActivePlayerZone(PlayerViewerInfo playerInfo, MagicPlayerZone zone) {
        if (playerInfo == controller.getViewerInfo().getPlayerInfo(true)) {
            playerCompositePanels[1].getPlayerPanel().setActiveZone(zone);
        } else {
            playerCompositePanels[0].getPlayerPanel().setActiveZone(zone);
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
            case PLAYER1: return new LayoutSlot(playerCompositePanels[0]);
            case PLAYER2: return new LayoutSlot(playerCompositePanels[1]);
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
        for (PlayerCompositePanel panel : playerCompositePanels) {
            if (panel.getPlayerPanel().getPlayerInfo().player == player) {
                return panel.getPlayerPanel();
            }
        }
        throw new RuntimeException("Missing GamePlayerPanel for " + player.getName());
    }

    public void doHighlightPlayerZone(CardViewerInfo cardInfo, MagicPlayerZone zone, boolean b) {
        final GamePlayerPanel playerPanel = playerCompositePanels[cardInfo.getControllerIndex()].getPlayerPanel();
        playerPanel.doHighlightPlayerZone(zone, b);
    }

    public Rectangle getLibraryButtonLayout(PlayerViewerInfo aPlayer, Component canvas) {
        return playerCompositePanels[aPlayer.player.getIndex()].getPlayerPanel().getLibraryButtonLayout(canvas);
    }

    public Rectangle getHandButtonLayout(PlayerViewerInfo aPlayer, Component canvas) {
        return playerCompositePanels[aPlayer.player.getIndex()].getPlayerPanel().getHandButtonLayout(canvas);
    }

    public Rectangle getTurnPanelLayout(Component container) {
        return gameStatusPanel.getTurnPanelLayout(container);
    }

    private class LayoutSlot {

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
