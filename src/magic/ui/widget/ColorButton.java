package magic.ui.widget;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import magic.translate.MText;

@SuppressWarnings("serial")
public class ColorButton extends JButton {

    // translatable strings
    private static final String _S1 = "Choose a color";

    private final MouseAdapter mouseListener;
    private final AbstractAction clickAction;

    public ColorButton(final Color defaultColor) {

        setBackground(defaultColor);
        setPreferredSize(new Dimension(48, 24));

        this.clickAction = getSelectColorAction();
        addActionListener(clickAction);

        this.mouseListener = getMouseListener();
        addMouseListener(mouseListener);
    }

    private AbstractAction getSelectColorAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Color newColor = JColorChooser.showDialog(null, MText.get(_S1), getBackground());
                if (newColor != null) {
                    setBackground(newColor);
                }
            }
        };
    }

    private MouseAdapter getMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        };
    }

    public Color getColor() {
        return getBackground();
    }

    public void setColor(Color aColor) {
        setBackground(aColor);
    }

    public void setLocked(boolean isLocked) {
        removeActionListener(clickAction);
        removeMouseListener(mouseListener);
        if (!isLocked) {
            addMouseListener(mouseListener);
            addActionListener(clickAction);
        }
    }

}
