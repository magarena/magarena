package magic.ui.viewer;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.CostPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public abstract class CardListViewer extends JPanel implements ChoiceViewer {
	
	private static final long serialVersionUID = 1L;

	private static final int LINE_HEIGHT=26;

	private final GameController controller;
	private final boolean graveyard;
	private final JScrollPane scrollPane;
	private final JPanel viewPanel;
    private final Collection<CardButton> buttons;	
	
	public CardListViewer(final GameController controller,final boolean graveyard) {

		this.controller=controller;
		this.graveyard=graveyard;
		
		controller.registerChoiceViewer(this);
		
		setOpaque(false);
		setLayout(new BorderLayout());
		
		scrollPane=new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setBlockIncrement(LINE_HEIGHT*2);
		scrollPane.getVerticalScrollBar().setUnitIncrement(LINE_HEIGHT*2);
		add(scrollPane,BorderLayout.CENTER);
		
		viewPanel=new JPanel();
		viewPanel.setOpaque(false);
		viewPanel.setLayout(new BorderLayout());
		scrollPane.getViewport().add(viewPanel);
		
		buttons=new ArrayList<CardButton>();
	}
			
	public void viewCard() {

		final MagicCardList cardList=getCardList();
		if (cardList.size()>0) {
			final MagicCard bottomCard=cardList.getCardAtBottom();
			controller.viewCard(bottomCard);
		}		
	}
		
	public void update() {

		final MagicCardList cardList=getCardList();
		
		final JPanel cardPanel=new JPanel();
		cardPanel.setBackground(ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_VIEWER_BACKGROUND));
		cardPanel.setBorder(FontsAndBorders.BLACK_BORDER);
		cardPanel.setLayout(new GridLayout(cardList.size(),1));

		buttons.clear();
		if (cardList.isEmpty()) {
			cardPanel.setPreferredSize(new Dimension(0,6));
		} else {
			for (final MagicCard card : cardList) {
				final CardButton button=new CardButton(card);
				buttons.add(button);
				cardPanel.add(button);
			}
		}
		viewPanel.removeAll();
		viewPanel.add(cardPanel,BorderLayout.NORTH);

		showValidChoices(controller.getValidChoices());
		revalidate();
		repaint();
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {
		for (final CardButton button : buttons) {
			button.showValidChoices(validChoices);
		}
	}	
	
	protected abstract String getTitle();
		
	protected abstract MagicCardList getCardList();	

	private class CardButton extends PanelButton implements ChoiceViewer {
		
		private static final long serialVersionUID = 1L;

		private final MagicCard card;
		private final JLabel nameLabel;
		
		public CardButton(final MagicCard card) {
	
			super();
			this.card=card;
			
			final JPanel mainPanel=new JPanel();
			mainPanel.setOpaque(false);
			mainPanel.setLayout(new BorderLayout());
			mainPanel.setPreferredSize(new Dimension(0,LINE_HEIGHT));
			setComponent(mainPanel);
			
			final MagicCardDefinition cardDefinition=card.getCardDefinition();

			final CostPanel costPanel=new CostPanel(graveyard||cardDefinition.isLand()?null:cardDefinition.getCost());
			
			nameLabel=new JLabel(cardDefinition.getName());
			nameLabel.setForeground(cardDefinition.getRarityColor());
			
			final JLabel typeLabel=new JLabel(cardDefinition.getIcon());
			typeLabel.setPreferredSize(new Dimension(24,0));

			mainPanel.add(costPanel,BorderLayout.WEST);
			mainPanel.add(nameLabel,BorderLayout.CENTER);
			mainPanel.add(typeLabel,BorderLayout.EAST);			
		}

		@Override
		public void mouseClicked() {
			
			controller.processClick(card);
		}

		@Override
		public void mouseEntered() {
			
			controller.viewCard(card);
		}

		@Override
		public void showValidChoices(final Set<Object> validChoices) {

			setValid(validChoices.contains(card));
		}

		@Override
		public Color getValidColor() {

			return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
		}			
	} 
}
