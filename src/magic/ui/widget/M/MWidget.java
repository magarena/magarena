package magic.ui.widget.M;

import java.awt.event.MouseListener;
import javax.swing.JComponent;

/**
 * Base class for creating custom Magarena UI widgets based on
 * Swing components but using composition instead of inheritance for
 * a much cleaner API.<br>
 * <br>
 * Naming style is to use an 'M' prefix to distinguish from 'J' swing components.
 */
public abstract class MWidget {

    /**
     * Reference to swing component for use in a layout manager only.
     * <b>Avoid setting properties directly on the component</b>.
     */
    public abstract JComponent component();

    public void setToolTipText(String text) {
        component().setToolTipText(text);
    }

    public void addMouseListener(MouseListener ml) {
        component().addMouseListener(ml);
    }

    public void setFocusable(boolean b) {
        component().setFocusable(b);
    }
}
