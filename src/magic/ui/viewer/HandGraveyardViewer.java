package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import magic.data.IconImages;
import magic.ui.GameController;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

public class HandGraveyardViewer extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private final HandViewer handViewer;
	private final GraveyardViewer playerGraveyardViewer;
	private final GraveyardViewer opponentGraveyardViewer;
	private final JPanel cardPanel;
	private final CardLayout cardLayout;
	private final TitleBar titleBar;
	private final TabSelector tabSelector;

	public HandGraveyardViewer(final ViewerInfo viewerInfo,final GameController controller) {
				
		handViewer=new HandViewer(viewerInfo,controller);
		playerGraveyardViewer=new GraveyardViewer(viewerInfo,controller,false);		
		opponentGraveyardViewer=new GraveyardViewer(viewerInfo,controller,true);
		
		setOpaque(false);
		setLayout(new BorderLayout());
		
		titleBar=new TitleBar("");
		add(titleBar,BorderLayout.NORTH);
						
		cardLayout=new CardLayout();
		cardPanel=new JPanel(cardLayout);
		cardPanel.setOpaque(false);
		cardPanel.add(handViewer,"0");
		cardPanel.add(playerGraveyardViewer,"1");
		cardPanel.add(opponentGraveyardViewer,"2");
		add(cardPanel,BorderLayout.CENTER);
		
		tabSelector=new TabSelector(this,false);
		tabSelector.addTab(IconImages.HAND);
		tabSelector.addTab(IconImages.GRAVEYARD);
		tabSelector.addTab(IconImages.GRAVEYARD);		
		titleBar.add(tabSelector,BorderLayout.EAST);
		
		handViewer.viewCard();
	}
	
	public void update() {
		
		handViewer.update();
		playerGraveyardViewer.update();
		opponentGraveyardViewer.update();
	}
	
	public void setSelectedTab(final int selectedTab) {
		
		if (selectedTab>=0) {
			tabSelector.setSelectedTab(selectedTab);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {

		final int selectedTab=tabSelector.getSelectedTab();
		cardLayout.show(cardPanel,""+selectedTab);
		switch (selectedTab) {
			case 0: 
				titleBar.setText(handViewer.getTitle());
				break;
			case 1:
				titleBar.setText(playerGraveyardViewer.getTitle());
				break;
			case 2:
				titleBar.setText(opponentGraveyardViewer.getTitle());
				break;
		}
	}
}