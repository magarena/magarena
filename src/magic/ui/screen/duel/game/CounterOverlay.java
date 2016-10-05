package magic.ui.screen.duel.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import magic.ui.utility.GraphicsUtils;

public class CounterOverlay {

    private static final Font VALUE_FONT = new Font("Dialog", Font.BOLD, 14);

    private final BufferedImage image;
    private int counterValue = 0;
    private Color counterColor;

    public CounterOverlay(final int width, final int height, final Color color) {
        this.counterColor = color;
        image = GraphicsUtils.getCompatibleBufferedImage(width, height, Transparency.TRANSLUCENT);
        drawCounter();
    }

    public BufferedImage getCounterImage() {
        return image;
    }

    private void drawCounter() {
        GraphicsUtils.clearImage(image);
        final Graphics2D g2d = image.createGraphics();
//        drawBorder(g2d);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawCounterShape(g2d);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawCounterValue(g2d);
        g2d.dispose();
    }

    private void drawCounterShape(final Graphics2D g2d) {
        drawFillShape(g2d);
//        drawOutlineShape(g2d);
    }

    private void setShapeFrame(final Ellipse2D shape, final float strokeWidth) {
        final float strokeAdj = strokeWidth / 2.0f;
        final float origin = strokeAdj;
        final float sizeAdj = strokeWidth + 1.0f;
        final float shapeWidth = (float)image.getWidth() - sizeAdj;
        final float shapeHeight = (float)image.getHeight() - sizeAdj;
        shape.setFrame(origin, origin, shapeWidth, shapeHeight);
    }

    private void drawFillShape(final Graphics2D g2d) {
        final float strokeWidth = 1.0f;
        final Ellipse2D shape = new Ellipse2D.Float();
        setShapeFrame(shape, strokeWidth);
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.setColor(counterColor);
        g2d.fill(shape);
    }

    private void drawOutlineShape(final Graphics2D g2d) {
        final float strokeWidth = 2.1f; // for some reason the circle appears asymmetrical when 2.0f.
        final Ellipse2D shape = new Ellipse2D.Float();
        setShapeFrame(shape, strokeWidth);
        g2d.setStroke(new BasicStroke(strokeWidth));
        g2d.setColor(counterColor);
        g2d.draw(shape);
    }

    private void drawCounterValue(final Graphics2D g2d) {
        g2d.setFont(VALUE_FONT);
//        FontRenderContext frc = g2d.getFontRenderContext();
//        final int textHeight = Math.round(VALUE_FONT.getLineMetrics(text, frc).getHeight());
        final String text = Integer.toString(counterValue);
        final FontMetrics metrics = g2d.getFontMetrics(VALUE_FONT);
        final int textWidth = metrics.stringWidth(text);
        final int textX = image.getWidth(null) / 2 - textWidth / 2;
        final int textY = image.getHeight(null) / 2 + 4;
        GraphicsUtils.drawStringWithOutline(g2d, text, textX, textY);
    }

    public void setCounterValue(final int value) {
        if (this.counterValue != value) {
            this.counterValue = value;
            drawCounter();
        }
    }

    /**
     * useful for debugging purposes.
     */
    private void drawBorder(final Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(255, 0, 0, 200));
        g2d.fillRect(0, 0, image.getWidth()-1, image.getHeight()-1);
    }

}
