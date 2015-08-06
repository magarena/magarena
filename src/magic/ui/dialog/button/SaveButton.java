package magic.ui.dialog.button;

import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.translate.UiString;

@SuppressWarnings("serial")
public class SaveButton extends MagicDialogButton {

    // translatable strings
    private static final String _S1 = "Save";
    
    public SaveButton(String text) {
        super(text);
        setIcon(IconImages.getIcon(MagicIcon.LEGAL_ICON));
    }

    public SaveButton() {
        this(UiString.get(_S1));
    }

}
