package magic.ui.explorer.filter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.FontsAndBorders;


@SuppressWarnings("serial")
class FilterButton extends JButton {

    private static final Font ON_FONT = FontsAndBorders.FONT1;
    private static final Font OFF_FONT = ON_FONT.deriveFont(Font.PLAIN);

    FilterButton(String title, ActionListener aListener) {
        super(title);
        setFont(OFF_FONT);
        setMinimumSize(new Dimension(66, 36));
        addActionListener(aListener);
    }

    void setActiveFlag(boolean isActive) {
        setFont(isActive ? ON_FONT : OFF_FONT);
        setBorder(isActive ? BorderFactory.createMatteBorder(2, 2, 2, 2, MagicStyle.getRolloverColor()) : null);
    }
}
