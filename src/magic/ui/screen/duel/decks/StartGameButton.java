package magic.ui.screen.duel.decks;

import javax.swing.AbstractAction;
import magic.translate.MText;
import magic.ui.screen.widget.MenuButton;

@SuppressWarnings("serial")
class StartGameButton extends MenuButton {

    private static final String _S1 = "Preparing game, please wait...";

    StartGameButton(String text, AbstractAction action) {
        super(text, action);
    }

    void setBusy(boolean b) {
        setEnabled(!b);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        setToolTipText(b ? null : MText.get(_S1));
    }
}
