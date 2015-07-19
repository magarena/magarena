package magic.ui.dialog.button;

import java.awt.Dimension;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MagicDialogButton extends JButton {

    private static final Dimension PREFERRED_SIZE = new Dimension(100, 30);

    public MagicDialogButton(String text) {
        super(text);
        setPreferredSize(PREFERRED_SIZE);
        setMinimumSize(PREFERRED_SIZE);
    }

}
