package magic.ui.screen.images.download;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import magic.ui.screen.AbstractScreen;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DownloadImagesScreen extends AbstractScreen {

    private final DownloadDialogPanel dialogPanel;

    public DownloadImagesScreen() {
        // hint label replaces tooltips.
        ToolTipManager.sharedInstance().setEnabled(false);
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
        final boolean isBusy = dialogPanel.isBusy();
        ToolTipManager.sharedInstance().setEnabled(isBusy == false);
        return isBusy == false;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        ToolTipManager.sharedInstance().setEnabled(b == false);
    }

}
