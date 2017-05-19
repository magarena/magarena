package magic.ui.screen.widget;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import magic.ui.MagicSound;
import magic.ui.helpers.ImageHelper;

@SuppressWarnings("serial")
public class BigDialButton extends ActionBarButton {

    private final int INTERVAL; // in degrees
    private final int positions;
    private int position;

    public BigDialButton(IDialButtonHandler handler) {

        super(null);

        this.positions = handler.getDialPositionsCount();
        this.position = handler.getDialPosition();
        INTERVAL = 360 / positions;

        rotateIconImage();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (handler.doLeftClickAction(getNextPosition())) {
                        position = getNextPosition();
                    } else {
                        MagicSound.BEEP.play();
                        return;
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (handler.doRightClickAction(getPreviousPosition())) {
                        position = getPreviousPosition();
                    } else {
                        MagicSound.BEEP.play();
                        return;
                    }
                }
                rotateIconImage();
                MagicSound.CLICK.play();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                handler.onMouseEntered(position);
            }
        });
    }

    private int getPreviousPosition() {
        return position == 0 ? positions - 1  : position - 1;
    }

    private int getNextPosition() {
        return position >= positions - 1 ? 0 : position + 1;
    }

    private static final GradientPaint tintColor =
            new GradientPaint(
                    0, 0, new Color(255, 255, 255, 160),
                    24, 24, new Color(255, 255, 255, 255)
            );

    private void rotateIconImage() {

        final int W = 24;
        final int H = 24;

        final BufferedImage image = ImageHelper.getCompatibleBufferedImage(
            W, H, BufferedImage.TRANSLUCENT
        );
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        // This makes a big difference to quality of rotated image if rotation is not a factor of 90 degrees.
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC) ;

        g2d.setTransform(AffineTransform.getRotateInstance(
                Math.toRadians(INTERVAL * position),
                W * 0.5d, H * 0.5d)
        );

        g2d.setPaint(tintColor);
        g2d.fillOval(2, 2, W-4, H-4);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OUT);
        g2d.setComposite(ac);
        g2d.fillOval((W - 8) / 2, 3, 8, 8);
        g2d.dispose();

        super.setIcon(new ImageIcon(image));
    }

    public int getPosition() {
        return position;
    }

}
