package magic.ui.screen.deck.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
public class ViewToggleButton extends JToggleButton {

    private final Dimension PREF_BUTTON_SIZE = new Dimension(160, 26);
    private final Dimension MAX_BUTTON_SIZE = PREF_BUTTON_SIZE;

    public ViewToggleButton(String text) {
        super(text);

        setFont(FontsAndBorders.FONT2.deriveFont(Font.PLAIN));
        setForeground(Color.LIGHT_GRAY);
        setOpaque(true);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setRolloverEnabled(false);
        setPreferredSize(PREF_BUTTON_SIZE);
        setMaximumSize(MAX_BUTTON_SIZE);

        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                setFont(getFont().deriveFont(isSelected() ? Font.BOLD : Font.PLAIN));
                setForeground(isSelected() ? MagicStyle.getRolloverColor() : Color.LIGHT_GRAY);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                MagicStyle.setHightlight(ViewToggleButton.this, true);
                if (isSelected() == false) {
                    setForeground(MagicStyle.getRolloverColor());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                MagicStyle.setHightlight(ViewToggleButton.this, false);
                setForeground(isSelected() ? MagicStyle.getRolloverColor() : Color.LIGHT_GRAY);
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(getWidth()-1, 3, getWidth()-1, getHeight());
    }

}
