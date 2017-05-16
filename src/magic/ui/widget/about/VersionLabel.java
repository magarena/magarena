package magic.ui.widget.about;

import java.awt.Color;
import javax.swing.JLabel;
import magic.ui.FontsAndBorders;
import magic.utility.MagicSystem;

@SuppressWarnings("serial")
class VersionLabel extends JLabel {

    VersionLabel() {
        super(MagicSystem.getVersionTitle());
        setFont(FontsAndBorders.FONT0);
        setForeground(Color.WHITE);
    }
}
