package magic.ui.widget;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.helpers.ImageHelper;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class MenuIconLabel extends JLabel {

    private static final ImageIcon NORMAL_ICON = MagicImages.getIcon(MagicIcon.MENU);
    private static final Icon HILITE_ICON = ImageHelper.getRecoloredIcon(NORMAL_ICON, MagicStyle.getRolloverColor());
    private static final Icon PRESSED_ICON = ImageHelper.getRecoloredIcon(NORMAL_ICON, MagicStyle.getPressedColor());

    private JPopupMenu menu;

    public MenuIconLabel() {
        this.menu = null;
        setIcon(null);
        setMouseListener();
    }

    public MenuIconLabel(final JPopupMenu aMenu) {
        this.menu = aMenu;
        setIcon(NORMAL_ICON);
        setMouseListener();
    }

    private void setMouseListener() {
        addMouseListener(new MouseAdapter() {

            private boolean isMenuVisible = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isMenuVisible = menu.isVisible();
                    setIcon(PRESSED_ICON);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setIcon(HILITE_ICON);
                    if (isMenuVisible == false) {
                        final Rectangle rect = getBounds();
                        menu.show(MenuIconLabel.this.getParent(), rect.x, rect.y + rect.height + 2);
                        isMenuVisible = true;
                    } else {
                        isMenuVisible = false;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setIcon(HILITE_ICON);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setIcon(NORMAL_ICON);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

        });
    }

    void setPopupMenu(JPopupMenu aMenu) {
        this.menu = aMenu;
        setIcon(aMenu != null ? NORMAL_ICON : null);
    }
}
