package magic.ui.screen.card.explorer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class SplitterButton extends JButton {

    private static final int TRIANGLE_WIDTH = 10;
    private static final int TRIANGLE_HEIGHT = 12;
    private static final int TRIANGLE_PADX = 4;

    private boolean isArrowUp = true;
    private boolean isMouseOver = false;
    private Color defaultColor = getForeground();

    public SplitterButton(String text) {
        super(text);

        setOpaque(true);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setBorder(null);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                isMouseOver = true;
                defaultColor = getForeground();
                setForeground(MagicStyle.getRolloverColor());
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                isMouseOver = false;
                setForeground(defaultColor);
                repaint();
            }
        });

    }

    public SplitterButton() {
        this(null);
    }

    public void setIsArrowUp(final boolean b) {
        this.isArrowUp = b;
        isMouseOver = false;
        setForeground(defaultColor);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        g.setColor(isMouseOver ? Color.DARK_GRAY.brighter() : Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setColor(isMouseOver ? MagicStyle.getRolloverColor() : Color.GRAY);

        if (isArrowUp) {
            g2d.fillPolygon(getUpTriangle(TRIANGLE_PADX));
            g2d.fillPolygon(getUpTriangle(getWidth() - TRIANGLE_WIDTH - TRIANGLE_PADX));
        } else {
            g2d.fillPolygon(getDownTriangle(TRIANGLE_PADX));
            g2d.fillPolygon(getDownTriangle(getWidth() - TRIANGLE_WIDTH - TRIANGLE_PADX));
        }

        super.paintComponent(g);
    }

    private Polygon getUpTriangle(final int x) {
        final int y = (getHeight() - TRIANGLE_HEIGHT) / 2;
        final Polygon p = new Polygon();
        p.addPoint(x, getHeight() - y - 1);
        p.addPoint(x + (TRIANGLE_WIDTH / 2), y);
        p.addPoint(x + TRIANGLE_WIDTH, getHeight() - y - 1);
        return p;
    }

    private Polygon getDownTriangle(final int x) {
        final int y = (getHeight() - TRIANGLE_HEIGHT) / 2;
        final Polygon p = new Polygon();
        p.addPoint(x, y + 1);
        p.addPoint(x + (TRIANGLE_WIDTH / 2), getHeight() - y);
        p.addPoint(x + TRIANGLE_WIDTH, y + 1);
        return p;
    }

}
