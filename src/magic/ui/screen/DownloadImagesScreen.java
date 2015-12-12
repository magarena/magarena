package magic.ui.screen;

import javax.swing.JPanel;
import magic.ui.image.download.DownloadDialogPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DownloadImagesScreen extends AbstractScreen {

    private final DownloadDialogPanel dialogPanel;

    public DownloadImagesScreen() {
        dialogPanel = new DownloadDialogPanel();
        setContent(getContentPanel());
    }

    private JPanel getContentPanel() {
        final JPanel panel = new JPanel(new MigLayout("alignx center, aligny center"));
        panel.setOpaque(false);
        panel.add(dialogPanel);
        return panel;
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return dialogPanel.isBusy() == false;
    }

}
