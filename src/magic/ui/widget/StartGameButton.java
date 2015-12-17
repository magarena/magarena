package magic.ui.widget;

import javax.swing.AbstractAction;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
public class StartGameButton extends MenuButton {

    public StartGameButton(String text, AbstractAction action) {
        super(text, action);
    }

    public void setBusy(boolean b) {
        setEnabled(!b);
    }
}
