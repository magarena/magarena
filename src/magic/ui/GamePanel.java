package magic.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import magic.data.CardImages;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.model.MagicGame;
import magic.ui.resolution.ResolutionProfileResult;
import magic.ui.resolution.ResolutionProfileType;
import magic.ui.resolution.ResolutionProfiles;
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

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final MagicFrame frame;
	private final MagicGame game;
	private final GameController controller;
	private final ViewerInfo viewerInfo;
	private final PlayerViewer playerViewer;
	private final PlayerViewer opponentViewer;
	private final CardViewer cardViewer;
	private final GameTournamentViewer gameTournamentViewer;
	private final LogBookViewer logBookViewer;
	private final JLabel logBookButton;
	private final JToggleButton textViewButton;
	private final GameControllerThread thread;
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
	
	public GamePanel(final MagicFrame frame,final MagicGame game) {

		this.frame=frame;
		this.game=game;
		controller=new GameController(this,game);
		viewerInfo=new ViewerInfo();
		viewerInfo.update(game);

		setLayout(null);
		setOpaque(false);

		logBookViewer=new LogBookViewer(game.getLogBook());
		logBookViewer.setVisible(false);
		
		cardViewer=new CardViewer(false);
		add(cardViewer);
		controller.setCardViewer(cardViewer);
		
		imageCardViewer=new CardViewer(true);
		imageCardViewer.setSize(CardImages.CARD_WIDTH,CardImages.CARD_HEIGHT);
		imageCardViewer.setVisible(false);
		controller.setImageCardViewer(imageCardViewer);
						
		playerViewer=new PlayerViewer(viewerInfo,controller,false);
		add(playerViewer);		
		
		opponentViewer=new PlayerViewer(viewerInfo,controller,true);
		add(opponentViewer);
		
		gameTournamentViewer=new GameTournamentViewer(game,controller);
		controller.setGameViewer(gameTournamentViewer.getGameViewer());
		add(gameTournamentViewer);
						
		logBookButton=new JLabel(IconImages.LOG);
		add(logBookButton);		
		logBookButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(final MouseEvent event) {

				logBookViewer.update();
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
												
						logBookViewer.setVisible(true);
					}
				});
			}

			@Override
			public void mouseExited(final MouseEvent event) {

				logBookViewer.setVisible(false);
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
		stackTitleBar.setIcon(IconImages.SPELL);
		imageStackViewer.add(stackTitleBar,BorderLayout.SOUTH);
		
		updateView();
		thread=new GameControllerThread(controller);
		thread.start();
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