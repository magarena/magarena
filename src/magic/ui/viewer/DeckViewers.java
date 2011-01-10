package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import magic.data.IconImages;
import magic.model.MagicCubeDefinition;
import magic.ui.MagicFrame;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

public class DeckViewers extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;
	
	private final DeckViewer spellViewer;
	private final DeckViewer landViewer;
	private final TitleBar titleBar;
	private final JPanel cardPanel;
	private final CardLayout cardLayout;
	private final TabSelector tabSelector;
	
	public DeckViewers(final MagicFrame frame,final DeckStatisticsViewer statisticsViewer,
			final CardViewer cardViewer,final boolean edit,final MagicCubeDefinition cubeDefinition) {

		setLayout(new BorderLayout());
		setOpaque(false);
		
		titleBar=new TitleBar("");
		add(titleBar,BorderLayout.NORTH);
		
		spellViewer=new DeckViewer(frame,statisticsViewer,cardViewer,false,edit,cubeDefinition);
		landViewer=new DeckViewer(frame,statisticsViewer,cardViewer,true,edit,cubeDefinition);

		cardLayout=new CardLayout();
		cardPanel=new JPanel(cardLayout);
		cardPanel.setOpaque(false);
		cardPanel.add(spellViewer,"0");
		cardPanel.add(landViewer,"1");
		add(cardPanel,BorderLayout.CENTER);
		
		tabSelector=new TabSelector(this,false);
		tabSelector.addTab(IconImages.SPELL,"Spells");
		tabSelector.addTab(IconImages.LAND,"Lands");
		titleBar.add(tabSelector,BorderLayout.EAST);
	}
	
	public DeckViewer getSpellViewer() {
		
		return spellViewer;
	}
	
	public DeckViewer getLandViewer() {
		
		return landViewer;
	}
	
	public void update() {
		
		spellViewer.update();
		landViewer.update();
	}
	
	public void updateAfterEdit() {
		
		spellViewer.updateAfterEdit();
		landViewer.update();
	}

	@Override
	public void stateChanged(final ChangeEvent event) {

		final int selectedTab=tabSelector.getSelectedTab();
		titleBar.setText(selectedTab==0?"Deck : Spells":"Deck : Lands");
		cardLayout.show(cardPanel,""+selectedTab);
	}
}