package magic.ui.screen;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import magic.ui.UiString;
import magic.ui.ScreenController;
import magic.ui.dialog.DownloadImagesDialog;
import magic.ui.dialog.FiremindWorkerDialog;
import magic.ui.screen.widget.MenuPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SettingsMenuScreen extends AbstractScreen {

    // translatable strings.
    private static final String _S1 = "Settings Menu";
    private static final String _S2 = "Preferences";
    private static final String _S3 = "Import";
    private static final String _S4 = "Migrate data from an existing installation of Magarena.";
    private static final String _S5 = "Download card images";
    private static final String _S6 = "Run Firemind Worker";
    private static final String _S7 = "Toggle full-screen";
    private static final String _S8 = "Shortcut key: F11";
    private static final String _S9 = "Close menu";

    private static DownloadImagesDialog downloadDialog;
    private static FiremindWorkerDialog firemindWorkerDialog;

    public SettingsMenuScreen() {
        setContent(getScreenContent());
    }

    private JPanel getScreenContent() {

        final JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new MigLayout("insets 0, gap 0, center, center"));

        final MenuPanel menu = new MenuPanel(UiString.get(_S1));

        menu.addMenuItem(UiString.get(_S2), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showPreferencesDialog();
            }
        });

        menu.addMenuItem(UiString.get(_S3), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.showImportDialog();
            }
        }, UiString.get(_S4));

        menu.addMenuItem(UiString.get(_S5), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (downloadDialog == null || !downloadDialog.isDisplayable()) {
                    downloadDialog = new DownloadImagesDialog(getFrame());
                } else {
                    downloadDialog.setVisible(true);
                }
            }
        });
        
        menu.addMenuItem(UiString.get(_S6), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (firemindWorkerDialog == null || !firemindWorkerDialog.isDisplayable()) {
                    firemindWorkerDialog = new FiremindWorkerDialog(getFrame());
                } else {
                    firemindWorkerDialog.setVisible(true);
                }
            }
        });
        
        menu.addMenuItem(UiString.get(_S7), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getFrame().toggleFullScreenMode();
            }
        }, UiString.get(_S8));

        menu.addBlankItem();
        menu.addMenuItem(UiString.get(_S9), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ScreenController.closeActiveScreen(false);
            }
        });

        menu.refreshLayout();
        content.add(menu);
        return content;

    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

}
