package magic.ui.widget.alerter;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.ScreenController;

@SuppressWarnings("serial")
public class MissingImagesAlertButton extends AlertButton {

    // translatable strings
    private static final String _S1 =  "Download missing card images";

    private static boolean isSoundEffectPlayed = false;
    private static boolean hasChecked = false;
    private boolean isMissingImages = false;

    @Override
    protected AbstractAction getAlertAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                ScreenController.showDownloadImagesScreen();
                hasChecked = false;
            }
        };
    }

    @Override
    protected void playNewAlertSoundEffect() {
        if (!isSoundEffectPlayed) {
            super.playNewAlertSoundEffect();
            isSoundEffectPlayed = true;
        }
    }

    @Override
    protected String getAlertCaption() {

        assert !SwingUtilities.isEventDispatchThread();

        if (!hasChecked || isVisible()) {
            isMissingImages = CardDefinitions.isMissingPlayableImages();
            GeneralConfig.getInstance().setIsMissingFiles(isMissingImages);
            hasChecked = true;
        }
        if (isMissingImages) {
            return UiString.get(_S1);
        } else {
            return "";
        }

    }

    @Override
    public void doAlertCheck() {
        if (GeneralConfig.getInstance().getImagesOnDemand()) {
            setVisible(false);
        } else {
            super.doAlertCheck();
        }
    }

}
