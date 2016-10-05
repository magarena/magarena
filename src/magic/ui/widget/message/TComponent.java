package magic.ui.widget.message;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

abstract class TComponent {

    protected static final Rectangle NO_BOUNDS = new Rectangle();

    int lx;
    int ly;

    void setLocation(final int x, final int y) {
        this.lx = x;
        this.ly = y;
    }

    abstract boolean requiresNewLine();

    abstract Dimension getPreferredSize();

    abstract void paint(final JComponent com, final Graphics g, final int x, final int y);

    abstract Rectangle getBounds();

    boolean isInteractive() {
        return false;
    }
}
