package magic.ui.widget.message;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

class EmptyComponent extends TComponent {

    @Override
    boolean requiresNewLine() {
        return false;
    }

    @Override
    Dimension getPreferredSize() {
        return new Dimension(0, 0);
    }

    @Override
    void paint(final JComponent com, final Graphics g, final int x, final int y) {
        // nothing to paint.
    }

    @Override
    Rectangle getBounds() {
        return NO_BOUNDS;
    }
}
