package magic.ui.widget;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

public class BackgroundLabel extends JLabel {

	private static final long serialVersionUID = 1L;
			
	public void paint(final Graphics g) {

		final BufferedImage image=ThemeFactory.getInstance().getCurrentTheme().getTexture(Theme.TEXTURE_BACKGROUND);
		final int imageWidth=image.getWidth();
		final int imageHeight=image.getHeight();
		final int width=this.getWidth();
		final int height=this.getHeight();
		
		for (int y=0;y<height;y+=imageHeight) {
			
			for (int x=0;x<width;x+=imageWidth) {
				
				g.drawImage(image,x,y,this);
			}
		}
		
		super.paint(g);
	}
}