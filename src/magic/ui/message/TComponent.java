package magic.ui.message;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

abstract class TComponent {

    int lx;
    int ly;

    void setLocation(final int x, final int y) {

        this.lx = x;
        this.ly = y;
    }

    abstract boolean requiresNewLine();

    abstract Dimension getPreferredSize();

    abstract void paint(final JComponent com, final Graphics g, final int x, final int y);
}
