package magic.ui.screen.widget;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class ActionBarButton extends PlainMenuButton {

    private void setToolTip(String title, String tooltip) {
        title = title == null ? "" : title.trim();
        tooltip = tooltip == null ? "" : tooltip.trim();
        if (title.isEmpty() && tooltip.isEmpty()) {
            setToolTipText(null);
        } else {
            final String s1 = !title.isEmpty() ? "<b>" + title + "</b>" : "";
            final String s2 = s1 + (!title.isEmpty() && !tooltip.isEmpty() ? "<br>" : "") + tooltip;
            setToolTipText("<html>" + s2 + "</html>");
        }
    }

    // CTR
    public ActionBarButton(ImageIcon icon, String actionName, String tooltip, AbstractAction action, boolean showSeparator) {
        super("", action, tooltip, showSeparator);
        setIcon(icon);
        setToolTip(actionName, tooltip);
    }

    public ActionBarButton(ImageIcon icon, String actionName, String tooltip, AbstractAction action) {
        this(icon, actionName, tooltip, action, true);
    }

    public ActionBarButton(ImageIcon icon, String actionName, AbstractAction action) {
        this(icon, actionName, "", action);
    }

    public ActionBarButton(ImageIcon icon, AbstractAction action) {
        this(icon, null, action);
    }

    // CTR - text only action.
    public ActionBarButton(final String caption, final String tooltip, final AbstractAction action, final boolean showSeparator) {
        super(caption, action, tooltip, showSeparator);
        if (tooltip != null) {
            setToolTipText("<html><b>" + caption + "</b><br>" + tooltip + "</html>");
        }
    }

    public ActionBarButton(AbstractAction action) {
        this("", null, action);
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
