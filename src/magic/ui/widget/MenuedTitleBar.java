package magic.ui.widget;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.ui.utility.GraphicsUtils;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public class MenuedTitleBar extends TitleBar {

    private static final ImageIcon NORMAL_ICON = IconImages.getIcon(MagicIcon.MENU_ICON);
    private static final Icon HILITE_ICON = GraphicsUtils.getRecoloredIcon(NORMAL_ICON, MagicStyle.getRolloverColor());
    private static final Icon PRESSED_ICON = GraphicsUtils.getRecoloredIcon(NORMAL_ICON, MagicStyle.getPressedColor());

    public MenuedTitleBar(String text, final JPopupMenu aMenu) {
        
        super(text);

        setPreferredSize(new Dimension(getPreferredSize().width, 26));
        setPreferredSize(getPreferredSize());

        final JLabel iconLabel = new JLabel(NORMAL_ICON);
        add(iconLabel, "alignx right");
        iconLabel.addMouseListener(new MouseAdapter() {
            private boolean isMenuVisible = false;

            @Override
            public void mousePressed(MouseEvent e) {
                isMenuVisible = aMenu.isVisible();
                iconLabel.setIcon(PRESSED_ICON);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                iconLabel.setIcon(HILITE_ICON);
                if (isMenuVisible == false) {
                    final Rectangle rect = iconLabel.getBounds();
                    aMenu.show(MenuedTitleBar.this, rect.x, rect.y + rect.height + 2);
                    isMenuVisible = true;
                } else {
                    isMenuVisible = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                iconLabel.setIcon(HILITE_ICON);
                iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                iconLabel.setIcon(NORMAL_ICON);
                iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });


    }

}
