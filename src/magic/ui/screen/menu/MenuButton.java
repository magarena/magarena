package magic.ui.screen.menu;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
public abstract class MenuButton extends JButton {

    // translatable strings
    protected static final String _S1 = "Close";

    protected static final Color COLOR_NORMAL = Color.WHITE;
    protected static final Color COLOR_DISABLED = Color.DARK_GRAY;

    protected boolean isRunnable;

    public MenuButton() {
        isRunnable = false;
    }

    public MenuButton(String text, AbstractAction action, String tooltip) {
        super(text);
        this.isRunnable = (action != null);
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
                if (isEnabled()) {
                    setForeground(MagicStyle.getRolloverColor());
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    setForeground(Color.WHITE);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    setForeground(MagicStyle.getPressedColor());
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }

            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    setForeground(Color.WHITE);
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
        });
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        isRunnable = b;
        setForeground(b ? COLOR_NORMAL : COLOR_DISABLED);
    }

}
