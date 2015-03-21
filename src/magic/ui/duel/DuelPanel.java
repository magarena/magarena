package magic.ui.duel;

import magic.ui.duel.dialog.DuelDialogPanel;
import magic.ui.duel.animation.PlayCardAnimation;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.exception.InvalidDeckException;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayerZone;
import magic.model.event.MagicEvent;
import magic.ui.SwingGameController;
import magic.ui.MagicFrame;
import magic.ui.ScreenController;
import magic.ui.card.AnnotatedCardPanel;
import magic.ui.duel.animation.AnimationCanvas;
import magic.ui.duel.animation.GamePlayAnimator;
import magic.ui.duel.resolution.DefaultResolutionProfile;
import magic.ui.duel.resolution.ResolutionProfileResult;
import magic.ui.duel.resolution.ResolutionProfiles;
import magic.ui.duel.viewer.LogBookViewer;
import magic.ui.duel.viewer.PlayerViewerInfo;
import magic.ui.widget.ZoneBackgroundLabel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public final class DuelPanel extends JPanel {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();
    private static final String ACTION_KEY="action";
    private static final String UNDO_KEY="undo";
    private static final String SWITCH_KEY="switch";
    private static final String PASS_KEY="pass";

    private final MagicFrame frame;
    private final ZoneBackgroundLabel backgroundLabel;
    private final SwingGameController controller;
  
    private final AnnotatedCardPanel imageCardViewer;

    private final DuelSideBarPanel sidebarPanel;
    private BattlefieldPanel battlefieldPanel;
    private final BattlefieldPanel textView;
    private final BattlefieldPanel imageView;

    private ResolutionProfileResult result;

    private final GamePlayAnimator animator;
    private final AnimationCanvas animationCanvas;
    private final DuelDialogPanel dialogPanel;

    public DuelPanel(
            final MagicFrame frame,
            final MagicGame game,
            final ZoneBackgroundLabel backgroundLabel) {

        this.frame = frame;
        this.backgroundLabel = backgroundLabel;
        
        controller = new SwingGameController(this, game);
        animator = new GamePlayAnimator(frame, this);
        animationCanvas = new AnimationCanvas();
        dialogPanel = new DuelDialogPanel();

        setOpaque(false);
        setFocusable(true);

        imageCardViewer = new AnnotatedCardPanel(getWindowRect(), controller);
        imageCardViewer.setVisible(false);
        controller.setImageCardViewer(imageCardViewer);

        textView = new TextModeBattlefieldPanel(controller);
        imageView = new ImageModeBattlefieldPanel(controller);
        battlefieldPanel = isTextView() ? textView : imageView;

        sidebarPanel = new DuelSideBarPanel(controller, battlefieldPanel.getStackViewer());

        // TODO: should not have to run this, but required while sidebarPanel is created after battlefieldPanel.
        controller.notifyPlayerZoneChanged(controller.getViewerInfo().getPlayerInfo(false), MagicPlayerZone.HAND);

        controller.setUserActionPanel(sidebarPanel.getGameStatusPanel().getUserActionPanel());

        updateView();
        
        createActionMaps();
        createShortcutKeys();
        createMouseListener();

    }

    private static Rectangle getWindowRect() {
        return new Rectangle(
                    ScreenController.getMainFrame().getLocationOnScreen(),
                    ScreenController.getMainFrame().getSize());
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

    public void startGameThread() {
        assert SwingUtilities.isEventDispatchThread();
        // defer until all pending events on the EDT have been processed.
        // which means (I think) that all UI components will have been layed out.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //start game logic controller in another thread
                new Thread() {
                    @Override
                    public void run() {
                        //reduce priority
                        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
                        controller.runGame();
                    }
                }.start();
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
        getActionMap().put(ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (dialogPanel.isVisible()) {
                    dialogPanel.setVisible(false);
                } else {
                    controller.actionKeyPressed();
                }
            }
        });
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

    private static boolean isTextView() {
        return CONFIG.getTextView();
    }

    public SwingGameController getController() {
        return controller;
    }

    public AnnotatedCardPanel getImageCardViewer() {
        return imageCardViewer;
    }

    public LogBookViewer getLogBookViewer() {
        return sidebarPanel.getLogBookViewer();
    }

    public void focusViewers(final int handGraveyard) {
        battlefieldPanel.focusViewers(handGraveyard);
    }

    public void showCards(final MagicCardList cards) {
        battlefieldPanel.showCards(cards);
    }

    public void update() {
        assert SwingUtilities.isEventDispatchThread();
        sidebarPanel.doUpdate();
        battlefieldPanel.doUpdate();
    }

    public void updateView() {
        backgroundLabel.setImage(!isTextView());
        battlefieldPanel = isTextView() ? textView : imageView;
        resizeComponents();
        revalidate();
        repaint();
    }

    public void close() throws InvalidDeckException {
        frame.closeDuelScreen();
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
                update();
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

    /**
     * Run animation(s) and wait until complete.
     * <p>
     * This method should be run from a non-EDT thread otherwise UI would freeze.
     */
    public void runAnimation() {
        assert !SwingUtilities.isEventDispatchThread();
        final PlayCardAnimation animationEvent = battlefieldPanel.getPlayCardFromHandAnimation();
        if (animationEvent != null && CONFIG.isAnimateGameplay()) {
            animator.runAnimation(animationEvent);
        }
        battlefieldPanel.setPlayCardFromHandAnimation(null);
    }

    /**
     * Produces an animation of a card being played from a player's hand
     * to the battlefield or stack the next time GamePanel is refreshed.
     */
    public void setAnimationEvent(final MagicEvent event) {
        battlefieldPanel.setAnimationEvent(event, this);
    }

    private void doThreadSleep(final long msecs) {
        try {
            Thread.sleep(msecs);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    // TODO: move up into GameController?
    public void doNewTurnNotification(final MagicGame game) {
        assert !SwingUtilities.isEventDispatchThread();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                sidebarPanel.getGameStatusPanel().showNewTurnNotification(game);
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

    public AnimationCanvas getAnimationCanvas() {
        return animationCanvas;
    }

    public void showEndGameMessage() {
        dialogPanel.showEndGameMessage(controller);
    }

    public JPanel getDialogPanel() {
        return dialogPanel;
    }

    public void setFullScreenActivePlayerZone(PlayerViewerInfo playerInfo, MagicPlayerZone zone) {
        battlefieldPanel.setFullScreenActivePlayerZone(playerInfo, zone);
    }

    public void switchPlayers() {
        sidebarPanel.switchPlayers();
    }
}
