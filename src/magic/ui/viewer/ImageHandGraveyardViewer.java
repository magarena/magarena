package magic.ui.viewer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import magic.data.IconImages;
import magic.ui.GameController;
import magic.ui.widget.TabSelector;

public class ImageHandGraveyardViewer extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private final ViewerInfo viewerInfo;
	private final TabSelector tabSelector;
	private final ImageCardListViewer cardListViewer;
	
	public ImageHandGraveyardViewer(final ViewerInfo viewerInfo,final GameController controller) {
		
		this.viewerInfo=viewerInfo;
		
		setOpaque(false);
		setLayout(new BorderLayout(6,0));
		
		tabSelector=new TabSelector(this,true);
		tabSelector.addTab(IconImages.HAND);
		tabSelector.addTab(IconImages.GRAVEYARD);
		tabSelector.addTab(IconImages.GRAVEYARD);		
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
			}
			repaint();
		}
	}
		
	@Override
	public void stateChanged(final ChangeEvent event) {
		
		update();
	}
}