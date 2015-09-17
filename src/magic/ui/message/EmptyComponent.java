package magic.ui.message;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

class EmptyComponent extends TComponent {

    @Override
    public boolean requiresNewLine() {

        return false;
    }

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(0, 0);
    }

    @Override
    public void paint(final JComponent com, final Graphics g, final int x, final int y) {

    }
}
