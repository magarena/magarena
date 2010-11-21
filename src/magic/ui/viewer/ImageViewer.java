package magic.ui.viewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import magic.MagicMain;
import magic.model.MagicRandom;

/**
 *  Landscape image viewer.
 */
public class ImageViewer extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final int VIEWER_WIDTH=300;
	private static final int ZOOM_FACTOR=4;
	
	private static final File imageFiles[];
	private static final List<Integer> imageIndices;
	
	private final BufferedImage image;
	private Image scaledImage=null;
	private boolean scaled=false;
	private int imageWidth;
	private int imageHeight;
	private int viewerHeight;
	private int zoomX;
	private int zoomY;
	private int sx1;
	private int sy1;
	private int sx2;
	private int sy2;
	
	static {

		final File imagePathFile=new File(MagicMain.getGamePath()+File.separator+"images");
		imageIndices=new ArrayList<Integer>();
		if (imagePathFile.exists()) {
			imageFiles=imagePathFile.listFiles();
		} else {
			imageFiles=null;
		}
	}
	
	private static synchronized File nextFile() {
		
		if (imageFiles==null||imageFiles.length==0) {
			return null;
		}
		if (imageIndices.size()==0) {
			for (int index=0;index<imageFiles.length;index++) {
				
				imageIndices.add(index);
			}
		}
		final Integer index=imageIndices.remove(MagicRandom.nextInt(imageIndices.size()));
		return imageFiles[index];
	}
	
	public ImageViewer() {

		setOpaque(false);
		BufferedImage readImage=null;
		try {
			final File imageFile=nextFile();
			if (imageFile!=null) {
				readImage=ImageIO.read(imageFile);
			}			
		} catch (final Exception ex) {}

		image=readImage;
		if (image!=null) {
			imageWidth=image.getWidth();
			imageHeight=image.getHeight();
			viewerHeight=imageHeight*VIEWER_WIDTH/imageWidth;
			zoomX=imageWidth/ZOOM_FACTOR;
			zoomY=imageHeight/ZOOM_FACTOR;
			
			final MouseAdapter mouseListener=new MouseAdapter() {

				@Override
				public void mouseExited(final MouseEvent e) {

					scaled=false;
					repaint();
				}				

				@Override
				public void mouseMoved(final MouseEvent e) {

					final int y=e.getY()-getHeight()+viewerHeight;
					if (y>=0) {
						final int x=e.getX();
						int px=(x*imageWidth)/getWidth();
						int py=(y*imageHeight)/viewerHeight;
						if (px<zoomX) {
							px=zoomX;						
						} else if (px+zoomX>=imageWidth) {
							px=imageWidth-zoomX;
						}
						if (py<zoomY) {
							py=zoomY;
						} else if (py+zoomY>=imageHeight) {
							py=imageHeight-zoomY;
						}
						scaled=true;
						sx1=px-zoomX;
						sy1=py-zoomY;
						sx2=px+zoomX;
						sy2=py+zoomY;
					} else {
						scaled=false;
					}
					repaint();
				}
			};
			
			addMouseListener(mouseListener);
			addMouseMotionListener(mouseListener);
		}
	}
	
	@Override
	public void paint(final Graphics g) {

		super.paint(g);
		if (image!=null) {
			final int height=getHeight();
			final int y=height-viewerHeight;
			if (scaled) {
				final Graphics2D g2d=(Graphics2D)g;
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);		
				g.drawImage(image,0,y,VIEWER_WIDTH,height,sx1,sy1,sx2,sy2,this);
			} else {
				if (scaledImage==null) {
					scaledImage=image.getScaledInstance(VIEWER_WIDTH,viewerHeight,Image.SCALE_SMOOTH);
				}
				g.drawImage(scaledImage,0,y,this);
			}
			g.setColor(Color.black);
			g.drawRect(0,y,getWidth()-1,viewerHeight-1);
		}
	}
}