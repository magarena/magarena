package magic.ui.screen.menu;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.data.MagicIcon;
import magic.ui.MagicImages;

/**
 * Action bar button used to change screen layout.
 */
@SuppressWarnings("serial")
class NewLayoutButton extends NewActionBarButton {

    private static final String _S7 = "Screen layout";
    private static final String _S8 = "Cycles through different screen layouts.";

    NewLayoutButton(final Runnable action) {
        super(MagicImages.getIcon(MagicIcon.LAYOUT), _S7, _S8,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action.run();
                    }
                }
        );
    }

}
