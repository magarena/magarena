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
import magic.translate.UiString;

@SuppressWarnings("serial")
public class ColorButton extends JButton {

    // translatable strings
    private static final String _S1 = "Choose Color";

    public ColorButton(final Color defaultColor) {
        setBackground(defaultColor);
        addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final Color newColor = JColorChooser.showDialog(null, UiString.get(_S1), getBackground());
                if (newColor != null) {
                    setBackground(newColor);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
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
        });

        setPreferredSize(new Dimension(48, 24));
    }

    public Color getColor() {
        return getBackground();
    }
    
}
