package magic.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import magic.data.CubeDefinitions;
import magic.data.TournamentConfig;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicTournament;
import magic.ui.resolution.ResolutionProfileResult;
import magic.ui.resolution.ResolutionProfileType;
import magic.ui.resolution.ResolutionProfiles;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.DeckStatisticsViewer;
import magic.ui.viewer.DeckStrengthViewer;
import magic.ui.viewer.DeckViewers;
import magic.ui.viewer.PlayersViewer;
import magic.ui.viewer.TournamentDifficultyViewer;
import magic.ui.widget.ZoneBackgroundLabel;
import magic.ui.widget.FontsAndBorders;

public class TournamentPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private final MagicFrame frame;
	private final MagicTournament tournament;
	private final ZoneBackgroundLabel backgroundLabel;
	private final PlayersViewer playersViewer;
	private final CardViewer cardViewer;
	private final DeckViewers deckViewers;
	private final DeckStatisticsViewer statsViewer;
	private final DeckStrengthViewer strengthViewer;
	private final TournamentDifficultyViewer tournamentDifficultyViewer;
	private final JButton playButton;
	private final JButton newButton;

	public TournamentPanel(final MagicFrame frame,final MagicTournament tournament) {

		this.frame=frame;
		this.tournament=tournament;
		
		setLayout(null);
		
		cardViewer=new CardViewer("Card",false,true);
		add(cardViewer);
				
		statsViewer=new DeckStatisticsViewer();
		add(statsViewer);
		
		final MagicCubeDefinition cubeDefinition=CubeDefinitions.getInstance().getCubeDefinition(tournament.getConfiguration().getCube());
		deckViewers=new DeckViewers(frame,statsViewer,cardViewer,tournament.isEditable(),cubeDefinition);
		add(deckViewers);
		
		strengthViewer=new DeckStrengthViewer(tournament);
		//add(strengthViewer);
		
		playersViewer=new PlayersViewer(tournament);
		playersViewer.addChangeListener(deckViewers);
		playersViewer.addChangeListener(statsViewer);
		playersViewer.changePlayer(0);
		add(playersViewer);
		
		tournamentDifficultyViewer=new TournamentDifficultyViewer(tournament);
		add(tournamentDifficultyViewer);

		playButton=new JButton("PLAY GAME");
		playButton.setFont(FontsAndBorders.FONT4);
		playButton.addActionListener(this);
		playButton.setFocusable(false);
		playButton.setEnabled(!tournament.isFinished());
		add(playButton);
				
		newButton=new JButton("NEW");
		newButton.setFont(FontsAndBorders.FONT4);
		newButton.addActionListener(this);
		newButton.setFocusable(false);
		add(newButton);
		
		backgroundLabel=new ZoneBackgroundLabel();
		backgroundLabel.setBounds(0,0,0,0);
		add(backgroundLabel);
				
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				resizeComponents();
			}
		});				
	}

	public MagicFrame getFrame() {
		return frame;
	}
	
	public MagicTournament getTournament() {
		return tournament;
	}
	
	private void resizeComponents() {
		final Dimension size=getSize();
		final ResolutionProfileResult result=ResolutionProfiles.calculate(size);

		backgroundLabel.setSize(size);
		playersViewer.setBounds(result.getBoundary(ResolutionProfileType.TournamentPlayersViewer));
		playersViewer.revalidate();
		deckViewers.setBounds(result.getBoundary(ResolutionProfileType.TournamentDeckViewers));
		deckViewers.update();
		cardViewer.setBounds(result.getBoundary(ResolutionProfileType.TournamentCardViewer));
		statsViewer.setBounds(result.getBoundary(ResolutionProfileType.TournamentDeckStatisticsViewer));
		strengthViewer.setBounds(result.getBoundary(ResolutionProfileType.TournamentDeckStrengthViewer));
		tournamentDifficultyViewer.setBounds(result.getBoundary(ResolutionProfileType.TournamentDifficultyViewer));
		playButton.setBounds(result.getBoundary(ResolutionProfileType.TournamentPlayButton));
		newButton.setBounds(result.getBoundary(ResolutionProfileType.TournamentNewButton));
	}
	
	public MagicPlayerDefinition getSelectedPlayer() {
		return deckViewers.getSpellViewer().getPlayer();
	}
	
	public void updateDecksAfterEdit() {
		deckViewers.updateAfterEdit();
	}

	public void haltStrengthViewer() {
		strengthViewer.halt();
	}
	
	public void actionPerformed(final ActionEvent event) {
		final Object source=event.getSource();
		if (source==playButton) {
			frame.nextGame();
		} else if (source==newButton) {
			final TournamentConfig config=TournamentConfig.getInstance();
			frame.newTournament(config);
		}
	}
}
