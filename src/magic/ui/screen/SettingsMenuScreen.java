package magic.ui.screen;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.ui.UiString;
import magic.ui.ScreenController;
import magic.ui.dialog.DownloadImagesDialog;
import magic.ui.dialog.FiremindWorkerDialog;
import magic.ui.screen.widget.MenuPanel;
import magic.ui.theme.ThemeFactory;
import magic.utility.MagicFileSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class SettingsMenuScreen extends AbstractScreen {

    // translatable strings.
    private static final String _S1 = "Settings Menu";
    private static final String _S2 = "Preferences";
    private static final String _S5 = "Download card images";
    private static final String _S6 = "Run Firemind Worker";
    private static final String _S7 = "Toggle full-screen";
    private static final String _S8 = "Shortcut key: F11";
    private static final String _S9 = "Close menu";
    private static final String _S10 = "Reset & Restart";
    private static final String _S11 = "Reset settings to default and run startup wizard?";
    private static final String _S12 = "This action cannot be undone!";
    private static final String _S13 = "Reset & Restart";
    private static final String _S14 = "Yes";
    private static final String _S15 = "No";

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

        menu.addMenuItem(UiString.get(_S10), new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                doReset();
            }
        });

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

    private void doReset() {
        final int response = JOptionPane.showOptionDialog(
                ScreenController.getMainFrame(),
                String.format("<html>%s<br><br><b>%s</b></html>", UiString.get(_S11), UiString.get(_S12)),
                UiString.get(_S13),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{UiString.get(_S14), UiString.get(_S15)}, UiString.get(_S15));
        if (response == JOptionPane.YES_OPTION) {
            setVisible(false);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MagicFileSystem.deleteGeneralConfigFile();
                    GeneralConfig.getInstance().load();
                    ThemeFactory.getInstance().setCurrentTheme(GeneralConfig.getInstance().getTheme());
                    ScreenController.showStartScreen();
                }
            });

        }
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

}
