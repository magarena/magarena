package magic.ui;

import magic.data.DeckUtils;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.data.DuelConfig;
import magic.model.MagicCubeDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicGame;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicDuel;
import magic.test.TestGameBuilder;
import magic.ui.widget.ZoneBackgroundLabel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

public class MagicFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final Dimension MIN_SIZE = new Dimension(GeneralConfig.DEFAULT_WIDTH, GeneralConfig.DEFAULT_HEIGHT);
	private static final String NAME = "Magarena";
	private static final String SAVE_DUEL_ITEM = "Save";
	private static final String RESTART_DUEL_ITEM = "Restart";
	private static final String RESET_DUEL_ITEM = "Reset";
	private static final String NEW_DECK_ITEM = "NewDeck";
	private static final String LOAD_DECK_ITEM = "LoadDeck";
	private static final String SAVE_DECK_ITEM = "SaveDeck";
	private static final String SWAP_DECKS_ITEM = "Swap";
	private static final String PLAY_GAME_ITEM = "Play";
	private static final String RESET_GAME_ITEM = "Reset";
	private static final String CONCEDE_GAME_ITEM = "Concede";
	private static final String CARD_EXPLORER_ITEM = "Explorer";
	private static final String KEYWORDS_ITEM = "Keywords";
	private static final String README_ITEM = "ReadMe";
	private static final String ABOUT_ITEM = "About";
    //java -DtestGame=X to start with a specific game 
	private static final String testGame = System.getProperty("testGame");
	
	private final GeneralConfig config;
	private final JPanel contentPanel;
	private JMenuItem newDuelItem;
	private JMenuItem loadDuelItem;
	private JMenuItem saveDuelItem;
	private JMenuItem restartDuelItem;
	private JMenuItem resetDuelItem;
	private JMenuItem newDeckItem;
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
	private JMenuItem aboutItem;
	private JRadioButtonMenuItem textModeItem;
	private JRadioButtonMenuItem imageModeItem;
	private MagicDuel duel;
	private DuelPanel duelPanel;
	private ExplorerPanel explorerPanel;
	private GamePanel gamePanel;
    private final LinkedList<JComponent> contents;	
	
	public MagicFrame() {
		this.explorerPanel = null;
	
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
		
		setMinimumSize(MIN_SIZE);

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

        //in selfMode start game immediate based on configuration from duel.cfg
        if (System.getProperty("selfMode") != null) {
            final DuelConfig config=DuelConfig.getInstance();
            config.load();
            newDuel(config);
        }
	}

	private void enableMenuItem(final String item,final boolean enabled) {
		
		if (SAVE_DUEL_ITEM.equals(item)) {
			saveDuelItem.setEnabled(enabled);
		} else if (RESTART_DUEL_ITEM.equals(item)) {
			restartDuelItem.setEnabled(enabled);
		} else if (RESET_DUEL_ITEM.equals(item)) {
			resetDuelItem.setEnabled(enabled);
		} else if (NEW_DECK_ITEM.equals(item)) {
			newDeckItem.setEnabled(enabled);
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
		}else if (ABOUT_ITEM.equals(item)) {
			aboutItem.setEnabled(enabled);
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
		//the following statement causes a
        //Exception in thread "AWT-EventQueue-0" sun.awt.X11.XException: Cannot write XdndAware property
        //on Java(TM) SE Runtime Environment (build 1.6.0_26-b03)
        contentPanel.add(content,BorderLayout.CENTER);
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	private void addContent(final JComponent content) {
		contents.add(content);
		showContent(content);
	}
	
	private void setContent(final JComponent content) {

		if (duelPanel!=null) {
			duelPanel.haltStrengthViewer();
			duelPanel=null;
		}
		contents.clear();
		addContent(content);
		enableMenuItem(SAVE_DUEL_ITEM,false);
		enableMenuItem(RESTART_DUEL_ITEM,false);
		enableMenuItem(RESET_DUEL_ITEM, false);
		enableMenuItem(NEW_DECK_ITEM,false);
		enableMenuItem(LOAD_DECK_ITEM,false);
		enableMenuItem(SAVE_DECK_ITEM,false);
		enableMenuItem(SWAP_DECKS_ITEM,false);
		enableMenuItem(PLAY_GAME_ITEM,false);
		enableMenuItem(RESET_GAME_ITEM,false);
		enableMenuItem(CONCEDE_GAME_ITEM,false);
		enableMenuItem(CARD_EXPLORER_ITEM,true);
		enableMenuItem(KEYWORDS_ITEM,true);
		enableMenuItem(README_ITEM,true);
		enableMenuItem(ABOUT_ITEM,true);
	}
	
	private void closeContent() {
		contents.removeLast();
		showContent(contents.getLast());
	}
		
	private void createMenuBar() {
		// arena menu
		final JMenu arenaMenu=new JMenu("Arena");
		
		newDuelItem=new JMenuItem("New duel");
		newDuelItem.addActionListener(this);
		arenaMenu.add(newDuelItem);
		
		loadDuelItem=new JMenuItem("Load duel");
		loadDuelItem.addActionListener(this);
		arenaMenu.add(loadDuelItem);

		saveDuelItem=new JMenuItem("Save duel");
		saveDuelItem.addActionListener(this);
		arenaMenu.add(saveDuelItem);
		
		restartDuelItem=new JMenuItem("Restart duel");
		restartDuelItem.addActionListener(this);
		// arenaMenu.add(restartDuelItem);
		
		resetDuelItem=new JMenuItem("Reset duel");
		resetDuelItem.addActionListener(this);
		arenaMenu.add(resetDuelItem);
		
		arenaMenu.addSeparator();
				
        downloadImagesItem = new JMenuItem("Download images");
        downloadImagesItem.addActionListener(this);
		arenaMenu.add(downloadImagesItem);
        
        preferencesItem=new JMenuItem("Preferences");
		preferencesItem.addActionListener(this);
		arenaMenu.add(preferencesItem);
        
        updateItem=new JMenuItem("Update");
        updateItem.addActionListener(this);
        //arenaMenu.add(updateItem);
		
		quitItem=new JMenuItem("Quit");
		quitItem.addActionListener(this);
		arenaMenu.add(quitItem);
		
		// duel menu		
		final JMenu duelMenu = new JMenu("Duel");
		
		newDeckItem=new JMenuItem("New deck");
		newDeckItem.addActionListener(this);
		duelMenu.add(newDeckItem);
		
		loadDeckItem=new JMenuItem("Load deck");
		loadDeckItem.addActionListener(this);
		duelMenu.add(loadDeckItem);
		
		saveDeckItem=new JMenuItem("Save deck");
		saveDeckItem.addActionListener(this);
		duelMenu.add(saveDeckItem);
		
		swapDecksItem=new JMenuItem("Swap decks");
		swapDecksItem.addActionListener(this);
		duelMenu.add(swapDecksItem);

		duelMenu.addSeparator();

		playGameItem=new JMenuItem("Play game");
		playGameItem.addActionListener(this);
		duelMenu.add(playGameItem);
		
		resetGameItem=new JMenuItem("Reset game");
		resetGameItem.addActionListener(this);
		duelMenu.add(resetGameItem);
		
		concedeGameItem=new JMenuItem("Concede game");
		concedeGameItem.addActionListener(this);
		duelMenu.add(concedeGameItem);
		
		// view menu		
		final JMenu viewMenu = new JMenu("View");
		
		ButtonGroup modeGroup = new ButtonGroup();
		
		textModeItem = new JRadioButtonMenuItem("Text Mode");
		textModeItem.setSelected(GeneralConfig.getInstance().getTextView());
		textModeItem.addActionListener(this);
		modeGroup.add(textModeItem);
		viewMenu.add(textModeItem);
		
		imageModeItem = new JRadioButtonMenuItem("Image Mode");
		imageModeItem.setSelected(!GeneralConfig.getInstance().getTextView());
		imageModeItem.addActionListener(this);
		modeGroup.add(imageModeItem);
		viewMenu.add(imageModeItem);

		viewMenu.addSeparator();

		keywordsItem=new JMenuItem("Keywords");
		keywordsItem.addActionListener(this);
		viewMenu.add(keywordsItem);
		
		cardExplorerItem=new JMenuItem("Card Explorer");
		cardExplorerItem.addActionListener(this);
		viewMenu.add(cardExplorerItem);
		
		// help menu
		final JMenu helpMenu=new JMenu("Help");
		
		readMeItem=new JMenuItem("Read Me");
		readMeItem.addActionListener(this);
		helpMenu.add(readMeItem);
		
		aboutItem=new JMenuItem("About Magarena");
		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);
		
		final JMenuBar menuBar=new JMenuBar();
		menuBar.add(arenaMenu);
		menuBar.add(duelMenu);
		menuBar.add(viewMenu);
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
	
	public void showDuel() {
		gamePanel=null;
		if (duel!=null) {
			final DuelPanel newDuelPanel=new DuelPanel(this,duel);
			setContent(newDuelPanel);
			duelPanel=newDuelPanel;
			enableMenuItem(SAVE_DUEL_ITEM,true);
			enableMenuItem(RESTART_DUEL_ITEM,true);
			enableMenuItem(RESET_DUEL_ITEM,true);
			enableMenuItem(NEW_DECK_ITEM,duel.isEditable());
			enableMenuItem(LOAD_DECK_ITEM,duel.isEditable());
			enableMenuItem(SAVE_DECK_ITEM,duel.isEditable());
			enableMenuItem(SWAP_DECKS_ITEM,duel.isEditable());
			enableMenuItem(PLAY_GAME_ITEM,!duel.isFinished());
            if (System.getProperty("selfMode") != null) {
                if (!duel.isFinished()) {
                    nextGame();
                } else {
                    newDuel(DuelConfig.getInstance());
                }
            }
		} else {
			setContent(new ZoneBackgroundLabel());
		}
	}
	
	public void showNewDuelDialog() {
		new DuelDialog(this);
	}
	
	public void newDuel(final DuelConfig configuration) {
		duel=new MagicDuel(configuration);
		duel.initialize();
		showDuel();
	}
	
	public void resetDuel() {
		newDuel(DuelConfig.getInstance());
	}
	
	public void loadDuel() {
		final File duelFile=MagicDuel.getDuelFile();
		if (duelFile.exists()) {
			duel=new MagicDuel();
			duel.load(duelFile);
			showDuel();
		}
	}
	
	private void saveDuel() {
		if (duel!=null) {
			duel.save(MagicDuel.getDuelFile());
		}
	}
	
	private void restartDuel() {
		if (duel!=null) {
			duel.restart();
			showDuel();
		}
	}
	
	private void newDeck() {
		if (duelPanel!=null) {
			final MagicPlayerDefinition player=duelPanel.getSelectedPlayer();
			player.getDeck().clear();
			duelPanel.updateDecksAfterEdit();
			if(explorerPanel != null) {
				explorerPanel.updateDeck();
			}
		}
	}
	
	private void loadDeck() {
		if (duelPanel!=null) {
			final MagicPlayerDefinition player=duelPanel.getSelectedPlayer();
			final JFileChooser fileChooser=new JFileChooser(DeckUtils.getDeckFolder());
			fileChooser.setDialogTitle("Load deck");
			fileChooser.setFileFilter(DeckUtils.DECK_FILEFILTER);
			fileChooser.setAcceptAllFileFilterUsed(false);
			final int action=fileChooser.showOpenDialog(this);
			if (action==JFileChooser.APPROVE_OPTION) {
				final String filename=fileChooser.getSelectedFile().getAbsolutePath();
				DeckUtils.loadDeck(filename,player);
				duelPanel.updateDecksAfterEdit();
				if(explorerPanel != null) {
					explorerPanel.updateDeck();
				}
			}			
		}
	}
	
	private void saveDeck() {
		if (duelPanel!=null) {
			final MagicPlayerDefinition player=duelPanel.getSelectedPlayer();
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
				if(DeckUtils.saveDeck(filename,player)) {
					String shortFilename = fileChooser.getSelectedFile().getName();
					if (shortFilename.indexOf(".dec") == -1) {
						shortFilename += ".dec";
					}
					player.getDeck().setName(shortFilename);
					duelPanel.updateDecksAfterEdit();
					if(explorerPanel != null) {
						explorerPanel.updateDeck();
					}
				}
			}
		}
	}
	
	private void swapDecks() {
		if (duel!=null) {
			duel.restart();
			final MagicPlayerDefinition players[]=duel.getPlayers();
			final MagicPlayerProfile profile1=players[0].getProfile();
			final MagicPlayerProfile profile2=players[1].getProfile();
			final MagicDeck deck1 = new MagicDeck(players[0].getDeck());			
			final MagicDeck deck2 = new MagicDeck(players[1].getDeck());
			players[0].setProfile(profile2);
			players[0].setDeck(deck2);
			players[1].setProfile(profile1);
			players[1].setDeck(deck1);
			showDuel();
		}
	}
	
	public boolean isLegalDeckAndShowErrors(MagicDeck deck, String playerName) {
		String brokenRulesText = MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));
		
		if(brokenRulesText.length() > 0) {
			JOptionPane.showMessageDialog(this, playerName + "'s deck is illegal.\n\n" + brokenRulesText, "Illegal Deck", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	private void resetGame() {
		if (gamePanel!=null) {
			gamePanel.getController().resetGame();
		}
	}
	
	private void concedeGame() {
		if (gamePanel!=null) {
			gamePanel.getController().concede();
		}
	}
		
	public void nextGame() {
		duel.updateDifficulty();
		
		final MagicPlayerDefinition players[]=duel.getPlayers();
		if(isLegalDeckAndShowErrors(players[0].getDeck(), players[0].getName()) && isLegalDeckAndShowErrors(players[1].getDeck(), players[1].getName())) {
			openGame(duel.nextGame(true));
		}
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
	
	public void updateGameView() {
		if(gamePanel != null) {
			gamePanel.updateView();
		}
	}
		
	private void openCardExplorer() {
		enableMenuItem(CARD_EXPLORER_ITEM,false);
		explorerPanel = new ExplorerPanel(this, ExplorerPanel.ALL, null, null);
		addContent(explorerPanel);
	}
	
	public void openDeckEditor(final MagicPlayerDefinition player, final MagicCubeDefinition cube) {
		enableMenuItem(CARD_EXPLORER_ITEM,false);
		// final int mode=editDeckCard.getCard().isLand()?ExplorerPanel.LAND:ExplorerPanel.SPELL;
		explorerPanel=new ExplorerPanel(this,ExplorerPanel.ALL,player, cube);
		addContent(explorerPanel);
	}
	
	public void closeCardExplorer() {
		closeContent();
		enableMenuItem(CARD_EXPLORER_ITEM,true);	
		if (gamePanel!=null) {
			gamePanel.requestFocus();
		}
	}
	
	public void closeDeckEditor() {
		if(isLegalDeckAndShowErrors(explorerPanel.getPlayer().getDeck(), explorerPanel.getPlayer().getName())) {
			closeCardExplorer();
			if (duelPanel != null) {
				duelPanel.updateDecksAfterEdit();
			}
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
	
	public void setTextImageMode(final boolean isTextMode) {
		GeneralConfig.getInstance().setTextView(isTextMode);
		if (gamePanel != null) {
			gamePanel.updateView();
		}
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {

		final Object source=event.getSource();
		if (source==newDuelItem) {
			showNewDuelDialog();
		} else if (source==loadDuelItem) {
			loadDuel();
		} else if (source==saveDuelItem) {
			saveDuel();
		} else if (source==restartDuelItem) {
			restartDuel();
		} else if (source == resetDuelItem) {
			resetDuel();
		} else if (source==newDeckItem) {
			newDeck();
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
		} else if (source==readMeItem) {
			openReadme();
		} else if (source==aboutItem) {
			new AboutDialog(this);
		} else if (source == textModeItem) {
			setTextImageMode(true);
		} else if (source == imageModeItem) {
			setTextImageMode(false);
		}
	}
    
    private boolean updateApp() {
        //check for new version
        
        
        //if there is a new version, download the jar file
        
        
        //restart the app
        final String javaBin = System.getProperty("java.home") + "/bin/java";
        File jarFile;
        try { //get File object of jar
            jarFile = new File(this.getClass().
                    getProtectionDomain().
                    getCodeSource().
                    getLocation().
                    toURI());
        } catch (final java.net.URISyntaxException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }

        /* is it a jar file? */
        if ( !jarFile.getName().endsWith(".jar") ) {
            //no, it's a .class probably
            return false;
        }

        final String toExec[] = new String[] {javaBin, "-jar", jarFile.getPath()};
        try { //restart the application 
            final Process p = Runtime.getRuntime().exec(toExec);
        } catch (final IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }

        System.exit(0);
        return true;
    }
}
