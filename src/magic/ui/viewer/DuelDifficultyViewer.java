package magic.ui.viewer;

import magic.data.CardImagesProvider;
import magic.data.IconImages;
import magic.model.MagicDuel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

public class DuelDifficultyViewer extends JPanel implements ChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	public static final Dimension PREFERRED_SIZE = new Dimension(CardImagesProvider.CARD_WIDTH, 120);

	private final DuelViewer duelViewer;
	private final DifficultyViewer difficultyViewer;
	private final JPanel cardPanel;
	private final CardLayout cardLayout;
	private final TitleBar titleBar;
	private final TabSelector tabSelector;
	
	public DuelDifficultyViewer(final MagicDuel duel) {

		duelViewer=new DuelViewer(duel);
		difficultyViewer=new DifficultyViewer();

		setPreferredSize(PREFERRED_SIZE);
		setBorder(FontsAndBorders.UP_BORDER);
		
		setLayout(new BorderLayout());
		titleBar=new TitleBar("");
		add(titleBar,BorderLayout.NORTH);
		
		cardLayout=new CardLayout();
		cardPanel=new JPanel(cardLayout);
		cardPanel.add(duelViewer,"0");
		cardPanel.add(difficultyViewer,"1");
		add(cardPanel,BorderLayout.CENTER);

		tabSelector=new TabSelector(this,false);
		tabSelector.addTab(IconImages.PROGRESS,"Progress");
		tabSelector.addTab(IconImages.DIFFICULTY,"Difficulty");
		titleBar.add(tabSelector,BorderLayout.EAST);
	}

	private void update() {

		switch (tabSelector.getSelectedTab()) {
			case 0: 
				DuelViewer.setTitle(titleBar);
				break;
			case 1:
				DifficultyViewer.setTitle(titleBar);
				break;
		}		
	}
	
	@Override
	public void stateChanged(final ChangeEvent event) {
		final int selectedTab=tabSelector.getSelectedTab();
		cardLayout.show(cardPanel,Integer.toString(selectedTab));
		update();
	}
}
