package magic.ui.screen.widget;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class ActionBarButton extends MenuButton {

    // CTR
    public ActionBarButton(ImageIcon icon, String actionName, String tooltip, AbstractAction action) {
        super("", action);
        setIcon(icon);
        setToolTipText("<html><b>" + actionName + "</b><br>" + tooltip + "</html>");
    }
    public ActionBarButton(final String caption, final AbstractAction action) {
        super(caption, action);
    }

    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return new Point(0, -45);
    }

}
