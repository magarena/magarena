package magic.ui.viewer;

import magic.ui.GameController;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ImagePermanentsViewer extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int POSITION_SPACING = 60;
	private static final int HORIZONTAL_SPACING = 40;
	private static final int VERTICAL_SPACING = 30;

	private final GameController controller;
	private List<ImagePermanentViewer> viewers;
	private Set<Object> validChoices;
	
	public ImagePermanentsViewer(final GameController controller) {
		this.controller=controller;
		
		setLayout(null);
		setOpaque(false);
		
		viewers=Collections.emptyList();
		validChoices=Collections.emptySet();
	}

	private static void divideOverOneRow(final List<ImagePermanentViewer> viewers) {
		for (final ImagePermanentViewer viewer : viewers) {
			viewer.setLogicalRow(1);
		}
	}
	
	private static void divideOverTwoRows(final List<ImagePermanentViewer> viewers) {
		final int size=viewers.size();
		int firstWidth=0;
		int secondWidth=0;
		int firstIndex=0;
		int secondIndex=size-1;
		
		while (firstIndex<=secondIndex) {
			final ImagePermanentViewer firstViewer=viewers.get(firstIndex);
			final ImagePermanentViewer secondViewer=viewers.get(secondIndex);
			final int width1=firstViewer.getWidth()+HORIZONTAL_SPACING;
			final int width2=secondViewer.getWidth()+HORIZONTAL_SPACING;
			if (firstWidth+width1<=secondWidth+width2) {
				firstViewer.setLogicalRow(1);
				firstWidth+=width1;
				firstIndex++;
			} else {
				secondViewer.setLogicalRow(2);
				secondWidth+=width2;
				secondIndex--;
			}
		}		
	}

	private int calculatePositions(final List<ImagePermanentViewer> aViewers) {
		int currentRow=1;
		int x=0;
		int y=0;
		int maxWidth=0;
		int rowHeight=0;
		int prevPosition=aViewers.get(0).getPosition();

		for (final ImagePermanentViewer viewer : aViewers) {
		
			if (currentRow!=viewer.getLogicalRow()) {
				currentRow++;
				x=0;
				y+=VERTICAL_SPACING+rowHeight;
			}
			if (viewer.getPosition()!=prevPosition) {
				prevPosition=viewer.getPosition();
				if (x>0) {
					x+=POSITION_SPACING;
				}
			}
			viewer.setLogicalPosition(new Point(x,y));
			final Dimension logicalSize=viewer.getLogicalSize();
			x+=logicalSize.width;
			maxWidth=Math.max(maxWidth,x);
			x+=HORIZONTAL_SPACING;
			rowHeight=Math.max(rowHeight,logicalSize.height);
		}	

		final int maxHeight=y+rowHeight;
		final int width=getWidth();
		final int height=getHeight();
		int scaleMult=width;
		int scaleDiv=maxWidth;
		if ((maxHeight*scaleMult)/scaleDiv>height) {
			scaleMult=height;
			scaleDiv=maxHeight;
		}
		if (scaleMult>scaleDiv/2) {
			scaleMult=scaleDiv/2;
		}
		
		for (final ImagePermanentViewer viewer : aViewers) {
			final Point point=viewer.getLogicalPosition();
			viewer.setLocation((point.x*scaleMult)/scaleDiv,(point.y*scaleMult)/scaleDiv);
			final Dimension size=viewer.getLogicalSize();
			viewer.setSize((size.width*scaleMult)/scaleDiv,(size.height*scaleMult)/scaleDiv);
		}
		
		return (1000*scaleMult)/scaleDiv;
	}

	private void calculateOptimalPositions(final List<ImagePermanentViewer> aViewers) {
		final int size=aViewers.size();
		if (size==0) {
			return;
		} else if (size<7) {
			divideOverOneRow(aViewers);
			calculatePositions(aViewers);
		} else {			
			divideOverOneRow(aViewers);
			final int scale=calculatePositions(aViewers);
			divideOverTwoRows(aViewers);
			if (calculatePositions(aViewers)<=scale) {
				divideOverOneRow(aViewers);
				calculatePositions(aViewers);
			}
		} 
	}
	
	public void viewPermanents(final Collection<PermanentViewerInfo> permanentInfos) {
		final List<ImagePermanentViewer> newViewers=new ArrayList<ImagePermanentViewer>();
		for (final PermanentViewerInfo permanentInfo : permanentInfos) {
			newViewers.add(new ImagePermanentViewer(this,permanentInfo));
		}
		calculateOptimalPositions(newViewers);
		removeAll();
		for (final ImagePermanentViewer viewer : newViewers) {
			add(viewer);
		}		
		viewers=newViewers;		
		revalidate();
		repaint();
	}
	
	public GameController getController() {
		return controller;
	}
	
	public void showValidChoices(final Set<Object> aValidChoices) {
		this.validChoices=aValidChoices;
		for (final ImagePermanentViewer viewer : viewers) {
			viewer.repaint();
		}
	}
	
	public boolean isValidChoice(final PermanentViewerInfo permanentInfo) {
		return validChoices.contains(permanentInfo.permanent);
	}
}
