package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicDeckCard;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.ui.EditDeckCard;
import magic.ui.MagicFrame;
import magic.ui.widget.CostPanel;
import magic.ui.widget.FontsAndBorders;

public class DeckViewer extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;
	
	private static final int LINE_HEIGHT=28;
	
	private final List<DeckEntry> entries;
	private final JScrollPane scrollPane;
	private final JPanel viewPanel;
	private final MagicFrame frame;
	private final DeckStatisticsViewer statisticsViewer;
	private final CardViewer cardViewer;
	private final boolean lands;
	private final boolean edit;
	private MagicPlayerDefinition player;
	private Font nameFont=FontsAndBorders.FONT1;
	
	public DeckViewer(final MagicFrame frame,final DeckStatisticsViewer statisticsViewer,final CardViewer cardViewer,final boolean lands,final boolean edit) {
		
		this.frame=frame;
		this.statisticsViewer=statisticsViewer;
		this.cardViewer=cardViewer;		
		this.lands=lands;
		this.edit=edit;
		entries=new ArrayList<DeckEntry>();

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
	}
	
	private void setCardImage(final DeckEntry entry) {

		cardViewer.setCard(entry.card,0);
	}

	public void setNameFont(final Font nameFont) {
		
		this.nameFont=nameFont;
	}
	
	public void setPlayer(final MagicPlayerDefinition player) {
		
		this.player=player;
	}
	
	public MagicPlayerDefinition getPlayer() {
		
		return player;
	}
	
	public void update() {
		
		final Map<MagicCardDefinition,DeckEntry> entriesMap=new HashMap<MagicCardDefinition,DeckEntry>();
		for (final MagicDeckCard deckCard : player.getDraftedDeck()) {

			final MagicCardDefinition card=deckCard.getCardDefinition();
			if (card.isLand()==lands) {
				DeckEntry entry=entriesMap.get(card);
				if (entry==null) {
					entry=new DeckEntry(deckCard);
					entriesMap.put(card,entry);
				} else {
					entry.count++;
				}
			}
		}

		entries.clear();
		entries.addAll(entriesMap.values());
		Collections.sort(entries);
		
		final JPanel cardPanel=new JPanel();
		cardPanel.setBorder(FontsAndBorders.BLACK_BORDER);
		cardPanel.setLayout(new GridLayout(entries.size(),1));		
		boolean light=true;
		for (final DeckEntry entry : entries) {
			
			entry.build(light,edit);
			cardPanel.add(entry,light);
			light=!light;
		}

		viewPanel.removeAll();
		viewPanel.add(cardPanel,BorderLayout.NORTH);
		
		if (entries.size()>0) {
			setCardImage(entries.get(0));
		}
		
		revalidate();
		repaint();
	}
	
	public void updateAfterEdit() {
		
		statisticsViewer.setPlayer(player);
		update();
	}
	
	@Override
	public void stateChanged(ChangeEvent event) {

		setPlayer((MagicPlayerDefinition)event.getSource());
		update();
	}

	private static class ColorPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public ColorPanel(final MagicPlayerDefinition player,final MagicCardDefinition card) {
			
			setOpaque(false);
			setLayout(new GridLayout(1,3));
			final JLabel colorLabels[]=new JLabel[3];
			for (int i=0;i<colorLabels.length;i++) {
				
				colorLabels[i]=new JLabel();
				colorLabels[i].setPreferredSize(new Dimension(19,0));
				colorLabels[i].setHorizontalAlignment(JLabel.CENTER);
				add(colorLabels[i]);
			}
			final MagicPlayerProfile profile=player.getProfile();
			int cindex=0;
			for (final MagicColor color : profile.getColors()) {

				if (color.hasColor(card.getColorFlags())||(card.isLand()&&card.getManaSource(color)>0)) {
					colorLabels[cindex].setIcon(color.getManaType().getIcon(true));
				}
				cindex++;
			}
		}
	}
		
	private class DeckEntry extends JPanel implements Comparable<DeckEntry>,ActionListener {
		
		private static final long serialVersionUID = 1L;
		
		private final MagicDeckCard deckCard;
		private final MagicCardDefinition card;
		int count;
		
		public DeckEntry(final MagicDeckCard deckCard) {

			this.deckCard=deckCard;
			card=deckCard.getCardDefinition();
			count=1;			
		}

		@Override
		public int compareTo(final DeckEntry entry) {

			boolean basic1=card.isBasic();
			boolean basic2=entry.card.isBasic();
			if (basic1!=basic2) {
				return basic1?-1:1;
			}
			
			final int dif=card.getConvertedCost()-entry.card.getConvertedCost();
			if (dif!=0) {
				return dif;
			}
			return card.getName().compareTo(entry.card.getName());
		}
		
		public void build(final boolean light,final boolean edit) {

			setPreferredSize(new Dimension(0,LINE_HEIGHT));
			setLayout(new BorderLayout(3,0));			
			if (light) {
				this.setBackground(FontsAndBorders.GRAY1);
			}
			
			this.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseEntered(MouseEvent event) {

					setCardImage(DeckEntry.this);
				}
			});	

			// Color or cost
			final JPanel leftPanel;
			if (card.isLand()) {
				leftPanel=new ColorPanel(player,card);
			} else {
				leftPanel=new CostPanel(card.getCost());
			}
			
			// Name
			final JLabel nameLabel=new JLabel(card.getName());			
			nameLabel.setFont(nameFont);
			nameLabel.setForeground(card.getRarityColor());

			// Type
			final JLabel typeLabel=new JLabel(card.getIcon());
			
			// Center
			final JPanel centerPanel=new JPanel();
			centerPanel.setOpaque(false);
			centerPanel.setLayout(new BorderLayout(3,0));
			centerPanel.add(leftPanel,BorderLayout.WEST);
			centerPanel.add(nameLabel,BorderLayout.CENTER);
			centerPanel.add(typeLabel,BorderLayout.EAST);
			add(centerPanel,BorderLayout.CENTER);
	
			// Count
			final JPanel rightPanel=new JPanel();
			rightPanel.setOpaque(false);
			rightPanel.setLayout(new BorderLayout(1,0));
			final JLabel countLabel=new JLabel(count>1?""+count:"");
			countLabel.setPreferredSize(new Dimension(16,0));
			countLabel.setHorizontalAlignment(JLabel.CENTER);
			rightPanel.add(countLabel,BorderLayout.WEST);
			
			// Edit
			if (edit) {
				final JButton editButton=new JButton(IconImages.EDIT);
				editButton.setPreferredSize(new Dimension(32,0));
				rightPanel.add(editButton,BorderLayout.EAST);
				editButton.addActionListener(this);
				editButton.setFocusable(false);
			}
			add(rightPanel,BorderLayout.EAST);
		}

		@Override
		public void actionPerformed(final ActionEvent event) {

			frame.editCardWithExplorer(new EditDeckCard(DeckViewer.this,player,deckCard));
		}
	}
}