package magic.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

@SuppressWarnings("serial")
public class BackgroundPanel extends JPanel {

    public BackgroundPanel(final LayoutManager layout) {
        super(layout);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Dimension size = getSize();
        final Theme theme = ThemeFactory.getInstance().getCurrentTheme();
        final boolean stretchTexture = theme.getValue(Theme.VALUE_BACKGROUND_STRETCH) == 1;
        paintZone(g,
                theme.getTexture(Theme.TEXTURE_BACKGROUND),
                new Rectangle(0, 0, size.width, size.height),
                stretchTexture);
    }

    private void paintZone(final Graphics g,final BufferedImage aImage,final Rectangle rect,final boolean stretch) {
        if (stretch) {
            paintZoneStretch(g, aImage, rect);
        } else {
            paintZoneTile(g, aImage, rect);
        }
    }

    private void paintZoneStretch(final Graphics g,final BufferedImage aImage,final Rectangle rect) {
        final int iw=aImage.getWidth();
        final int ih=aImage.getHeight();
        final int iw2=ih*rect.width/rect.height;
        final Rectangle imageRect;
        if (iw2<=iw) {
            imageRect=new Rectangle((iw-iw2)/2,0,iw2,ih);
        } else {
            final int ih2=iw*rect.height/rect.width;
            imageRect=new Rectangle(0,(ih-ih2)/2,iw,ih2);
        }
        g.drawImage(aImage,rect.x,rect.y,rect.x+rect.width,rect.y+rect.height,
                imageRect.x,imageRect.y,imageRect.x+imageRect.width,imageRect.y+imageRect.height,this);
    }

    private void paintZoneTile(final Graphics g,final BufferedImage aImage,final Rectangle rect) {
        final int imageWidth=aImage.getWidth();
        final int imageHeight=aImage.getHeight();
        final int x2=rect.x+rect.width;
        final int y2=rect.y+rect.height;
        for (int y=rect.y;y<y2;y+=imageHeight) {
            for (int x=rect.x;x<x2;x+=imageWidth) {
                g.drawImage(aImage,x,y,this);
            }
        }
    }

}
