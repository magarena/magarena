package magic.ui;

import magic.data.CardImagesProvider;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.model.MagicGame;
import magic.model.MagicGameReport;
import magic.ui.resolution.ResolutionProfileResult;
import magic.ui.resolution.ResolutionProfileType;
import magic.ui.resolution.ResolutionProfiles;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.viewer.BattlefieldViewer;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.GameTournamentViewer;
import magic.ui.viewer.HandGraveyardExileViewer;
import magic.ui.viewer.ImageBattlefieldViewer;
import magic.ui.viewer.ImageCombatViewer;
import magic.ui.viewer.ImageHandGraveyardExileViewer;
import magic.ui.viewer.ImageViewer;
import magic.ui.viewer.LogBookViewer;
import magic.ui.viewer.PlayerViewer;
import magic.ui.viewer.StackCombatViewer;
import magic.ui.viewer.StackViewer;
import magic.ui.viewer.ViewerInfo;
import magic.ui.widget.TitleBar;
import magic.ui.widget.ZoneBackgroundLabel;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public final class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String ACTION_KEY="action";
	private static final String UNDO_KEY="undo";
	private static final String SWITCH_KEY="switch";
	private static final String LOG_KEY="log";
	private static final String PASS_KEY="pass";
	
	private final MagicFrame frame;
	private final MagicGame game;
	private final ZoneBackgroundLabel backgroundLabel;
	private final GameController controller;
	private final ViewerInfo viewerInfo;
	private final PlayerViewer playerViewer;
	private final PlayerViewer opponentViewer;
	private final CardViewer cardViewer;
	private final GameTournamentViewer gameTournamentViewer;
	private final LogBookViewer logBookViewer;
	private final JToggleButton logBookButton;
	private final JToggleButton textViewButton;
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
	private final ImageViewer imageViewer;
	
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

		setLayout(null);
		setOpaque(false);
		setFocusable(true);
		
		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();

		logBookViewer=new LogBookViewer(game.getLogBook());
		logBookViewer.setVisible(false);
		
		cardViewer=new CardViewer("Card",false,true);
		add(cardViewer);
		controller.setCardViewer(cardViewer);
		
		imageCardViewer=new CardViewer("",true,false);
		imageCardViewer.setSize(CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
		imageCardViewer.setVisible(false);
		controller.setImageCardViewer(imageCardViewer);
						
		playerViewer=new PlayerViewer(viewerInfo,controller,false);
		add(playerViewer);		
		
		opponentViewer=new PlayerViewer(viewerInfo,controller,true);
		add(opponentViewer);
		
		gameTournamentViewer=new GameTournamentViewer(game,controller);
		controller.setGameViewer(gameTournamentViewer.getGameViewer());
		add(gameTournamentViewer);
						
		logBookButton=new JToggleButton(theme.getIcon(Theme.ICON_MESSAGE),false);
		logBookButton.setFocusable(false);
		logBookButton.setOpaque(false);
		add(logBookButton);		
		logBookButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				logBookViewer.setVisible(logBookButton.isSelected());
			}
		});
		logBookButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(final MouseEvent event) {
				showLogBook(true);
			}
			@Override
			public void mouseExited(final MouseEvent event) {
				if (!logBookButton.isSelected()) {
					showLogBook(false);
				}
			}
		});

		textViewButton=new JToggleButton(IconImages.TEXT,isTextView());
		textViewButton.setToolTipText("Images / Text");
		textViewButton.setFocusable(false);
		textViewButton.setOpaque(false);
		add(textViewButton);
		textViewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				final boolean selected=textViewButton.isSelected();
				GeneralConfig.getInstance().setTextView(selected);
				updateView();
			}
		});
		
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
		
		getActionMap().put(LOG_KEY, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(final ActionEvent e) {
				final boolean selected=!logBookButton.isSelected();
				logBookButton.setSelected(selected);
				showLogBook(selected);
			}
		});
		
        getActionMap().put(PASS_KEY, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(final ActionEvent e) {
				controller.passKeyPressed();
			}
		});
	
        //defining shortcut keys
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),ACTION_KEY);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),ACTION_KEY);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),UNDO_KEY);		
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),UNDO_KEY);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),SWITCH_KEY);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),LOG_KEY);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0),LOG_KEY);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_MASK),PASS_KEY);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_MASK),PASS_KEY);
				
		stackCombatViewer=new StackCombatViewer(viewerInfo,controller);
		handGraveyardViewer=new HandGraveyardExileViewer(viewerInfo,controller);		
		playerPermanentViewer=new BattlefieldViewer(viewerInfo,controller,false);
		opponentPermanentViewer=new BattlefieldViewer(viewerInfo,controller,true);
		imageStackViewer=new StackViewer(viewerInfo,controller,true);
		imageHandGraveyardViewer=new ImageHandGraveyardExileViewer(viewerInfo,controller);
		imagePlayerPermanentViewer=new ImageBattlefieldViewer(viewerInfo,controller,false);
		imageOpponentPermanentViewer=new ImageBattlefieldViewer(viewerInfo,controller,true);
		imageCombatViewer=new ImageCombatViewer(viewerInfo,controller);
		imageViewer=new ImageViewer();

		final TitleBar stackTitleBar = new TitleBar("Stack");
		stackTitleBar.setIcon(theme.getIcon(Theme.ICON_SMALL_STACK));
		imageStackViewer.add(stackTitleBar,BorderLayout.SOUTH);
		
		updateView();

        //start runGame in background using SwingWorker
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
	
	public boolean canClickAction() {
		return gameTournamentViewer.getGameViewer().isActionEnabled();
	}
	
	public boolean canClickUndo() {
		return gameTournamentViewer.getGameViewer().isUndoEnabled();
	}
		
	void switchKeyPressed() {
		if (textViewButton.isEnabled()) {
			final boolean selected=!textViewButton.isSelected();
			textViewButton.setSelected(selected);
			GeneralConfig.getInstance().setTextView(selected);
			updateView();
		}
	}
	
	void showLogBook(final boolean visible) {
		if (visible) {
			logBookViewer.update();
            logBookViewer.setVisible(true);
		} else {
			logBookViewer.setVisible(false);	
		}		
	}
	
	private boolean isTextView() {
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
	
    public void updateInfo() {
        viewerInfo.update(game);
    }

	public void update() {
		playerViewer.update();
		opponentViewer.update();
		gameTournamentViewer.update();
		
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
			remove(imageStackViewer);
			remove(imageHandGraveyardViewer);
			remove(imagePlayerPermanentViewer);
			remove(imageOpponentPermanentViewer);
			remove(imageCombatViewer);
			remove(imageViewer);
			add(cardViewer);
			add(handGraveyardViewer);
			add(stackCombatViewer);
			add(playerPermanentViewer);
			add(opponentPermanentViewer);		
			imageCardViewer.setVisible(false);
		} else if (imageHandGraveyardViewer!=null) {
			backgroundLabel.setImage(true);
			remove(cardViewer);
			remove(handGraveyardViewer);
			remove(stackCombatViewer);
			remove(playerPermanentViewer);
			remove(opponentPermanentViewer);
			add(imageStackViewer);
			add(imageHandGraveyardViewer);
			add(imagePlayerPermanentViewer);
			add(imageOpponentPermanentViewer);
			add(imageCombatViewer);
			add(imageViewer);
		}
		resizeComponents();
		update();
		revalidate();
		repaint();
	}
	
	public void close() {
		frame.showTournament();
	}
	
	public void resizeComponents() {
		final Dimension size=getSize();
		final ResolutionProfileResult result=ResolutionProfiles.calculate(size);

		backgroundLabel.setZones(result);
		
		playerViewer.setBounds(result.getBoundary(ResolutionProfileType.GamePlayerViewer));
		playerViewer.setSmall(result.getFlag(ResolutionProfileType.GamePlayerViewerSmall));
		opponentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameOpponentViewer));
		opponentViewer.setSmall(result.getFlag(ResolutionProfileType.GamePlayerViewerSmall));
		gameTournamentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameTournamentViewer));
		logBookButton.setBounds(result.getBoundary(ResolutionProfileType.GameLogBookButton));
		textViewButton.setBounds(result.getBoundary(ResolutionProfileType.TextViewButton));
		logBookViewer.setBounds(result.getBoundary(ResolutionProfileType.GameLogBookViewer));

		if (isTextView()) {
			cardViewer.setBounds(result.getBoundary(ResolutionProfileType.GameCardViewer));
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
			imageViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageViewer));
		}
		
		controller.update();
	}	
}
