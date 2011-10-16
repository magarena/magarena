package magic.ui;

import magic.data.CardImagesProvider;
import magic.data.CubeDefinitions;
import magic.data.DuelConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.model.MagicDeck;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicDuel;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.DeckStatisticsViewer;
import magic.ui.viewer.DeckStrengthViewer;
import magic.ui.viewer.DuelDifficultyViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.ZoneBackgroundLabel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

public class DuelPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final int SPACING = 10;
	private static final String PLAY_BUTTON_TEXT = "Play Game";
	private static final String RESET_BUTTON_TEXT = "Reset Duel";
	private static final String EDIT_BUTTON_TEXT = "Edit Deck";
	
	private final MagicFrame frame;
	private final MagicDuel duel;
	private final JTabbedPane tabbedPane;
	private final JButton playButton;
	private final JButton resetButton;
	private final ZoneBackgroundLabel backgroundImage;
	private final DeckStrengthViewer strengthViewer;
	private final CardViewer cardViewer;
	private final DuelDifficultyViewer duelDifficultyViewer;
	private final CardTable cardTables[];
	private final JButton editButtons[];
	private final DeckStatisticsViewer statsViewers[];

	public DuelPanel(final MagicFrame frame,final MagicDuel duel) {

		this.frame=frame;
		this.duel=duel;
		
		final SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		// buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setOpaque(false);
		
		// reset button
		resetButton=new JButton(RESET_BUTTON_TEXT);
		resetButton.setFont(FontsAndBorders.FONT2);
		resetButton.addActionListener(this);
		resetButton.setFocusable(false);
		buttonsPanel.add(resetButton);
		
		buttonsPanel.add(Box.createHorizontalStrut(SPACING));
		
		// play button
		playButton=new JButton(PLAY_BUTTON_TEXT);
		playButton.setFont(FontsAndBorders.FONT2);
		playButton.addActionListener(this);
		playButton.setFocusable(false);
		playButton.setEnabled(!duel.isFinished());
		buttonsPanel.add(playButton);
		
		// center buttons
		// JPanel centeredButtonsPanel = new JPanel(new BorderLayout());
		
		
		add(buttonsPanel);
		
		// left top
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setOpaque(false);
		
		// card image
		cardViewer=new CardViewer("",false,true);
		cardViewer.setPreferredSize(CardImagesProvider.CARD_DIMENSION);
		cardViewer.setMaximumSize(CardImagesProvider.CARD_DIMENSION);
		cardViewer.setCard(MagicCardDefinition.UNKNOWN,0);
		cardViewer.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftPanel.add(cardViewer);
		
		leftPanel.add(Box.createVerticalStrut(SPACING));
		
		// games won info
		duelDifficultyViewer=new DuelDifficultyViewer(duel);
		duelDifficultyViewer.setAlignmentX(Component.LEFT_ALIGNMENT);
		duelDifficultyViewer.setMaximumSize(DuelDifficultyViewer.PREFERRED_SIZE);
		leftPanel.add(duelDifficultyViewer);	
		
		// add scrolling to left side
		JScrollPane leftScrollPane = new JScrollPane(leftPanel);
		leftScrollPane.setBorder(FontsAndBorders.NO_BORDER);
		leftScrollPane.setOpaque(false);
		leftScrollPane.getViewport().setOpaque(false);
		leftScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(leftScrollPane);
		
		// create tabs for each player
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		
		MagicPlayerDefinition players[] = duel.getPlayers();
		cardTables = new CardTable[players.length];
		statsViewers = new DeckStatisticsViewer[players.length];
		editButtons = new JButton[players.length];
		
		// deck strength tester
		strengthViewer=new DeckStrengthViewer(duel);
		strengthViewer.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		for(int i = 0; i < players.length; i++) {
			final MagicPlayerDefinition player = players[i];
			
			// deck stats
			statsViewers[i] = new DeckStatisticsViewer();
			statsViewers[i].setPlayer(player);
			statsViewers[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			statsViewers[i].setMaximumSize(DeckStatisticsViewer.PREFERRED_SIZE);
			
			// edit deck button
			final MagicCubeDefinition cubeDefinition=
				CubeDefinitions.getInstance().getCubeDefinition(duel.getConfiguration().getCube());
				
			editButtons[i] = new JButton(EDIT_BUTTON_TEXT);
			editButtons[i].setFont(FontsAndBorders.FONT2);
			editButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent event) {
					frame.openDeckEditor(player, cubeDefinition);
				}
			});
			editButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
			
			// right side
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
			rightPanel.setOpaque(false);
			
			rightPanel.add(statsViewers[i]);
			rightPanel.add(Box.createVerticalStrut(SPACING));
				
			if (!player.isArtificial()) {
				rightPanel.add(strengthViewer);
				rightPanel.add(Box.createVerticalStrut(SPACING));
				
				// show card
				cardViewer.setCard(player.getDeck().get(0),0);
			}
			
			rightPanel.add(editButtons[i]);
			
			// table of cards
			cardTables[i] = new CardTable(player.getDeck(), cardViewer, generateTitle(player.getDeck()), true);
			
			// contents of tab
			JPanel tabPanel = new JPanel();
			SpringLayout tabLayout = new SpringLayout();
			tabPanel.setLayout(tabLayout);			
			tabPanel.add(cardTables[i]);
			tabPanel.add(rightPanel);
			
			tabLayout.putConstraint(SpringLayout.WEST, cardTables[i],
								 SPACING, SpringLayout.WEST, tabPanel);
			tabLayout.putConstraint(SpringLayout.NORTH, cardTables[i],
								 SPACING, SpringLayout.NORTH, tabPanel);
			tabLayout.putConstraint(SpringLayout.SOUTH, cardTables[i],
								 -SPACING, SpringLayout.SOUTH, tabPanel);
								 
			tabLayout.putConstraint(SpringLayout.EAST, cardTables[i],
								 -SPACING, SpringLayout.WEST, rightPanel);
								 
			tabLayout.putConstraint(SpringLayout.EAST, rightPanel,
								 -SPACING, SpringLayout.EAST, tabPanel);
			tabLayout.putConstraint(SpringLayout.NORTH, rightPanel,
								 SPACING, SpringLayout.NORTH, tabPanel);
			
			// add as a tab
			tabbedPane.addTab(player.getName() + "   ", theme.getAvatarIcon(player.getFace(), 2), tabPanel);
		}
		
		add(tabbedPane);
		
		// background - must be added last (i.e., behind everything else)
		backgroundImage=new ZoneBackgroundLabel();
		backgroundImage.setBounds(0,0,0,0);
		add(backgroundImage);
		
		// set sizes by defining gaps between components
		Container contentPane = this;
		
		// background's gaps with top left bottom and right are 0
		// (i.e., it fills the window)
        springLayout.putConstraint(SpringLayout.WEST, backgroundImage,
                             0, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.NORTH, backgroundImage,
                             0, SpringLayout.NORTH, contentPane);
        springLayout.putConstraint(SpringLayout.EAST, backgroundImage,
                             0, SpringLayout.EAST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, backgroundImage,
                             0, SpringLayout.SOUTH, contentPane);
							 
		// left side's gap (left top)
        springLayout.putConstraint(SpringLayout.NORTH, leftScrollPane,
                             SPACING, SpringLayout.NORTH, backgroundImage);
        springLayout.putConstraint(SpringLayout.WEST, leftScrollPane,
                             SPACING, SpringLayout.WEST, backgroundImage);
							 
		// left side's gap with tabbed pane
        springLayout.putConstraint(SpringLayout.WEST, tabbedPane,
                             SPACING, SpringLayout.EAST, leftScrollPane);
							 
		// tabbed pane's gap (top right bottom)
        springLayout.putConstraint(SpringLayout.NORTH, tabbedPane,
                             0, SpringLayout.NORTH, leftPanel);
        springLayout.putConstraint(SpringLayout.EAST, tabbedPane,
                             -SPACING, SpringLayout.EAST, backgroundImage);
        springLayout.putConstraint(SpringLayout.SOUTH, tabbedPane,
                             -SPACING, SpringLayout.SOUTH, backgroundImage);
							 
		// buttons' gap (top left right bottom)
        springLayout.putConstraint(SpringLayout.SOUTH, leftScrollPane,
                             -SPACING, SpringLayout.NORTH, buttonsPanel);
        springLayout.putConstraint(SpringLayout.EAST, buttonsPanel,
                             -20, SpringLayout.EAST, leftScrollPane);
        springLayout.putConstraint(SpringLayout.SOUTH, buttonsPanel,
                             -SPACING, SpringLayout.SOUTH, backgroundImage);
		
	}

	String generateTitle(MagicDeck deck) {
		return "Deck (" + deck.getName() + ") - " + deck.size() + " cards";
	}
	
	public MagicFrame getFrame() {
		return frame;
	}
	
	public MagicDuel getDuel() {
		return duel;
	}
	
	public MagicPlayerDefinition getSelectedPlayer() {
		return duel.getPlayers()[tabbedPane.getSelectedIndex()];
	}
	
	public void updateDecksAfterEdit() {
		for (int i = 0; i < statsViewers.length; i++) {
			cardTables[i].setCards(duel.getPlayers()[i].getDeck());
			cardTables[i].setTitle(generateTitle(duel.getPlayers()[i].getDeck()));
			statsViewers[i].setPlayer(duel.getPlayers()[i]);
		}
	}

	public void haltStrengthViewer() {
		strengthViewer.halt();
	}
	
	public void actionPerformed(final ActionEvent event) {
		final Object source=event.getSource();
		if (source==playButton) {
			frame.nextGame();
		} else if (source==resetButton) {
			frame.resetDuel();
		}
	}
}
