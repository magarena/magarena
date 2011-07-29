package magic.ui.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;

public class TransparentImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private BufferedImage image=null;
	private RescaleOp rescaleOp=null;

	public void setOpacity(final float opacity) {
		if (opacity>=1.0f) {
			setOpaque(true);
			rescaleOp=null;
		} else {
			setOpaque(false);
			final float scales[]=new float[]{1.0f,1.0f,1.0f,opacity};
			final float offsets[]=new float[4];
			rescaleOp=new RescaleOp(scales, offsets, null);
		}
	}
	
	public void setImage(final BufferedImage image) {
		this.image=image;
	}
	
	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		if (image!=null) {
			if (rescaleOp==null) {
				g.drawImage(image,0,0,this);
			} else {
				final Graphics2D g2d=(Graphics2D)g;
				g2d.drawImage(image,rescaleOp,0,0);
			}
		}
	}		
}
