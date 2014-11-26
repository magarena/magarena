package magic.ui.widget.alerter;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import magic.MagicMain;
import magic.data.GeneralConfig;
import magic.data.SoundEffects;
import magic.data.URLUtils;
import magic.data.json.NewVersionJsonParser;

@SuppressWarnings("serial")
public class NewVersionAlertButton extends JButton {

    private String newVersion = "";
    private static boolean hasChecked = false;

    public NewVersionAlertButton() {

        setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                final String caption = "Version " + newVersion + " has been released.";
                String[] buttons = {"Open download page", "Don't remind me again", "Cancel"};
                int rc = JOptionPane.showOptionDialog(
                        MagicMain.rootFrame,
                        caption,
                        "New version alert",
                        0,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        buttons, buttons[0]);
                if (rc == 0) {
                    URLUtils.openURL(URLUtils.URL_HOMEPAGE);
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
        });
        setFont(getFont().deriveFont(Font.BOLD));
        setVisible(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
    }

    private boolean isNewVersionAvailable() {
        final String ignoredVersion = GeneralConfig.getInstance().getIgnoredVersionAlert();
        return !newVersion.isEmpty() && !ignoredVersion.equals(newVersion);
    }

    public void checkForNewVersion() {
        if (!hasChecked) {
            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return NewVersionJsonParser.getLatestVersion();
                }
                @Override
                protected void done() {
                    try {
                        newVersion = get();
                        if (isNewVersionAvailable()) {
                            playNewAlertSoundEffect();
                            setText("New version released (" + newVersion + ")");
                            setVisible(true);
                        } else {
                            setVisible(false);
                        }
                        hasChecked = true;
                    } catch (InterruptedException | ExecutionException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }.execute();
        }
    }

    private void playNewAlertSoundEffect() {
        SoundEffects.playClip(SoundEffects.RESOLVE_SOUND, true);
    }

}
