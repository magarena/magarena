package magic.ui.screen;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public final class ScreenHelper {
    private ScreenHelper() { }

    public static void setKeyboardAction(JComponent widget, int keyEvent, Runnable action) {
        String inputMapKey = "key_" + keyEvent;
        widget.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(keyEvent, 0),
                inputMapKey
        );
        widget.getActionMap().put(inputMapKey, new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                action.run();
            }
        });
    }

}
