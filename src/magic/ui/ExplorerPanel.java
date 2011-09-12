package magic.ui;

import magic.data.CardImagesProvider;
import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerProfile;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.ZoneBackgroundLabel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;


public class ExplorerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	public static final int ALL = 0;
	public static final int LAND = 1;
	public static final int SPELL = 2;	
	
	private static final String CLOSE_BUTTON_TEXT = "Close";
	private static final String SWAP_BUTTON_TEXT = "Swap Selected Cards";
	private static final Dimension CARD_DIMENSION = new Dimension(CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
 	private static final int SPACING=10;
	
 	private final MagicFrame frame;
	private final EditDeckCard editDeckCard;
	
	private final CardTable cardPoolTable;
	private final CardTable deckTable;
 	private final ZoneBackgroundLabel backgroundImage;
	private final CardViewer cardViewer;
	private final ExplorerFilterPanel filterPanel;
	private final JButton closeButton;
	private final JButton swapButton;
	
	private List<MagicCardDefinition> cardPoolDefs;
	private List<MagicCardDefinition> deckDefs;
	
	public ExplorerPanel(final MagicFrame frame,final int mode,final EditDeckCard editDeckCard) {
		this.frame=frame;
		this.editDeckCard=editDeckCard;
		
		final SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		// card image
		cardViewer = new CardViewer("",false,true);
		cardViewer.setPreferredSize(CARD_DIMENSION);
		cardViewer.setMaximumSize(CARD_DIMENSION);
		cardViewer.setCard(MagicCardDefinition.UNKNOWN,0);
		add(cardViewer);
		
		// filters
		MagicPlayerProfile profile=null;
		MagicCubeDefinition cube=null;
		if (isEditingDeck()) {
			profile=editDeckCard.getPlayer().getProfile();
			cube=editDeckCard.getCube();
		}
		filterPanel = new ExplorerFilterPanel(this, mode, profile, cube);
		
		final JScrollPane filterScrollPane = new JScrollPane(filterPanel);
		filterScrollPane.setBorder(null);
		filterScrollPane.setOpaque(false);
		filterScrollPane.getViewport().setOpaque(false);
		add(filterScrollPane);
		
		// card pool
		cardPoolDefs = filterPanel.getCardDefinitions();
		cardPoolTable = new CardTable(cardPoolDefs, cardViewer);

		// deck
		final Container cardsPanel; // reference panel holding both card pool and deck
		
		if (isEditingDeck()) {
			deckDefs = editDeckCard.getPlayer().getDeck();
			deckTable = new CardTable(deckDefs, cardViewer);
			
			JSplitPane cardsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			cardsSplitPane.setOneTouchExpandable(true);
			cardsSplitPane.setLeftComponent(cardPoolTable);
			cardsSplitPane.setRightComponent(deckTable);
			cardsSplitPane.setResizeWeight(0.7);
			
			add(cardsSplitPane);
			cardsPanel = cardsSplitPane;
		} else {
			// no deck
			deckDefs = null;
			deckTable = null;
			
			add(cardPoolTable);
			cardsPanel = cardPoolTable;
		}
		
		// close button
		closeButton = new JButton(CLOSE_BUTTON_TEXT);
		closeButton.setFocusable(false);
		closeButton.addActionListener(this);
		
		add(closeButton);
		
		// create OK button for deck editing
		if (isEditingDeck()) {
			swapButton = new JButton(SWAP_BUTTON_TEXT);
			swapButton.setFocusable(false);
			swapButton.addActionListener(this);
			add(swapButton);
		} else {
			swapButton = null;
		}
		
		// background - must be added last or anything else
		// will be hidden behind it
		backgroundImage = new ZoneBackgroundLabel();
		add(backgroundImage);
		
		// set sizes by defining gaps between components
		Container contentPane = this;
		
		// background's gaps with top left bottom and right are 0
		// (i.e., it fills the window)
        springLayout.putConstraint(SpringLayout.WEST, backgroundImage,
                             0, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.NORTH, backgroundImage,
                             0, SpringLayout.NORTH, contentPane);
        springLayout.putConstraint(SpringLayout.EAST, backgroundImage,
                             0, SpringLayout.EAST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, backgroundImage,
                             0, SpringLayout.SOUTH, contentPane);		
		
		// card image's gap (top left)
        springLayout.putConstraint(SpringLayout.WEST, cardViewer,
                             SPACING, SpringLayout.WEST, backgroundImage);
        springLayout.putConstraint(SpringLayout.NORTH, cardViewer,
                             SPACING, SpringLayout.NORTH, backgroundImage);
				
        // gap between card image and filter
        springLayout.putConstraint(SpringLayout.WEST, filterScrollPane,
                             SPACING, SpringLayout.EAST, cardViewer);
							 
		// filter panel's gaps (top right)
        springLayout.putConstraint(SpringLayout.NORTH, filterScrollPane,
                             0, SpringLayout.NORTH, cardViewer);
        springLayout.putConstraint(SpringLayout.EAST, filterScrollPane,
                             -SPACING, SpringLayout.EAST, backgroundImage);
		
		// filter panel's gap with card pool
        springLayout.putConstraint(SpringLayout.WEST, cardsPanel,
                             0, SpringLayout.WEST, filterScrollPane);
        springLayout.putConstraint(SpringLayout.NORTH, cardsPanel,
                             SPACING, SpringLayout.SOUTH, filterScrollPane);
		
		// card pool's gap (right)
		springLayout.putConstraint(SpringLayout.EAST, cardsPanel,
							 -SPACING, SpringLayout.EAST, backgroundImage);
			springLayout.putConstraint(SpringLayout.SOUTH, cardsPanel,
								 -SPACING, SpringLayout.SOUTH, backgroundImage);
							 
		// close button's gap (top right)
        springLayout.putConstraint(SpringLayout.EAST, closeButton,
                             0, SpringLayout.EAST, cardViewer);
        springLayout.putConstraint(SpringLayout.NORTH, closeButton,
                             SPACING, SpringLayout.SOUTH, cardViewer);
		
		if(isEditingDeck()) {
			springLayout.putConstraint(SpringLayout.EAST, swapButton,
                             -SPACING, SpringLayout.WEST, closeButton);
			springLayout.putConstraint(SpringLayout.NORTH, swapButton,
                             0, SpringLayout.NORTH, closeButton);
		}
		
		// set initial card image
		if (cardPoolDefs.isEmpty()) {
			cardViewer.setCard(MagicCardDefinition.UNKNOWN,0);
 		} else {
 			cardViewer.setCard(cardPoolDefs.get(0),0);
 		}
	}
	
	private boolean isEditingDeck() {
		return editDeckCard != null;
	}
	
	public void updateCardPool() {
		cardPoolDefs = filterPanel.getCardDefinitions();
		cardPoolTable.setCards(cardPoolDefs);
	}
	
	public void updateDeck() {
		deckDefs = editDeckCard.getPlayer().getDeck();
		deckTable.setCards(deckDefs);
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
	
		final Object source=event.getSource();
		
		if (source == closeButton) {
			frame.closeCardExplorer();
		} else if (source == swapButton && isEditingDeck()) {
			MagicCardDefinition cardPoolCard = cardPoolTable.getSelectedCard();
			MagicCardDefinition deckCard = deckTable.getSelectedCard();
			if (cardPoolCard != null && deckCard != null) {
				editDeckCard.getPlayer().getDeck().remove(deckCard);
				editDeckCard.getPlayer().getDeck().add(cardPoolCard);
				editDeckCard.getDeckViewer().updateAfterEdit();
				updateDeck();
			}
		}
	}
}
