package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicTournament;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class TournamentDifficultyViewer extends JPanel implements ChangeListener {
	
	private static final long serialVersionUID = 1L;

	private final TournamentViewer tournamentViewer;
	private final DifficultyViewer difficultyViewer;
	private final JPanel cardPanel;
	private final CardLayout cardLayout;
	private final TitleBar titleBar;
	private final TabSelector tabSelector;
	
	public TournamentDifficultyViewer(final MagicTournament tournament) {

		tournamentViewer=new TournamentViewer(tournament);
		difficultyViewer=new DifficultyViewer();

		setLayout(new BorderLayout());
		titleBar=new TitleBar("");
		add(titleBar,BorderLayout.NORTH);
		
		cardLayout=new CardLayout();
		cardPanel=new JPanel(cardLayout);
		cardPanel.add(tournamentViewer,"0");
		cardPanel.add(difficultyViewer,"1");
		add(cardPanel,BorderLayout.CENTER);

		tabSelector=new TabSelector(this,false);
		tabSelector.addTab(IconImages.PROGRESS,"Progress");
		tabSelector.addTab(IconImages.DIFFICULTY,"Difficulty");
		titleBar.add(tabSelector,BorderLayout.EAST);
	}

	public void update() {

		switch (tabSelector.getSelectedTab()) {
			case 0: 
				tournamentViewer.setTitle(titleBar);
				break;
			case 1:
				difficultyViewer.setTitle(titleBar);
				break;
		}		
	}
	
	@Override
	public void stateChanged(final ChangeEvent event) {

		final int selectedTab=tabSelector.getSelectedTab();
		cardLayout.show(cardPanel,""+selectedTab);
		update();
	}
}