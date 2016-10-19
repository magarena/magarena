package magic.ui.widget.alerter;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import magic.data.json.NewVersionJsonParser;
import magic.ui.ScreenController;
import magic.translate.UiString;

@SuppressWarnings("serial")
public class UpgradeJavaAlertButton extends AlertButton {

    private static final String JRE_VERSION = System.getProperty("java.version");

    // translatable strings
    private static final String _S1 = "Close";
    private static final String _S2 = "Please upgrade Java to version 1.8 or greater.";
    private static final String _S3 = "This is the last version of Magarena that will work with Java %s.";
    private static final String _S4 = "Sorry for the inconvenience.";
    private static final String _S5 = "Java upgrade required!";

    @Override
    protected AbstractAction getAlertAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                String[] buttons = {UiString.get(_S1)};
                int rc = JOptionPane.showOptionDialog(
                    ScreenController.getFrame(),
                    String.format("<html>%s<br><br>%s<br><br>%s</html>",
                        UiString.get(_S2),
                        UiString.get(_S3, JRE_VERSION),
                        UiString.get(_S4)
                    ),
                    UiString.get(_S5),
                    0,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    buttons, buttons[0]);
                if (rc == 0) {
                    setVisible(false);
                }
            }
        };
    }

    @Override
    protected String getAlertCaption() {
        assert !SwingUtilities.isEventDispatchThread();
        if (NewVersionJsonParser.versionCompare(JRE_VERSION, "1.8") < 0) {
            return UiString.get(_S5);
        } else {
            return "";
        }
    }

}
