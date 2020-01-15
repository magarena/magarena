package magic.ui.dialog.button;

import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.MagicImages;

@SuppressWarnings("serial")
public class CancelButton extends MagicDialogButton {

    // translatable strings
    private static final String _S1 = "Cancel";

    public CancelButton() {
        super(MText.get(_S1));
        setIcon(MagicImages.getIcon(MagicIcon.BANNED));
    }

}
