package magic.ui.widget;

import java.awt.Dimension;
import javax.swing.JButton;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.ui.UiString;

@SuppressWarnings("serial")
public class SaveButton extends JButton {

    // translatable strings
    private static final String _S1 = "Save";

    private static final Dimension PREFERRED_SIZE = new Dimension(100, 30);

    public SaveButton() {
        super(UiString.get(_S1));
        setIcon(IconImages.getIcon(MagicIcon.LEGAL_ICON));
        setPreferredSize(PREFERRED_SIZE);
        setMinimumSize(PREFERRED_SIZE);
    }

}
