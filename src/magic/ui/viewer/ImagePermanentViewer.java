package magic.ui.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import magic.data.CardImagesProvider;
import magic.data.HighQualityCardImagesProvider;
import magic.data.IconImages;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;

public class ImagePermanentViewer extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int LOGICAL_X_MARGIN=50;
	private static final int LOGICAL_Y_MARGIN=70;
	
	private final ImagePermanentsViewer viewer;
	private final PermanentViewerInfo permanentInfo;
	private final List<PermanentViewerInfo> linkedInfos;
	private final Dimension logicalSize;
	private final List<Rectangle> linkedLogicalRectangles;
	private List<Rectangle> linkedScreenRectangles;
	private Point logicalPosition;
	private int logicalRow=1;
	
	public ImagePermanentViewer(final ImagePermanentsViewer viewer,final PermanentViewerInfo permanentInfo) {
		this.viewer=viewer;
		this.permanentInfo=permanentInfo;
		linkedInfos=new ArrayList<PermanentViewerInfo>();
		buildLinkedPermanents(linkedInfos,permanentInfo);
		linkedLogicalRectangles=new ArrayList<Rectangle>();
		logicalSize=calculateLogicalSize(linkedLogicalRectangles);
		linkedScreenRectangles=Collections.emptyList();
		
		setOpaque(false);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent event) {
				final int index=getPermanentInfoIndexAt(event.getX(),event.getY());
				if (index>=0) {					
					viewer.getController().processClick(linkedInfos.get(index).permanent);
				}
			}
			@Override
			public void mouseExited(final MouseEvent event) {
				viewer.getController().hideInfo();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(final MouseEvent event) {
				final int index=getPermanentInfoIndexAt(event.getX(),event.getY());
				if (index>=0) {
					final PermanentViewerInfo info=linkedInfos.get(index);
					final Point pointOnScreen=getLocationOnScreen();
					final Rectangle rect=new Rectangle(linkedScreenRectangles.get(index));
					rect.x+=pointOnScreen.x;
					rect.y+=pointOnScreen.y;
					viewer.getController().viewInfoAbove(info.cardDefinition,info.index,rect);
				} else {
					viewer.getController().hideInfo();
				}				
			}
		});
	}
	
	private int getPermanentInfoIndexAt(final int x,final int y) {
		for (int index=linkedScreenRectangles.size()-1;index>=0;index--) {
			final Rectangle rect=linkedScreenRectangles.get(index);
			if (x>=rect.x&&y>=rect.y&&x<rect.x+rect.width&&y<rect.y+rect.height) {
				return index;
			}
		}
		return -1;
	}

	private void buildLinkedPermanents(final List<PermanentViewerInfo> linkedInfos,final PermanentViewerInfo info) {
		
		for (final PermanentViewerInfo blocker : info.blockers) {

			buildLinkedPermanents(linkedInfos,blocker);
		}
		linkedInfos.addAll(info.linked);
		linkedInfos.add(info);			
	}
	
	private Dimension calculateLogicalSize(final List<Rectangle> linkedLogicalRectangles) {

		int width=0;
		int height=0;
		int x=-LOGICAL_X_MARGIN;
		for (final PermanentViewerInfo linkedInfo : linkedInfos) {

			x+=LOGICAL_X_MARGIN;
			final int y=linkedInfo.lowered?LOGICAL_Y_MARGIN:0;
			final Rectangle rect;
			if (linkedInfo.tapped) {
				width=Math.max(width,x+CardImagesProvider.CARD_HEIGHT);
				height=Math.max(height,y+CardImagesProvider.CARD_WIDTH);			
				rect=new Rectangle(x,y,CardImagesProvider.CARD_HEIGHT,CardImagesProvider.CARD_WIDTH);
			} else {
				width=Math.max(width,x+CardImagesProvider.CARD_WIDTH);
				height=Math.max(height,y+CardImagesProvider.CARD_HEIGHT);							
				rect=new Rectangle(x,y,CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT);
			}
			linkedLogicalRectangles.add(rect);
		}
		return new Dimension(width,height);
	}
	
	@Override
	public void setSize(final int width,final int height) {
		
		super.setSize(width,height);
		
		linkedScreenRectangles=new ArrayList<Rectangle>();
		for (final Rectangle logicalRect : linkedLogicalRectangles) {
			
			final Rectangle screenRect=new Rectangle();
			screenRect.x=(logicalRect.x*width)/logicalSize.width;
			screenRect.y=(logicalRect.y*height)/logicalSize.height;
			screenRect.width=(logicalRect.width*width)/logicalSize.width;
			screenRect.height=(logicalRect.height*height)/logicalSize.height;
			linkedScreenRectangles.add(screenRect);
		}
	}
	
	public int getPosition() {
		
		return permanentInfo.position;
	}
	
	public Dimension getLogicalSize() {
		
		return logicalSize;
	}
	
	public void setLogicalPosition(final Point logicalPosition) {
		
		this.logicalPosition=logicalPosition;
	}
	
	public Point getLogicalPosition() {
		
		return logicalPosition;
	}
	
	public void setLogicalRow(final int logicalRow) {
		
		this.logicalRow=logicalRow;
	}
	
	public int getLogicalRow() {
		
		return logicalRow;
	}
			
	@Override
	public void paint(final Graphics g) {

		super.paint(g);

		final Color choiceColor;
		if (viewer.getController().isCombatChoice()) {
			choiceColor=Color.RED; 
            //ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_COMBAT_CHOICE);			
		} else {
			choiceColor=Color.GREEN;
            //ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
		}
		
		g.setFont(FontsAndBorders.FONT1);
		final FontMetrics metrics=g.getFontMetrics();
		final Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		for (int index=0;index<linkedScreenRectangles.size();index++) {

			final PermanentViewerInfo linkedInfo=linkedInfos.get(index);
			final Image image=
                HighQualityCardImagesProvider.getInstance().getImage(linkedInfo.cardDefinition,linkedInfo.index,false);
			final Rectangle linkedRect=linkedScreenRectangles.get(index);
			final int x1=linkedRect.x;
			final int y1=linkedRect.y;
			final int x2=x1+linkedRect.width-1;
			final int y2=y1+linkedRect.height-1;

			if (linkedInfo.tapped) {
				final AffineTransform transform=new AffineTransform();
				final float scale=((float)linkedRect.width)/CardImagesProvider.CARD_HEIGHT;
				transform.translate(x1,y1);
				transform.scale(scale,scale);			
				transform.translate(CardImagesProvider.CARD_HEIGHT/2,CardImagesProvider.CARD_WIDTH/2);
				transform.rotate(Math.PI/2);
				transform.translate(-CardImagesProvider.CARD_WIDTH/2,-CardImagesProvider.CARD_HEIGHT/2);
				g2d.drawImage(image,transform,this);
			} else {
				g.drawImage(image,x1,y1,x2,y2,0,0,CardImagesProvider.CARD_WIDTH,CardImagesProvider.CARD_HEIGHT,this);
			}

			int ax=x1+1;
			int ay=y2-17;
			// Charge counters
			if (linkedInfo.chargeCounters>0) {
				g.drawImage(IconImages.CHARGE.getImage(),ax,ay,this);
				g.setColor(Color.DARK_GRAY);
				g.drawString(String.valueOf(linkedInfo.chargeCounters),ax+5,ay+14);
				ax+=16;
			}			
			if (permanentInfo.creature) {
				// Common combat ability icons.
				final long abilityFlags=linkedInfo.abilityFlags;
				if (linkedInfo.canNotTap) {
					g.drawImage(IconImages.CANNOTTAP.getImage(),ax,ay,this);
					ax+=16;
				}
				ax=ImageDrawingUtils.drawAbilityInfo(g,this, abilityFlags,ax,ay);
			}
			if (permanentInfo.cardDefinition.isLand()) {
				ax=ImageDrawingUtils.drawManaInfo(g,this,permanentInfo.cardDefinition,ax,ay);
			}
			
			final String pt=linkedInfo.powerToughness;
			if (!pt.isEmpty()) {
				final String damage=linkedInfo.damage>0?String.valueOf(linkedInfo.damage):null;
				final int ptWidth=metrics.stringWidth(pt);				
				if (linkedInfo.blocking) {
					ImageDrawingUtils.drawCreatureInfo(g,metrics,pt,ptWidth,damage,x1,y1,false);
				} else {
					ImageDrawingUtils.drawCreatureInfo(g,metrics,pt,ptWidth,damage,x2-ptWidth-4,y2-(damage!=null?32:18),true);
				}
			}
					
			if (viewer.isValidChoice(linkedInfo)) {
                //draw a transparent overlay of choiceColor
				// g2d.setPaint(choiceColor);
				// g2d.fillRect(x1-1,y1-1,x2-x1+2,y2-y1+2);
                //draw a one pixel border of choiceColor
				g2d.setPaint(new Color(choiceColor.getRGB()));
                g2d.setStroke(new BasicStroke(2));
				g2d.drawRect(x1+1,y1+1,x2-x1-1,y2-y1-1);
			}
		}
	}
}
