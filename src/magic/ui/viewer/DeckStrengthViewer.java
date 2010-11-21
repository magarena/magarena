package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import magic.data.IconImages;
import magic.data.TournamentConfig;
import magic.model.MagicGame;
import magic.model.MagicTournament;
import magic.ui.GameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;

public class DeckStrengthViewer extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String PURPOSE=
		"<html><body>"+
		"Calculates an indicative win percentage for your deck. " +
		"123 games are played against your opponent at a low difficulty level. "+
		"</body></html>";

	private static final Color HIGH_COLOR=new Color(0x23,0x8E,0x23);
	private static final Color MEDIUM_COLOR=new Color(0xFF,0x7F,0x00);
	private static final Color LOW_COLOR=new Color(0xEE,0x2C,0x2C);
	
	private final MagicTournament tournament;
	private final JProgressBar progressBar;
	private final JLabel gameLabel;
	private final JLabel strengthLabel;
	private final JButton startButton;
	private CalculateThread calculateThread=null;
	
	public DeckStrengthViewer(final MagicTournament tournament) {
		
		this.tournament=tournament;
		
		setLayout(new BorderLayout());
		
		final TitleBar titleBar=new TitleBar("Deck Strength");
		add(titleBar,BorderLayout.NORTH);		

		final JPanel mainPanel=new TexturedPanel();
		mainPanel.setLayout(new BorderLayout(0,4));
		mainPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
		add(mainPanel,BorderLayout.CENTER);
		
		final JLabel purposeLabel=new JLabel(PURPOSE);
		purposeLabel.setIcon(IconImages.STRENGTH);
		mainPanel.add(purposeLabel,BorderLayout.NORTH);
	
		final JPanel centerPanel=new JPanel(new BorderLayout(4,0));
		centerPanel.setOpaque(false);
		centerPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
		mainPanel.add(centerPanel,BorderLayout.CENTER);

		gameLabel=new JLabel("");
		gameLabel.setFont(FontsAndBorders.FONT2);
		gameLabel.setHorizontalAlignment(JLabel.CENTER);
		gameLabel.setPreferredSize(new Dimension(75,0));
		centerPanel.add(gameLabel,BorderLayout.WEST);
		
		strengthLabel=new JLabel("0 %");
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
			strengthLabel.setForeground(Color.BLACK);
		}
		strengthLabel.repaint();
	}
	
	private class CalculateThread extends Thread {

		private final AtomicBoolean running=new AtomicBoolean(true);
		private GameController controller=null;
		
		public void run() {

			final TournamentConfig config=new TournamentConfig(TournamentConfig.getInstance());
			config.setNrOfGames(123);
			final MagicTournament testTournament=new MagicTournament(config,tournament);
			testTournament.setDifficulty(2);
			progressBar.setMaximum(testTournament.getGamesTotal());
			progressBar.setValue(0);
			setStrength(0);

			while (running.get()&&!testTournament.isFinished()) {
			
				gameLabel.setText("Game "+(testTournament.getGamesPlayed()+1));
				final MagicGame game=testTournament.nextGame();
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