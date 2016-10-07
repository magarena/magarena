package magic.ui.screen.widget;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class ActionBarButton extends MenuButton {

    // CTR
    public ActionBarButton(ImageIcon icon, String actionName, String tooltip, AbstractAction action, boolean showSeparator) {
        super("", action, tooltip, showSeparator);
        setIcon(icon);
        if (tooltip != null) {
            setToolTipText("<html><b>" + actionName + "</b><br>" + tooltip + "</html>");
        }
    }
    public ActionBarButton(ImageIcon icon, String actionName, String tooltip, AbstractAction action) {
        this(icon, actionName, tooltip, action, true);
    }
    // CTR - text only action.
    public ActionBarButton(final String caption, final String tooltip, final AbstractAction action, final boolean showSeparator) {
        super(caption, action, tooltip, showSeparator);
        if (tooltip != null) {
            setToolTipText("<html><b>" + caption + "</b><br>" + tooltip + "</html>");
        }
    }
    public ActionBarButton(final String caption, final String tooltip, final AbstractAction action) {
        this(caption, tooltip, action, true);
    }
    public ActionBarButton(final String caption, final AbstractAction action) {
        this(caption, null, action);
    }
    protected ActionBarButton() {}

    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return new Point(0, -45);
    }

    @Override
    public void setIcon(Icon defaultIcon) {
        super.setIcon(defaultIcon);
        setRolloverIcon(ImageHelper.getRecoloredIcon(
                (ImageIcon) defaultIcon,
                MagicStyle.getRolloverColor())
        );
        setPressedIcon(ImageHelper.getRecoloredIcon(
                (ImageIcon) defaultIcon,
                MagicStyle.getPressedColor())
        );
    }



}
