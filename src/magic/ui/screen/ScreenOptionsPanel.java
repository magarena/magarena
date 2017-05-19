package magic.ui.screen;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import magic.data.MagicIcon;
import magic.ui.helpers.ImageHelper;

@SuppressWarnings("serial")
public abstract class ScreenOptionsPanel extends JPanel {

    protected static final ImageIcon MENU_ICON = ImageHelper.getRecoloredIcon(
        MagicIcon.OPTION_MENU, Color.BLACK, Color.WHITE
    );

}
