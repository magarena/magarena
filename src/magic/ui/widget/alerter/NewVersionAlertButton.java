package magic.ui.widget.alerter;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.ui.helpers.UrlHelper;
import magic.data.json.NewVersionJsonParser;
import magic.ui.ScreenController;
import magic.translate.MText;

@SuppressWarnings("serial")
public class NewVersionAlertButton extends AlertButton {

    // translatable strings
    private static final String _S1 = "Version %s has been released.";
    private static final String _S2 = "Open download page";
    private static final String _S3 = "Don't remind me again";
    private static final String _S4 = "Cancel";
    private static final String _S5 = "New version alert";
    private static final String _S6 = "New version released (%s)";

    private String newVersion = "";
    private static boolean hasChecked = false;

    @Override
    protected AbstractAction getAlertAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                String[] buttons = {MText.get(_S2), MText.get(_S3), MText.get(_S4)};
                int rc = JOptionPane.showOptionDialog(ScreenController.getFrame(),
                        MText.get(_S1, newVersion),
                        MText.get(_S5),
                        0,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        buttons, buttons[0]);
                if (rc == 0) {
                    UrlHelper.openURL(UrlHelper.URL_HOMEPAGE);
                    // don't display alert again until next restart.
                    newVersion = "";
                } else if (rc == 1) {
                    // suppress alert for this release.
                    final GeneralConfig config = GeneralConfig.getInstance();
                    config.setIgnoredVersionAlert(newVersion);
                    config.save();
                    setVisible(false);
                } else {
                    setVisible(true);
                }
            }
        };
    }

    @Override
    protected String getAlertCaption() {

        assert !SwingUtilities.isEventDispatchThread();

        // Only download json once at startup.
        if (!hasChecked) {
            newVersion = NewVersionJsonParser.getLatestVersion();
            hasChecked = true;
        }
        if (isNewVersionAvailable()) {
            return MText.get(_S6, newVersion);
        } else {
            return "";
        }
    }


    private boolean isNewVersionAvailable() {
        final String ignoredVersion = GeneralConfig.getInstance().getIgnoredVersionAlert();
        return !newVersion.isEmpty() && !ignoredVersion.equals(newVersion);
    }

}
