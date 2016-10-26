package magic.ui.screen.menu.settings;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.translate.UiString;
import magic.ui.ScreenController;
import magic.ui.dialog.FiremindWorkerDialog;
import magic.ui.theme.ThemeFactory;
import magic.ui.screen.menu.MenuScreenContentPanel;
import magic.utility.MagicFileSystem;

@SuppressWarnings("serial")
class SettingsMenuContentPanel extends MenuScreenContentPanel {

    // translatable strings.
    private static final String _S1 = "Settings Menu";
    private static final String _S2 = "Preferences";
    private static final String _S3 = "Download card images";
    private static final String _S4 = "Run Firemind Worker";
    private static final String _S5 = "Toggle full-screen";
    private static final String _S6 = "Close menu";
    private static final String _S7 = "Reset & Restart";
    private static final String _S11 = "Reset settings to default and run startup wizard?";
    private static final String _S12 = "This action cannot be undone!";
    private static final String _S13 = "Reset & Restart";
    private static final String _S14 = "Yes";
    private static final String _S15 = "No";
    private static final String _S16 = "Please switch off the 'Download images on demand' setting in preferences first.";

    private static FiremindWorkerDialog firemindWorkerDialog;

    SettingsMenuContentPanel() {
        super(UiString.get(_S1), true);
        addMenuItem(UiString.get(_S2), this::showPreferencesDialog);
        addMenuItem(UiString.get(_S3), this::showDowloadImagesDialog);
        addMenuItem(UiString.get(_S4), this::showFiremindWorkerDialog);
        addMenuItem(UiString.get(_S5), this::doToggleFullScreen);
        addMenuItem(UiString.get(_S7), this::doResetRestart);
        addSpace();
        addMenuItem(UiString.get(_S6), this::doCloseMenu);
        refreshMenuLayout();
    }

    private void showDowloadImagesDialog() {
        if (GeneralConfig.getInstance().getImagesOnDemand()) {
            ScreenController.showInfoMessage(UiString.get(_S16));
        } else {
            ScreenController.showDownloadImagesScreen();
        }
    }

    private void showFiremindWorkerDialog() {
        if (firemindWorkerDialog == null || !firemindWorkerDialog.isDisplayable()) {
            firemindWorkerDialog = new FiremindWorkerDialog(ScreenController.getFrame());
        } else {
            firemindWorkerDialog.setVisible(true);
        }
    }

    private void doResetRestart() {
        final int response = JOptionPane.showOptionDialog(
                ScreenController.getFrame(),
                String.format("<html>%s<br><br><b>%s</b></html>", UiString.get(_S11), UiString.get(_S12)),
                UiString.get(_S13),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{UiString.get(_S14), UiString.get(_S15)}, UiString.get(_S15));
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
