package magic.ui.widget.button;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import magic.data.MagicIcon;
import magic.ui.MagicImages;
import magic.ui.screen.widget.ActionBarButton;

/**
 * Action bar button used to change screen layout.
 */
@SuppressWarnings("serial")
public class LayoutButton extends ActionBarButton {

    private static final String _S7 = "Screen layout";
    private static final String _S8 = "Cycles through different screen layouts.";

    public LayoutButton(final Runnable action) {
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
