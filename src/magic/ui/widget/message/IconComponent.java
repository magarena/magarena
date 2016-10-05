package magic.ui.widget.message;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

class IconComponent extends TComponent {

    private final ImageIcon icon;

    IconComponent(final ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    boolean requiresNewLine() {
        return true;
    }

    @Override
    Dimension getPreferredSize() {
        return new Dimension(icon.getIconWidth() + 1, icon.getIconHeight());
    }

    @Override
    void paint(final JComponent com, final Graphics g, final int x, final int y) {
        icon.paintIcon(com, g, lx + x, ly + y);
    }

    @Override
    Rectangle getBounds() {
        return NO_BOUNDS;
    }
}
