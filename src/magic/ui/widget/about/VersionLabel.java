package magic.ui.widget.about;

import java.awt.Color;
import javax.swing.JLabel;
import magic.ui.widget.FontsAndBorders;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
class VersionLabel extends JLabel {

    VersionLabel() {
        super(MagicSystem.SOFTWARE_TITLE);
        setFont(FontsAndBorders.FONT0);
        setForeground(Color.WHITE);
    }
}
