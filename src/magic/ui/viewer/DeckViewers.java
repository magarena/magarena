package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerDefinition;
import magic.ui.MagicFrame;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;

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
    
    private void updateTitle() {
        
        final String deckName=spellViewer.getDeckName();
        titleBar.setText(tabSelector.getSelectedTab()==0?deckName+" : Spells":deckName+" : Lands");
    }
    
    public void update() {
        
        spellViewer.update();
        landViewer.update();
        updateTitle();
    }
    
    public void updateAfterEdit() {
        
        spellViewer.updateAfterEdit();
        landViewer.update();
        updateTitle();
    }

    @Override
    public void stateChanged(final ChangeEvent event) {

        final Object source=event.getSource();
        if (source instanceof MagicPlayerDefinition) {
            final MagicPlayerDefinition player=(MagicPlayerDefinition)source;
            spellViewer.changePlayer(player);
            landViewer.changePlayer(player);
        } else {
            final int selectedTab=tabSelector.getSelectedTab();            
            cardLayout.show(cardPanel,Integer.toString(selectedTab));            
        }
        updateTitle();
    }
}
