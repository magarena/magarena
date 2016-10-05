package magic.ui.widget.card.filter;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import magic.ui.MagicUI;
import magic.ui.utility.MagicStyle;

@SuppressWarnings("serial")
class FilterOptionButton extends JButton {

    FilterOptionButton(String text) {
        super(text);
        setButtonTransparent();
        setForeground(Color.WHITE);
        setMouseAdapter();
    }

    private void setButtonTransparent() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setBorder(null);
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
                    MagicUI.showBusyCursorFor(FilterOptionButton.this);
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    setForeground(Color.WHITE);
                    MagicUI.showDefaultCursorFor(FilterOptionButton.this);
                }
            }
        });
    }
}
