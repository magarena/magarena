package magic.ui.screen.duel.game;

import magic.ui.duel.viewer.info.PlayerViewerInfo;
import magic.ui.duel.viewer.info.GameViewerInfo;
import magic.ui.duel.sidebar.DuelSideBarPanel;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayerZone;
import magic.ui.ScreenController;
import magic.ui.duel.animation.GameLayoutInfo;
import magic.ui.duel.resolution.DefaultResolutionProfile;
import magic.ui.duel.resolution.ResolutionProfileResult;
import magic.ui.duel.resolution.ResolutionProfiles;
import magic.ui.duel.viewer.ImageBattlefieldViewer;
import magic.ui.duel.viewer.ImageCardListViewer;
import magic.ui.duel.viewer.info.CardViewerInfo;
import magic.ui.widget.ZoneBackgroundLabel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public final class DuelPanel extends JPanel {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final String ACTION_KEY="action";
    private static final String UNDO_KEY="undo";
    private static final String SWITCH_KEY="switch";
    private static final String PASS_KEY="pass";

    private ZoneBackgroundLabel backgroundLabel;
    private SwingGameController controller;

    private DuelSideBarPanel sidebarPanel;
    private BattlefieldPanel battlefieldPanel;
    private ResolutionProfileResult result;

    public DuelPanel(final MagicGame game) {

        setOpaque(false);
        setFocusable(true);
    }

    public void setController(final SwingGameController aController) {

        this.controller = aController;

        battlefieldPanel = new ImageModeBattlefieldPanel(controller);

        sidebarPanel = new DuelSideBarPanel(controller, battlefieldPanel.getStackViewer());

        updateView();

        createActionMaps();
        createShortcutKeys();
        createMouseListener();

        // TODO: should not have to run this, but required while sidebarPanel is created after battlefieldPanel.
        controller.notifyPlayerZoneChanged(controller.getViewerInfo().getPlayerInfo(false), MagicPlayerZone.HAND);
        controller.setUserActionPanel(sidebarPanel.getGameStatusPanel().getUserActionPanel());

    }

    private void createMouseListener() {
        //hide info when mouse moves onto background
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                controller.hideInfo();
            }
        });
    }

    private void createShortcutKeys() {
        //defining shortcut keys
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),ACTION_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),ACTION_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK),PASS_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_MASK),PASS_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),UNDO_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),UNDO_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),UNDO_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),SWITCH_KEY);
    }

    private void createActionMaps() {
        getActionMap().put(ACTION_KEY, controller.getActionKeyPressedAction());
        getActionMap().put(UNDO_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                controller.undoKeyPressed();
            }
        });
        getActionMap().put(SWITCH_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.switchKeyPressed();
            }
        });
        getActionMap().put(PASS_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.passKeyPressed();
            }
        });
    }

    public boolean canClickAction() {
        return sidebarPanel.getGameStatusPanel().getUserActionPanel().isActionEnabled();
    }

    public boolean canClickUndo() {
        return sidebarPanel.getGameStatusPanel().getUserActionPanel().isUndoEnabled();
    }

    public SwingGameController getController() {
        return controller;
    }

    public void focusViewers(final int handGraveyard) {
        battlefieldPanel.focusViewers(handGraveyard);
    }

    public void showCards(final MagicCardList cards) {
        battlefieldPanel.showCards(cards);
    }

    public void update(final GameViewerInfo gameInfo) {
        assert SwingUtilities.isEventDispatchThread();
        sidebarPanel.doUpdate(gameInfo);
        battlefieldPanel.doUpdate();
    }

    void updateView() {
        resizeComponents();
        revalidate();
        repaint();
    }

    public void close() {
        ScreenController.getMainFrame().closeDuelScreen();
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        resizeComponents();
    }

    public void resizeComponents() {
        final Dimension size = getSize();
        result = ResolutionProfiles.calculate(size);
        backgroundLabel.setZones(result);
        battlefieldPanel.resizeComponents(result);
        setGamePanelLayout();
        // defer until all pending events on the EDT have been processed.
        // this ensures that cards layout is adjusted correctly if the screen is resized.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                update(controller.getViewerInfo());
            }
        });
    }

    /**
     * The game screen layout is split into two columns using MigLayout.
     * The LHS is a fixed-width column that uses MigLayout to position UI components.
     * The RHS column represents the battlefield and uses absolute positioning.
     */
    private void setGamePanelLayout() {
        removeAll();
        setLayout(new MigLayout("insets 0, gap 0, flowx"));
        add(sidebarPanel, "w " + DefaultResolutionProfile.getPanelWidthLHS() +"!, h 100%");
        add(battlefieldPanel, "w 100%, h 100%");
        sidebarPanel.doSetLayout();
    }

    private void doThreadSleep(final long msecs) {
        try {
            Thread.sleep(msecs);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    // TODO: move up into GameController?
    public void doNewTurnNotification(GameViewerInfo gameInfo) {
        assert !SwingUtilities.isEventDispatchThread();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sidebarPanel.getGameStatusPanel().showNewTurnNotification(gameInfo);
            }
        });

        // TODO: do while gameStatusPanel.isBusy() { sleep(100); }
        doThreadSleep(CONFIG.getNewTurnAlertDuration());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sidebarPanel.getGameStatusPanel().hideNewTurnNotification();
            }
        });

    }

    public void refreshSidebarLayout() {
        sidebarPanel.refreshLayout();
    }

    public void doFlashPlayerHandZoneButton(PlayerViewerInfo aPlayer) {
        sidebarPanel.doFlashPlayerHandZoneButton(aPlayer);
    }

    void doFlashLibraryZoneButton(PlayerViewerInfo aPlayer) {
        sidebarPanel.doFlashLibraryZoneButton(aPlayer);
    }

    public void highlightCard(CardViewerInfo cardInfo, boolean b) {
        switch (cardInfo.getLocation()) {
            case Battlefield:
                battlefieldPanel.highlightCard(cardInfo, b);
                break;
            case Graveyard:
                sidebarPanel.doHighlightPlayerZone(cardInfo, MagicPlayerZone.GRAVEYARD, b);
                break;
            case Exile:
                sidebarPanel.doHighlightPlayerZone(cardInfo, MagicPlayerZone.EXILE, b);
                break;
            case OwnersHand:
                sidebarPanel.doHighlightPlayerZone(cardInfo, MagicPlayerZone.HAND, b);
                break;
            case OwnersLibrary:
                sidebarPanel.doHighlightPlayerZone(cardInfo, MagicPlayerZone.LIBRARY, b);
                break;
        }
    }

    public Rectangle getBattlefieldPanelBounds() {
        return battlefieldPanel.getBounds();
    }

    private Point getLocationOnDuelPanel(final JComponent component) {
        final DuelPanel duelPanel = (DuelPanel)component.getParent().getParent();
        return SwingUtilities.convertPoint(component.getParent(), component.getLocation(), duelPanel);
    }

    public GameLayoutInfo getLayoutInfo(final GameViewerInfo gameInfo, final CardViewerInfo cardInfo) {

        assert SwingUtilities.isEventDispatchThread();

        final GameLayoutInfo info = new GameLayoutInfo(this.getSize());
        final ImageModeBattlefieldPanel battlefield = (ImageModeBattlefieldPanel) battlefieldPanel;

        info.setTurnPanelLayout(sidebarPanel.getTurnPanelLayout(this));

        for (PlayerViewerInfo playerInfo : gameInfo.getPlayers()) {

            final int playerIndex = playerInfo.player.getIndex();

            info.setLibraryButtonLayout(playerIndex, sidebarPanel.getLibraryButtonLayout(playerInfo, this));
            info.setHandButtonLayout(playerIndex, sidebarPanel.getHandButtonLayout(playerInfo, this));

            final ImageBattlefieldViewer battlefieldViewer = playerIndex == 0
                ? battlefield.imagePlayerPermanentViewer
                : battlefield.imageOpponentPermanentViewer;

            info.setPermanentsZoneLayout(
                playerIndex,
                new Rectangle(
                    getLocationOnDuelPanel(battlefieldViewer),
                    battlefieldViewer.getSize()
                )
            );

        }

        info.setStackLayout(sidebarPanel.getStackViewerRectangle(this));

        setCardLayoutInfo(cardInfo, info);
        return info;

    }

    private void setCardLayoutInfo(CardViewerInfo cardInfo, GameLayoutInfo info) {
        if (cardInfo.isNotEmpty()) {
            final ImageCardListViewer handViewer = controller.getPlayerZoneViewer().getImageCardsListViewer();
            if (handViewer.getCardPosition(cardInfo) != null) {
                info.setCardInHandLayout(
                    new Rectangle(
                        handViewer.getCardPosition(cardInfo),
                        handViewer.getCardSize())
                );
            }
        }
    }

    void setBackgroundLabel(ZoneBackgroundLabel backgroundLabel) {
        this.backgroundLabel = backgroundLabel;
    }

}
