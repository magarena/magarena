package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import magic.model.MagicPlayerDefinition;
import magic.model.MagicTournament;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PlayerAvatarPanel;

public class PlayersViewer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final MagicTournament tournament;
	private final List<ChangeListener> changeListeners;
	private PlayerAvatarPanel playerPanels[];
	
	public PlayersViewer(final MagicTournament tournament) {
		
		this.tournament=tournament;
		changeListeners=new ArrayList<ChangeListener>();
		createViewer();
	}
	
	public void addChangeListener(final ChangeListener listener) {
		
		changeListeners.add(listener);
	}
	
	public void changePlayer(final int index) {

		for (final PlayerAvatarPanel panel : playerPanels) {
			
			panel.setSelected(false);
		}
		playerPanels[index].setSelected(true);
		
		final MagicPlayerDefinition player=tournament.getPlayer(index);
		final ChangeEvent event=new ChangeEvent(player);
		for (final ChangeListener listener : changeListeners) {
			
			listener.stateChanged(event);
		}
	}
	
	private void createViewer() {

		setLayout(new BorderLayout());
		setOpaque(false);
				
		final JScrollPane scrollPane=new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.getVerticalScrollBar().setBlockIncrement(100);
		add(scrollPane,BorderLayout.CENTER);
		
		final MagicPlayerDefinition players[]=tournament.getPlayers();
		final JPanel scrollPanel=new JPanel();
		scrollPanel.setOpaque(false);
		scrollPanel.setLayout(new BorderLayout());
		scrollPane.getViewport().add(scrollPanel);

		final JPanel topPanel=new JPanel(new BorderLayout());
		topPanel.setOpaque(false);
		scrollPanel.add(topPanel,BorderLayout.NORTH);
		
		final JPanel playersPanel=new JPanel();
		playersPanel.setOpaque(false);
		playersPanel.setLayout(new GridLayout(players.length,1));
		playersPanel.setBorder(FontsAndBorders.BLACK_BORDER);
		topPanel.add(playersPanel,BorderLayout.WEST);
		
		final MouseListener playerListener=new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent event) {

				final PlayerAvatarPanel panel=(PlayerAvatarPanel)event.getSource();
				panel.setSelected(true);
				changePlayer(panel.getIndex());
			}
		};
		
		playerPanels=new PlayerAvatarPanel[tournament.getNrOfPlayers()];
		for (int index=0;index<playerPanels.length;index++) {
			
			playerPanels[index]=new PlayerAvatarPanel(index);
			playerPanels[index].setPlayerDefinition(tournament.getPlayer(index));
			playerPanels[index].addMouseListener(playerListener);
			playersPanel.add(playerPanels[index]);
		}
	}
}