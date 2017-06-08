package magic.ui.screen.menu.settings;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.translate.MText;
import magic.ui.ScreenController;
import magic.ui.dialog.FiremindWorkerDialog;
import magic.ui.screen.menu.main.NewMenuScreenContentPanel;
import magic.ui.theme.ThemeFactory;
import magic.utility.MagicFileSystem;

@SuppressWarnings("serial")
class SettingsMenuContentPanel extends NewMenuScreenContentPanel {

    // translatable strings.
    private static final String _S2 = "Preferences";
    private static final String _S3 = "Setup card images";
    private static final String _S4 = "Run Firemind Worker";
    private static final String _S5 = "Toggle full-screen on/off";
    private static final String _S6 = "Main menu";
    private static final String _S7 = "Reset & Restart";
    private static final String _S11 = "Reset settings to default and run startup wizard?";
    private static final String _S12 = "This action cannot be undone!";
    private static final String _S13 = "Reset & Restart";
    private static final String _S14 = "Yes";
    private static final String _S15 = "No";

    private static FiremindWorkerDialog firemindWorkerDialog;

    SettingsMenuContentPanel() {
        super(false);
        addMenuItem(MText.get(_S2), this::showPreferencesDialog);
        addMenuItem(MText.get(_S3), this::showDowloadImagesDialog);
        addMenuItem(MText.get(_S4), this::showFiremindWorkerDialog);
        addMenuItem(MText.get(_S5), this::doToggleFullScreen);
        addMenuItem(MText.get(_S7), this::doResetRestart);
        addSpace();
        addMenuItem(MText.get(_S6), this::doCloseMenu);
        refreshMenuLayout();
    }

    private void showDowloadImagesDialog() {
        ScreenController.showDownloadImagesScreen();
    }

    private void showFiremindWorkerDialog() {
        if (firemindWorkerDialog == null || !firemindWorkerDialog.isDisplayable()) {
            firemindWorkerDialog = new FiremindWorkerDialog(ScreenController.getFrame());
        } else {
            firemindWorkerDialog.setVisible(true);
        }
    }

    private void doResetRestart() {
        final int response = JOptionPane.showOptionDialog(ScreenController.getFrame(),
                String.format("<html>%s<br><br><b>%s</b></html>", MText.get(_S11), MText.get(_S12)),
                MText.get(_S13),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{MText.get(_S14), MText.get(_S15)}, MText.get(_S15));
        if (response == JOptionPane.YES_OPTION) {
            setVisible(false);
            SwingUtilities.invokeLater(() -> {
                MagicFileSystem.deleteGeneralConfigFile();
                GeneralConfig.getInstance().load();
                ThemeFactory.getInstance().setCurrentTheme(GeneralConfig.getInstance().getTheme());
                ScreenController.showStartScreen();
            });
        }
    }

    private void showPreferencesDialog() {
        ScreenController.showPreferencesDialog();
    }

    private void doToggleFullScreen() {
        ScreenController.getFrame().toggleFullScreenMode();
    }

    private void doCloseMenu() {
        ScreenController.closeActiveScreen(false);
    }

}
