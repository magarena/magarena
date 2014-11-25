package magic.ui.screen.widget;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.data.URLUtils;
import magic.data.json.NewVersionJsonParser;
import magic.ui.dialog.DownloadImagesDialog;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AlertPanel extends JPanel {
    
    private final MigLayout miglayout = new MigLayout();
    private static DownloadImagesDialog downloadDialog;
    private String newVersion = "";

    public AlertPanel() {
        setOpaque(false);
        setLayout(miglayout);
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        miglayout.setLayoutConstraints("insets 1 2 0 4, hidemode 3");
        add(getNewImagesButton());
        add(getNewVersionButton());
        revalidate();
        repaint();
    }

    private JButton getNewImagesButton() {
        final JButton btn = new JButton();
        btn.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn.setVisible(false);
                if (downloadDialog == null || !downloadDialog.isDisplayable()) {
                    downloadDialog = new DownloadImagesDialog(MagicMain.rootFrame);
                } else {
                    downloadDialog.setVisible(true);
                }
                checkForMissingFiles();
            }
        });
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setText("Download new card images");
        btn.setVisible(GeneralConfig.getInstance().isMissingFiles());
        return btn;
    }

    private JButton getNewVersionButton() {
        final JButton btn = new JButton();
        btn.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn.setVisible(false);
                final String caption = "Version " + newVersion + " has been released.";
                String[] buttons = {"Open download page", "Don't remind me again", "Cancel"};
                int rc = JOptionPane.showOptionDialog(
                        MagicMain.rootFrame, 
                        "<html>" + caption + "</html>",
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
                    refreshLayout();
                } else {
                    refreshLayout();
                }
            }
        });
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setText("New version released (" + newVersion + ")");
        final String ignoredVersion = GeneralConfig.getInstance().getIgnoredVersionAlert();
        btn.setVisible(!newVersion.isEmpty() && !ignoredVersion.equals(newVersion));
        return btn;
    }

    public void refreshAlerts() {
        checkForMissingFiles();
        checkForNewVersion();
    }

    private void checkForNewVersion() {
        new SwingWorker<String, Void> () {
            @Override
            protected String doInBackground() throws Exception {
                return NewVersionJsonParser.getLatestVersion();
            }
            @Override
            protected void done() {
                try {
                    newVersion = get();
                    refreshLayout();
                } catch (InterruptedException | ExecutionException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }.execute();
    }

    private void checkForMissingFiles() {
        new SwingWorker<Boolean, Void> () {
            @Override
            protected Boolean doInBackground() throws Exception {
                return CardDefinitions.isMissingImages();
            }
            @Override
            protected void done() {
                try {
                    GeneralConfig.getInstance().setIsMissingFiles(get());
                    refreshLayout();
                } catch (InterruptedException | ExecutionException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }.execute();
    }

}
