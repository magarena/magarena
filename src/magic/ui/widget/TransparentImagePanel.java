package magic.ui.widget;

import javax.swing.JPanel;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class TransparentImagePanel extends JPanel {

    private BufferedImage image;
    private float opacity = 1.0f;

    public void setOpacity(final float opacity) {
        this.opacity = opacity >= 1.0f ? 1.0f : opacity;
    }

    public void setImage(final BufferedImage image) {
        this.image = image;
        repaint();
    }

    @Override
    public void paintComponent(final Graphics g) {
        if (image != null) {
            final Graphics2D g2d = (Graphics2D)g;
            if (opacity < 1.0f) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            }
            g2d.drawImage(image, 0, 0, this);
        }
    }
}
