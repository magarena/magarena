package magic.ui.screen.widget;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.data.URLUtils;
import magic.ui.dialog.DownloadImagesDialog;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class AlertPanel extends JPanel {
    
    private final MigLayout miglayout = new MigLayout();
    private static DownloadImagesDialog downloadDialog;

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
                URLUtils.openURL(URLUtils.URL_HOMEPAGE);
            }
        });
        btn.setToolTipText("Click to open the download page in your browser.");
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setText("Get new version (1.57)");
        btn.setVisible(false);
        return btn;
    }

    public void refreshAlerts() {
        checkForMissingFiles();
        // TODO: checkForNewVersion();
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
