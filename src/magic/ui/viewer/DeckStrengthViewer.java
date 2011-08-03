package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.Border;

import magic.ai.ArtificialWorkerPool;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.data.GeneralConfig;
import magic.data.IconImages;
import magic.data.TournamentConfig;
import magic.model.MagicGame;
import magic.model.MagicTournament;
import magic.ui.GameController;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

public class DeckStrengthViewer extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String PURPOSE=
		"<html><body>"+
		"Determine the win% of your deck vs opponent's for given number of games and AI level."+
		"</body></html>";
	
	private static final Border INPUT_BORDER=BorderFactory.createEmptyBorder(0,10,0,10);
	private static final Color HIGH_COLOR=new Color(0x23,0x8E,0x23);
	private static final Color MEDIUM_COLOR=new Color(0xFF,0x7F,0x00);
	private static final Color LOW_COLOR=new Color(0xEE,0x2C,0x2C);
	
	private static final MagicAI DEFAULT_AIS[]=new MagicAI[]{MagicAIImpl.MMAB.getAI(),MagicAIImpl.MMAB.getAI()};

	private final MagicTournament tournament;
	private final JProgressBar progressBar;
	private final JLabel gameLabel;
	private final JLabel strengthLabel;
	private final JTextField gamesTextField;
	private final JComboBox difficultyComboBox;
	private final JButton startButton;
	private final Color textColor;
	private CalculateThread calculateThread=null;
	
	public DeckStrengthViewer(final MagicTournament tournament) {
		
		this.tournament=tournament;
		textColor=ThemeFactory.getInstance().getCurrentTheme().getTextColor();
		
		setLayout(new BorderLayout());
		
		final TitleBar titleBar=new TitleBar("Deck Strength");
		add(titleBar,BorderLayout.NORTH);		

		final JPanel mainPanel=new TexturedPanel();
		mainPanel.setLayout(new BorderLayout(0,4));
		mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
		add(mainPanel,BorderLayout.CENTER);
	
		final JPanel topPanel=new JPanel(new BorderLayout(0,4));
		topPanel.setOpaque(false);
		
		final JLabel purposeLabel=new JLabel(PURPOSE);
		purposeLabel.setIcon(IconImages.STRENGTH);
		purposeLabel.setForeground(textColor);

		final GeneralConfig config=GeneralConfig.getInstance();		

		final JPanel gamesPanel=new JPanel(new BorderLayout(6,0));		
		gamesPanel.setBorder(INPUT_BORDER);
		gamesPanel.setOpaque(false);	
		gamesTextField=new JTextField(String.valueOf(config.getStrengthGames()));
		final Integer levels[]=new Integer[MagicAI.MAX_LEVEL];
		for (int level=1;level<=levels.length;level++) {
			levels[level-1]=level;
		}
		gamesPanel.add(new JLabel(IconImages.TROPHY),BorderLayout.WEST);
		gamesPanel.add(gamesTextField,BorderLayout.CENTER);

		final JPanel difficultyPanel=new JPanel(new BorderLayout(6,0));
		difficultyPanel.setBorder(INPUT_BORDER);
		difficultyPanel.setOpaque(false);
		final ComboBoxModel difficultyModel=new DefaultComboBoxModel(levels);
		difficultyComboBox=new JComboBox(difficultyModel);
		difficultyComboBox.setSelectedItem(Integer.valueOf(config.getStrengthDifficulty()));
		difficultyComboBox.setFocusable(false);
		difficultyPanel.add(new JLabel(IconImages.DIFFICULTY),BorderLayout.WEST);
		difficultyPanel.add(difficultyComboBox,BorderLayout.CENTER);

		final JPanel settingsPanel=new JPanel(new GridLayout(1,2));
		settingsPanel.setOpaque(false);
		settingsPanel.add(gamesPanel);
		settingsPanel.add(difficultyPanel);

		topPanel.add(purposeLabel,BorderLayout.CENTER);
		topPanel.add(settingsPanel,BorderLayout.SOUTH);
		mainPanel.add(topPanel,BorderLayout.NORTH);
		
		final JPanel centerPanel=new JPanel(new BorderLayout(4,0));
		centerPanel.setOpaque(false);
		centerPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
		mainPanel.add(centerPanel,BorderLayout.CENTER);

		gameLabel=new JLabel("");
		gameLabel.setFont(FontsAndBorders.FONT2);
		gameLabel.setForeground(textColor);
		gameLabel.setHorizontalAlignment(JLabel.CENTER);
		gameLabel.setPreferredSize(new Dimension(75,0));
		centerPanel.add(gameLabel,BorderLayout.WEST);
		
		strengthLabel=new JLabel("0 %");
		strengthLabel.setForeground(textColor);
		strengthLabel.setHorizontalAlignment(JLabel.CENTER);
		strengthLabel.setFont(FontsAndBorders.FONT5);
		centerPanel.add(strengthLabel,BorderLayout.CENTER);
		
		startButton=new JButton(IconImages.START);
		startButton.setFocusable(false);
		startButton.setPreferredSize(new Dimension(75,0));
		startButton.addActionListener(this);
		centerPanel.add(startButton,BorderLayout.EAST);
		
		progressBar=new JProgressBar();
		progressBar.setMinimum(0);
		mainPanel.add(progressBar,BorderLayout.SOUTH);
	}
	
	public void halt() {
		
		if (calculateThread!=null) {
			calculateThread.halt();
			calculateThread=null;
		}
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {

		if (calculateThread!=null) {
			startButton.setEnabled(false);
			startButton.repaint();
			halt();
		} else {
			final GeneralConfig generalConfig=GeneralConfig.getInstance();
			generalConfig.setStrengthDifficulty((Integer)difficultyComboBox.getSelectedItem());
			try {
				final int games=Integer.parseInt(gamesTextField.getText());
				if (games >0 && games < 1000) {
					generalConfig.setStrengthGames(games);
				} else {
					gamesTextField.setText(String.valueOf(generalConfig.getStrengthGames()));
				}
			} catch (final NumberFormatException ex) {
				gamesTextField.setText(String.valueOf(generalConfig.getStrengthGames()));
			}
			startButton.setIcon(IconImages.STOP);
			startButton.repaint();
			calculateThread=new CalculateThread();
			calculateThread.start();
		}
	}
	
	private void setStrength(final int percentage) {

		strengthLabel.setText(percentage+" %");
		if (percentage>=60) {
			strengthLabel.setForeground(HIGH_COLOR);
		} else if (percentage>=30) {
			strengthLabel.setForeground(MEDIUM_COLOR);
		} else if (percentage>0) {
			strengthLabel.setForeground(LOW_COLOR);
		} else {
			strengthLabel.setForeground(textColor);
		}
		strengthLabel.repaint();
	}
	
	private class CalculateThread extends Thread {

		private final AtomicBoolean running=new AtomicBoolean(true);
		private GameController controller=null;
		
		public void run() {

			final GeneralConfig generalConfig=GeneralConfig.getInstance();
			final TournamentConfig config=new TournamentConfig(TournamentConfig.getInstance());
			config.setNrOfGames(generalConfig.getStrengthGames());
			final MagicTournament testTournament=new MagicTournament(config,tournament);
			testTournament.setDifficulty(generalConfig.getStrengthDifficulty());
			testTournament.setAIs(DEFAULT_AIS);
			progressBar.setMaximum(testTournament.getGamesTotal());
			progressBar.setValue(0);
			setStrength(0);

			while (running.get()&&!testTournament.isFinished()) {
			
				gameLabel.setText("Game "+(testTournament.getGamesPlayed()+1));
				final MagicGame game=testTournament.nextGame(false);
				controller=new GameController(null,game);
				controller.runGame();
				progressBar.setValue(testTournament.getGamesPlayed());
				if (testTournament.getGamesPlayed()>0) {
					final int percentage=(testTournament.getGamesWon()*100)/testTournament.getGamesPlayed();
					setStrength(percentage);
				}
			}

			startButton.setIcon(IconImages.START);
			startButton.setEnabled(true);
			startButton.repaint();
			calculateThread=null;
		}
		
		public void halt() {
			
			running.set(false);
			if (controller!=null) {
				controller.haltGame();
			}
		}
	}
}
