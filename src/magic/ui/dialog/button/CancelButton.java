package magic.ui.dialog.button;

import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.translate.UiString;

@SuppressWarnings("serial")
public class CancelButton extends MagicDialogButton {

    // translatable strings
    private static final String _S1 = "Cancel";

    public CancelButton() {
        super(UiString.get(_S1));
        setIcon(IconImages.getIcon(MagicIcon.BANNED_ICON));
    }

}
