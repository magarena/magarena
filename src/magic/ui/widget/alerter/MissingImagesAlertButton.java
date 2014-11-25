package magic.ui.widget.alerter;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.data.SoundEffects;
import magic.ui.dialog.DownloadImagesDialog;

@SuppressWarnings("serial")
public class MissingImagesAlertButton extends JButton {
    
    private static DownloadImagesDialog downloadDialog;

    public MissingImagesAlertButton() {

        setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                if (downloadDialog == null || !downloadDialog.isDisplayable()) {
                    downloadDialog = new DownloadImagesDialog(MagicMain.rootFrame);
                } else {
                    downloadDialog.setVisible(true);
                }
                checkForMissingFiles();
            }
        });
        setFont(getFont().deriveFont(Font.BOLD));
        setText("Download new card images");
        setVisible(GeneralConfig.getInstance().isMissingFiles());
    }

    public void checkForMissingFiles() {
        new SwingWorker<Boolean, Void> () {
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
                } catch (InterruptedException | ExecutionException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }.execute();
    }

    private void playNewAlertSoundEffect() {
        SoundEffects.playClip(SoundEffects.COMBAT_SOUND);
    }

}
