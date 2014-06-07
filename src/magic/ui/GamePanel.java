package magic.ui;

import magic.data.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.ui.animation.GamePlayAnimator;
import magic.ui.animation.PlayCardFromHandAnimation;
import magic.ui.resolution.DefaultResolutionProfile;
import magic.ui.resolution.ResolutionProfileResult;
import magic.ui.resolution.ResolutionProfileType;
import magic.ui.resolution.ResolutionProfiles;
import magic.ui.viewer.BattlefieldViewer;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.GameDuelViewer;
import magic.ui.viewer.HandGraveyardExileViewer;
import magic.ui.viewer.ImageBattlefieldViewer;
import magic.ui.viewer.ImageCardListViewer;
import magic.ui.viewer.ImageCombatViewer;
import magic.ui.viewer.ImageHandGraveyardExileViewer;
import magic.ui.viewer.LogBookViewer;
import magic.ui.viewer.LogStackViewer;
import magic.ui.viewer.PlayerViewer;
import magic.ui.viewer.StackCombatViewer;
import magic.ui.viewer.StackViewer;
import magic.ui.viewer.ViewerInfo;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.ZoneBackgroundLabel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

@SuppressWarnings("serial")
public final class GamePanel extends JPanel {

    private static final String ACTION_KEY="action";
    private static final String UNDO_KEY="undo";
    private static final String SWITCH_KEY="switch";
    private static final String PASS_KEY="pass";

    private final MagicFrame frame;
    private final MagicGame game;
    private final ZoneBackgroundLabel backgroundLabel;
    private final GameController controller;
    private final ViewerInfo viewerInfo;
    private final PlayerViewer playerViewer;
    private final PlayerViewer opponentViewer;
    private final CardViewer cardViewer;
    private final GameDuelViewer gameDuelViewer;
    private final LogBookViewer logBookViewer;
    private final StackCombatViewer stackCombatViewer;
    private final HandGraveyardExileViewer handGraveyardViewer;
    private final BattlefieldViewer playerPermanentViewer;
    private final BattlefieldViewer opponentPermanentViewer;
    private final CardViewer imageCardViewer;
    private final StackViewer imageStackViewer;
    private final ImageHandGraveyardExileViewer imageHandGraveyardViewer;
    private final ImageBattlefieldViewer imagePlayerPermanentViewer;
    private final ImageBattlefieldViewer imageOpponentPermanentViewer;
    private final ImageCombatViewer imageCombatViewer;
    private final JPanel lhsPanel, rhsPanel;

    private ResolutionProfileResult result;
    private final LogStackViewer logStackViewer;

    private final GamePlayAnimator animator;
    private PlayCardFromHandAnimation animationEvent = null;

