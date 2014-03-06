package magic.ui.screen.widget;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
public class MenuButton extends JButton {

    private final static Color COLOR_NORMAL = Color.WHITE;
    private final static Color COLOR_DISABLED = Color.GRAY;

    private final boolean isRunnable;

    public MenuButton(final String caption, final AbstractAction action, final String tooltip) {
        super(caption);
        isRunnable = (action != null);
        setFont(FontsAndBorders.FONT_MENU_BUTTON);
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(COLOR_NORMAL);
        setButtonTransparent();
        setFocusable(true);
        setToolTipText(tooltip);
        if (isRunnable) {
            setMouseAdapter();
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(action);
        }
    }
    public MenuButton(final String caption, final AbstractAction action) {
        this(caption, action, null);
    }

    public boolean isRunnable() {
        return isRunnable;
    }

    private void setButtonTransparent() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        if (!isRunnable) {
            setBorder(null);
        }
    }

    private void setMouseAdapter() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.YELLOW);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.WHITE);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                setForeground(Color.YELLOW.darker());
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setForeground(Color.WHITE);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setForeground(b ? COLOR_NORMAL : COLOR_DISABLED);
    }

}
