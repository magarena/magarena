package magic.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import magic.data.GeneralConfig;
import magic.data.TournamentConfig;
import magic.model.MagicDeckCard;
import magic.model.MagicGame;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicTournament;
import magic.test.TestGameBuilder;
import magic.ui.widget.BackgroundLabel;

public class MagicFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NAME="Magarena";
	private static final String SAVE_TOURNAMENT_ITEM="Save";
	private static final String RESTART_TOURNAMENT_ITEM="Restart";
	private static final String SWAP_DECKS_ITEM="Swap";
	private static final String PLAY_GAME_ITEM="Play";
	private static final String CONCEDE_GAME_ITEM="Concede";
	private static final String CARD_EXPLORER_ITEM="Explorer";
	private static final String KEYWORDS_ITEM="Keywords";
	
	private static final boolean testGame=System.getProperty("testGame")!=null;
	
	private final GeneralConfig config;
	private final JPanel contentPanel;
	private JMenuItem newTournamentItem;
	private JMenuItem loadTournamentItem;
	private JMenuItem saveTournamentItem;
	private JMenuItem restartTournamentItem;
	private JMenuItem swapDecksItem;
	private JMenuItem playGameItem;
	private JMenuItem concedeGameItem;
	private JMenuItem downloadImagesItem;
	private JMenuItem preferencesItem;
	private JMenuItem quitItem;
	private JMenuItem cardExplorerItem;
	private JMenuItem keywordsItem;
	private MagicTournament tournament=null;
	private TournamentPanel tournamentPanel=null;
	private GamePanel gamePanel=null;
	private LinkedList<JComponent> contents;
	
	public MagicFrame() {

		config=GeneralConfig.getInstance();
		config.load();
		
		this.setTitle(NAME);
		this.setSize(config.getWidth(),config.getHeight());
		if (config.getLeft()!=-1) {
			this.setLocation(config.getLeft(),config.getTop());
		} else {
			this.setLocationRelativeTo(null);
		}
		if (config.isMaximized()) {
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}

		contentPanel=new JPanel(new BorderLayout());
		contentPanel.setOpaque(true);
		setContentPane(contentPanel);
		
		contents=new LinkedList<JComponent>();
	
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent event) {

				final boolean maximized=(MagicFrame.this.getExtendedState()&JFrame.MAXIMIZED_BOTH)==JFrame.MAXIMIZED_BOTH;
				if (maximized) {
					config.setMaximized(true);
				} else {					
					config.setLeft(getX());
					config.setTop(getY());
					config.setWidth(getWidth());
					config.setHeight(getHeight());
					config.setMaximized(false);
				}
				config.save();
				System.exit(0);
			}
		});
		
		createMenuBar();
		setInitialContent();
		setVisible(true);		
	}

	private void enableMenuItem(final String item,final boolean enabled) {
		
		if (SAVE_TOURNAMENT_ITEM.equals(item)) {
			saveTournamentItem.setEnabled(enabled);
		} else if (RESTART_TOURNAMENT_ITEM.equals(item)) {
			restartTournamentItem.setEnabled(enabled);
		} else if (SWAP_DECKS_ITEM.equals(item)) {
			swapDecksItem.setEnabled(enabled);
		} else if (PLAY_GAME_ITEM.equals(item)) {
			playGameItem.setEnabled(enabled);
		} else if (CONCEDE_GAME_ITEM.equals(item)) {
			concedeGameItem.setEnabled(enabled);
		} else if (CARD_EXPLORER_ITEM.equals(item)) {
			cardExplorerItem.setEnabled(enabled);
		} else if (KEYWORDS_ITEM.equals(item)) {
			keywordsItem.setEnabled(enabled);
		} 
	}
	
	private void setInitialContent() {
		
		setContent(new BackgroundLabel());			
		if (testGame) {
			openGame(TestGameBuilder.buildGame());
		} 
	}
	
	private void showContent(final JComponent content) {

		contentPanel.removeAll();
		contentPanel.add(content,BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	private void addContent(final JComponent content) {

		contents.add(content);
		showContent(content);
	}
	
	private void setContent(final JComponent content) {

		if (tournamentPanel!=null) {
			tournamentPanel.haltStrengthViewer();
			tournamentPanel=null;
		}
		contents.clear();
		addContent(content);
		enableMenuItem(SAVE_TOURNAMENT_ITEM,false);
		enableMenuItem(RESTART_TOURNAMENT_ITEM,false);
		enableMenuItem(SWAP_DECKS_ITEM,false);
		enableMenuItem(PLAY_GAME_ITEM,false);
		enableMenuItem(CONCEDE_GAME_ITEM,false);
		enableMenuItem(CARD_EXPLORER_ITEM,true);
		enableMenuItem(KEYWORDS_ITEM,true);
	}
	
	private void closeContent() {
		
		contents.removeLast();
		showContent(contents.getLast());
	}
		
	private void createMenuBar() {

		final JMenu tournamentMenu=new JMenu("Arena");
		
		newTournamentItem=new JMenuItem("New duel...");
		newTournamentItem.addActionListener(this);
		tournamentMenu.add(newTournamentItem);
		
		loadTournamentItem=new JMenuItem("Load duel");
		loadTournamentItem.addActionListener(this);
		tournamentMenu.add(loadTournamentItem);

		saveTournamentItem=new JMenuItem("Save duel");
		saveTournamentItem.addActionListener(this);
		tournamentMenu.add(saveTournamentItem);
		
		restartTournamentItem=new JMenuItem("Restart duel");
		restartTournamentItem.addActionListener(this);
		tournamentMenu.add(restartTournamentItem);
		
		swapDecksItem=new JMenuItem("Swap decks");
		swapDecksItem.addActionListener(this);
		tournamentMenu.add(swapDecksItem);

		tournamentMenu.addSeparator();

		playGameItem=new JMenuItem("Play game");
		playGameItem.addActionListener(this);
		tournamentMenu.add(playGameItem);
		
		concedeGameItem=new JMenuItem("Concede game");
		concedeGameItem.addActionListener(this);
		tournamentMenu.add(concedeGameItem);
		
		tournamentMenu.addSeparator();
		
		downloadImagesItem=new JMenuItem("Download images...");
		downloadImagesItem.addActionListener(this);
		tournamentMenu.add(downloadImagesItem);
		
		preferencesItem=new JMenuItem("Preferences...");
		preferencesItem.addActionListener(this);
		tournamentMenu.add(preferencesItem);
		
		quitItem=new JMenuItem("Quit");
		quitItem.addActionListener(this);
		tournamentMenu.add(quitItem);
		
		final JMenu helpMenu=new JMenu("Help");
		
		cardExplorerItem=new JMenuItem("Card explorer...");
		cardExplorerItem.addActionListener(this);
		helpMenu.add(cardExplorerItem);

		keywordsItem=new JMenuItem("Keywords...");
		keywordsItem.addActionListener(this);
		helpMenu.add(keywordsItem);
		
		final JMenuBar menuBar=new JMenuBar();
		menuBar.add(tournamentMenu);		
		menuBar.add(helpMenu);
		
		this.setJMenuBar(menuBar);
	}
	
	public void showTournament() {

		gamePanel=null;
		if (tournament!=null) {
			final TournamentPanel newTournamentPanel=new TournamentPanel(this,tournament);
			setContent(newTournamentPanel);
			tournamentPanel=newTournamentPanel;
			enableMenuItem(SAVE_TOURNAMENT_ITEM,true);
			enableMenuItem(RESTART_TOURNAMENT_ITEM,true);
			enableMenuItem(SWAP_DECKS_ITEM,true);
			enableMenuItem(PLAY_GAME_ITEM,!tournament.isFinished());
		} else {
			setContent(new BackgroundLabel());
		}
	}
	
	public void newTournament(final TournamentConfig configuration) {

		tournament=new MagicTournament(configuration);
		tournament.initialize();
		showTournament();
	}
	
	public void loadTournament() {

		final File tournamentFile=MagicTournament.getTournamentFile();
		if (tournamentFile.exists()) {
			tournament=new MagicTournament();
			tournament.load(tournamentFile);
			showTournament();
		}
	}
	
	public void saveTournament() {

		if (tournament!=null) {
			tournament.save(MagicTournament.getTournamentFile());
		}
	}
	
	public void restartTournament() {

		if (tournament!=null) {
			tournament.restart();
			showTournament();
		}
	}
	
	public void swapDecks() {
		
		if (tournament!=null) {
			tournament.restart();
			final MagicPlayerDefinition players[]=tournament.getPlayers();
			final MagicPlayerProfile profile1=players[0].getProfile();
			final MagicPlayerProfile profile2=players[1].getProfile();
			final List<MagicDeckCard> draftedDeck1=players[0].getDraftedDeck();			
			final List<MagicDeckCard> draftedDeck2=players[1].getDraftedDeck();
			players[0].setProfile(profile2);
			players[0].setDraftedDeck(draftedDeck2);
			players[1].setProfile(profile1);
			players[1].setDraftedDeck(draftedDeck1);
			showTournament();
		}
	}
	
	public void concedeGame() {
		
		if (gamePanel!=null) {
			gamePanel.getController().concede();
		}
	}
	
	public void nextGame() {

		tournament.updateDifficulty();
		openGame(tournament.nextGame());
	}
	
	private void openGame(final MagicGame game) {

		gamePanel=new GamePanel(this,game);
		final GameLayeredPane gamePane=new GameLayeredPane(gamePanel);
		setContent(gamePane);		
		enableMenuItem(CONCEDE_GAME_ITEM,true);
	}
		
	private void openCardExplorer() {
		
		enableMenuItem(CARD_EXPLORER_ITEM,false);
		final ExplorerPanel explorerPanel=new ExplorerPanel(this,ExplorerPanel.ALL,null,null);
		addContent(explorerPanel);
	}
	
	public void editCardWithExplorer(final EditDeckCard editDeckCard) {
		
		enableMenuItem(CARD_EXPLORER_ITEM,false);
		final int mode=editDeckCard.getDeckCard().getCardDefinition().isLand()?ExplorerPanel.LAND:ExplorerPanel.SPELL;
		final ExplorerPanel explorerPanel=new ExplorerPanel(this,mode,editDeckCard.getPlayer().getProfile(),editDeckCard);
		addContent(explorerPanel);
	}
	
	public void closeCardExplorer() {
	
		closeContent();
		enableMenuItem(CARD_EXPLORER_ITEM,true);		
	}
	
	private void openKeywords() {

		enableMenuItem(KEYWORDS_ITEM,false);
		final KeywordsPanel keywordsPanel=new KeywordsPanel(this);
		addContent(keywordsPanel);
	}
	
	public void closeKeywords() {

		closeContent();
		enableMenuItem(KEYWORDS_ITEM,true);
	}
			
	@Override
	public void actionPerformed(ActionEvent event) {

		final Object source=event.getSource();
		if (source==newTournamentItem) {
			new TournamentDialog(this);
		} else if (source==loadTournamentItem) {
			loadTournament();
		} else if (source==saveTournamentItem) {
			saveTournament();
		} else if (source==restartTournamentItem) {
			restartTournament();
		} else if (source==swapDecksItem) {
			swapDecks();
		} else if (source==playGameItem) {
			nextGame();
		} else if (source==concedeGameItem) {
			concedeGame();
		} else if (source==downloadImagesItem) {
			new DownloadImagesDialog(this);
		} else if (source==preferencesItem) {
			new PreferencesDialog(this);
		} else if (source==quitItem) {
			System.exit(0);
		} else if (source==cardExplorerItem) {
			openCardExplorer();
		} else if (source==keywordsItem) {
			openKeywords();
		}
	}
}