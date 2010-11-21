package magic.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLayeredPane;

import magic.ui.viewer.CardViewer;
import magic.ui.viewer.LogBookViewer;
import magic.ui.widget.BackgroundLabel;

public class GameLayeredPane extends JLayeredPane {
	
	private static final long serialVersionUID = 1L;

	private final BackgroundLabel backgroundLabel;

	public GameLayeredPane(final GamePanel gamePanel) {
		
		setLayout(null);
		
		backgroundLabel=new BackgroundLabel();
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

				backgroundLabel.setSize(getSize());
				gamePanel.setSize(getSize());
				gamePanel.resizeComponents();
				repaint();
			}
		});			
	}	
}