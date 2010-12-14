package magic.ui.viewer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import magic.data.IconImages;
import magic.ui.GameController;
import magic.ui.widget.TabSelector;

public class ImageHandGraveyardExileViewer extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private final ViewerInfo viewerInfo;
	private final TabSelector tabSelector;
	private final ImageCardListViewer cardListViewer;
	
	public ImageHandGraveyardExileViewer(final ViewerInfo viewerInfo,final GameController controller) {
		
		this.viewerInfo=viewerInfo;
		
		setOpaque(false);
		setLayout(new BorderLayout(6,0));
		
		final String playerName=viewerInfo.getPlayerInfo(false).name;
		final String opponentName=viewerInfo.getPlayerInfo(true).name;
		
		tabSelector=new TabSelector(this,true);
		tabSelector.addTab(IconImages.HAND,"Hand : "+playerName);
		tabSelector.addTab(IconImages.GRAVEYARD,"Graveyard : "+playerName);
		tabSelector.addTab(IconImages.GRAVEYARD,"Graveyard : "+opponentName);
		tabSelector.addTab(IconImages.EXILE,"Exile : "+playerName);
		tabSelector.addTab(IconImages.EXILE,"Exile : "+opponentName);
		add(tabSelector,BorderLayout.WEST);
		
		cardListViewer=new ImageCardListViewer(controller);
		add(cardListViewer,BorderLayout.CENTER);
	}
	
	public void setSelectedTab(final int selectedTab) {

		if (selectedTab>=0) {
			tabSelector.setSelectedTab(selectedTab);
		}
	}
	
	public void update() {

		if (cardListViewer!=null) {
			switch (tabSelector.getSelectedTab()) {
				case 0: cardListViewer.setCardList(viewerInfo.getPlayerInfo(false).hand); break;
				case 1: cardListViewer.setCardList(viewerInfo.getPlayerInfo(false).graveyard); break;
				case 2: cardListViewer.setCardList(viewerInfo.getPlayerInfo(true).graveyard); break;
				case 3: cardListViewer.setCardList(viewerInfo.getPlayerInfo(false).exile); break;
				case 4: cardListViewer.setCardList(viewerInfo.getPlayerInfo(true).exile); break;
			}
			repaint();
		}
	}
		
	@Override
	public void stateChanged(final ChangeEvent event) {
		
		update();
	}
}