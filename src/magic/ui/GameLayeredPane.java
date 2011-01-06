package magic.ui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLayeredPane;

import magic.ui.viewer.CardViewer;
import magic.ui.viewer.LogBookViewer;
import magic.ui.widget.BattlefieldBackgroundLabel;

public class GameLayeredPane extends JLayeredPane {
	
	private static final long serialVersionUID = 1L;

	public GameLayeredPane(final GamePanel gamePanel,final BattlefieldBackgroundLabel backgroundLabel) {
		
		setLayout(null);
		
		backgroundLabel.setLocation(0,0);
		add(backgroundLabel);
		setLayer(backgroundLabel,new Integer(0));
		
		gamePanel.setLocation(0,0);
		add(gamePanel);
		setLayer(gamePanel,new Integer(1));

		final CardViewer cardViewer=gamePanel.getImageCardViewer();
		add(cardViewer);
		setLayer(cardViewer,new Integer(2));
		
		final LogBookViewer logBookViewer=gamePanel.getLogBookViewer();
		add(logBookViewer);
		setLayer(logBookViewer,new Integer(3));
		
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(final ComponentEvent event) {

				final Dimension size=getSize();
				backgroundLabel.setSize(size);
				gamePanel.setSize(size);
				gamePanel.resizeComponents();
				repaint();
			}
		});			
	}	
}