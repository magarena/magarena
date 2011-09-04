package magic.ui.viewer;

import magic.data.CardImagesProvider;
import magic.data.HighQualityCardImagesProvider;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.ui.GameController;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ImageCardListViewer extends JPanel implements ChoiceViewer {

	private static final long serialVersionUID = 1L;

	private static final MagicCardList EMPTY_CARD_LIST=new MagicCardList();
	private static final int CARD_WIDTH=100;
	private static final int CARD_HEIGHT=140;
	private static final int SPACING=10;
	
	private final GameController controller;
	private MagicCardList cardList;
	private List<Point> cardPoints;
	private Set<Object> validChoices;
	private boolean showInfo=false;
	
	public ImageCardListViewer(final GameController controller) {
		setOpaque(false);

		this.controller=controller;
		cardList=EMPTY_CARD_LIST;
		cardPoints=new ArrayList<Point>();
		validChoices=Collections.emptySet();
				
		controller.registerChoiceViewer(this);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3) {
					controller.actionKeyPressed();
					return;
				}
				final int index=getCardIndexAt(event.getX(),event.getY());
				if (index>=0) {
					controller.processClick(cardList.get(index));
				}
			}
			@Override
			public void mouseExited(final MouseEvent event) {
				controller.hideInfo();
			}			
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(final MouseEvent event) {
				final int index=getCardIndexAt(event.getX(),event.getY());
				if (index>=0) {
					final MagicCard card=cardList.get(index);
					final Point pointOnScreen=getLocationOnScreen();
					final Point point=cardPoints.get(index);
					final Rectangle rect=
                        new Rectangle(pointOnScreen.x+point.x,pointOnScreen.y+point.y,CARD_WIDTH,CARD_HEIGHT);
					ImageCardListViewer.this.controller.viewInfoAbove(card.getCardDefinition(),card.getImageIndex(),rect);
				} else {
					ImageCardListViewer.this.controller.hideInfo();
				}
			}
		});
	}
	
	private int getCardIndexAt(final int x,final int y) {
		for (int index=cardPoints.size()-1;index>=0;index--) {
			final Point point=cardPoints.get(index);
			if (x>=point.x&&y>=point.y&&x<point.x+CARD_WIDTH&&y<point.y+CARD_HEIGHT) {
				return index;
			}
		}
		return -1;
	}
		
	public void setCardList(final MagicCardList cardList,final boolean showInfo) {
		final List<Point> cardPoints=new ArrayList<Point>();
		final int size=cardList.size();
		final int cardWidth=CARD_WIDTH*size+(size-1)*SPACING;			
		int width=getWidth();
		if (cardWidth<width||size==1) {
			int x=0;
			int step=CARD_WIDTH+SPACING;	
			for (int index=0;index<size;index++) {
				
				cardPoints.add(new Point(x,1));
				x+=step;
			}
		} else {
			width-=CARD_WIDTH;
			for (int index=0;index<size;index++) {

				cardPoints.add(new Point((width*index)/(size-1),1));
			}
		}
		this.cardList=cardList;
		this.cardPoints=cardPoints;
		this.showInfo=showInfo;
	}

	@Override
	public void paint(final Graphics g) {
		
		super.paint(g);
		
		if (cardList.isEmpty()) {
			return;
		}
		
		final Color choiceColor = ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
		
		g.setFont(FontsAndBorders.FONT1);
		final FontMetrics metrics=g.getFontMetrics();
		final Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		for (int index=0; index < cardList.size(); index++) {
			final MagicCard card=cardList.get(index);
			final MagicCardDefinition cardDefinition=card.getCardDefinition();
			final Point point=cardPoints.get(index);
			final BufferedImage image=
                HighQualityCardImagesProvider.getInstance().getImage(cardDefinition,card.getImageIndex(),false);
			final int x1=point.x;
			final int y1=point.y;
			final int x2=point.x+CARD_WIDTH;
			final int y2=point.y+CARD_HEIGHT;

            //draw the card image
			g.drawImage(image,x1,y1,x2,y2,0,0,CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT,this);

            //draw the overlay icons
			if (showInfo) {
				if (cardDefinition.isLand()) {
					ImageDrawingUtils.drawManaInfo(g,this,cardDefinition,x1+1,y2-17);
				} else {
					ImageDrawingUtils.drawCostInfo(g,this,cardDefinition.getCost(),x1,x2-1,y1+2);
				}
				if (cardDefinition.isCreature()) {
					ImageDrawingUtils.drawAbilityInfo(g,this,cardDefinition.getAbilityFlags(),x1+2,y2-18);
					final String pt=cardDefinition.getPower()+"/"+cardDefinition.getToughness();
					final int ptWidth=metrics.stringWidth(pt);				
					ImageDrawingUtils.drawCreatureInfo(g,metrics,pt,ptWidth,"",x2-ptWidth-4,y2-18,false);
				}
			}

            //show that card is a valid choice
            //instead of adding a choiceColor transparent layer,
            //draw a border of the choiceColor
			if (validChoices.contains(card)) {
				
				if (ThemeFactory.getInstance().getCurrentTheme().getOptionUseOverlay()) {
					//draw a transparent overlay of choiceColor
					g2d.setPaint(choiceColor);
					g2d.fillRect(x1-1,y1-1,CARD_WIDTH+2,CARD_HEIGHT+2);
				} else  {
					//draw a one pixel border of choiceColor
					g2d.setPaint(new Color(choiceColor.getRGB()));
	                g2d.setStroke(new BasicStroke(2));
					g2d.drawRect(x1,y1,CARD_WIDTH-1,CARD_HEIGHT);
				}
			}
		}
	}

	@Override
	public void showValidChoices(final Set<Object> validChoices) {
		this.validChoices=validChoices;
		repaint();
	}
}
