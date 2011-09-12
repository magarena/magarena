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
import javax.swing.SpringLayout;


public class ExplorerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	public static final int ALL = 0;
	public static final int LAND = 1;
	public static final int SPELL = 2;	
	
	private static final String CLOSE_BUTTON_TEXT = "Close";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";
	private static final String OK_BUTTON_TEXT = "Use Selected Card";
	private static final Dimension CARD_DIMENSION = new Dimension(CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
 	private static final int SPACING=10;
	
 	private final MagicFrame frame;
	private final EditDeckCard editDeckCard;
	private final CardTable cardPoolTable;
 	private final ZoneBackgroundLabel backgroundImage;
	private final CardViewer cardViewer;
	private final ExplorerFilterPanel filterPanel;
	private final JButton closeButton;
	private final JButton okButton;
	private List<MagicCardDefinition> cardDefinitions;
	
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
		cardDefinitions = filterPanel.getCardDefinitions();
		cardPoolTable = new CardTable(cardDefinitions, cardViewer);
		
		final JScrollPane cardsScrollPane = new JScrollPane(cardPoolTable);
		cardsScrollPane.setBorder(null);
		cardsScrollPane.setOpaque(false);
		cardsScrollPane.getViewport().setOpaque(false);
		add(cardsScrollPane);		
		
		// close button
		closeButton = (isEditingDeck())? new JButton(CANCEL_BUTTON_TEXT) : new JButton(CLOSE_BUTTON_TEXT);
		closeButton.setFocusable(false);
		closeButton.addActionListener(this);
		
		add(closeButton);
		
		// create OK button for deck editing
		if (isEditingDeck()) {
			okButton = new JButton(OK_BUTTON_TEXT);
			okButton.setFocusable(false);
			okButton.addActionListener(this);
			add(okButton);
		} else {
			okButton = null;
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
        springLayout.putConstraint(SpringLayout.WEST, cardsScrollPane,
                             0, SpringLayout.WEST, filterScrollPane);
        springLayout.putConstraint(SpringLayout.NORTH, cardsScrollPane,
                             SPACING, SpringLayout.SOUTH, filterScrollPane);
		
		// card pool's gap (right bottom)
        springLayout.putConstraint(SpringLayout.EAST, cardsScrollPane,
                             -SPACING, SpringLayout.EAST, backgroundImage);
        springLayout.putConstraint(SpringLayout.SOUTH, cardsScrollPane,
                             -SPACING, SpringLayout.SOUTH, backgroundImage);
							 
		// close button's gap (top right)
        springLayout.putConstraint(SpringLayout.EAST, closeButton,
                             0, SpringLayout.EAST, cardViewer);
        springLayout.putConstraint(SpringLayout.NORTH, closeButton,
                             SPACING, SpringLayout.SOUTH, cardViewer);
		
		if(isEditingDeck()) {
			springLayout.putConstraint(SpringLayout.EAST, okButton,
                             -SPACING, SpringLayout.WEST, closeButton);
			springLayout.putConstraint(SpringLayout.NORTH, okButton,
                             0, SpringLayout.NORTH, closeButton);
		}
		
		// set initial card image
		if (cardDefinitions.isEmpty()) {
			cardViewer.setCard(MagicCardDefinition.UNKNOWN,0);
 		} else {
 			cardViewer.setCard(cardDefinitions.get(0),0);
 		}
	}
	
	private boolean isEditingDeck() {
		return editDeckCard != null;
	}
	
	public void updateCardPool() {
		cardPoolTable.setCards(filterPanel.getCardDefinitions());
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
	
		final Object source=event.getSource();
		
		if (source == closeButton) {
			frame.closeCardExplorer();
		} else if (source == okButton && isEditingDeck()) {
			MagicCardDefinition card = cardPoolTable.getSelectedCard();
			if (card != null) {
				editDeckCard.getPlayer().getDeck().remove(editDeckCard.getCard());
				editDeckCard.getPlayer().getDeck().add(card);
				editDeckCard.getDeckViewer().updateAfterEdit();						
				frame.closeCardExplorer();
			}
		}
	}
}
