package magic.ui.widget.about;

import java.awt.Color;
import javax.swing.JLabel;
import magic.ui.widget.FontsAndBorders;

@SuppressWarnings("serial")
class ScreenLabel extends JLabel {

    ScreenLabel(String text) {
        super(text);
        setFont(FontsAndBorders.FONT_MENU_BUTTON);
        setForeground(Color.WHITE);
    }   
}
