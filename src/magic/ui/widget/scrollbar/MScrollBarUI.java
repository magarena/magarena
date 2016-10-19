package magic.ui.widget.scrollbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MScrollBarUI extends BasicScrollBarUI {

    // Various colors of a gray hue.
    public static final Color COLOR_A = new Color(234, 234, 234);
    private static final Color COLOR_B = new Color(100, 100, 100);
    private static final Color COLOR_C = new Color(150, 150, 150);
    private static final Color COLOR_D = new Color(182, 182, 182);
    private static final Color COLOR_E = new Color(218, 218, 218);

    private static final Color[] DEFAULT_COLORS = new Color[]{COLOR_A, COLOR_B, COLOR_C};
    private static final Color[] ROLLOVER_COLORS = new Color[]{COLOR_A, COLOR_C, COLOR_C};

    public static MScrollBarUI createUI(JComponent c) {
        return new MScrollBarUI();
    }

    private void doPaintCustomThumb(Graphics2D g2d, JComponent c, Rectangle r) {

        JScrollBar sb = (JScrollBar) c;
        if(!sb.isEnabled()) {
            return;
        }
        final boolean isVertical = sb.getOrientation() == JScrollBar.VERTICAL;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        final Color[] colors = isThumbRollover()
                ? ROLLOVER_COLORS
                : DEFAULT_COLORS;

        //
        // fill paint gradient
        //
        final Point endPoint = isVertical
                ? new Point(r.width + r.width / 4, 0)
                : new Point(0, r.height);

        final GradientPaint fillPaint = new GradientPaint(
            0, 0, colors[0], endPoint.x, endPoint.y, colors[1]
        );

        //
        // shape
        //
        final int offset1 = 3;
        final int offset2 = 9;
        final Dimension offset = isVertical
                ? new Dimension(offset1, offset2)
                : new Dimension(offset2, offset1);

        final Rectangle rect = new Rectangle(
                r.x + offset.width,
                r.y + offset.height,
                r.width - (offset.width * 2),
                r.height - (offset.height * 2)
        );

        g2d.setPaint(fillPaint);
        g2d.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
        g2d.setColor(colors[2]);
        g2d.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 10, 10);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2d = (Graphics2D) g.create();
        doPaintCustomThumb(g2d, c, r);
        g2d.dispose();
    }

    private void doPaintTrack(Graphics2D g2d, JComponent c, Rectangle r) {

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        JScrollBar sb = (JScrollBar) c;
        final boolean isVertical = sb.getOrientation() == JScrollBar.VERTICAL;

        //
        // fill paint gradient
        //
        final Point endPoint1 = isVertical
                ? new Point(r.width + r.width / 2, 0)
                : new Point(0, r.height + r.height / 2);

        final GradientPaint fillPaint = new GradientPaint(
                0, 0, COLOR_E, endPoint1.x, endPoint1.y, Color.WHITE
        );

        //
        // shape
        //
        final Point endPoint2 = isVertical
                ? new Point(0, r.height)
                : new Point(r.width, 0);

        g2d.setPaint(fillPaint);
        g2d.fillRect(r.x, r.y, r.width, r.height);
        g2d.setColor(COLOR_D);
        g2d.drawLine(0, 0, endPoint2.x, endPoint2.y);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2d = (Graphics2D) g.create();
        doPaintTrack(g2d, c, r);
        g2d.dispose();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new NoButton();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new NoButton();
    }

    @Override
    protected void setThumbBounds(int x, int y, int width, int height) {
        super.setThumbBounds(x, y, width, height);
        scrollbar.repaint();
    }
}