    public GamePanel(
            final MagicFrame frame,
            final MagicGame game,
            final ZoneBackgroundLabel backgroundLabel) {

        this.frame=frame;
        this.game=game;
        this.backgroundLabel=backgroundLabel;
        controller=new GameController(this,game);
        animator = new GamePlayAnimator(frame, this);

        //hide info when mouse moves onto background
        backgroundLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(final MouseEvent event) {
                controller.hideInfo();
            }
        });

        viewerInfo=new ViewerInfo();
        viewerInfo.update(game);

        setOpaque(false);
        setFocusable(true);

        // One-time creation of UI panels.
        lhsPanel = new JPanel();
        lhsPanel.setOpaque(false);
        rhsPanel = new JPanel(null);
        rhsPanel.setOpaque(false);

        logBookViewer=new LogBookViewer(game.getLogBook());
        logBookViewer.setVisible(!GeneralConfig.getInstance().isLogViewerDisabled());

        cardViewer=new CardViewer("Card",false);
        add(cardViewer, "w 100%, h 100%");
        cardViewer.setVisible(false);
        controller.setCardViewer(cardViewer);

        imageCardViewer=new CardViewer(true);
        imageCardViewer.setSize(CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
        imageCardViewer.setVisible(false);
        controller.setImageCardViewer(imageCardViewer);

        playerViewer=new PlayerViewer(viewerInfo,controller,false);
        opponentViewer=new PlayerViewer(viewerInfo,controller,true);

        gameDuelViewer=new GameDuelViewer(game,controller);
        gameDuelViewer.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        controller.setGameViewer(gameDuelViewer.getGameViewer());

        createActionMaps();
        createShortcutKeys();

        stackCombatViewer=new StackCombatViewer(viewerInfo,controller);
        handGraveyardViewer=new HandGraveyardExileViewer(viewerInfo,controller);
        playerPermanentViewer=new BattlefieldViewer(viewerInfo,controller,false);
        opponentPermanentViewer=new BattlefieldViewer(viewerInfo,controller,true);
        imageStackViewer=new StackViewer(viewerInfo,controller,true);
        logStackViewer = new LogStackViewer(logBookViewer, imageStackViewer);
        logStackViewer.setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        imageHandGraveyardViewer=new ImageHandGraveyardExileViewer(viewerInfo,controller);
        imagePlayerPermanentViewer=new ImageBattlefieldViewer(viewerInfo,controller,false);
        imageOpponentPermanentViewer=new ImageBattlefieldViewer(viewerInfo,controller,true);
        imageCombatViewer=new ImageCombatViewer(viewerInfo,controller);

        updateView();

        //start game logic controller in another thread
        (new Thread(){
            @Override
            public void run() {
                //reduce priority
                final Thread cur=Thread.currentThread();
                cur.setPriority(Thread.MIN_PRIORITY);
                System.err.println("Starting game...");
                controller.runGame();
                System.err.println("Stopping game...");
            }
        }).start();
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
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(final ActionEvent event) {
                controller.actionKeyPressed();
            }
        });

        getActionMap().put(UNDO_KEY, new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(final ActionEvent event) {
                controller.undoKeyPressed();
            }
        });

        getActionMap().put(SWITCH_KEY, new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(final ActionEvent e) {
                switchKeyPressed();
            }
        });

        getActionMap().put(PASS_KEY, new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(final ActionEvent e) {
                controller.passKeyPressed();
            }
        });

    }

    public boolean canClickAction() {
        return gameDuelViewer.getGameViewer().isActionEnabled();
    }

    public boolean canClickUndo() {
        return gameDuelViewer.getGameViewer().isUndoEnabled();
    }

    private void switchKeyPressed() {
        game.setVisiblePlayer(game.getVisiblePlayer().getOpponent());
        updateView();
    }

    private static boolean isTextView() {
        return GeneralConfig.getInstance().getTextView();
    }

    public MagicGame getGame() {
        return game;
    }

    public GameController getController() {
        return controller;
    }

    public CardViewer getImageCardViewer() {
        return imageCardViewer;
    }

    public LogBookViewer getLogBookViewer() {
        return logBookViewer;
    }

    public void focusViewers(final int handGraveyard) {
        if (isTextView()) {
            handGraveyardViewer.setSelectedTab(handGraveyard);
        } else {
            imageHandGraveyardViewer.setSelectedTab(handGraveyard);
        }
    }

    public void showCards(final MagicCardList cards) {
        if (isTextView()) {
            handGraveyardViewer.showCards(cards);
            handGraveyardViewer.setSelectedTab(5);
        } else {
            imageHandGraveyardViewer.showCards(cards);
            imageHandGraveyardViewer.setSelectedTab(5);
        }
    }

    public void updateInfo() {
        runAnimation();
        viewerInfo.update(game);
    }

    public void update() {

        playerViewer.update();
        opponentViewer.update();
        gameDuelViewer.update();
        imageStackViewer.update();

        if (isTextView()) {
            handGraveyardViewer.update();
            stackCombatViewer.update();
            playerPermanentViewer.update();
            opponentPermanentViewer.update();
        } else {
            imageHandGraveyardViewer.update();
            imagePlayerPermanentViewer.update();
            imageOpponentPermanentViewer.update();
            imageCombatViewer.update();
        }
    }

    public void updateView() {
        if (isTextView()) {
            backgroundLabel.setImage(false);
            rhsPanel.remove(imageHandGraveyardViewer);
            rhsPanel.remove(imagePlayerPermanentViewer);
            rhsPanel.remove(imageOpponentPermanentViewer);
            rhsPanel.remove(imageCombatViewer);
            rhsPanel.add(cardViewer);
            rhsPanel.add(handGraveyardViewer);
            rhsPanel.add(stackCombatViewer);
            rhsPanel.add(playerPermanentViewer);
            rhsPanel.add(opponentPermanentViewer);
            imageCardViewer.setVisible(false);
        } else if (imageHandGraveyardViewer!=null) {
            backgroundLabel.setImage(true);
            rhsPanel.remove(cardViewer);
            rhsPanel.remove(handGraveyardViewer);
            rhsPanel.remove(stackCombatViewer);
            rhsPanel.remove(playerPermanentViewer);
            rhsPanel.remove(opponentPermanentViewer);
            rhsPanel.add(imageStackViewer);
            rhsPanel.add(imageHandGraveyardViewer);
            rhsPanel.add(imagePlayerPermanentViewer);
            rhsPanel.add(imageOpponentPermanentViewer);
            rhsPanel.add(imageCombatViewer);
        }
        resizeComponents();
        revalidate();
        repaint();
    }

    public void close() {
        frame.closeDuelScreen();
    }

    public void resizeComponents() {

        final Dimension size=getSize();
        result = ResolutionProfiles.calculate(size);

        backgroundLabel.setZones(result);

        playerViewer.setBounds(result.getBoundary(ResolutionProfileType.GamePlayerViewer));
        playerViewer.setSmall(result.getFlag(ResolutionProfileType.GamePlayerViewerSmall));
        opponentViewer.setSmall(result.getFlag(ResolutionProfileType.GamePlayerViewerSmall));
        gameDuelViewer.setBounds(result.getBoundary(ResolutionProfileType.GameDuelViewer));

        if (isTextView()) {
            stackCombatViewer.setBounds(result.getBoundary(ResolutionProfileType.GameStackCombatViewer));
            handGraveyardViewer.setBounds(result.getBoundary(ResolutionProfileType.GameHandGraveyardViewer));
            playerPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GamePlayerPermanentViewer));
            opponentPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameOpponentPermanentViewer));
        } else {
            imageHandGraveyardViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageHandGraveyardViewer));
            imagePlayerPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImagePlayerPermanentViewer));
            imageOpponentPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageOpponentPermanentViewer));
            imageCombatViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageCombatViewer));
        }

        setGamePanelLayout();

        controller.update();
    }

    /**
     * The game screen layout is split into two columns using MigLayout.
     * The LHS is a fixed-width column that uses MigLayout to position UI components.
     * The RHS column represents the battlefield and uses absolute positioning.
     */
    private void setGamePanelLayout() {

        removeAll();
        setLayout(new MigLayout("insets 0, gap 0, flowx"));

        add(lhsPanel, "w " + DefaultResolutionProfile.getPanelWidthLHS() +"!, h 100%");
        add(rhsPanel, "w 100%, h 100%");

        setLeftSideLayout();

    }

    /**
     *  The LHS of the game screen contains the two player info views, the Log/Stack
     *  viewer and the game console. All are at a fixed height except the Log/Stack view
     *  which should expand and contract to fill any remaining space depending on screen height.
     */
    private void setLeftSideLayout() {

        final int insets = 6;
        final int maxWidth = DefaultResolutionProfile.getPanelWidthLHS() - (insets * 2);

        lhsPanel.removeAll();
        lhsPanel.setLayout(new MigLayout("insets " + insets + ", gap 0 10, flowy"));

        lhsPanel.add(opponentViewer, "w " + maxWidth + "!, h " + DefaultResolutionProfile.PLAYER_VIEWER_HEIGHT_SMALL + "!");
        lhsPanel.add(logStackViewer, "w " + maxWidth + "!, h 100%");
        lhsPanel.add(gameDuelViewer, "w " + maxWidth + "!, h " + DefaultResolutionProfile.GAME_VIEWER_HEIGHT + "!");
        lhsPanel.add(playerViewer,   "w " + maxWidth + "!, h " + DefaultResolutionProfile.PLAYER_VIEWER_HEIGHT_SMALL + "!");

        logStackViewer.setLogStackLayout();

    }

    /**
     * Run animation(s) and wait until complete.
     * <p>
     * This method should be run from a non-EDT thread otherwise UI would freeze.
     */
    public void runAnimation() {
        if (animationEvent != null && GeneralConfig.getInstance().isAnimateGameplay()) {
            animator.runAnimation(animationEvent);
        }
        animationEvent = null;
    }

    public void setAnimationEvent(final MagicEvent event) {

        final MagicCardDefinition card = event.getSource().getCardDefinition();
        final ImageCardListViewer handViewer = imageHandGraveyardViewer.getCardListViewer();
        final Point startPoint = handViewer.getCardPosition(card);

        animationEvent = new PlayCardFromHandAnimation(
                event.getPlayer(),
                event.getSource().getCardDefinition(),
                this);

        animationEvent.setStartSize(handViewer.getCardSize());
        animationEvent.setStartPoint(startPoint);

//        System.err.println(card.getName() + (card.usesStack() ? " to stack" : " to battlefield"));
        if (card.usesStack()) {
            animationEvent.setEndPoint(new Point(150, imageStackViewer.getLocation().y));
        }

    }

}
