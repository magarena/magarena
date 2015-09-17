package magic.ui.message;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

class IconComponent extends TComponent {

    private final ImageIcon icon;

    public IconComponent(final ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public boolean requiresNewLine() {
        return true;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(icon.getIconWidth() + 1, icon.getIconHeight());
    }

    @Override
    public void paint(final JComponent com, final Graphics g, final int x, final int y) {
        icon.paintIcon(com, g, lx + x, ly + y);
    }
}
