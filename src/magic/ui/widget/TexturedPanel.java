package magic.ui.widget;

import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class TexturedPanel extends JPanel {

    private boolean start;
    private int startX;
    private int startY;
    private boolean isTranslucent = false;

    public TexturedPanel() {
        setOpaque(true);
        start=false;
    }

    private static int getStart(final int value,final int size) {

        int start=(value*97)%size;
        if (start>0) {
            start-=size;
        }
        return start;
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (isOpaque() || isTranslucent) {
            if (isTranslucent) {
                paintTranslucentBackground(g);
            } else {
                paintTexturedBackground(g);
            }
        } else {
            super.paintComponent(g);
        }
    }

    private void paintTranslucentBackground(final Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void paintTexturedBackground(final Graphics g) {
        final BufferedImage image=ThemeFactory.getInstance().getCurrentTheme().getTexture(Theme.TEXTURE_COMPONENT);
        final int imageWidth=image.getWidth();
        final int imageHeight=image.getHeight();
        final int width=this.getWidth();
        final int height=this.getHeight();

        if (!start) {
            final Point p=getLocationOnScreen();
            startX=getStart(p.x,imageWidth);
            startY=getStart(p.y,imageHeight);
            start=true;
        }

        for (int y=startY;y<height;y+=imageHeight) {

            for (int x=startX;x<width;x+=imageWidth) {

                g.drawImage(image,x,y,this);
            }
        }
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        // If the background color is set to non-opaque (ie. alpha < 255) then
        // display semi-transparent background instead of texture.
        if (bg.getAlpha() < 255) {
            isTranslucent = true;
            setOpaque(false);
        } else {
            isTranslucent = false;
            setOpaque(true);
        }
    }

}
