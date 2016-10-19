package magic.ui.screen;

import java.awt.Component;
import javax.swing.JComponent;
import magic.ui.screen.interfaces.IThemeStyle;

final class MScreenHelper {

    static void refreshComponentStyle(final JComponent container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComponent) {
                final JComponent widget = (JComponent)component;
                if (widget.getComponentCount() > 0) {
                    refreshComponentStyle(widget);
                }
                if (widget instanceof IThemeStyle) {
                    ((IThemeStyle)widget).refreshStyle();
                }
            }
        }
    }

    private MScreenHelper() {
    }
}
