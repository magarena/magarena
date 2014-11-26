package magic.ui.widget.alerter;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private static boolean isSoundEffectPlayed = false;
    private static boolean hasChecked = false;

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
                hasChecked = false;
                checkForMissingFiles();
            }
        });
        setFont(getFont().deriveFont(Font.BOLD));
        setText("Download missing card images");
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

    private void playNewAlertSoundEffect() {
        if (!isSoundEffectPlayed) {
            SoundEffects.playClip(SoundEffects.RESOLVE_SOUND, true);
            isSoundEffectPlayed = true;
        }
    }

}
