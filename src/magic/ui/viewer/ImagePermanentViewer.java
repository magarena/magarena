package magic.ui.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import magic.data.CardImages;
import magic.data.IconImages;
import magic.model.MagicAbility;
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
				width=Math.max(width,x+CardImages.CARD_HEIGHT);
				height=Math.max(height,y+CardImages.CARD_WIDTH);			
				rect=new Rectangle(x,y,CardImages.CARD_HEIGHT,CardImages.CARD_WIDTH);
			} else {
				width=Math.max(width,x+CardImages.CARD_WIDTH);
				height=Math.max(height,y+CardImages.CARD_HEIGHT);							
				rect=new Rectangle(x,y,CardImages.CARD_WIDTH,CardImages.CARD_HEIGHT);
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
	
	private void drawCreatureInfo(final Graphics g,final FontMetrics metrics,
			final String pt,final int ptWidth,final String damage,final int x,final int y,final boolean flip) {

		g.setColor(FontsAndBorders.GRAY2);
		g.fillRect(x,y,ptWidth+4,damage!=null?32:18);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(x,y,ptWidth+4,damage!=null?32:18);
		g.drawString(pt,x+3,damage!=null&&flip?y+28:y+14);
		if (damage!=null) {
			final int damageWidth=metrics.stringWidth(damage);
			int dx=x+3+(ptWidth-damageWidth)/2;
			g.setColor(Color.RED);
			g.drawString(damage,dx,flip?y+14:y+28);
		}
	}
		
	@Override
	public void paint(final Graphics g) {

		super.paint(g);

		g.setFont(FontsAndBorders.FONT1);
		final FontMetrics metrics=g.getFontMetrics();
		final Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		for (int index=0;index<linkedScreenRectangles.size();index++) {

			final PermanentViewerInfo linkedInfo=linkedInfos.get(index);
			final BufferedImage image=CardImages.getInstance().getImage(linkedInfo.cardDefinition,linkedInfo.index);
			final Rectangle linkedRect=linkedScreenRectangles.get(index);
			final int x1=linkedRect.x;
			final int y1=linkedRect.y;
			final int x2=x1+linkedRect.width-1;
			final int y2=y1+linkedRect.height-1;

			if (linkedInfo.tapped) {
				final AffineTransform transform=new AffineTransform();
				final float scale=((float)linkedRect.width)/CardImages.CARD_HEIGHT;
				transform.translate(x1,y1);
				transform.scale(scale,scale);			
				transform.translate(CardImages.CARD_HEIGHT/2,CardImages.CARD_WIDTH/2);
				transform.rotate(Math.PI/2);
				transform.translate(-CardImages.CARD_WIDTH/2,-CardImages.CARD_HEIGHT/2);
				g2d.drawImage(image,transform,this);
			} else {
				g.drawImage(image,x1,y1,x2,y2,0,0,CardImages.CARD_WIDTH,CardImages.CARD_HEIGHT,this);
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
				if (MagicAbility.Flying.hasAbility(abilityFlags)) {				
					g.drawImage(IconImages.FLYING.getImage(),ax,ay,this);
					ax+=16;
				}
				if (MagicAbility.FirstStrike.hasAbility(abilityFlags)||MagicAbility.DoubleStrike.hasAbility(abilityFlags)) {				
					g.drawImage(IconImages.STRIKE.getImage(),ax,ay,this);
					ax+=16;
				}
				if (MagicAbility.Trample.hasAbility(abilityFlags)) {
					g.drawImage(IconImages.TRAMPLE.getImage(),ax,ay,this);
					ax+=16;				
				}
				if (MagicAbility.Deathtouch.hasAbility(abilityFlags)) {
					g.drawImage(IconImages.DEATHTOUCH.getImage(),ax,ay,this);
					ax+=16;								
				}
			}
			
			final String pt=linkedInfo.powerToughness;
			if (!pt.isEmpty()) {
				final String damage=linkedInfo.damage>0?String.valueOf(linkedInfo.damage):null;
				final int ptWidth=metrics.stringWidth(pt);				
				if (linkedInfo.blocking) {
					drawCreatureInfo(g,metrics,pt,ptWidth,damage,x1,y1,false);
				} else {
					drawCreatureInfo(g,metrics,pt,ptWidth,damage,x2-ptWidth-4,y2-(damage!=null?32:18),true);
				}
			}
						
			if (viewer.isValidChoice(linkedInfo)) {
				g2d.setPaint(viewer.getController().isCombatChoice()?FontsAndBorders.COMBAT_TARGET_COLOR:FontsAndBorders.TARGET_COLOR);
				g2d.fillRect(x1-1,y1-1,x2-x1+2,y2-y1+2);
			}
		}
	}
}