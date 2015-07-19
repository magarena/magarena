package magic.ui.widget;

import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.ui.UiString;

@SuppressWarnings("serial")
public class SaveButton extends MagicDialogButton {

    // translatable strings
    private static final String _S1 = "Save";

    public SaveButton() {
        super(UiString.get(_S1));
        setIcon(IconImages.getIcon(MagicIcon.LEGAL_ICON));
    }

}
