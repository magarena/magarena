package magic.ui;

import magic.data.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicCardList;
import magic.ui.resolution.ResolutionProfileResult;
import magic.ui.resolution.ResolutionProfileType;
import magic.ui.resolution.ResolutionProfiles;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.viewer.BattlefieldViewer;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.GameDuelViewer;
import magic.ui.viewer.HandGraveyardExileViewer;
import magic.ui.viewer.ImageBattlefieldViewer;
import magic.ui.viewer.ImageCombatViewer;
import magic.ui.viewer.ImageHandGraveyardExileViewer;
import magic.ui.viewer.LogBookViewer;
import magic.ui.viewer.PlayerViewer;
import magic.ui.viewer.StackCombatViewer;
import magic.ui.viewer.StackViewer;
import magic.ui.viewer.ViewerInfo;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TitleBar;
import magic.ui.widget.ZoneBackgroundLabel;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public final class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final String ACTION_KEY="action";
    private static final String UNDO_KEY="undo";
    private static final String SWITCH_KEY="switch";
    private static final String LOG_KEY="log";
    private static final String PASS_KEY="pass";
    private static final Theme theme = ThemeFactory.getInstance().getCurrentTheme();

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

    public GamePanel(
            final MagicFrame frame,
            final MagicGame game,
            final ZoneBackgroundLabel backgroundLabel) {

        this.frame=frame;
        this.game=game;
        this.backgroundLabel=backgroundLabel;
        controller=new GameController(this,game);

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
        logBookViewer.setVisible(true);

        cardViewer=new CardViewer("Card",false,true);
        add(cardViewer, "w 100%, h 100%");
        cardViewer.setVisible(false);
        controller.setCardViewer(cardViewer);

        imageCardViewer=new CardViewer(true,false);
        imageCardViewer.setSize(CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
        imageCardViewer.setVisible(false);
        controller.setImageCardViewer(imageCardViewer);

        playerViewer=new PlayerViewer(viewerInfo,controller,false);
        opponentViewer=new PlayerViewer(viewerInfo,controller,true);
        gameDuelViewer=new GameDuelViewer(game,controller);
        controller.setGameViewer(gameDuelViewer.getGameViewer());

        createActionMaps();
        createShortcutKeys();

        stackCombatViewer=new StackCombatViewer(viewerInfo,controller);
        handGraveyardViewer=new HandGraveyardExileViewer(viewerInfo,controller);
        playerPermanentViewer=new BattlefieldViewer(viewerInfo,controller,false);
        opponentPermanentViewer=new BattlefieldViewer(viewerInfo,controller,true);
        imageStackViewer=new StackViewer(viewerInfo,controller,true);
        imageHandGraveyardViewer=new ImageHandGraveyardExileViewer(viewerInfo,controller);
        imagePlayerPermanentViewer=new ImageBattlefieldViewer(viewerInfo,controller,false);
        imageOpponentPermanentViewer=new ImageBattlefieldViewer(viewerInfo,controller,true);
        imageCombatViewer=new ImageCombatViewer(viewerInfo,controller);

        final TitleBar stackTitleBar = new TitleBar("Stack");
        stackTitleBar.setIcon(theme.getIcon(Theme.ICON_SMALL_STACK));
        imageStackViewer.add(stackTitleBar,BorderLayout.SOUTH);

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
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),UNDO_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),LOG_KEY);
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0),LOG_KEY);
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
        /*
        if (textViewButton.isEnabled()) {
            final boolean selected=!textViewButton.isSelected();
            textViewButton.setSelected(selected);
            frame.setTextImageMode(selected);
        }
        */
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

    public void focusViewers(final int handGraveyard,final int stackCombat) {
        if (isTextView()) {
            handGraveyardViewer.setSelectedTab(handGraveyard);
            stackCombatViewer.setSelectedTab(stackCombat);
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
        viewerInfo.update(game);
    }

    public void update() {
        playerViewer.update();
        opponentViewer.update();
        gameDuelViewer.update();

        if (isTextView()) {
            handGraveyardViewer.update();
            stackCombatViewer.update();
            playerPermanentViewer.update();
            opponentPermanentViewer.update();
        } else {
            imageStackViewer.update();
            imageHandGraveyardViewer.update();
            imagePlayerPermanentViewer.update();
            imageOpponentPermanentViewer.update();
            imageCombatViewer.update();
        }
    }

    public void updateView() {
        if (isTextView()) {
            backgroundLabel.setImage(false);
            rhsPanel.remove(imageStackViewer);
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
        frame.showDuel();
    }

    public void resizeComponents() {
        final Dimension size=getSize();
        final ResolutionProfileResult result=ResolutionProfiles.calculate(size);

        backgroundLabel.setZones(result);

        playerViewer.setBounds(result.getBoundary(ResolutionProfileType.GamePlayerViewer));
        playerViewer.setSmall(result.getFlag(ResolutionProfileType.GamePlayerViewerSmall));
        opponentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameOpponentViewer));
        opponentViewer.setSmall(result.getFlag(ResolutionProfileType.GamePlayerViewerSmall));
        gameDuelViewer.setBounds(result.getBoundary(ResolutionProfileType.GameDuelViewer));
        //logBookViewer.setBounds(result.getBoundary(ResolutionProfileType.GameLogBookViewer));

        if (isTextView()) {
            stackCombatViewer.setBounds(result.getBoundary(ResolutionProfileType.GameStackCombatViewer));
            handGraveyardViewer.setBounds(result.getBoundary(ResolutionProfileType.GameHandGraveyardViewer));
            playerPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GamePlayerPermanentViewer));
            opponentPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameOpponentPermanentViewer));
        } else {
            imageStackViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageStackViewer));
            imageHandGraveyardViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageHandGraveyardViewer));
            imagePlayerPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImagePlayerPermanentViewer));
            imageOpponentPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageOpponentPermanentViewer));
            imageCombatViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageCombatViewer));
        }

        setThisLayout(result);

        controller.update();
    }

    private void setThisLayout(final ResolutionProfileResult result) {

       	final int spacing = theme.getValue(Theme.VALUE_SPACING);
    	StringBuilder sb = new StringBuilder();

    	Rectangle r = result.getBoundary(ResolutionProfileType.GameLHS);

    	removeAll();
    	setLayout(new MigLayout(
    			"insets 0, gap 0, flowx, wrap 2",
    			"[" + r.width +"px!][]"));

    	JPanel splitterPanel = new JPanel(new MigLayout("insets 0, gap 0"));
    	splitterPanel.add(imageStackViewer, "w 100%, pushy, bottom");

    	JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    	splitter.setBorder(FontsAndBorders.BLACK_BORDER_2);
    	splitter.setTopComponent(logBookViewer);
    	splitter.setBottomComponent(splitterPanel);
    	splitter.setOneTouchExpandable(false);
    	splitter.setContinuousLayout(true);
    	splitter.setResizeWeight(0.5);

        // LHS
        lhsPanel.removeAll();
        lhsPanel.setLayout(
        		new MigLayout(
        				sb.append("insets ").append(spacing).append(",")	// margins
        				.append("gap 0 ").append(spacing).append(",")		// gapx [gapy]
        				.append("flowy,")
        				.append("").toString()));

        r = result.getBoundary(ResolutionProfileType.GameOpponentViewer);
        lhsPanel.add(opponentViewer, "w 100%, h " + r.height + "px!");

        lhsPanel.add(splitter, "w 100%, h 100%");
        //lhsPanel.add(imageStackViewer, "w 100%, pushy, bottom");

        r = result.getBoundary(ResolutionProfileType.GameDuelViewer);
        lhsPanel.add(gameDuelViewer, "w 100%, h " + r.height + "px!");

        r = result.getBoundary(ResolutionProfileType.GamePlayerViewer);
        lhsPanel.add(playerViewer, "w 100%, h " + r.height + "px!");

        add(lhsPanel, "w 100%, h 100%");

        // RHS
        add(rhsPanel, "w 100%, h 100%");

    }

}
