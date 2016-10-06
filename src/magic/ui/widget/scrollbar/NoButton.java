package magic.ui.widget.scrollbar;

import java.awt.Dimension;
import javax.swing.JButton;

@SuppressWarnings("serial")
class NoButton extends JButton {

    private static final Dimension d = new Dimension();

    @Override
    public Dimension getPreferredSize() {
        return d;
    }
}
