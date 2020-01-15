package magic.ui.dialog.button;

import magic.data.MagicIcon;
import magic.translate.MText;
import magic.ui.MagicImages;

@SuppressWarnings("serial")
public class SaveButton extends MagicDialogButton {

    // translatable strings
    private static final String _S1 = "Save";

    public SaveButton(String text) {
        super(text);
        setIcon(MagicImages.getIcon(MagicIcon.LEGAL));
    }

    public SaveButton() {
        this(MText.get(_S1));
    }

}
