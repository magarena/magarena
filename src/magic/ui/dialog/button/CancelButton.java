package magic.ui.dialog.button;

import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.translate.UiString;

@SuppressWarnings("serial")
public class CancelButton extends MagicDialogButton {

    // translatable strings
    private static final String _S1 = "Cancel";

    public CancelButton() {
        super(UiString.get(_S1));
        setIcon(MagicImages.getIcon(MagicIcon.BANNED));
    }

}
