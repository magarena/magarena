package magic.ui;

import magic.data.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerProfile;
import magic.ui.resolution.DefaultResolutionProfile;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.ZoneBackgroundLabel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ExplorerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	public static final int ALL=0;
	public static final int LAND=1;
	public static final int SPELL=2;

	private static final Border SCROLL_BORDER=BorderFactory.createEmptyBorder(0,0,5,0);
	
	private static final int CARD_WIDTH=DefaultResolutionProfile.CARD_VIEWER_WIDTH;
	private static final int CARD_HEIGHT=DefaultResolutionProfile.CARD_VIEWER_HEIGHT;
	private static final int FILTER_HEIGHT = 350;
	private static final int LINE_SPACING=4;
	private static final int LINE_WIDTH=200;
	private static final int LINE_WIDTH2=LINE_WIDTH+LINE_SPACING+2;
	private static final int LINE_HEIGHT=24;
	private static final Dimension LINE_DIMENSION=new Dimension(LINE_WIDTH,LINE_HEIGHT);
 	private static final int SPACING=20;
	
 	private final MagicFrame frame;
	private final EditDeckCard editDeckCard;
 	private final ZoneBackgroundLabel backgroundLabel;
	private final CardViewer cardViewer;
	private final ExplorerFilterPanel filterPanel;
	private final JScrollPane cardsScrollPane;
	private final JPanel cardsPanel;
	private final JButton closeButton;
	private List<MagicCardDefinition> cardDefinitions;
	
	public ExplorerPanel(final MagicFrame frame,final int mode,final EditDeckCard editDeckCard) {

		this.frame=frame;
		this.editDeckCard=editDeckCard;
		
		setLayout(null);
		
		cardViewer=new CardViewer("Card",false,true);
		cardViewer.setSize(CARD_WIDTH,CARD_HEIGHT);		
		cardViewer.setCard(null,0);
		add(cardViewer);
		
		MagicPlayerProfile profile=null;
		MagicCubeDefinition cube=null;
		if (editDeckCard!=null) {
			profile=editDeckCard.getPlayer().getProfile();
			cube=editDeckCard.getCube();
		}
		
		filterPanel=new ExplorerFilterPanel(this,mode,profile,cube);
		filterPanel.setSize(DefaultResolutionProfile.CARD_VIEWER_WIDTH,FILTER_HEIGHT);
		add(filterPanel);

		final JPanel scrollPanel=new JPanel(new BorderLayout());
		scrollPanel.setOpaque(false);
		scrollPanel.setBorder(SCROLL_BORDER);
		
		final JPanel topPanel=new JPanel(new BorderLayout());
		topPanel.setOpaque(false);
		scrollPanel.add(topPanel,BorderLayout.WEST);
		
		cardsPanel=new JPanel();
		cardsPanel.setOpaque(false);
		topPanel.add(cardsPanel,BorderLayout.SOUTH);
		
		cardsScrollPane=new JScrollPane();
		cardsScrollPane.setBorder(null);
		cardsScrollPane.setOpaque(false);
		cardsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		cardsScrollPane.getHorizontalScrollBar().setUnitIncrement(LINE_WIDTH2);
		cardsScrollPane.getHorizontalScrollBar().setBlockIncrement(LINE_WIDTH2);
		cardsScrollPane.getViewport().setOpaque(false);
		cardsScrollPane.getViewport().add(scrollPanel);
		add(cardsScrollPane);		
		
		closeButton=new JButton(IconImages.CLOSE);
		closeButton.setFocusable(false);
		closeButton.setSize(28,28);
		closeButton.addActionListener(this);
		add(closeButton);
		
		backgroundLabel=new ZoneBackgroundLabel();
		backgroundLabel.setBounds(0,0,0,0);
		add(backgroundLabel);
		
		updateCards();
		
		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				resizeComponents();
			}
		});
	}
	
	private void resizeComponents() {
		
		final Dimension size=getSize();
		backgroundLabel.setSize(size);

		int y=(size.height-CARD_HEIGHT-SPACING-FILTER_HEIGHT)/2;
		cardViewer.setLocation(SPACING,y);
		y+=SPACING+CARD_HEIGHT;
		filterPanel.setLocation(SPACING,y);
		cardsScrollPane.setBounds(SPACING*2+CARD_WIDTH,0,size.width-CARD_WIDTH-2*SPACING-32,size.height-SPACING);
		closeButton.setLocation(size.width-closeButton.getWidth(),0);
		
		update();
		repaint();
	}
	
	private void update() {

		if (cardDefinitions.isEmpty()) {
			cardViewer.setCard(null,0);
 		} else {
 			cardViewer.setCard(cardDefinitions.get(0),0);
 		}
		cardsPanel.removeAll();

		final int height=cardsScrollPane.getHeight()-LINE_HEIGHT-16;
		if (height>0) {
			int lines=Math.min(height/LINE_HEIGHT,cardDefinitions.size());
			if (lines>0) {
				int size=cardDefinitions.size();
				final int columns=(size+lines-1)/lines;
				if (columns>1&&columns*LINE_WIDTH2-10<cardsScrollPane.getWidth()) {
					lines++;
				}
				cardsPanel.setLayout(new GridLayout(1,columns,LINE_SPACING,0));
		
				boolean startLight=true;
				boolean light=false;
				int line=lines;
				JPanel currentPanel=null;
				for (final MagicCardDefinition cardDefinition : cardDefinitions) {
					
					if (line==lines) {
						light=startLight;
						startLight=!startLight;
						line=0;
						final JPanel currentMainPanel=new JPanel(new BorderLayout());
						currentMainPanel.setOpaque(false);						
						currentPanel=new JPanel(new GridLayout(Math.min(lines,size),1));
						currentPanel.setBorder(FontsAndBorders.BLACK_BORDER);
						currentMainPanel.add(currentPanel,BorderLayout.NORTH);
						cardsPanel.add(currentMainPanel);
					}
					currentPanel.add(new CardLabel(cardDefinition,light));
					line++;
					size--;
					light=!light;
				}
			}
		}
		
		cardsScrollPane.revalidate();
		repaint();
	}
	
	public void updateCards() {

		cardDefinitions=filterPanel.getCardDefinitions();
		update();
		cardsScrollPane.getViewport().setViewPosition(new Point(0,0));
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
	
		final Object source=event.getSource();
		if (source==closeButton) {
			frame.closeCardExplorer();
		}
	}
	
	private class CardLabel extends JLabel {

		private static final long serialVersionUID = 1L;

		private final MagicCardDefinition cardDefinition;
		
		public CardLabel(final MagicCardDefinition cardDefinition,final boolean light) {

			this.cardDefinition=cardDefinition;

			setText(cardDefinition.getName());
			setIcon(cardDefinition.getIcon());
			setIconTextGap(4);
			setForeground(cardDefinition.getRarityColor());
			setPreferredSize(LINE_DIMENSION);
			setOpaque(true);
			
			if (!light) {
				setBackground(Color.LIGHT_GRAY);
			}
						
			this.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseEntered(final MouseEvent event) {

					cardViewer.setCard(CardLabel.this.cardDefinition,0);
					setForeground(Color.RED);
				}

				@Override
				public void mouseExited(final MouseEvent event) {

					setForeground(cardDefinition.getRarityColor());
				}

				@Override
				public void mousePressed(final MouseEvent event) {

					if (editDeckCard!=null) {
						editDeckCard.getPlayer().getDeck().remove(editDeckCard.getCard());
						editDeckCard.getPlayer().getDeck().add(cardDefinition);
						editDeckCard.getDeckViewer().updateAfterEdit();						
						frame.closeCardExplorer();
					}
				}
			});
		}
	}
}