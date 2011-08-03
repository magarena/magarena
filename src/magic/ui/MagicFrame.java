package magic.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import magic.data.DeckUtils;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.data.TournamentConfig;
import magic.model.MagicDeck;
import magic.model.MagicGame;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicTournament;
import magic.test.TestGameBuilder;
import magic.ui.widget.ZoneBackgroundLabel;

public class MagicFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NAME="Magarena";
	private static final String SAVE_TOURNAMENT_ITEM="Save";
	private static final String RESTART_TOURNAMENT_ITEM="Restart";
	private static final String LOAD_DECK_ITEM="LoadDeck";
	private static final String SAVE_DECK_ITEM="SaveDeck";
	private static final String SWAP_DECKS_ITEM="Swap";
	private static final String PLAY_GAME_ITEM="Play";
	private static final String RESET_GAME_ITEM="Reset";
	private static final String CONCEDE_GAME_ITEM="Concede";
	private static final String CARD_EXPLORER_ITEM="Explorer";
	private static final String KEYWORDS_ITEM="Keywords";
	private static final String README_ITEM="ReadMe";
    //java -DtestGame=X to start with a specific game 
	private static final String testGame=System.getProperty("testGame");
	
	private final GeneralConfig config;
	private final JPanel contentPanel;
	private JMenuItem newTournamentItem;
	private JMenuItem loadTournamentItem;
	private JMenuItem saveTournamentItem;
	private JMenuItem restartTournamentItem;
	private JMenuItem loadDeckItem;
	private JMenuItem saveDeckItem;
	private JMenuItem swapDecksItem;
	private JMenuItem playGameItem;
	private JMenuItem resetGameItem;
	private JMenuItem concedeGameItem;
    private JMenuItem downloadImagesItem;	
    private JMenuItem preferencesItem;
	private JMenuItem quitItem;
	private JMenuItem updateItem;
	private JMenuItem cardExplorerItem;
	private JMenuItem keywordsItem;
	private JMenuItem readMeItem;
	private MagicTournament tournament=null;
	private TournamentPanel tournamentPanel=null;
	private GamePanel gamePanel=null;
	private LinkedList<JComponent> contents;
	
	public MagicFrame() {

		config=GeneralConfig.getInstance();
		config.load();
		
		this.setTitle(NAME);
		this.setSize(config.getWidth(),config.getHeight());
		this.setIconImage(IconImages.ARENA.getImage());
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

                /*
                if (gamePanel != null) {
    		        gamePanel.getController().haltGame();
                }
                */
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
		} else if (LOAD_DECK_ITEM.equals(item)) {
			loadDeckItem.setEnabled(enabled);
		} else if (SAVE_DECK_ITEM.equals(item)) {
			saveDeckItem.setEnabled(enabled);
		} else if (SWAP_DECKS_ITEM.equals(item)) {
			swapDecksItem.setEnabled(enabled);
		} else if (PLAY_GAME_ITEM.equals(item)) {
			playGameItem.setEnabled(enabled);
		} else if (RESET_GAME_ITEM.equals(item)) {
			resetGameItem.setEnabled(enabled);
		} else if (CONCEDE_GAME_ITEM.equals(item)) {
			concedeGameItem.setEnabled(enabled);
		} else if (CARD_EXPLORER_ITEM.equals(item)) {
			cardExplorerItem.setEnabled(enabled);
		} else if (KEYWORDS_ITEM.equals(item)) {
			keywordsItem.setEnabled(enabled);
		}else if (README_ITEM.equals(item)) {
			readMeItem.setEnabled(enabled);
		}
	}
	
	private void setInitialContent() {
		setContent(new VersionPanel(this));			
		if (testGame != null) {
			openGame(TestGameBuilder.buildGame(testGame));
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
		enableMenuItem(LOAD_DECK_ITEM,false);
		enableMenuItem(SAVE_DECK_ITEM,false);
		enableMenuItem(SWAP_DECKS_ITEM,false);
		enableMenuItem(PLAY_GAME_ITEM,false);
		enableMenuItem(RESET_GAME_ITEM,false);
		enableMenuItem(CONCEDE_GAME_ITEM,false);
		enableMenuItem(CARD_EXPLORER_ITEM,true);
		enableMenuItem(KEYWORDS_ITEM,true);
		enableMenuItem(README_ITEM,true);
	}
	
	private void closeContent() {
		contents.removeLast();
		showContent(contents.getLast());
	}
		
	private void createMenuBar() {

		final JMenu tournamentMenu=new JMenu("Arena");
		
		newTournamentItem=new JMenuItem("New duel");
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
		
		tournamentMenu.addSeparator();
		
		loadDeckItem=new JMenuItem("Load deck");
		loadDeckItem.addActionListener(this);
		tournamentMenu.add(loadDeckItem);
		
		saveDeckItem=new JMenuItem("Save deck");
		saveDeckItem.addActionListener(this);
		tournamentMenu.add(saveDeckItem);
		
		swapDecksItem=new JMenuItem("Swap decks");
		swapDecksItem.addActionListener(this);
		tournamentMenu.add(swapDecksItem);

		tournamentMenu.addSeparator();

		playGameItem=new JMenuItem("Play game");
		playGameItem.addActionListener(this);
		tournamentMenu.add(playGameItem);
		
		resetGameItem=new JMenuItem("Reset game");
		resetGameItem.addActionListener(this);
		tournamentMenu.add(resetGameItem);
		
		concedeGameItem=new JMenuItem("Concede game");
		concedeGameItem.addActionListener(this);
		tournamentMenu.add(concedeGameItem);
		
		tournamentMenu.addSeparator();
				
        downloadImagesItem = new JMenuItem("Download images");
        downloadImagesItem.addActionListener(this);
		tournamentMenu.add(downloadImagesItem);
        
        preferencesItem=new JMenuItem("Preferences");
		preferencesItem.addActionListener(this);
		tournamentMenu.add(preferencesItem);
        
        updateItem=new JMenuItem("Update");
        updateItem.addActionListener(this);
        //tournamentMenu.add(updateItem);
		
		quitItem=new JMenuItem("Quit");
		quitItem.addActionListener(this);
		tournamentMenu.add(quitItem);
		
		final JMenu helpMenu=new JMenu("Help");
		
		readMeItem=new JMenuItem("Read Me");
		readMeItem.addActionListener(this);
		helpMenu.add(readMeItem);
		
		cardExplorerItem=new JMenuItem("Card explorer");
		cardExplorerItem.addActionListener(this);
		helpMenu.add(cardExplorerItem);

		keywordsItem=new JMenuItem("Keywords");
		keywordsItem.addActionListener(this);
		helpMenu.add(keywordsItem);
		
		final JMenuBar menuBar=new JMenuBar();
		menuBar.add(tournamentMenu);
		menuBar.add(helpMenu);

        /*
        final JMenu prefMenu = new JMenu("Preferences");
        prefMenu.add(new JCheckBoxMenuItem("Enable sound effects"));
        prefMenu.add(new JCheckBoxMenuItem("Show card image in original size"));
		prefMenu.add(new JCheckBoxMenuItem("Skip single option choices when appropriate"));
		prefMenu.add(new JCheckBoxMenuItem("Always pass during draw and begin of combat step"));
		prefMenu.add(new JCheckBoxMenuItem("Filter legal targets when appropriate"));
        menuBar.add(prefMenu);
        */

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
			enableMenuItem(LOAD_DECK_ITEM,tournament.isEditable());
			enableMenuItem(SAVE_DECK_ITEM,tournament.isEditable());
			enableMenuItem(SWAP_DECKS_ITEM,tournament.isEditable());
			enableMenuItem(PLAY_GAME_ITEM,!tournament.isFinished());
            if (System.getProperty("selfMode") != null) {
                if (!tournament.isFinished()) {
                    nextGame();
                } else {
                    newTournament(TournamentConfig.getInstance());
                }
            }
		} else {
			setContent(new ZoneBackgroundLabel());
		}
	}
	
	public void showNewTournamentDialog() {
		new TournamentDialog(this);
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
	
	private void saveTournament() {

		if (tournament!=null) {
			tournament.save(MagicTournament.getTournamentFile());
		}
	}
	
	private void restartTournament() {

		if (tournament!=null) {
			tournament.restart();
			showTournament();
		}
	}
	
	private void loadDeck() {
		
		if (tournamentPanel!=null) {
			final MagicPlayerDefinition player=tournamentPanel.getSelectedPlayer();
			final JFileChooser fileChooser=new JFileChooser(DeckUtils.getDeckFolder());
			fileChooser.setDialogTitle("Load deck");
			fileChooser.setFileFilter(DeckUtils.DECK_FILEFILTER);
			fileChooser.setAcceptAllFileFilterUsed(false);
			final int action=fileChooser.showOpenDialog(this);
			if (action==JFileChooser.APPROVE_OPTION) {
				final String filename=fileChooser.getSelectedFile().getAbsolutePath();
				DeckUtils.loadDeck(filename,player);
				tournamentPanel.updateDecksAfterEdit();
			}			
		}
	}
	
	private void saveDeck() {
		
		if (tournamentPanel!=null) {
			final MagicPlayerDefinition player=tournamentPanel.getSelectedPlayer();
			final JFileChooser fileChooser=new JFileChooser(DeckUtils.getDeckFolder());
			fileChooser.setDialogTitle("Save deck");
			fileChooser.setFileFilter(DeckUtils.DECK_FILEFILTER);
			fileChooser.setAcceptAllFileFilterUsed(false);
			final int action=fileChooser.showSaveDialog(this);
			if (action==JFileChooser.APPROVE_OPTION) {
				String filename=fileChooser.getSelectedFile().getAbsolutePath();
				if (!filename.endsWith(DeckUtils.DECK_EXTENSION)) {
					filename+=DeckUtils.DECK_EXTENSION;
				}
				DeckUtils.saveDeck(filename,player);
			}
		}
	}
	
	private void swapDecks() {
		
		if (tournament!=null) {
			tournament.restart();
			final MagicPlayerDefinition players[]=tournament.getPlayers();
			final MagicPlayerProfile profile1=players[0].getProfile();
			final MagicPlayerProfile profile2=players[1].getProfile();
			final MagicDeck deck1=players[0].getDeck();			
			final MagicDeck deck2=players[1].getDeck();
			players[0].setProfile(profile2);
			players[0].setDeck(deck2);
			players[1].setProfile(profile1);
			players[1].setDeck(deck1);
			showTournament();
		}
	}
	
	public void resetGame() {
		if (gamePanel!=null) {
			gamePanel.getController().resetGame();
		}
	}
	
	public void concedeGame() {
		if (gamePanel!=null) {
			gamePanel.getController().concede();
		}
	}
		
	public void nextGame() {
		tournament.updateDifficulty();
		openGame(tournament.nextGame(true));
	}
	
	private void openGame(final MagicGame game) {
		final ZoneBackgroundLabel backgroundLabel=new ZoneBackgroundLabel();
		backgroundLabel.setGame(true);
		gamePanel=new GamePanel(this,game,backgroundLabel);
		final GameLayeredPane gamePane=new GameLayeredPane(gamePanel,backgroundLabel);
		setContent(gamePane);		
		gamePanel.requestFocus();
		enableMenuItem(RESET_GAME_ITEM,true);
		enableMenuItem(CONCEDE_GAME_ITEM,true);
	}
		
	private void openCardExplorer() {
		
		enableMenuItem(CARD_EXPLORER_ITEM,false);
		final ExplorerPanel explorerPanel=new ExplorerPanel(this,ExplorerPanel.ALL,null);
		addContent(explorerPanel);
	}
	
	public void editCardWithExplorer(final EditDeckCard editDeckCard) {
		
		enableMenuItem(CARD_EXPLORER_ITEM,false);
		final int mode=editDeckCard.getCard().isLand()?ExplorerPanel.LAND:ExplorerPanel.SPELL;
		final ExplorerPanel explorerPanel=new ExplorerPanel(this,mode,editDeckCard);
		addContent(explorerPanel);
	}
	
	public void closeCardExplorer() {
		closeContent();
		enableMenuItem(CARD_EXPLORER_ITEM,true);	
		if (gamePanel!=null) {
			gamePanel.requestFocus();
		}
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
    
    private void openReadme() {
		enableMenuItem(README_ITEM,false);
		final ReadmePanel rmPanel = new ReadmePanel(this);
		addContent(rmPanel);
	}
	
    public void closeReadme() {
		closeContent();
		enableMenuItem(README_ITEM,true);
	}
			
	@Override
	public void actionPerformed(ActionEvent event) {

		final Object source=event.getSource();
		if (source==newTournamentItem) {
			showNewTournamentDialog();
		} else if (source==loadTournamentItem) {
			loadTournament();
		} else if (source==saveTournamentItem) {
			saveTournament();
		} else if (source==restartTournamentItem) {
			restartTournament();
		} else if (source==loadDeckItem) {
			loadDeck();
		} else if (source==saveDeckItem) {
			saveDeck();
		} else if (source==swapDecksItem) {
			swapDecks();
		} else if (source==playGameItem) {
			nextGame();
		} else if (source==resetGameItem) {
			resetGame();
		} else if (source==concedeGameItem) {
			concedeGame();
        } else if (source==downloadImagesItem) {
            new DownloadImagesDialog(this);
		} else if (source==preferencesItem) {
			new PreferencesDialog(this);
		} else if (source==quitItem) {
            processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else if (source==updateItem) {
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(final WindowEvent event) {
                    updateApp();
                }
            });
            processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else if (source==cardExplorerItem) {
			openCardExplorer();
		} else if (source==keywordsItem) {
			openKeywords();
		}else if (source==readMeItem) {
			openReadme();
		}
	}
    
    public boolean updateApp() {
        //check for new version
        
        
        //if there is a new version, download the jar file
        
        
        //restart the app
        String javaBin = System.getProperty("java.home") + "/bin/java";
        File jarFile;
        try{
            jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (final Exception ex) {
            return false;
        }

        /* is it a jar file? */
        if ( !jarFile.getName().endsWith(".jar") )
        return false;   //no, it's a .class probably

        String toExec[] = new String[] {javaBin, "-jar", jarFile.getPath()};
        try{
            Process p = Runtime.getRuntime().exec(toExec);
        } catch (final Exception ex) {
            ex.printStackTrace();
            return false;
        }

        System.exit(0);
        return true;
    }
}
