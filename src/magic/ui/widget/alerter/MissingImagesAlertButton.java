package magic.ui.widget.alerter;

import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.SwingWorker;
import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.ui.dialog.DownloadImagesDialog;

@SuppressWarnings("serial")
public class MissingImagesAlertButton extends AlertButton {
    
    private static DownloadImagesDialog downloadDialog;
    private static boolean isSoundEffectPlayed = false;
    private static boolean hasChecked = false;
    private AbstractAction alertAction;

    public MissingImagesAlertButton() {
        setText("Download missing card images");
    }

    @Override
    protected AbstractAction getAlertAction() {
        if (alertAction == null) {
            alertAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    if (downloadDialog == null || !downloadDialog.isDisplayable()) {
                        downloadDialog = new DownloadImagesDialog(MagicMain.rootFrame);
                    } else {
                        downloadDialog.setVisible(true);
                    }
                    hasChecked = false;
                    checkForMissingFiles();
                }
            };
        }
        return alertAction;
    }

    public void checkForMissingFiles() {
        if (!hasChecked || isVisible()) {
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return CardDefinitions.isMissingImages();
                }

                @Override
                protected void done() {
                    try {
                        final boolean isMissingImages = get();
                        GeneralConfig.getInstance().setIsMissingFiles(isMissingImages);
                        if (isMissingImages) {
                            playNewAlertSoundEffect();
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

    @Override
    protected void playNewAlertSoundEffect() {
        if (!isSoundEffectPlayed) {
            super.playNewAlertSoundEffect();
            isSoundEffectPlayed = true;
        }
    }

    @Override
    protected boolean isAlertTriggered() {
        return false;
    }

}
