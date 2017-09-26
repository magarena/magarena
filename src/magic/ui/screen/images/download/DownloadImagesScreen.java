package magic.ui.screen.images.download;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import magic.ui.screen.MScreen;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DownloadImagesScreen extends MScreen {

    private DownloadDialogPanel dialogPanel;

    public DownloadImagesScreen() {
        // hint label replaces tooltips.
        ToolTipManager.sharedInstance().setEnabled(false);
        useCardsLoadingScreen(this::initUI);
    }

    private void initUI() {
        dialogPanel = new DownloadDialogPanel();
        setMainContent(getContent());
    }

    @Override
    protected boolean needsPlayableCards() {
        return true;
    }

    @Override
    protected boolean needsMissingCards() {
        return true;
    }

    private JPanel getContent() {
        final JPanel panel = new JPanel(new MigLayout("alignx center, aligny center"));
        panel.setOpaque(false);
        panel.add(dialogPanel);
        return panel;
    }

    @Override
    public boolean isScreenReadyToClose(MScreen nextScreen) {
        final boolean isBusy = dialogPanel != null && dialogPanel.isBusy();
        ToolTipManager.sharedInstance().setEnabled(isBusy == false);
        return isBusy == false;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        ToolTipManager.sharedInstance().setEnabled(b == false);
    }

}
