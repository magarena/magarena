package magic.ui.screen;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.ui.ScreenController;

import magic.ui.dialog.DownloadImagesDialog;
import magic.ui.dialog.FiremindWorkerDialog;
import magic.ui.screen.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SettingsMenuScreen extends AbstractScreen {

    private static DownloadImagesDialog downloadDialog;
    private static FiremindWorkerDialog firemindWorkerDialog;

    public SettingsMenuScreen() {
        setContent(getScreenContent());
    }

    private JPanel getScreenContent() {

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center"));

        final MenuPanel menu = new MenuPanel("Settings Menu");

        menu.addMenuItem("Preferences", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showPreferencesDialog();
            }
        });

        menu.addMenuItem("Import", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showImportDialog();
            }
        }, "Migrate data from an existing installation of Magarena.");

        menu.addMenuItem("Download card images", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (downloadDialog == null || !downloadDialog.isDisplayable()) {
                    downloadDialog = new DownloadImagesDialog(getFrame());
                } else {
                    downloadDialog.setVisible(true);
                }
            }
        });
        
        menu.addMenuItem("Run Firemind Worker", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (firemindWorkerDialog == null || !firemindWorkerDialog.isDisplayable()) {
                    firemindWorkerDialog = new FiremindWorkerDialog(getFrame());
                } else {
                    firemindWorkerDialog.setVisible(true);
                }
            }
        });
        
        menu.addMenuItem("Toggle full-screen", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().toggleFullScreenMode();
            }
        }, "Shortcut key: F11");
        menu.addBlankItem();
        menu.addMenuItem("Close menu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.closeActiveScreen(false);
            }
        });

        menu.refreshLayout();
        content.add(menu);
        return content;

    }

    /* (non-Javadoc)
     * @see magic.ui.MagScreen#isScreenReadyToClose(magic.ui.MagScreen)
     */
    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

}
