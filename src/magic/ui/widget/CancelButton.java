package magic.ui.widget;

import javax.swing.JButton;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.ui.UiString;

@SuppressWarnings("serial")
public class CancelButton extends JButton {

    // translatable strings
    private static final String _S1 = "Cancel";

    public CancelButton() {
        super(UiString.get(_S1));
        setIcon(IconImages.getIcon(MagicIcon.BANNED_ICON));
    }

}
